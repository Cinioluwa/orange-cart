package com.ocart.orange_cart.util;

import javafx.animation.AnimationTimer;

/**
 * Simple utility to help diagnose JavaFX pulse issues. Call PulseMonitor.start() once
 * (e.g., from your Application start()) and it will print a line every ~60 pulses
 * so you can confirm the UI thread is alive. If it stops printing while app is "frozen",
 * the FX Application Thread is blocked by some operation.
 */
public final class PulseMonitor {

    private static boolean started = false;

    public static void start() {
        if (started) return;
        started = true;
        AnimationTimer timer = new AnimationTimer() {
            private long count = 0;
            @Override
            public void handle(long now) {
                count++;
                if (count % 60 == 0) { // roughly once per second at 60fps
                    System.out.println("[PulseMonitor] Pulses OK (" + count + ") Thread=" + Thread.currentThread().getName());
                }
            }
        };
        timer.start();
    }
}