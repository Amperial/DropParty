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
package ninja.amp.dropparty.commands.set;

import ninja.amp.dropparty.DropParty;
import ninja.amp.dropparty.message.DPMessage;
import ninja.amp.dropparty.parties.Party;
import ninja.amp.dropparty.parties.PartySetting;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.messenger.DefaultMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that sets a party setting of a drop party.
 */
public class SetPartySetting extends Command {

    private final DropParty dropParty;

    public SetPartySetting(DropParty dropParty) {
        super(dropParty, "partysetting");
        setDescription("Sets a party setting of a drop party.");
        setCommandUsage("/dp set partysetting <party> <setting> <value>");
        setPermission(new Permission("dropparty.set.partysetting", PermissionDefault.OP));
        setArgumentRange(3, 3);
        setPlayerOnly(false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        PartySetting partySetting = PartySetting.fromName(args[1]);
        if (partySetting == null) {
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_NOTAPARTYSETTING);
        } else if (dropParty.getPartyManager().hasParty(partyName)) {
            Party party = dropParty.getPartyManager().getParty(partyName);
            String value = args[2];
            if (partySetting.getType() == Integer.class) {
                try {
                    party.set(partySetting, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    dropParty.getMessenger().sendMessage(sender, DefaultMessage.ERROR_NUMBERFORMAT);
                    return;
                }
            } else if (partySetting.getType() == Long.class) {
                try {
                    party.set(partySetting, Long.parseLong(value));
                } catch (NumberFormatException e) {
                    dropParty.getMessenger().sendMessage(sender, DefaultMessage.ERROR_NUMBERFORMAT);
                    return;
                }
            } else if (partySetting.getType() == Boolean.class) {
                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    party.set(partySetting, Boolean.parseBoolean(value));
                } else {
                    dropParty.getMessenger().sendMessage(sender, DefaultMessage.ERROR_BOOLEANFORMAT);
                    return;
                }
            }
            dropParty.getMessenger().sendMessage(sender, DPMessage.SET_PARTYSETTING, partySetting.getDisplayName(), partyName, party.get(partySetting).toString());
        } else {
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_DOESNTEXIST, partyName);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 0) {
            return dropParty.getPartyManager().getPartyList();
        } else if (args.length == 1) {
            return PartySetting.getPartySettingNames();
        } else {
            return new ArrayList<>();
        }
    }

}
