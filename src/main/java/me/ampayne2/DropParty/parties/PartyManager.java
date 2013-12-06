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

import java.util.HashMap;
import java.util.Map;

public class PartyManager {
    private final DropParty dropParty;
    private Map<String, Party> parties = new HashMap<>();

    public PartyManager(DropParty dropParty) {
        this.dropParty = dropParty;
    }

    public void addParty(Party party) {
        parties.put(party.getName(), party);
    }

    public void removeParty(Party party) {
        party.stop();
        parties.remove(party.getName());
    }

    public void removeParty(String partyName) {
        Party party = parties.get(partyName);
        if (party != null) {
            removeParty(party);
        }
    }

    public boolean hasParty(String partyName) {
        return parties.containsKey(partyName);
    }

    public Party getParty(String partyName) {
        return parties.get(partyName);
    }

    public Map<String, Party> getParties() {
        return parties;
    }

    public void stopParties() {
        for (Party party : parties.values()) {
            party.stop();
        }
    }
}
