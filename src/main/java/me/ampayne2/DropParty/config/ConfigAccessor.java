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
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Used to access a YamlConfiguration file.
 */
public class ConfigAccessor {
    private final DropParty dropParty;
    private final String fileName;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    /**
     * Creates a new Config Accessor.
     *
     * @param dropParty The DropParty instance.
     * @param fileName  The name of the configuration file.
     * @param parent    The parent file.
     */
    public ConfigAccessor(DropParty dropParty, String fileName, File parent) {
        this.dropParty = dropParty;
        this.fileName = fileName;
        this.configFile = new File(parent, fileName);
    }

    /**
     * Reloads the configuration file from disk.
     */
    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = dropParty.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    /**
     * Gets the config.
     *
     * @return The FileConfiguration.
     */
    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    /**
     * Saves the config to disk.
     */
    public void saveConfig() {
        if (fileConfiguration != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException e) {
                dropParty.getMessage().log(Level.SEVERE, "Could not save config to " + configFile);
                dropParty.getMessage().debug(e);
            }
        }
    }

    /**
     * Generates the default config if it hasn't already been generated.
     */
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            dropParty.saveResource(fileName, false);
        }
    }
}
