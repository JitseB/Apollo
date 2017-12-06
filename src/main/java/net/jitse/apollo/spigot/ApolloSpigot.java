package net.jitse.apollo.spigot;

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
import net.jitse.apollo.mysql.MySQL;
import net.jitse.apollo.spigot.config.SpigotConfig;
import net.jitse.apollo.spigot.tasks.DataUpdater;
import net.jitse.apollo.spigot.tasks.InitialDataUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * @auhor Jitse B.
 * @since 10.24.2017
 */
public class ApolloSpigot extends JavaPlugin {

    private int id;
    private SpigotConfig config;
    private MySQL mySQL;
    private DataUpdater dataUpdaterRunnable;
    private Thread dataUpdaterThread;

    @Override
    public void onEnable() {
        config = new SpigotConfig(this);
        if (!config.init()) {
            getLogger().log(Level.INFO, "Please fill out the config.yml, then reload the server.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().log(Level.INFO, "Loaded config.yml.");

        mySQL = new MySQL(this);
        try {
            mySQL.connect(
                    config.getConfig().getString("MySQL.Host"),
                    config.getConfig().getInt("MySQL.Port"),
                    config.getConfig().getString("MySQL.Username"),
                    config.getConfig().getString("MySQL.Password"),
                    config.getConfig().getString("MySQL.Database"),
                    config.getConfig().getBoolean("MySQL.SSL")
            );
        } catch (Exception exception) {
            // Not sure what possible exceptions can be thrown here,
            // need to update this later to something more specific.
            getLogger().log(Level.WARNING, "Could not connect to database. Exception message: " + exception.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        mySQL.execute(DataType.getServersTable(), () -> mySQL.execute(DataType.getGraphsTable(), () -> new InitialDataUpdater(this).runTaskAsynchronously(this)));

        // Todo : Change to config variables.
        dataUpdaterRunnable = new DataUpdater(this, false, 5000);
        dataUpdaterThread = new Thread(dataUpdaterRunnable);
        dataUpdaterThread.start();

        getLogger().log(Level.INFO, "Enabled " + getDescription().getName() + " v" + getDescription().getVersion() + ".");
    }

    @Override
    public void onDisable() {
        if (dataUpdaterRunnable != null) {
            dataUpdaterRunnable.purge();
        }
        if (dataUpdaterThread != null) {
            dataUpdaterThread.interrupt();
        }
    }

    public void setId(int id) {
        getLogger().log(Level.INFO, "Found server identifier (" + id + ").");
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public MySQL getMySQL() {
        return mySQL;
    }
}
