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

//import com.vexsoftware.votifier.model.VotifierEvent;
import ninja.amp.dropparty.parties.Party;
import ninja.amp.dropparty.parties.PartySetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * The drop party vote listener
 */
public class VoteListener implements Listener {
    private final DropParty dropParty;

    /**
     * Creates a new vote listener.
     *
     * @param dropParty The DropParty instance.
     */
    public VoteListener(DropParty dropParty) {
        this.dropParty = dropParty;
        //dropParty.getServer().getPluginManager().registerEvents(this, dropParty);
    }

    /*
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVote(VotifierEvent event) {
        String playerName = event.getVote().getUsername();
        for (Party party : dropParty.getPartyManager().getParties()) {
            if (party.get(PartySetting.VOTE_TO_START, Boolean.class) && party.get(PartySetting.VOTIFIER, Boolean.class) && !party.hasVoted(playerName)) {
                party.addVote(playerName);
            }
        }
    }
    */
}
