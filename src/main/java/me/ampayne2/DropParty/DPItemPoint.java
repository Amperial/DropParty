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

import me.ampayne2.dropparty.parties.Party;
import org.bukkit.Location;

public class DPItemPoint {
    private final DropParty dropParty;
    private final Party party;
    private final Location location;

    public DPItemPoint(DropParty dropParty, Party party, Location location) {
        this.dropParty = dropParty;
        this.party = party;
        this.location = location;
    }

    public Party getParty() {
        return party;
    }

    public Location getLocation() {
        return location;
    }

    public String toConfig() {
        return new StringBuilder()
                .append(party.getName()).append(":")
                .append(DPUtils.locationToString(location))
                .toString();
    }

    public static DPItemPoint fromConfig(DropParty dropParty, String string) {
        try {
            String[] parts = string.split(":", 2);
            if (parts.length == 2) {
                String partyName = parts[0];
                if (dropParty.getPartyManager().hasParty(partyName)) {
                    return new DPItemPoint(dropParty, dropParty.getPartyManager().getParty(partyName), DPUtils.stringToLocation(parts[1]));
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
