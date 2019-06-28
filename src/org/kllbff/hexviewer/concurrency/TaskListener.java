package org.kllbff.hexviewer.concurrency;

import java.io.File;

/**
 * Позволяет прослушивать события задачи чтения, исполняемой в другом потоке
 * 
 * @author Кирилл Богатиков
 */
public interface TaskListener {
    /**
     * Вызывается по завершении чтения файла
     * 
     * @param file читаемый файл
     * @param bytes содержимое файла
     */
    public void onReadSuccess(File file, byte[] bytes);
    
    /**
     * Вызывается в случае возникновения исключительной ситуации при чтении
     * 
     * @param file читаемый файл
     * @param exception исключительная ситуация
     */
    public void onReadFail(File file, Exception exception);
    
    /**
     * Вызывается в случае обнаружения нехватки памяти
     * 
     * @param free объем свободной памяти, в байтах
     * @param need объем необходимой памяти, в байтах
     */
    public void onMemoryLeak(long free, long need);
    
    public void onWriteSuccess(File file);
    
    public void onWriteFail(File file, Exception e);
}
