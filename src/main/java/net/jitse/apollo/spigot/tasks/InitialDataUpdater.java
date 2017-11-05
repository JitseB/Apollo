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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
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
        String update = Arrays.asList(DataType.values()).stream().map(value -> "`" + value.getSQLName() + "`=?").collect(Collectors.joining(", "));
        String statement = "INSERT INTO ApolloServers VALUES(" + StringUtils.repeat("?", ",", DataType.values().length) + ") ON DUPLICATE KEY UPDATE " + update + ";";

        int length = DataType.values().length;
        Object[] values = new Object[DataType.values().length * 2];

        for (int i = 0; i < DataType.values().length; i++) {
            DataType type = DataType.values()[i];

            if (type.getSupplier() == null) {
                switch (type) {
                    case NAME:
                        values[i] = null;
                        values[i + length] = null;
                        break;

                    case ONLINE:
                        values[i] = true;
                        values[i + length] = true;
                        break;

                    default:
                        throw new IllegalStateException("Could not handle data type.");
                }
            } else {
                values[i] = type.getSupplier().get();
                values[i + length] = type.getSupplier().get();
            }
        }

        plugin.getMySQL().execute(statement, values);
        cancel();
    }
}
