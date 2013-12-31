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
        try {
            String partyName = args[0];
            PartySetting partySetting = PartySetting.fromName(args[1]);

            if (dropParty.getPartyManager().hasParty(partyName)) {
                Party party = dropParty.getPartyManager().getParty(partyName);
                String value = args[2];
                switch (partySetting) {
                    case MAX_LENGTH:
                        long maxLength = Long.parseLong(value);
                        if (maxLength <= 20) {
                            dropParty.getMessenger().sendMessage(sender, "error.numbertoolow", "20");
                            return;
                        } else {
                            party.setMaxLength(maxLength);
                        }
                        break;
                    case ITEM_DELAY:
                        long itemDelay = Long.parseLong(value);
                        if (itemDelay < 1) {
                            dropParty.getMessenger().sendMessage(sender, "error.numbertoolow", "1");
                            return;
                        } else {
                            party.setItemDelay(itemDelay);
                        }
                        break;
                    case MAX_STACK_SIZE:
                        int maxStackSize = Integer.parseInt(value);
                        if (maxStackSize < 1) {
                            dropParty.getMessenger().sendMessage(sender, "error.numbertoolow", "1");
                            return;
                        } else if (maxStackSize > 64) {
                            dropParty.getMessenger().sendMessage(sender, "error.numbertoohigh", "64");
                            return;
                        } else {
                            party.setMaxStackSize(Integer.parseInt(value));
                        }
                        break;
                    case FIREWORK_AMOUNT:
                        int fireworkAmount = Integer.parseInt(value);
                        if (fireworkAmount < 0) {
                            dropParty.getMessenger().sendMessage(sender, "error.numbertoolow", "0");
                            return;
                        } else {
                            party.setFireworkAmount(fireworkAmount);
                        }
                        break;
                    case FIREWORK_DELAY:
                        long fireworkDelay = Long.parseLong(value);
                        if (fireworkDelay < 1) {
                            dropParty.getMessenger().sendMessage(sender, "error.numbertoolow", "1");
                            return;
                        } else {
                            party.setFireworkDelay(fireworkDelay);
                        }
                        break;
                    case START_PERIODICALLY:
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                            party.setStartPeriodically(Boolean.parseBoolean(value));
                        } else {
                            dropParty.getMessenger().sendMessage(sender, "error.booleanformat");
                            return;
                        }
                        break;
                    case START_PERIOD:
                        long startPeriod = Long.parseLong(value);
                        if (startPeriod < 100) {
                            dropParty.getMessenger().sendMessage(sender, "error.numbertoolow", "100");
                            return;
                        } else {
                            party.setStartPeriod(Long.parseLong(value));
                        }
                        break;
                    case VOTE_TO_START:
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                            party.setVoteToStart(Boolean.parseBoolean(value));
                        } else {
                            dropParty.getMessenger().sendMessage(sender, "error.booleanformat");
                            return;
                        }
                        break;
                    case REQUIRED_VOTES:
                        int requiredVotes = Integer.parseInt(value);
                        if (requiredVotes < 1) {
                            dropParty.getMessenger().sendMessage(sender, "error.numbertoolow", "1");
                            return;
                        } else {
                            party.setRequiredVotes(requiredVotes);
                        }
                }
                dropParty.getMessenger().sendMessage(sender, "set.partysetting", partySetting.getDisplayName(), partyName, value);
            } else {
                dropParty.getMessenger().sendMessage(sender, "error.party.doesntexist", partyName);
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                dropParty.getMessenger().sendMessage(sender, "error.numberformat");
            } else if (e instanceof IllegalArgumentException) {
                dropParty.getMessenger().sendMessage(sender, "error.party.notapartysetting");
            }
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
