package net.jitse.apollo.spigot.tasks;

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
import net.jitse.apollo.spigot.ApolloSpigot;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @auhor Jitse B.
 * @since 11.05.2017
 */
public class InitialDataUpdater extends BukkitRunnable {

    private final ApolloSpigot plugin;

    public InitialDataUpdater(ApolloSpigot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getMySQL().select("SELECT ID FROM ApolloServers WHERE Port=?;", resultSet -> {
            try {
                if (resultSet.next()) {
                    plugin.setId(resultSet.getInt("ID"));
                    update();
                } else {
                    insert();
                }
            } catch (SQLException exception) {
                plugin.getLogger().log(Level.WARNING, "Could not grab server ID from table.");
            }
        }, Bukkit.getPort());
    }

    private void update() {
        String update = Arrays.asList(DataType.values())
                .stream().filter(value -> value != DataType.ID)
                .map(value -> "`" + value.getSQLName() + "`=?")
                .collect(Collectors.joining(", "));
        String statement = "UPDATE ApolloServers SET " + update + " WHERE ID=?;";

        Object[] values = new Object[DataType.values().length];
        values[DataType.values().length - 1] = plugin.getId();
        
        setValues(values);

        plugin.getMySQL().execute(statement, values);
    }

    private void insert() {
        String columns = Arrays.asList(DataType.values())
                .stream().filter(value -> value != DataType.ID)
                .map(value -> value.getSQLName())
                .collect(Collectors.joining(", "));
        String statement = "INSERT INTO ApolloServers(" + columns + ") VALUES(" + StringUtils.repeat("?", ",", DataType.values().length - 1) + ");";

        Object[] values = new Object[DataType.values().length - 1];

        setValues(values);

        plugin.getMySQL().execute(statement, () -> {
            plugin.getMySQL().select("SELECT ID FROM ApolloServers WHERE Port=?;", resultSet -> {
                try {
                    if (resultSet.next()) {
                        plugin.setId(resultSet.getInt("ID"));
                        update();
                    } else {
                        insert();
                    }
                } catch (SQLException exception) {
                    plugin.getLogger().log(Level.WARNING, "Could not grab server ID from table.");
                }
            }, Bukkit.getPort());
        }, values);
    }

    private void setValues(Object[] values) {
        for (int i = 0; i < DataType.values().length; i++) {
            if (i == DataType.values().length - 1) {
                continue;
            }
            DataType type = DataType.values()[i + 1];

            if (type.getSupplier() == null) {
                switch (type) {
                    case NAME:
                        values[i] = null;
                        break;

                    case ONLINE:
                        values[i] = true;
                        break;

                    default:
                        throw new IllegalStateException("Could not handle data type of " + type.getSQLName() + ".");
                }
            } else {
                values[i] = type.getSupplier().get();
            }
        }
    }
}
