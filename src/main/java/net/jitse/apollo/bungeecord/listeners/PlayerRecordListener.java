package net.jitse.apollo.bungeecord.listeners;

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
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.util.logging.Level;

/**
 * @auhor Jitse B.
 * @since 12.07.2017
 */
public class PlayerRecordListener implements Listener {

    private final ApolloBungeeCord plugin;

    public PlayerRecordListener(ApolloBungeeCord plugin) {
        this.plugin = plugin;
    }

    @EventHandler // Called when successfully joined a server.
    public void onUserConnected(ServerConnectedEvent event) {
        plugin.getMySql().select("SELECT Record FROM ApolloStats;", resultSet -> {
            try {
                int now = plugin.getProxy().getOnlineCount();
                if (resultSet.next()) {
                    int record = resultSet.getInt("Record");
                    if (now > record) {
                        plugin.getMySql().execute("UPDATE ApolloStats SET Record=?;", now);
                    }
                } else {
                    plugin.getMySql().execute("INSERT INTO ApolloStats VALUES(?);", now);
                }
            } catch (SQLException exception) {
                plugin.getLogger().log(Level.SEVERE, "Could not grab player record! Message: " + exception.getMessage());
            }
        });
    }
}
