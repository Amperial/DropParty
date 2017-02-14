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
package ninja.amp.dropparty.commands;

import ninja.amp.dropparty.DropParty;
import ninja.amp.dropparty.PartyManager;
import ninja.amp.dropparty.message.DPMessage;
import ninja.amp.dropparty.parties.Party;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.messenger.DefaultMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that teleports the sender to a drop party.
 */
public class Teleport extends Command {

    private final DropParty dropParty;

    public Teleport(DropParty dropParty) {
        super(dropParty, "teleport");
        setDescription("Teleports you to a drop party.");
        setCommandUsage("/dp teleport [party]");
        setPermission(new Permission("dropparty.teleport", PermissionDefault.TRUE));
        setArgumentRange(0, 1);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        PartyManager partyManager = dropParty.getPartyManager();
        // Attempt to find a party when no party is specified
        if (args.length == 0) {
            // Attempt to find a single running party
            List<Party> runningParties = (List<Party>) partyManager.getRunningParties();
            if (runningParties.size() == 0) {
                // Attempt to find a single party if none are running
                List<Party> parties = (List<Party>) partyManager.getParties();
                if (parties.size() == 0) {
                    dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_NONEEXIST);
                } else if (parties.size() == 1) {
                    parties.get(0).teleport((Player) sender);
                } else {
                    dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_NOTSPECIFIED);
                    dropParty.getMessenger().sendMessage(sender, DefaultMessage.COMMAND_USAGE, getCommandUsage());
                }
            } else if (runningParties.size() == 1) {
                runningParties.get(0).teleport((Player) sender);
            } else {
                dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_NOTSPECIFIED);
                dropParty.getMessenger().sendMessage(sender, DefaultMessage.COMMAND_USAGE, getCommandUsage());
            }
        } else {
            String partyName = args[0];
            if (partyManager.hasParty(partyName)) {
                partyManager.getParty(partyName).teleport((Player) sender);
            } else {
                dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_DOESNTEXIST, partyName);
            }
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        return dropParty.getPartyManager().getPartyList();
    }

}
