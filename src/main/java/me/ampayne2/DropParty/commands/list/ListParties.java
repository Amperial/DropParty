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
package me.ampayne2.dropparty.commands.list;

import me.ampayne2.amplib.command.Command;
import me.ampayne2.amplib.messenger.PageList;
import me.ampayne2.dropparty.DropParty;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that lists the drop parties in the party manager.
 */
public class ListParties extends Command {
    private final DropParty dropParty;

    public ListParties(DropParty dropParty) {
        super(dropParty, "parties");
        setDescription("Lists all of the drop parties.");
        setCommandUsage("/dp list parties [page]");
        setPermission(new Permission("dropparty.list.parties", PermissionDefault.TRUE));
        setArgumentRange(0, 1);
        setPlayerOnly(false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        int pageNumber = 1;
        if (args.length == 1) {
            pageNumber = PageList.getPageNumber(args[0]);
        }
        dropParty.getPartyManager().getPageList().sendPage(pageNumber, sender);
    }
}
