package net.jitse.apollo.spigot.tasks;

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

import net.jitse.apollo.datatype.DataType;
import net.jitse.apollo.spigot.ApolloSpigot;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @auhor Jitse B.
 * @since 11.04.2017
 */
public class DataUpdater implements Runnable {

    private final ApolloSpigot plugin;
    private final long millisDelay;

    private int startOffset;
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
        this.startOffset = networkSync ? 0 : ((int) (Math.random() * 10)) * 100;
        if (startOffset >= millisDelay) {
            // If the start offset is bigger than the actual timer delay
            // reduce the start delay by using the modulo function.
            this.startOffset %= millisDelay;
        }

        plugin.getLogger().log(Level.INFO, "Starting the data updater " + (networkSync ? "..." : "with a " + startOffset + "ms offset..."));
    }

    @Override
    public synchronized void run() {
        do {
            if ((System.currentTimeMillis() + startOffset) % millisDelay == 0) {
                plugin.getLogger().log(Level.INFO, "Started the data updater.");
                break;
            }

            // Now let's slow down the runnable by 1ms so it
            // becomes a bit more manageable for the server.
            // (System#currentTimeMillis only changes every 1ms anyway)
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
                // Prepare DataTypes that need an action.
                List<DataType> scheduledTypes = new ArrayList<>();
                List<DataType> graphTypes = new ArrayList<>();

                for (DataType type : DataType.values()) {
                    if (type.isScheduled()) {
                        scheduledTypes.add(type);
                    }
                    if (type.hasGraph()) {
                        graphTypes.add(type);
                    }
                }

                // Update server values.
                String set = scheduledTypes.stream().map(type -> "`" + type.getSQLName() + "`=?").collect(Collectors.joining(", "));
                List<Object> values = scheduledTypes.stream().map(type -> type.getSupplier().get()).collect(Collectors.toList());
                values.add(plugin.getId()); // For the WHERE statement.

                plugin.getMySQL().execute("UPDATE ApolloServers SET " + set + " WHERE ID=?;", values.toArray());

                // Now on to the graph data.
                // Plus 2 for the Server ID and System Time.
                Object[] graphData = new Object[graphTypes.size() + 2];
                graphData[0] = plugin.getId();
                graphData[1] = System.currentTimeMillis();

                for (int i = 0; i < graphTypes.size(); i++) {
                    graphData[i + 2] = graphTypes.get(i).getSupplier().get();
                }

                String insert = "INSERT INTO ApolloGraphs VALUES(" + StringUtils.repeat("?", ",", graphData.length) + ");";
                plugin.getMySQL().execute(insert, graphData);

                // Now remove the old values in the graph table.
                long maxSaved = 1000 * 60 * 60 * 24; // 24h (1000ms = 1s -> 1 * 60 = 1m -> 1 * 60 = 1h -> 1 * 24 = 24h)
                String delete = "DELETE FROM ApolloGraphs WHERE ID=? AND Time<=?;";
                plugin.getMySQL().execute(delete, plugin.getId(), System.currentTimeMillis() - maxSaved);
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
