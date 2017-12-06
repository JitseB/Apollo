package net.jitse.apollo.bungeecord.config;

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

import net.jitse.apollo.bungeecord.ApolloBungeeCord;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @auhor Jitse B.
 * @since 10.27.2017
 */
public class BungeeCordConfig {

    private final ApolloBungeeCord plugin;
    private final File file;
    private Configuration configuration;

    public BungeeCordConfig(ApolloBungeeCord plugin) throws IOException {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        if (file.exists()) {
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }
    }

    /**
     * Init the configuration system.
     *
     * @return boolean whether the config.yml already existed.
     */
    public boolean init() throws IOException {
        if (!file.exists()) {
            file.createNewFile();

            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            configuration.set("SocketPort", 8193);
            configuration.set("ServerListSize", 45);

            configuration.set("MySQL.Host", "127.0.0.1");
            configuration.set("MySQL.Port", 3306);
            configuration.set("MySQL.Username", "default");
            configuration.set("MySQL.Password", "unknown");
            configuration.set("MySQL.Database", "apollodb");
            configuration.set("MySQL.SSL", false);

            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
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
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
            return true;
        } catch (IOException exception) {
            plugin.getLogger().log(Level.WARNING, "Could not save config file. Exception message: " + exception.getMessage());
            return false;
        }
    }

    public Configuration getConfig() {
        return configuration;
    }
}
