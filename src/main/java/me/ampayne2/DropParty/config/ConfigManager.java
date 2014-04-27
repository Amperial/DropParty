/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2013 <http://dev.bukkit.org/server-mods/dropparty//>
 *
 * DropParty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DropParty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DropParty.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.dropparty.config;

import me.ampayne2.dropparty.DropParty;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all of the custom drop party configs.
 */
public class ConfigManager {
    private final Map<ConfigType, ConfigAccessor> configs = new HashMap<>();

    /**
     * Creates a new config manager.
     *
     * @param dropParty The DropParty instance.
     */
    public ConfigManager(DropParty dropParty) {
        File dataFolder = dropParty.getDataFolder();

        dropParty.getConfig().options().copyDefaults(true);
        dropParty.saveConfig();
        for (ConfigType configType : ConfigType.class.getEnumConstants()) {
            addConfigAccessor(new ConfigAccessor(dropParty, configType, dataFolder).saveDefaultConfig());
        }
    }

    /**
     * Adds a ConfigAccessor to the config manager.
     *
     * @param configAccessor The ConfigAccessor.
     */
    public void addConfigAccessor(ConfigAccessor configAccessor) {
        configs.put(configAccessor.getConfigType(), configAccessor);
    }

    /**
     * Gets a certain ConfigAccessor.
     *
     * @param configType The type of the config.
     * @return The config accessor.
     */
    public ConfigAccessor getConfigAccessor(ConfigType configType) {
        return configs.get(configType);
    }

    /**
     * Gets a certain FileConfiguration.
     *
     * @param configType The type of the config.
     * @return The config.
     */
    public FileConfiguration getConfig(ConfigType configType) {
        return configs.get(configType).getConfig();
    }
}
