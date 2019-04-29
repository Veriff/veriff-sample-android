package com.veriff.demo.loging;

// Checkstyle off: RegexpSinglelineJava

import android.util.Log;

import timber.log.Timber;

// Checkstyle on: RegexpSinglelineJava

/**
 * //Checkstyle off: RegexpSinglelineJava
 * Release errors only logging tree. Only logs {@link android.util.Log#ERROR} and {@link android.util.Log#ASSERT} priority.
 * //Checkstyle on: RegexpSinglelineJava
 */
public class ErrorTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO || priority == Log.WARN) {
            return;
        }
        Log.println(priority, tag, message);
    }
}
