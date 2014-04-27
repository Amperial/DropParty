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
package me.ampayne2.dropparty.command.commands;

import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.command.DPCommand;
import me.ampayne2.dropparty.message.DPMessage;
import me.ampayne2.dropparty.parties.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that creates a drop party.
 */
public class Create extends DPCommand {
    private final DropParty dropParty;

    public Create(DropParty dropParty) {
        super(dropParty, "create", "Creates a drop party.", "/dp create <party>", new Permission("dropparty.create", PermissionDefault.OP), true);
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
