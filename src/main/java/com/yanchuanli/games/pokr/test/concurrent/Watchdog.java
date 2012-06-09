package com.yanchuanli.games.pokr.test.concurrent;

import org.apache.log4j.Logger;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-4
 */

public class Watchdog extends Thread {

    private static Logger log = Logger.getLogger(Watchdog.class);

    private Thread watched;
    private int sleep;
    private boolean[] semaphore;
    private int length;

    private boolean ok;
    private boolean run = true;

    public Watchdog(final Thread watched, final int sleep, final boolean[] semaphore) {
        this(null, watched, sleep, semaphore);
    }

    public Watchdog(final ThreadGroup threadGroup, final Thread watched, final int sleep, final boolean[] semaphore) {
        super(threadGroup, "Watching " + watched);
        this.watched = watched;
        this.sleep = sleep;
        this.semaphore = semaphore;
        length = semaphore.length;
        setDaemon(true);
    }

    public void run() {
        while (run && watched.isAlive()) {
            ok = true;
            for (int i = 0; i < length; i++) {
                if (!semaphore[i]) {
                    try {
                        sleep(sleep);
                    } catch (InterruptedException e) {
                        log.error(e);
                    }
                }
                if (!run) {
                    ok = true;
                    break;
                }
                ok = semaphore[i] && ok;
                semaphore[i] = false;
            }
            if (!ok) {
                trigger();
            }
        }
    }

    /**
     * invoke this method to trigger the watchdog regardless of the state of the semaphore.
     * Note that the <CODE>@warn()</CODE> method is invoked before the
     * watched thread is killed, and the result is still heeded.
     *
     * @see #warn
     */
    public void trigger() {
        if (warn()) {
            watched.stop();
            killed();
        }
    }

    /**
     * invoked before the watchdog terminates the thread it is watching.
     * The default implementation returns <CODE>true</CODE>.  Override this
     * method if you might want to cancel the shutdown of the watched thread.
     * <p/>
     * In the event that a shutdown is cancelled, the watchdog will go back
     * to sleep, then check the semaphores again after the sleep time.
     *
     * @return whether to continue shutting down the watched thread.  Return
     *         <CODE>true</CODE> to continue the shutdown.  Return
     *         <CODE>false</CODE> to cancel the shutdown.
     */
    protected boolean warn() {
        return true;
    }

    /**
     * invoked after the watched thread has been killed.  The default
     * implementation does nothing. Subclasses can use this as an
     * opportunity to clean up after the killed thread by closing streams,
     * removing temporary files, etc.
     */
    protected void killed() {
        /* does nothing */
    }

    /**
     * invoke this method to stop the watchdog in a clean manner.  The
     * watched thread will be left alone, and the watchdog will exit.
     */
    public void cease() {
        run = false;
        interrupt();
    }
}
