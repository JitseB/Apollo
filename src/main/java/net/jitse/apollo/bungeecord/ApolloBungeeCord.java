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
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.util.logging.Level;

/**
 * @auhor Jitse B.
 * @since 10.24.2017
 */
public class ApolloBungeeCord extends Plugin {

    private BungeeCordConfig config;

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
        }

        // todo
    }
}
