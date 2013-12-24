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
package me.ampayne2.dropparty.parties;

import me.ampayne2.dropparty.DPChest;
import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.config.ConfigType;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A party that spawns items taken from chests.
 */
public class ChestParty extends Party {
    private Set<DPChest> chests = new HashSet<>();

    /**
     * Creates a ChestParty from default settings.
     *
     * @param dropParty The DropParty instance.
     * @param partyName The name of the party.
     * @param teleport  The teleport location of the party.
     */
    public ChestParty(DropParty dropParty, String partyName, Location teleport) {
        super(dropParty, partyName, PartyType.CHEST_PARTY, teleport);
    }

    /**
     * Loads a ChestParty from a ConfigurationSection.
     *
     * @param dropParty The DropParty instance.
     * @param section   The ConfigurationSection.
     */
    public ChestParty(DropParty dropParty, ConfigurationSection section) {
        super(dropParty, section);
        List<String> dpChests = section.getStringList("chests");
        for (String chest : dpChests) {
            chests.add(DPChest.fromConfig(dropParty, this, chest));
        }
    }

    /**
     * Checks if the party has a chest.
     *
     * @param chest The chest.
     * @return True if the party has a chest, else false.
     */
    public boolean hasChest(Chest chest) {
        if (chest.getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
            for (DPChest dpChest : chests) {
                if (dpChest.getChest().equals(doubleChest.getLeftSide()) || dpChest.getChest().equals(doubleChest.getRightSide())) {
                    return true;
                }
            }
        } else {
            for (DPChest dpChest : chests) {
                if (dpChest.getChest().equals(chest)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds a chest.
     *
     * @param chest The chest.
     */
    public void addChest(DPChest chest) {
        chests.add(chest);
        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        String path = "Parties." + partyName + ".chests";
        List<String> dpChests = partyConfig.getStringList(path);
        dpChests.add(chest.toConfig());
        partyConfig.set(path, dpChests);
        dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
    }

    /**
     * Removes a chest.
     *
     * @param chest The chest.
     */
    public void removeChest(Chest chest) {
        for (DPChest dpChest : new HashSet<>(chests)) {
            if (dpChest.getChest().equals(chest)) {
                chests.remove(dpChest);
                FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
                String path = "Parties." + partyName + ".chests";
                List<String> dpChests = partyConfig.getStringList(path);
                dpChests.remove(dpChest.toConfig());
                partyConfig.set(path, dpChests);
                dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
            }
        }
    }

    /**
     * Gets the chests.
     *
     * @return The chests items are taken from during a party.
     */
    public Set<DPChest> getChests() {
        return chests;
    }

    @Override
    public boolean dropNext() {
        if (chests.size() > 0 && itemPoints.size() > 0) {
            ItemStack itemStack = null;
            for (DPChest chest : chests) {
                itemStack = chest.getNextItemStack();
                if (itemStack != null) {
                    break;
                }
            }
            return itemStack != null && dropItemStack(itemStack);
        } else {
            return false;
        }
    }

    @Override
    public void save(ConfigurationSection section) {
        super.save(section);
        List<String> dpChests = new ArrayList<>();
        for (DPChest chest : chests) {
            dpChests.add(chest.toConfig());
        }
        section.set("chests", dpChests);
    }
}
