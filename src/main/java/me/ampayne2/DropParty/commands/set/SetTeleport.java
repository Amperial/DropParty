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
package me.ampayne2.dropparty.commands.set;

import me.ampayne2.amplib.command.Command;
import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.message.DPMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that sets the teleport of a drop party.
 */
public class SetTeleport extends Command {
    private final DropParty dropParty;

    public SetTeleport(DropParty dropParty) {
        super(dropParty, "teleport");
        setDescription("Sets the teleport of a drop party.");
        setCommandUsage("/dp set teleport <party>");
        setPermission(new Permission("dropparty.set.teleport", PermissionDefault.OP));
        setArgumentRange(1, 1);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        if (dropParty.getPartyManager().hasParty(partyName)) {
            dropParty.getPartyManager().getParty(partyName).setTeleport(((Player) sender).getLocation());
        } else {
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_DOESNTEXIST, partyName);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        return dropParty.getPartyManager().getPartyList();
    }
}