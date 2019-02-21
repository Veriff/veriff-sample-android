package mobi.lab.veriff.sample.loging;

import android.text.TextUtils;

import mobi.lab.veriff.util.LogAccess;
import timber.log.Timber;

/**
 * Logging wrapper.
 */
public class Log implements LogAccess {

    private static
    @LogAccess.LogLevel
    String logLevel = Log.LOG_LEVEL_RELEASE_SILENT;

    private String tag;

    private Log(final String tag) {
        setLogLevel(Config.LOG_LEVEL);
        this.tag = tag;
    }

    /**
     * Method to get a new Log instance.
     *
     * @param object object to pass to Log instance.
     * @return Log instance.
     */
    public static Log getInstance(final Object object) {
        return getInstance(object.getClass());
    }

    /**
     * Method to get a new Log instance.
     *
     * @param clazz Class to pass to Log instance.
     * @return Log instance.
     */
    public static Log getInstance(final Class clazz) {
        return getInstance(clazz.getSimpleName());
    }

    /**
     * Method to get a new Log instance.
     *
     * @param tag tag to pass to Log instance.
     * @return Log instance.
     */
    public static Log getInstance(final String tag) {
        return new Log(tag);
    }

    /**
     * Method to set the logging Level.
     *
     * @param newLogLevel Logging level. Either {@link Log#LOG_LEVEL_DEBUG} or {@link Log#LOG_LEVEL_RELEASE_ERRORS_ONLY}.
     */
    public static void setLogLevel(final @LogLevel String newLogLevel) {
        if (!TextUtils.equals(logLevel, newLogLevel)) {
            Timber.uprootAll();
            if (LOG_LEVEL_DEBUG.equalsIgnoreCase(newLogLevel)) {
                Timber.plant(new Timber.DebugTree());
            } else {
                //noinspection StatementWithEmptyBody
                if (LOG_LEVEL_RELEASE_ERRORS_ONLY.equalsIgnoreCase(newLogLevel)) {
                    Timber.plant(new ErrorTree());
                } else {
                    // No trees
                }
            }
        }
        logLevel = newLogLevel;
    }

    @Override
    public void i(final String msg) {
        i(msg, null);
    }


    @Override
    public void i(final String msg, final Throwable e) {
        i(tag, msg, e);
    }

    @Override
    public void i(final String tag, final String msg, final Throwable e) {
        Timber.tag(tag);
        if (e == null) {
            Timber.i(msg);
        } else {
            Timber.i(e, msg);
        }
    }


    @Override
    public void d(final String msg) {
        d(msg, null);
    }


    @Override
    public void d(final String msg, final Throwable e) {
        d(tag, msg, e);
    }

    @Override
    public void d(final String tag, final String msg, final Throwable e) {
        Timber.tag(tag);
        if (e == null) {
            Timber.d(msg);
        } else {
            Timber.d(e, msg);
        }
    }


    @Override
    public void w(final String msg) {
        w(msg, null);
    }


    @Override
    public void w(final String msg, final Throwable e) {
        w(tag, msg, e);
    }

    @Override
    public void w(final String tag, final String msg, final Throwable e) {
        Timber.tag(tag);
        if (e == null) {
            Timber.w(msg);
        } else {
            Timber.w(e, msg);
        }
    }

    @Override
    public void e(final String msg) {
        e(msg, null);
    }


    @Override
    public void e(final String msg, final Throwable e) {
        e(tag, msg, e);
    }

    @Override
    public void e(final String tag, final String msg, final Throwable e) {
        Timber.tag(tag);
        if (e == null) {
            Timber.e(msg);
        } else {
            Timber.e(e, msg);
        }
    }

    @Override
    public void wtf(final String msg) {
        wtf(msg, null);
    }


    @Override
    public void wtf(final String msg, final Throwable e) {
        wtf(tag, msg, e);
    }

    @Override
    public void wtf(final String tag, final String msg, final Throwable e) {
        Timber.tag(tag);
        if (e == null) {
            Timber.wtf(msg);
        } else {
            Timber.wtf(e, msg);
        }
    }

}
