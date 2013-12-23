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
import me.ampayne2.dropparty.parties.ChestParty;
import me.ampayne2.dropparty.parties.CustomParty;
import me.ampayne2.dropparty.parties.PartyType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that creates a drop party.
 */
public class Create extends DPCommand {
    private final DropParty dropParty;

    public Create(DropParty dropParty) {
        super(dropParty, "create", "Creates a drop party.", "/dp create <party> <type>", new Permission("dropparty.create", PermissionDefault.OP), 2, true);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        if (dropParty.getPartyManager().hasParty(partyName)) {
            dropParty.getMessage().sendMessage(sender, "error.party.alreadyexists", partyName);
        } else {
            PartyType partyType = PartyType.fromName(args[1]);
            switch (partyType) {
                case CHEST_PARTY:
                    dropParty.getPartyManager().addParty(new ChestParty(dropParty, partyName, ((Player) sender).getLocation()));
                    break;
                case CUSTOM_PARTY:
                    dropParty.getPartyManager().addParty(new CustomParty(dropParty, partyName, ((Player) sender).getLocation()));
                    break;
            }
            dropParty.getMessage().sendMessage(sender, "party.create", partyName);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 1) {
            return PartyType.getPartyTypeNames();
        } else {
            return new ArrayList<>();
        }
    }
}
