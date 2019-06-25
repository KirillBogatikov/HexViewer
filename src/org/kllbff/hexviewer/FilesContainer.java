package org.kllbff.hexviewer;

import javafx.scene.control.Tab;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FilesContainer {
    private HashMap<Tab, Object[]> filesStore = new HashMap<>();
    private Charset charset = StandardCharsets.US_ASCII;

    public void store(Tab tab, File file, byte[] bytes) {
        filesStore.put(tab, new Object[]{ file, bytes });
    }

    public void useCharset(Charset charset) {
        this.charset = charset;
    }

    public String getStringContent(Tab tab) {
        return new String((byte[])filesStore.get(tab)[1], charset);
    }

    public String getFileSize(Tab tab) {
        File file = (File)(filesStore.get(tab)[0]);
        long size = file.length();
        if(size < 1024) {
            return size + " байт";
        }

        size /= 1024;
        if(size < 1024) {
            return size + " КБ";
        }

        size /= 1024;
        if(size < 1024) {
            return size + " МБ";
        }

        size /= 1024;
        return size + " ГБ";
    }

    public String getLastModifiedDate(Tab tab) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        File file = (File)(filesStore.get(tab)[0]);
        return sdf.format(new Date(file.lastModified()));
    }

    public void kick(Tab tab) {
        filesStore.remove(tab);
    }
}
