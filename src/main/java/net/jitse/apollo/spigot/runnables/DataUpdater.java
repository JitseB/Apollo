package net.jitse.apollo.spigot.runnables;

/*
 * A server overview system for Minecraft networks.
 * Copyright (C) 2017  Jitse Boonstra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import net.jitse.apollo.spigot.ApolloSpigot;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

/**
 * @auhor Jitse B.
 * @since 11.04.2017
 */
public class DataUpdater implements Runnable {

    private final ApolloSpigot plugin;
    private final long millisDelay;

    private int startDelay;
    private Timer timer;

    public DataUpdater(ApolloSpigot plugin, boolean networkSync, long millisDelay) {
        this.plugin = plugin;
        this.millisDelay = millisDelay;

        // This is for balancing the query load on the database (if enabled)
        // It creates separate `channels` with different timings to send its
        // queries to the database.
        // Equation: ((INT) RANDOM * A) * B
        // A -> number of possible channels.
        // B -> size of steps.
        this.startDelay = networkSync ? 0 : ((int) (Math.random() * 10)) * 100;
        if (startDelay > millisDelay) {
            // If the start delay is bigger than the actual timer delay
            // reduce the start delay by using the modulo function.
            this.startDelay %= millisDelay;
        }
        Bukkit.broadcastMessage("" + startDelay);
    }

    @Override
    public synchronized void run() {
        do {
            if ((System.currentTimeMillis() + startDelay) % millisDelay == 0) {
                plugin.getLogger().log(Level.INFO, "Started data updater on " + new Date(System.currentTimeMillis()) + ".");
                break;
            }

            // Now let's slow down the runnable by 1ms so it
            // becomes a bit more manageable for the server.
            try {
                wait(1);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        } while (true);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Todo : Update ApolloServers.
            }
        }, 0, millisDelay);
    }

    public void purge() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
