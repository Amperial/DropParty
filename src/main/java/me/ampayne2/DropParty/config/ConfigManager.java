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

import java.io.File;

/**
 * Manages all of the extra configs.
 */
public class ConfigManager {
    private final ConfigAccessor messageConfig;
    private final ConfigAccessor partyConfig;
    private final ConfigAccessor chestConfig;
    private final ConfigAccessor itempointConfig;
    private final ConfigAccessor fireworkpointConfig;

    /**
     * Creates a new config manager.
     *
     * @param dropParty The DropParty instance.
     */
    public ConfigManager(DropParty dropParty) {
        File dataFolder = dropParty.getDataFolder();

        messageConfig = new ConfigAccessor(dropParty, "Messages.yml", dataFolder);
        messageConfig.saveDefaultConfig();
        partyConfig = new ConfigAccessor(dropParty, "Parties.yml", dataFolder);
        partyConfig.saveDefaultConfig();
        chestConfig = new ConfigAccessor(dropParty, "Chests.yml", dataFolder);
        chestConfig.saveDefaultConfig();
        itempointConfig = new ConfigAccessor(dropParty, "ItemPoints.yml", dataFolder);
        itempointConfig.saveDefaultConfig();
        fireworkpointConfig = new ConfigAccessor(dropParty, "FireworkPoints.yml", dataFolder);
        fireworkpointConfig.saveDefaultConfig();
    }

    /**
     * Gets the Message config.
     *
     * @return The Message config.
     */
    public ConfigAccessor getMessageConfig() {
        return messageConfig;
    }

    /**
     * Gets the Party config.
     *
     * @return The Party config.
     */
    public ConfigAccessor getPartyConfig() {
        return partyConfig;
    }

    /**
     * Gets the Chest config.
     *
     * @return The Chest config.
     */
    public ConfigAccessor getChestConfig() {
        return chestConfig;
    }

    /**
     * Gets the ItemPoint config.
     *
     * @return The ItemPoint config.
     */
    public ConfigAccessor getItempointConfig() {
        return itempointConfig;
    }

    /**
     * Gets the FireworkPoint config.
     *
     * @return The FireworkPoint config.
     */
    public ConfigAccessor getFireworkpointConfig() {
        return fireworkpointConfig;
    }
}
