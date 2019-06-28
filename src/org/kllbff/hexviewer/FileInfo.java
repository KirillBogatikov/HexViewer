package org.kllbff.hexviewer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class FileInfo {
    private File file;
    private byte[] bytes;
    private boolean changed;
    
    public FileInfo(File file, byte[] bytes) {
        this.file = file;
        this.bytes = bytes;
        this.changed = false;
    }

    public File getFile() {
        return file;
    }

    public byte[] getBytes() {
        return bytes;
    }
    
    public void setByte(int index, byte b) {
        bytes[index] = b;
        this.changed = true;
    }
    
    public int addByte() {
        changed = true;
        bytes = Arrays.copyOf(bytes, bytes.length + 1);
        return bytes.length - 1;
    }
    
    public boolean isChanged() {
        return changed;
    }
    
    public void applyChanges() {
        this.changed = false;
    }
    
    /**
     * Возвращает человеко-понятный размер файла. В завсимости от величины
     * единицы измерения также варьируются от байт до гигабайт
     * 
     * @return размер файла
     */
    public String getFileSize() {
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

    /**
     * Возвращает дату последнего изменения файла в человеко-понятной форме ДД.ММ.ГГГГ
     * 
     * @return дату последнего изменения файла
     */
    public String getLastModifiedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(new Date(file.lastModified()));
    }
}
