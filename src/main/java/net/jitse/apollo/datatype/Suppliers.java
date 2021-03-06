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

import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * @auhor Jitse B.
 * @since 11.05.2017
 */
class Suppliers {

    private static final long MEGABYTE = 1024L * 1024L;
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("##.##", DecimalFormatSymbols.getInstance(Locale.US));
    private static String version;
    private static Class<?> minecraftServer;

    static {
        try {
            Class.forName("org.bukkit.Bukkit");
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            minecraftServer = Class.forName("net.minecraft.server." + version + ".MinecraftServer");
        } catch (ClassNotFoundException ignored) {
        }
    }

    protected static Supplier<?> getTPSSupplier() {
        return () -> {
            try {
                Object server = minecraftServer.getMethod("getServer").invoke(null);
                double currentTps = ((double[]) server.getClass().getField("recentTps").get(server))[0];
                return currentTps > 20 ? 20.00 : NUMBER_FORMAT.format(currentTps);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException ignored) {
            }
            return 00.00;
        };
    }

    protected static Supplier<?> getMemoryUsedSupplier() {
        return () -> {
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long used = runtime.totalMemory() - runtime.freeMemory();
            return (int) (used / MEGABYTE); // Return in MBs.
        };
    }

    protected static Supplier<?> getMemoryMaxSupplier() {
        return () -> {
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            return (int) (runtime.totalMemory() / MEGABYTE); // Return in MBs.
        };
    }

    protected static Supplier<?> getPortSupplier() {
        try {
            Class.forName("org.bukkit.Bukkit");
            return Bukkit::getPort;
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }

    protected static Supplier<?> getOnlineModeSupplier() {
        try {
            Class.forName("org.bukkit.Bukkit");
            return Bukkit::getOnlineMode;
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }

    protected static Supplier<?> getWhitelistSupplier() {
        try {
            Class.forName("org.bukkit.Bukkit");
            return Bukkit::hasWhitelist;
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }

    protected static Supplier<?> getOnlinePlayersSupplier() {
        try {
            Class.forName("org.bukkit.Bukkit");
            return Bukkit.getOnlinePlayers()::size;
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }

    protected static Supplier<?> getMaxPlayersSupplier() {
        try {
            Class.forName("org.bukkit.Bukkit");
            return Bukkit::getMaxPlayers;
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }

    protected static Supplier<?> getMotdSupplier() {
        try {
            Class.forName("org.bukkit.Bukkit");
            return Bukkit::getMotd;
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }
}
