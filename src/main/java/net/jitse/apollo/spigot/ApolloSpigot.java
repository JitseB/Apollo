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

import net.jitse.apollo.spigot.config.SpigotConfig;
import net.jitse.apollo.spigot.listeners.PlayerJoinListener;
import net.jitse.apollo.spigot.listeners.PlayerKickListener;
import net.jitse.apollo.spigot.listeners.PlayerQuitListener;
import net.jitse.apollo.spigot.runnables.DataUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * @auhor Jitse B.
 * @since 10.24.2017
 */
public class ApolloSpigot extends JavaPlugin {

    private SpigotConfig config;
    private DataUpdater dataUpdaterRunnable;
    private Thread dataUpdaterThread;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getLogger().log(Level.INFO, "Initialized event listeners.");

        config = new SpigotConfig(this);
        if (!config.init()) {
            getLogger().log(Level.INFO, "Please fill out the config.yml, then reload the server.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().log(Level.INFO, "Loaded config.yml.");

        // Todo : Change to config variables.
        dataUpdaterRunnable = new DataUpdater(this, false, 5000);
        dataUpdaterThread = new Thread(dataUpdaterRunnable);
        dataUpdaterThread.start();

        getLogger().log(Level.INFO, "Enabled " + getDescription().getName() + " v" + getDescription().getVersion() + ".");
    }

    @Override
    public void onDisable() {
        dataUpdaterRunnable.purge();
        dataUpdaterThread.interrupt();
    }
}
