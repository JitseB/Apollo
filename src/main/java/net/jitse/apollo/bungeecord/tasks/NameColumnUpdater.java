package net.jitse.apollo.bungeecord.tasks;

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
import net.md_5.bungee.api.config.ServerInfo;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @auhor Jitse B.
 * @since 12.02.2017
 */
public class NameColumnUpdater {

    private ApolloBungeeCord plugin;

    public NameColumnUpdater(ApolloBungeeCord plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getProxy().getScheduler().schedule(plugin, () ->
                plugin.getProxy().getScheduler().runAsync(plugin, () ->
                        plugin.getMySql().select("SELECT Port FROM ApolloServers WHERE Name IS NULL;", resultSet -> {
                            try {
                                int total = 0;
                                while (resultSet.next()) {
                                    int port = resultSet.getInt("Port");
                                    ServerInfo serverInfo = getServerInfoByPort(port);
                                    if (serverInfo == null) {
                                        continue;
                                    }
                                    plugin.getMySql().execute("UPDATE ApolloServers SET Name=? WHERE Port=?;", serverInfo.getName(), port);
                                    total++;
                                }
                                if (total > 0) {
                                    plugin.getLogger().log(Level.INFO, "Updated the server name of " + total + " server(s).");
                                }
                            } catch (SQLException exception) {
                                exception.printStackTrace();
                            }
                        })), 0, 1, TimeUnit.MINUTES);
    }

    private ServerInfo getServerInfoByPort(int port) {
        for (ServerInfo info : plugin.getProxy().getServers().values()) {
            if (info.getAddress().getPort() == port) {
                return info;
            }
        }
        return null;
    }
}
