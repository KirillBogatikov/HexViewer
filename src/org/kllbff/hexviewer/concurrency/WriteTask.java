package org.kllbff.hexviewer.concurrency;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteTask implements Runnable {
    private File file;
    private byte[] bytes;
    private TaskListener listener;

    /**
     * Устанавливает читаемый файл и слушатель, используемый для оповещения о событиях
     * 
     * @param file читаемый файл
     * @param listener слушатель событий
     */
    public WriteTask(File file, byte[] bytes, TaskListener listener) {
        this.file = file;
        this.bytes = bytes;
        this.listener = listener;
    }
    
    @Override
    public void run() {
        try(BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
            output.write(bytes);
            listener.onWriteSuccess(file);
        } catch(IOException ioe) {
            ioe.printStackTrace();
            listener.onWriteFail(file, ioe);
        }
    }

}
