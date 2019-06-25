package org.kllbff.hexviewer;

import javafx.scene.control.Tab;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Позволяет хранить и получать специфичную информацию об открытых в приложении файлах
 * 
 * @author Кирилл Богатиков
 */
public class FilesContainer {
    private HashMap<Tab, Object[]> filesStore = new HashMap<>();
    private Charset charset = StandardCharsets.US_ASCII;

    /**
     * Добавляет новый файл в хранилище
     * 
     * @param tab вкладка файла
     * @param file файл
     * @param bytes содержимое файла
     */
    public void store(Tab tab, File file, byte[] bytes) {
        filesStore.put(tab, new Object[]{ file, bytes });
    }

    /**
     * Устанавливает новую кодировку
     * 
     * @param charset кодировка
     */
    public void useCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * Возвращает строковое представление файла в заданной ранее кодировке 
     * 
     * @param tab вкладка файла
     * @return строковое представление файла
     */
    public String getStringContent(Tab tab) {
        return new String((byte[])filesStore.get(tab)[1], charset);
    }

    /**
     * Возвращает человеко-понятный размер файла. В завсимости от величины
     * единицы измерения также варьируются от байт до гигабайт
     * 
     * @param tab вкладка файла
     * @return размер файла
     */
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

    /**
     * Возвращает дату последнего изменения файла в человеко-понятной форме ДД.ММ.ГГГГ
     * 
     * @param tab вкладка файла
     * @return дату последнего изменения файла
     */
    public String getLastModifiedDate(Tab tab) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        File file = (File)(filesStore.get(tab)[0]);
        return sdf.format(new Date(file.lastModified()));
    }

    /**
     * Удаляет файл из хранилища, высвобождая занятую его содержимым ОЗУ
     * 
     * @param tab вкладка файла
     */
    public void kick(Tab tab) {
        filesStore.remove(tab);
    }
}
