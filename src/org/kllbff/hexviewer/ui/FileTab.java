package org.kllbff.hexviewer.ui;

import org.kllbff.hexviewer.FileInfo;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

public class FileTab extends Tab {
    
    public FileTab(FileInfo fileInfo) {
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPrefWidth(600.0);
        
        GridPane pane = new BytesGrid(fileInfo);
        pane.setPadding(new Insets(5));
        pane.setPrefWidth(575.0);
        scroll.setContent(pane);
        setContent(scroll);
        
        setText(fileInfo.getFile().getName());
    }
}   
