package net.jitse.apollo.datatype;

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

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auhor Jitse B.
 * @since 11.04.2017
 */
public enum DataType {

    // BungeeCord variables:
    NAME("VARCHAR(255)", "Name", false),

    // Server tracking variables:
    ONLINE("TINYINT(1)", "Online", false),
    LAST_ALIVE("BIGINT(50)", "LastAlive", false),

    // Bukkit variables:
    PORT("INT(5)", "Port", false),
    ONLINE_MODE("TINYINT(1)", "OnlineMode", false),
    WHITELIST("TINYINT(1)", "Whitelist", false),
    ONLINE_PLAYERS("INT(5)", "OnlinePlayers", true),
    MAX_PLAYERS("INT(5)", "MaxPlayers", false),
    MOTD("TEXT", "MOTD", false),

    // Computed variables:
    TICKS_PER_SECOND("FLOAT(4)", "TPS", true),
    MEMORY_USED("INT(6)", "MemoryUsed", true),
    MEMORY_MAX("INT(6)", "MemoryMax", false);

    private String sqlType;
    private String sqlName;
    private boolean graph;

    DataType(String sqlType, String sqlName, boolean graph) {
        this.sqlType = sqlType;
        this.sqlName = sqlName;
        this.graph = graph;
    }

    public boolean hasGraph() {
        return graph;
    }

    public static String getTable() {
        List<String> values = Arrays.stream(values()).map(value -> "`" + value.sqlName + "` " + value.sqlType).collect(Collectors.toList());
        return "CREATE TABLE IF NOT EXISTS ApolloServers (" + StringUtils.join(values, ", ") + ");";
    }
}
