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
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that stops a drop party.
 */
public class Stop extends Command {

    private final DropParty dropParty;

    public Stop(DropParty dropParty) {
        super(dropParty, "stop");
        setDescription("Stops a drop party.");
        setCommandUsage("/dp stop <party>");
        setPermission(new Permission("dropparty.stop", PermissionDefault.OP));
        setArgumentRange(1, 1);
        setPlayerOnly(false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        if (dropParty.getPartyManager().hasParty(partyName)) {
            Party party = dropParty.getPartyManager().getParty(partyName);
            if (party.isRunning()) {
                party.stop(false);
                dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_STOP, partyName);
            } else if (party.isShootingFireworks()) {
                party.stopShootingFireworks();
                dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_STOPFIREWORKS, partyName);
            } else {
                dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_NOTRUNNING, partyName);
            }
        } else {
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_DOESNTEXIST, partyName);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        return dropParty.getPartyManager().getPartyList();
    }

}
