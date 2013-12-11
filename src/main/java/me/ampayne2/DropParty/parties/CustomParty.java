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

import me.ampayne2.dropparty.DropParty;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/**
 * A party that spawns configurable items.
 */
public class CustomParty extends Party {
    /**
     * Creates a CustomParty from default settings.
     *
     * @param dropParty The DropParty instance.
     * @param partyName The name of the party.
     * @param teleport  The teleport location of the party.
     */
    public CustomParty(DropParty dropParty, String partyName, Location teleport) {
        super(dropParty, partyName, PartyType.CUSTOM_PARTY, teleport);
    }

    /**
     * Loads a party from a ConfigurationSection.
     *
     * @param dropParty The DropParty instance.
     * @param section   The ConfigurationSection.
     */
    public CustomParty(DropParty dropParty, ConfigurationSection section) {
        super(dropParty, section);
    }

    @Override
    public boolean dropNext() {
        return true;
    }
}
