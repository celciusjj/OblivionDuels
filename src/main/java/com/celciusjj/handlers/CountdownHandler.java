package com.celciusjj.handlers;

import com.celciusjj.Main;
import org.bukkit.Bukkit;
import java.util.function.Consumer;

public class CountdownHandler implements Runnable {

    private Main plugin;

    private Integer assignedTaskId;

    private int seconds;
    private int secondsLeft;

    private Consumer<CountdownHandler> everySecond;
    private Runnable beforeTimer;
    private Runnable afterTimer;


    public CountdownHandler(Main plugin, int seconds, Runnable beforeTimer, Runnable afterTimer,
                            Consumer<CountdownHandler> everySecond) {
        // Initializing fields
        this.plugin = plugin;

        this.seconds = seconds;
        this.secondsLeft = seconds;

        this.beforeTimer = beforeTimer;
        this.afterTimer = afterTimer;
        this.everySecond = everySecond;
    }

    public CountdownHandler(Main plugin, int seconds, double alpha, Runnable beforeTimer, Runnable afterTimer,
                            Consumer<CountdownHandler> everySecond) {
        // Initializing fields
        this.plugin = plugin;

        this.seconds = seconds;
        this.secondsLeft = seconds;

        this.beforeTimer = beforeTimer;
        this.afterTimer = afterTimer;
        this.everySecond = everySecond;
    }

    @Override
    public void run() {
        // Is the timer up?
        if (secondsLeft < 1) {
            // Do what was supposed to happen after the timer
            afterTimer.run();

            // Cancel timer
            if (assignedTaskId != null)
                Bukkit.getScheduler().cancelTask(assignedTaskId);
            return;
        }

        // Are we just starting?
        if (secondsLeft == seconds)
            beforeTimer.run();

        // Do what's supposed to happen every second
        everySecond.accept(this);

        // Decrement the seconds left
        secondsLeft--;
    }

    public int getTotalSeconds() {
        return seconds;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void scheduleTimer() {
        // Initialize our assigned task's id, for later use so we can cancel
        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
    }

}
