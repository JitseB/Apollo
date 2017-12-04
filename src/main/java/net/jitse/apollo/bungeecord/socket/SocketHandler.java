package net.jitse.apollo.bungeecord.socket;

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
 * Unless required by applicable law sor agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.s
 */

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.jitse.apollo.bungeecord.ApolloBungeeCord;
import net.jitse.apollo.datatype.DataType;

import java.sql.SQLException;

/**
 * @auhor Jitse B.
 * @since 11.23.2017
 */
public class SocketHandler {

    private SocketIOServer server;

    public SocketHandler(ApolloBungeeCord plugin) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPingInterval(5000);
        config.setPingTimeout(10000);
        config.setPort(plugin.getConfig().getConfig().getInt("SocketPort"));
        server = new SocketIOServer(config);

        server.addEventListener("server_list", null, (client, data, ackRequest) -> {
            int limit = plugin.getConfig().getConfig().getInt("ServerListSize");
            plugin.getMySql().select("SELECT * FROM ApolloServers WHERE Name IS NOT NULL LIMIT " + limit + ";", resultSet -> {
                try {
                    JsonArray servers = new JsonArray();

                    while (resultSet.next()) {
                        JsonObject serverData = new JsonObject();

                        for (DataType type : DataType.values()) {
                            Object value = resultSet.getObject(type.getSQLName());
                            if (value instanceof String) {
                                serverData.addProperty(type.getSQLName(), (String) value);
                            } else if (value instanceof Boolean) {
                                serverData.addProperty(type.getSQLName(), (Boolean) value);
                            } else if (value instanceof Number) {
                                serverData.addProperty(type.getSQLName(), (Number) value);
                            } else {
                                throw new IllegalStateException("Invalid primitive type for " + type.toString() + "!");
                            }
                        }
                        servers.add(serverData);
                    }

                    client.sendEvent("server_list", "", servers.toString());
                } catch (SQLException | IllegalStateException exception) {
                    client.sendEvent("server_list", exception.getMessage());
                }
            });
        });

        server.start();
    }

    public void stop() {
        server.stop();
    }
}
