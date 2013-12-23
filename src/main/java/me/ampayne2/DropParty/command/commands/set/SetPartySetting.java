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
                        party.setMaxLength(Long.parseLong(value));
                        break;
                    case ITEM_DELAY:
                        party.setItemDelay(Long.parseLong(value));
                        break;
                    case MAX_STACK_SIZE:
                        party.setMaxStackSize(Integer.parseInt(value));
                        break;
                    case FIREWORK_AMOUNT:
                        party.setFireworkAmount(Integer.parseInt(value));
                        break;
                    case FIREWORK_DELAY:
                        party.setFireworkDelay(Long.parseLong(value));
                        break;
                    case START_PERIODICALLY:
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                            party.setStartPeriodically(Boolean.parseBoolean(value));
                        } else {
                            dropParty.getMessage().sendMessage(sender, "error.booleanformat");
                        }
                        break;
                    case START_PERIOD:
                        party.setStartPeriod(Long.parseLong(value));
                        break;
                    case VOTE_TO_START:
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                            party.setVoteToStart(Boolean.parseBoolean(value));
                        } else {
                            dropParty.getMessage().sendMessage(sender, "error.booleanformat");
                        }
                        break;
                    case REQUIRED_VOTES:
                        party.setRequiredVotes(Integer.parseInt(value));
                }
            } else {
                dropParty.getMessage().sendMessage(sender, "error.party.doesntexist", partyName);
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                dropParty.getMessage().sendMessage(sender, "error.numberformat");
            } else if (e instanceof IllegalArgumentException) {
                dropParty.getMessage().sendMessage(sender, "error.party.notapartysetting");
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
