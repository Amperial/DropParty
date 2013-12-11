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
 * Gives the sender some information about drop party.
 */
public class About extends DPCommand {
    private final DropParty dropParty;

    public About(DropParty dropParty) {
        super(dropParty, "", new Permission("dropparty.about", PermissionDefault.TRUE), false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "<-------<| " + ChatColor.DARK_PURPLE + "About Drop Party " + ChatColor.GOLD + "|>------->");
        sender.sendMessage(ChatColor.GRAY + "dev.bukkit.org/server-mods/dropparty/");
        sender.sendMessage(ChatColor.GRAY + "Author: ampayne2");
        sender.sendMessage(ChatColor.GRAY + "Version: " + dropParty.getDescription().getVersion());
    }
}
