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
package me.ampayne2.dropparty.command.commands.list;

import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.command.DPCommand;
import me.ampayne2.dropparty.parties.PartyType;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that lists the chests of a drop party.
 */
public class ListChests extends DPCommand {
    private final DropParty dropParty;

    public ListChests(DropParty dropParty) {
        super(dropParty, "chests", "Lists the chests of a drop party.", "/dp list chests <party>", new Permission("dropparty.list.chests", PermissionDefault.TRUE), 1, false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        return dropParty.getPartyManager().getPartyList(PartyType.CHEST_PARTY);
    }
}
