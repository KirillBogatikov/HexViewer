package org.kllbff.hexviewer.ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Вспомогательный класс, упрощающий создание диалоговых окон.<br>
 * Все методы класса могут вызываться из любого потока.
 * 
 * @author Кирилл Богатиков
 *
 */
public class Alerts {

    /**
     * Позволяет вывести на экран окно, сообщающее об ошибке и содержащее стек вызовов 
     * 
     * @param throwable ошибка или исключительная ситуация
     * @param title заголовок окна
     * @param message сообщение об ошибке
     */
    public static void showErrorWithStackTrace(Throwable throwable, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(message);

            VBox pane = new VBox();
            Label label = new Label("Стек вызовов:");
            TextArea area = new TextArea();
            area.setEditable(false);
            area.setText(getStackTrace(throwable));
            pane.getChildren().addAll(label, area);

            alert.getDialogPane().setContent(pane);
            alert.showAndWait();
        });
    }

    /**
     * Выводит окно, сообщающее об ошибке, но не содержащее стек вызовов<br>
     * Дополнительно может вывести локализованное сообщение, содержащееся в объекте
     * <code>throwable</code>, если этот объект не является <code>null</code>.
     * 
     * @param throwable ошибка или исключительная ситуация
     * @param title заголовок окна
     * @param message сообщение об ошибке
     */
    public static void showError(Throwable throwable, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(message);
            if(throwable != null) {
                alert.setContentText(throwable.getLocalizedMessage());
            }
            alert.showAndWait();
        });
    }

    /**
     * Позволяет вывести информационное сообщение
     *  
     * @param title заголовок окна
     * @param message сообщение об ошибке
     */
    public static void showInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }

    private static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
