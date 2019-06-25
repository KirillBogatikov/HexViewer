package org.kllbff.hexviewer.ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class Alerts {

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
