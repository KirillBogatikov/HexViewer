package org.kllbff.hexviewer.concurrency;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadTask implements Runnable {
    private File file;
    private TaskListener listener;

    public ReadTask(File file, TaskListener listener) {
        this.file = file;
        this.listener = listener;
    }

    @Override
    public void run() {
        Runtime runtime = Runtime.getRuntime();
        int length = (int)file.length();
        long memory = runtime.maxMemory() - runtime.totalMemory();
        try {
            if(memory <= length) {
                throw new OutOfMemoryError("Failed to allocate " + length + " bytes, only " + memory + " available");
            }

            byte[] bytes = new byte[length];

            try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))) {
                int count = 0;
                do {
                    count += input.read(bytes, count, length - count);
                } while(count < length);
            }
            listener.onReadSuccess(file, bytes);
        } catch (IOException | SecurityException e) {
            listener.onReadFail(file, e);
        } catch(OutOfMemoryError e) {
            listener.onMemoryLeak(memory, length);
        }
    }
}
