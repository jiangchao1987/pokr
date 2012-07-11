package com.yanchuanli.games.pokr.core;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-11
 */
public class DiagSignalHandler implements SignalHandler {
    private SignalHandler oldHandler;

    // Static method to install the signal handler
    public static DiagSignalHandler install(String signalName) {
        Signal diagSignal = new Signal(signalName);
        DiagSignalHandler diagHandler = new DiagSignalHandler();
        diagHandler.oldHandler = Signal.handle(diagSignal, diagHandler);
        return diagHandler;
    }

    @Override
    public void handle(Signal signal) {
        System.out.println("Diagnostic Signal handler called for signal " + signal);
        try {
            // Output information for each thread
            Thread[] threadArray = new Thread[Thread.activeCount()];
            int numThreads = Thread.enumerate(threadArray);
            System.out.println("Current threads:");
            for (int i = 0; i < numThreads; i++) {
                System.out.println("    " + threadArray[i]);
            }

            // Chain back to previous handler, if one exists
            if (oldHandler != SIG_DFL && oldHandler != SIG_IGN) {
                oldHandler.handle(signal);
            }

        } catch (Exception e) {
            System.out.println("Signal handler failed, reason " + e);
        }
    }
}
