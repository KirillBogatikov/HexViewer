package org.kllbff.hexviewer.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import org.kllbff.hexviewer.FilesContainer;
import org.kllbff.hexviewer.Launcher;
import org.kllbff.hexviewer.concurrency.ReadTask;
import org.kllbff.hexviewer.concurrency.TaskListener;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Контроллер элементов управления графического интерфейса
 * 
 * @author Кирилл Богатиков
 */
public class UIController implements TaskListener, EventHandler<MouseEvent> {
    private static UIController instance;

    /**
     * Устанавливает текущему экземпляру контроллера необходимые для работы значения 
     * 
     * @param stage окно приложения
     * @param stringView поле для вывода строкового представления файла
     * @param tabPane панель вкладок для файлов
     * @param fileSize поле для вывода размера файла
     * @param fileModified поле для вывода даты последнего изменения
     * @return текущий экземпляр контроллера
     */
    public static UIController initialize(Stage stage, TextArea stringView, TabPane tabPane, Label fileSize, Label fileModified) {
        instance.stage = stage;
        instance.stringView = stringView;
        instance.tabPane = tabPane;
        instance.fileSize = fileSize;
        instance.fileModified = fileModified;
        return instance;
    }

    private FilesContainer container = new FilesContainer();
    private Label fileSize, fileModified;
    private Stage stage;
    private TextArea stringView;
    private TabPane tabPane;
    private Tab currentTab;
    private FileChooser fileChooser = new FileChooser();

    /**
     * Конструктор без параметров используется JavaFX при загрузке
     */
    public UIController() {
        UIController.instance = this;
    }

    /**
     * Вызывается при нажатии на пункт "Открыть"
     * 
     * Позволяет выбрать пользователю файл с помощью FileChooser и запускает чтение файла в потоке {@link Launcher#workloadThread}
     */
    public void onOpenClick() {
        File file = fileChooser.showOpenDialog(stage);
        Launcher.workloadThread.execute(new ReadTask(file, this));
    }

    /**
     * Вызывается при нажатии на пункт меню "Закрыть"
     */
    public void onCloseClick() {
        tabPane.getTabs().remove(currentTab);
        container.kick(currentTab);
    }

    /**
     * Вызывается при нажатии на пункт меню "О приложении"
     * 
     * Выводит на экран диалоговое окно с информацией о приложении
     */
    public void onAboutClick() {
        Alerts.showInfo("О приложении", "HEX-Viewer 0.0.1\nИспользование данного ПО доступно по лицензии Apache 2.0\nCopyright Кирилл Богатиков, 2019");
    }

    /**
     * Вызывается при нажатии на пункт меню "Выход"
     * 
     * Завершает выполнение приложения
     */
    public void onExitClick() {
        System.exit(0);
    }

    /**
     * Вызывается при изменении пользователем кодировки
     * 
     * @param event событие изменения пункта меню
     */
    @FXML
    public void onEncodingChange(ActionEvent event) {
        RadioMenuItem item = (RadioMenuItem)event.getSource();
        container.useCharset(Charset.forName(item.getText()));
    }

    @Override
    public void onReadSuccess(File file, byte[] bytes) {
        Platform.runLater(() -> {
            String byteView;

            Tab tab = new Tab();
            ScrollPane scroll = new ScrollPane();
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setPrefWidth(400.0);
            GridPane pane = new GridPane();
            pane.setPadding(new Insets(5));
            pane.setPrefWidth(385.0);
            scroll.setContent(pane);
            tab.setContent(scroll);

            for(int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                byteView = Integer.toHexString(b < 0 ? b + 255 : b);
                while (byteView.length() < 2) {
                    byteView = "0" + byteView;
                }

                Label tf = new Label(byteView);
                tf.setPrefWidth(60.0);
                pane.add(tf, i % 16, i / 16);
            }

            tab.setText(file.getName());
            tab.setOnSelectionChanged((e) -> {
                currentTab = ((Tab)e.getSource());
                if(currentTab == null) {
                    stringView.clear();
                    fileSize.setText("");
                    fileModified.setText("");
                } else {
                    stringView.setText(container.getStringContent(currentTab));

                    fileSize.setText(container.getFileSize(currentTab));
                    fileModified.setText(container.getLastModifiedDate(currentTab));
                }
            });

            container.store(tab, file, bytes);
            currentTab = tab;
            tabPane.getTabs().add(tab);
        });
    }

    @Override
    public void onReadFail(File file, Exception exception) {
        String message;
        if(exception instanceof FileNotFoundException) {
            message = "Не удалось найти указанный файл";
        } else if(exception instanceof IOException) {
            message = "Произошла ошибка ввода/вывода";
        } else if(exception instanceof SecurityException) {
            message = "Доступ к файлу ограничен системными настройками";
        } else {
            message = "Произошла неизвестная ошибка";
        }

        Alerts.showErrorWithStackTrace(exception, "Ошибка чтения " + file.getName(), message);
    }

    @Override
    public void onMemoryLeak(long free, long need) {
        Alerts.showError(null, "Ошибка выделения памяти", "Не удалось прочитать выбранный файл:" +
                                         " для чтения необходимо " + need + " байт свободной ОЗУ, однако доступно " + free);
    }

    @Override
    public void handle(MouseEvent event) {
        String itemName;
        try {
            itemName = ((Text)event.getTarget()).getText();
        } catch(ClassCastException exception) {
            return;
        }

        if(itemName.equals("Открыть")) {
            onOpenClick();
        } else if(itemName.equals("Закрыть")) {
            onCloseClick();
        } else if(itemName.equals("О приложении")) {
            onAboutClick();
        } else if(itemName.equals("Выход")) {
            onExitClick();
        }
    }
}
