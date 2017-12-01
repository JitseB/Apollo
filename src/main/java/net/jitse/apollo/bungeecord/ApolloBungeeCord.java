package net.jitse.apollo.bungeecord;

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

import net.jitse.apollo.bungeecord.config.BungeeCordConfig;
import net.jitse.apollo.bungeecord.socket.SocketHandler;
import net.jitse.apollo.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.util.logging.Level;

/**
 * @auhor Jitse B.
 * @since 10.24.2017
 */
public class ApolloBungeeCord extends Plugin {

    private BungeeCordConfig config;
    private MySQL mySql;
    private SocketHandler socket;

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Enabling plugin " + getDescription().getName() + " version " + getDescription().getVersion() + " by " + getDescription().getAuthor());

        try {
            config = new BungeeCordConfig(this);
            if (!config.init()) {
                getLogger().log(Level.INFO, "Please fill out the config.yml, then restart the proxy.");
                return;
            }
        } catch (IOException exception) {
            getLogger().log(Level.WARNING, "An unexpected exception was thrown while getting the config.yml file. Exception message: " + exception.getMessage());
            return;
        }

        mySql = new MySQL(this);
        try {
            mySql.connect(
                    config.getConfig().getString("MySQL.Host"),
                    config.getConfig().getInt("MySQL.Port"),
                    config.getConfig().getString("MySQL.Username"),
                    config.getConfig().getString("MySQL.Password"),
                    config.getConfig().getString("MySQL.Database"),
                    config.getConfig().getBoolean("MySQL.SSL")
            );
        } catch (Exception exception) {
            getLogger().log(Level.WARNING, "Was not able to connect to the database. Exception message: " + exception.getMessage());
            return;
        }

        socket = new SocketHandler(this);
    }

    @Override
    public void onDisable() {
        if (socket != null) {
            socket.stop();
        }
    }

    public BungeeCordConfig getConfig() {
        return this.config;
    }

    public MySQL getMySql() {
        return this.mySql;
    }
}
