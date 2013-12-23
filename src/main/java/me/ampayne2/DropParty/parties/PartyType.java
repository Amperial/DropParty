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
 * An enumeration of the types of drop parties.
 */
public enum PartyType {
    CHEST_PARTY("chest", ChestParty.class),
    CUSTOM_PARTY("custom", CustomParty.class);

    private final String name;
    private final Class<? extends Party> clazz;
    private final static List<String> partyTypeNames;

    private PartyType(String name, Class<? extends Party> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    /**
     * Gets the name that represents the party type.
     *
     * @return The party type's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the class of the party type.
     *
     * @return The party type's class.
     */
    public Class<? extends Party> getPartyClass() {
        return clazz;
    }

    /**
     * Gets a party type from its name.
     *
     * @param name The name.
     * @return The party type.
     */
    public static PartyType fromName(String name) {
        for (PartyType partyType : PartyType.values()) {
            if (partyType.getName().equalsIgnoreCase(name)) {
                return partyType;
            }
        }
        return null;
    }

    /**
     * Gets the list of party types.
     *
     * @return The list of party types.
     */
    public static List<String> getPartyTypeNames() {
        return partyTypeNames;
    }

    static {
        partyTypeNames = new ArrayList<>();
        for (PartyType partyType : PartyType.values()) {
            partyTypeNames.add(partyType.getName());
        }
    }
}
