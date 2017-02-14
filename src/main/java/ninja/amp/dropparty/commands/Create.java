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
import ninja.amp.dropparty.message.DPMessage;
import ninja.amp.dropparty.parties.Party;
import ninja.amp.amplib.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that creates a drop party.
 */
public class Create extends Command {

    private final DropParty dropParty;

    public Create(DropParty dropParty) {
        super(dropParty, "create");
        setDescription("Creates a drop party.");
        setCommandUsage("/dp create <party>");
        setPermission(new Permission("dropparty.create", PermissionDefault.OP));
        setArgumentRange(1, 1);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        if (dropParty.getPartyManager().hasParty(partyName)) {
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_ALREADYEXISTS, partyName);
        } else {
            dropParty.getPartyManager().addParty(new Party(dropParty, partyName, ((Player) sender).getLocation()));
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_CREATE, partyName);
        }
    }

}
