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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * Gives the sender a list of commands.
 */
public class Help extends DPCommand {
    private final DropParty dropParty;

    public Help(DropParty dropParty) {
        super(dropParty, "", "/dp", new Permission("dropparty.help", PermissionDefault.TRUE), 0, 1, false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        int pageNumber = 1;
        if (args.length == 1) {
            try {
                int argNumber = Integer.parseInt(args[0]);
                if (argNumber > 0) {
                    pageNumber = argNumber;
                }
            } catch (NumberFormatException e) {
                dropParty.getMessage().sendMessage(sender, "error.numberformat");
            }
        }
        sender.sendMessage(ChatColor.GOLD + "<-------<| " + ChatColor.DARK_PURPLE + "Commands: Page " + pageNumber + ChatColor.GOLD + "|>------->");
    }
}
