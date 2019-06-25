package org.kllbff.hexviewer.concurrency;

import java.io.File;

public interface TaskListener {
    public void onReadSuccess(File file, byte[] bytes);
    public void onReadFail(File file, Exception exception);
    public void onMemoryLeak(long free, long need);
}
