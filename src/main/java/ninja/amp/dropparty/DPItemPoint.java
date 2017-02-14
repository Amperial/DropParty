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

import ninja.amp.dropparty.parties.Party;
import org.bukkit.Location;

/**
 * A drop party item point.
 */
public class DPItemPoint {

    private final DropParty dropParty;
    private final Party party;
    private final Location location;

    /**
     * Creates a new item point.
     *
     * @param dropParty The DropParty instance.
     * @param party     The party of the item point.
     * @param location  The location of the item point.
     */
    public DPItemPoint(DropParty dropParty, Party party, Location location) {
        this.dropParty = dropParty;
        this.party = party;
        this.location = location;
    }

    /**
     * Gets the item point's party.
     *
     * @return The item point's party.
     */
    public Party getParty() {
        return party;
    }

    /**
     * Gets the item point's location.
     *
     * @return The item point's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Converts the item point into a string for storage in a config.
     *
     * @return The string representation of the item point.
     */
    public String toConfig() {
        return party.getName() + ";" + DPUtils.locationToString(location);
    }

    /**
     * Converts a string represnetation of a item point into a item point.
     *
     * @param dropParty The DropParty instance.
     * @param party     The party of the item point.
     * @param string    The string representation of the item point.
     * @return The item point.
     */
    public static DPItemPoint fromConfig(DropParty dropParty, Party party, String string) {
        try {
            String[] parts = string.split(";");
            if (parts.length == 2) {
                String partyName = parts[0];
                if (partyName.equals(party.getName())) {
                    return new DPItemPoint(dropParty, party, DPUtils.stringToLocation(parts[1]));
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
