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
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @auhor Jitse B.
 * @since 11.04.2017
 */
public enum DataType {

    // Server identifier:
    ID("INT PRIMARY KEY AUTO_INCREMENT NOT NULL", "ID", false, false, null),

    // BungeeCord variable:
    NAME("VARCHAR(255)", "Name", false, false, null),

    // Server tracking variables:
    ONLINE("TINYINT(1) NOT NULL", "Online", false, false, null),
    LAST_ALIVE("BIGINT(50) NOT NULL", "LastAlive", true, false, () -> System.currentTimeMillis()),

    // Bukkit variables:
    PORT("INT(5) NOT NULL", "Port", false, false, Suppliers.getPortSupplier()), // Primary key
    ONLINE_MODE("TINYINT(1) NOT NULL", "OnlineMode", false, false, Suppliers.getOnlineModeSupplier()),
    WHITELIST("TINYINT(1) NOT NULL", "Whitelist", false, false, Suppliers.getWhitelistSupplier()),
    ONLINE_PLAYERS("INT(5) NOT NULL", "OnlinePlayers", true, true, Suppliers.getOnlinePlayersSupplier()),
    MAX_PLAYERS("INT(5) NOT NULL", "MaxPlayers", false, false, Suppliers.getMaxPlayersSupplier()),
    MOTD("TEXT NOT NULL", "MOTD", false, false, Suppliers.getMotdSupplier()),

    // Computed variables:
    TICKS_PER_SECOND("FLOAT(4) NOT NULL", "TPS", true, true, Suppliers.getTPSSupplier()),
    MEMORY_USED("INT(6) NOT NULL", "MemoryUsed", true, true, Suppliers.getMemoryUsedSupplier()),
    MEMORY_MAX("INT(6) NOT NULL", "MemoryMax", true, false, Suppliers.getMemoryMaxSupplier());

    private final String sqlType;
    private final String sqlName;
    private final boolean schedule;
    private final boolean graph;
    private final Supplier<?> supplier;

    DataType(String sqlType, String sqlName, boolean schedule, boolean graph, Supplier<?> supplier) {
        this.sqlType = sqlType;
        this.sqlName = sqlName;
        this.schedule = schedule;
        this.graph = graph;
        this.supplier = supplier;
    }

    public String getSQLName() {
        return sqlName;
    }

    public boolean hasGraph() {
        return graph;
    }

    public boolean isScheduled() {
        return schedule;
    }

    public Supplier<?> getSupplier() {
        return supplier;
    }

    public static String getServersTable() {
        List<String> values = Arrays.stream(values()).map(value -> "`" + value.sqlName + "` " + value.sqlType).collect(Collectors.toList());
        return "CREATE TABLE IF NOT EXISTS ApolloServers (" + StringUtils.join(values, ", ") + ");";
    }

    public static String getGraphsTable() {
        List<String> values = Arrays.stream(values()).filter(value -> value.hasGraph()).map(value -> "`" + value.sqlName + "` " + value.sqlType).collect(Collectors.toList());
        return "CREATE TABLE IF NOT EXISTS ApolloGraphs (ID INT NOT NULL, Time BIGINT(50), " + StringUtils.join(values, ", ") + ");";
    }
}
