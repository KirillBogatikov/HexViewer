package org.kllbff.hexviewer.ui;

import org.kllbff.hexviewer.FileInfo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class BytesGrid extends GridPane {
    public BytesGrid(FileInfo info) {
        this.setOnMouseClicked((event) -> {
            int index = info.addByte();
            
            TextField byteField = new TextField("00");
            byteField.setPadding(new Insets(5, 2, 5, 2));
            byteField.setAlignment(Pos.CENTER);
            byteField.setPrefWidth(80.0);
            add(byteField, index % 16, index / 16);
        });
        
        byte[] bytes = info.getBytes();
        for(int i = 0; i < bytes.length; i++) {
            String byteString = Integer.toHexString(bytes[i] < 0 ? bytes[i] + 255 : bytes[i]);
            while (byteString.length() < 2) {
                byteString = "0" + byteString;
            }
            
            TextField byteField = new TextField(byteString);
            byteField.setPadding(new Insets(5, 2, 5, 2));
            byteField.setAlignment(Pos.CENTER);
            byteField.setPrefWidth(80.0);
            
            final int index = i;
            byteField.setOnKeyTyped((event) -> {
                if(event.getCharacter().matches("[0-9A-F]") && byteField.getLength() < 2) {
                    String newText = byteField.getText() + event.getCharacter();
                    info.setByte(index, (byte)Integer.parseInt(newText, 16));
                } else {
                    event.consume();
                }
            });
            
            add(byteField, index % 16, index / 16);
        }
    }
}
