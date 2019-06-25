package org.kllbff.hexviewer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kllbff.hexviewer.ui.UIController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static final ExecutorService workloadThread = Executors.newFixedThreadPool(2);

    private Label fileSize, fileModified;
    private TextArea stringView;
    private TabPane hexTabPaneView;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));

        stringView = (TextArea)root.lookup("#string_view");
        hexTabPaneView = (TabPane)root.lookup("#hex_view_tabs_pane");
        fileSize = (Label)root.lookup("#file_size");
        fileModified = (Label)root.lookup("#file_modified");

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Hex Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest((e) -> {
            workloadThread.shutdown();
            System.exit(0);
        });

        UIController controller = UIController.initialize(primaryStage, stringView, hexTabPaneView, fileSize, fileModified);
        MenuBar menuBar = (MenuBar)root.lookup("#top_menu");
        menuBar.addEventFilter(MouseEvent.MOUSE_PRESSED, controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
