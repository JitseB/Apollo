package net.jitse.apollo.spigot.config;

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
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @auhor Jitse B.
 * @since 10.26.2017
 */
public class SpigotConfig {

    private final ApolloSpigot plugin;
    private final File file;
    private final YamlConfiguration configuration;

    public SpigotConfig(ApolloSpigot plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Init the configuration system.
     *
     * @return boolean whether the config.yml already existed.
     */
    public boolean init() {
        if (!file.exists()) {
            configuration.set("SocketPort", 8193);

            configuration.set("MySQL.Host", "127.0.0.1");
            configuration.set("MySQL.Port", 3306);
            configuration.set("MySQL.Username", "default");
            configuration.set("MySQL.Password", "unknown");
            configuration.set("MySQL.Database", "apollodb");

            try {
                configuration.save(file);
            } catch (IOException exception) {
                plugin.getLogger().log(Level.WARNING, "Could not save config file. Exception message: " + exception.getMessage());
            }

            return false;
        } else {
            return true;
        }
    }

    public boolean save() {
        try {
            configuration.save(file);
            return true;
        } catch (IOException exception) {
            plugin.getLogger().log(Level.WARNING, "Could not save config file. Exception message: " + exception.getMessage());
            return false;
        }
    }

    public YamlConfiguration getConfig() {
        return configuration;
    }
}
