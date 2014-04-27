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
package me.ampayne2.dropparty.command.commands.set;

import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.command.DPCommand;
import me.ampayne2.dropparty.message.DPMessage;
import me.ampayne2.dropparty.parties.Party;
import me.ampayne2.dropparty.parties.PartySetting;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that sets a party setting of a drop party.
 */
public class SetPartySetting extends DPCommand {
    private final DropParty dropParty;

    public SetPartySetting(DropParty dropParty) {
        super(dropParty, "partysetting", "Sets a party setting of a drop party.", "/dp set partysetting <party> <setting> <value>", new Permission("dropparty.set.partysetting", PermissionDefault.OP), 3, false);
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
            switch (partySetting) {
                case MAX_LENGTH:
                    try {
                        party.set(PartySetting.MAX_LENGTH, Long.parseLong(value));
                    } catch (NumberFormatException e) {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_NUMBERFORMAT);
                    }
                    break;
                case ITEM_DELAY:
                    try {
                        party.set(PartySetting.ITEM_DELAY, Long.parseLong(value));
                    } catch (NumberFormatException e) {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_NUMBERFORMAT);
                    }
                    break;
                case MAX_STACK_SIZE:
                    try {
                        party.set(PartySetting.MAX_STACK_SIZE, Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_NUMBERFORMAT);
                    }
                    break;
                case FIREWORK_AMOUNT:
                    try {
                        party.set(PartySetting.FIREWORK_AMOUNT, Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_NUMBERFORMAT);
                    }
                    break;
                case FIREWORK_DELAY:
                    try {
                        party.set(PartySetting.FIREWORK_DELAY, Long.parseLong(value));
                    } catch (NumberFormatException e) {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_NUMBERFORMAT);
                    }
                    break;
                case START_PERIODICALLY:
                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                        party.set(PartySetting.START_PERIODICALLY, Boolean.parseBoolean(value));
                    } else {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_BOOLEANFORMAT);
                        return;
                    }
                    break;
                case START_PERIOD:
                    try {
                        party.set(PartySetting.START_PERIOD, Long.parseLong(value));
                    } catch (NumberFormatException e) {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_NUMBERFORMAT);
                    }
                    break;
                case VOTE_TO_START:
                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                        party.set(PartySetting.VOTE_TO_START, Boolean.parseBoolean(value));
                    } else {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_BOOLEANFORMAT);
                        return;
                    }
                    break;
                case REQUIRED_VOTES:
                    try {
                        party.set(PartySetting.REQUIRED_VOTES, Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_NUMBERFORMAT);
                    }
                    break;
                case EMPTY_CHEST:
                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                        party.set(PartySetting.EMPTY_CHEST, Boolean.parseBoolean(value));
                    } else {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.ERROR_BOOLEANFORMAT);
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
