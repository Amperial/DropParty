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
package me.ampayne2.dropparty;

import me.ampayne2.dropparty.config.ConfigType;
import me.ampayne2.dropparty.parties.ChestParty;
import me.ampayne2.dropparty.parties.CustomParty;
import me.ampayne2.dropparty.parties.Party;
import me.ampayne2.dropparty.parties.PartyType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all of the drop parties.
 */
public class PartyManager {
    private final DropParty dropParty;
    private Map<String, Party> parties = new HashMap<>();

    /**
     * Creates a new party manager.
     *
     * @param dropParty The DropParty instance.
     */
    public PartyManager(DropParty dropParty) {
        this.dropParty = dropParty;

        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        if (partyConfig.getConfigurationSection("Parties") != null) {
            for (String partyName : partyConfig.getConfigurationSection("Parties").getKeys(false)) {
                if (hasParty(partyName)) {
                    continue;
                }
                String path = "Parties." + partyName;
                PartyType partyType = PartyType.valueOf(partyConfig.getString(path + ".type"));
                switch (partyType) {
                    case CHEST_PARTY:
                        parties.put(partyName, new ChestParty(dropParty, partyConfig.getConfigurationSection(path)));
                        break;
                    case CUSTOM_PARTY:
                        parties.put(partyName, new CustomParty(dropParty, partyConfig.getConfigurationSection(path)));
                        break;
                }
            }
        }
    }

    /**
     * Checks if the manager has a party.
     *
     * @param partyName The name of the party.
     * @return True if the manager has a party, else false.
     */
    public boolean hasParty(String partyName) {
        return parties.containsKey(partyName);
    }

    /**
     * Adds a party to the manager.
     *
     * @param party The party.
     */
    public void addParty(Party party) {
        parties.put(party.getName(), party);
        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        String path = "Parties." + party.getName();
        partyConfig.createSection(path);
        party.save(partyConfig.getConfigurationSection(path));
        dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
    }

    /**
     * Removes a party from the manager.
     *
     * @param partyName The name of the party.
     */
    public void removeParty(String partyName) {
        Party party = parties.get(partyName);
        if (party != null) {
            party.stop();
            parties.remove(partyName);
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName, null);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    /**
     * Gets a party.
     *
     * @param partyName The name of the party.
     * @return The party.
     */
    public Party getParty(String partyName) {
        return parties.get(partyName);
    }

    /**
     * Gets the parties in the manager.
     *
     * @return All of the manager's parties.
     */
    public Map<String, Party> getParties() {
        return parties;
    }

    /**
     * Gets the parties in the manager of a certain type.
     *
     * @param partyType The type of party.
     * @return All of the manager's parties of the type.
     */
    public Map<String, Party> getParties(PartyType partyType) {
        Map<String, Party> partiesOfType = new HashMap<>();
        for (Party party : parties.values()) {
            if (party.getType() == partyType) {
                partiesOfType.put(party.getName(), party);
            }
        }
        return partiesOfType;
    }

    /**
     * Gets a string list of the parties in the manager.
     *
     * @return A string list of all of the manager's parties.
     */
    public List<String> getPartyList() {
        return new ArrayList<>(parties.keySet());
    }

    /**
     * Gets a string list of the parties in the manager of a certain type.
     *
     * @param partyType The type of party.
     * @return A string list of all of the manager's parties of the type.
     */
    public List<String> getPartyList(PartyType partyType) {
        List<String> partiesOfType = new ArrayList<>();
        for (Party party : parties.values()) {
            if (party.getType() == partyType) {
                partiesOfType.add(party.getName());
            }
        }
        return partiesOfType;
    }

    /**
     * Stops all of the parties in the manager.
     */
    public void stopParties() {
        for (Party party : parties.values()) {
            party.stop();
        }
    }
}
