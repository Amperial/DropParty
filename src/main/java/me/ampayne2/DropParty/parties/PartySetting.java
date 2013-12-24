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

import java.util.ArrayList;
import java.util.List;

/**
 * An enumeration of the settings of a drop party.
 */
public enum PartySetting {
    MAX_LENGTH("maxlength", "max length"),
    ITEM_DELAY("itemdelay", "item delay"),
    MAX_STACK_SIZE("maxstacksize", "max stack size"),
    FIREWORK_AMOUNT("fireworkamount", "firework amount"),
    FIREWORK_DELAY("fireworkdelay", "firework delay"),
    START_PERIODICALLY("startperiodically", "start periodically"),
    START_PERIOD("startperiod", "start period"),
    VOTE_TO_START("votetostart", "vote to start"),
    REQUIRED_VOTES("requiredvotes", "required votes");

    private final String name;
    private final String displayName;
    private final static List<String> partySettingNames;

    private PartySetting(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    /**
     * Gets the name that represents the party setting.
     *
     * @return The party setting's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the display name of the party setting.
     *
     * @return The party setting's display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets a party setting from its name.
     *
     * @param name The name.
     * @return The party setting.
     */
    public static PartySetting fromName(String name) {
        for (PartySetting partySetting : PartySetting.values()) {
            if (partySetting.getName().equalsIgnoreCase(name)) {
                return partySetting;
            }
        }
        return null;
    }

    /**
     * Gets the list of party setting names.
     *
     * @return The list of party setting names.
     */
    public static List<String> getPartySettingNames() {
        return partySettingNames;
    }

    static {
        partySettingNames = new ArrayList<>();
        for (PartySetting partySetting : PartySetting.values()) {
            partySettingNames.add(partySetting.getName());
        }
    }
}
