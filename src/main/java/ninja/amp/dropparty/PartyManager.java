/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2014 <http://dev.bukkit.org/server-mods/dropparty//>
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
package ninja.amp.dropparty;

import ninja.amp.dropparty.config.ConfigType;
import ninja.amp.dropparty.parties.Party;
import ninja.amp.amplib.messenger.Messenger;
import ninja.amp.amplib.messenger.PageList;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages all of the drop parties.
 */
public class PartyManager {
    private final DropParty dropParty;
    private final PageList pageList;
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
                parties.put(partyName, new Party(dropParty, partyConfig.getConfigurationSection("Parties." + partyName)));
            }
        }

        pageList = new PageList(dropParty, "Parties", 8);
        updatePageList();
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
        updatePageList();
    }

    /**
     * Removes a party from the manager.
     *
     * @param partyName The name of the party.
     */
    public void removeParty(String partyName) {
        Party party = parties.get(partyName);
        if (party != null) {
            party.stop(false);
            dropParty.getPlayerModeController().clearModes(party);
            parties.remove(partyName);
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName, null);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
            updatePageList();
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
    public Collection<Party> getParties() {
        return parties.values();
    }

    /**
     * Gets the parties in the manager that are currently running.
     *
     * @return The manager's parties that are running.
     */
    public Collection<Party> getRunningParties() {
        return parties.values().stream().filter(Party::isRunning).collect(Collectors.toList());
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
     * Gets the PageList of drop parties.
     *
     * @return The PageList.
     */
    public PageList getPageList() {
        return pageList;
    }

    /**
     * Updates the PageList of drop parties.
     */
    public void updatePageList() {
        List<String> strings = parties.keySet().stream().map(partyName -> Messenger.SECONDARY_COLOR + "-" + partyName).collect(Collectors.toList());
        pageList.setStrings(strings);
    }

    /**
     * Stops all of the parties in the manager.
     */
    public void stopParties() {
        for (Party party : parties.values()) {
            party.stop(false);
        }
    }
}
