package com.example.s215087038.incubator.activity.helpers;

import android.os.Looper;

public class ThreadFactory {
    private static final int MAX_ACTIVE_THREADS_PER_SOURCE = 10;
    public static final String TAG = ThreadFactory.class.getName();
    private static int curThreadId = Integer.MAX_VALUE;

    private static class InnerThreadFactory implements java.util.concurrent.ThreadFactory {
        private ThreadGroup curGroup;
        private int curPriority;
        private String curSource;
        private int threadId = 0;

        private InnerThreadFactory() {
        }

        public synchronized Thread newThread(Runnable r) {
            Thread ret;
            ThreadGroup threadGroup = this.curGroup;
            StringBuilder append = new StringBuilder().append(this.curSource).append("-");
            int i = this.threadId;
            this.threadId = i + 1;
            ret = new Thread(threadGroup, r, append.append(Integer.toHexString(i)).toString());
            ret.setPriority(this.curPriority);
            return ret;
        }
    }

    public static boolean isThisMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void startNewThread(String source, Runnable runnable) {
        startNewThread(source, runnable, 5);
    }

    public static synchronized void startNewThread(String source, Runnable runnable, int priority) {
        synchronized (ThreadFactory.class) {
            Thread thread = new Thread(runnable);
            StringBuilder append = new StringBuilder().append(source).append("-");
            int i = curThreadId + 1;
            curThreadId = i;
            thread.setName(append.append(Integer.toHexString(i)).toString());
            thread.setPriority(priority);
            thread.start();
        }
    }
}
