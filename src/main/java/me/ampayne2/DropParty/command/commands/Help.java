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

import me.ampayne2.dropparty.DPUtils;
import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.command.Command;
import me.ampayne2.dropparty.command.DPCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that lists all of the drop party commands.
 */
public class Help extends DPCommand {
    private final DropParty dropParty;
    private final static int COMMANDS_PER_PAGE = 4;

    public Help(DropParty dropParty) {
        super(dropParty, "help", "Lists all of the drop party commands.", "/dp help [page]", new Permission("dropparty.help", PermissionDefault.TRUE), 0, 1, false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        List<Command> commands = dropParty.getCommandController().getMainCommand().getChildren(true);
        int commandAmount = commands.size();
        int pageNumber = 1;
        if (args.length == 1) {
            try {
                pageNumber = DPUtils.clamp(Integer.parseInt(args[0]), 1, (commandAmount + 3) / 4);
            } catch (NumberFormatException e) {
                dropParty.getMessage().sendMessage(sender, "error.numberformat");
                return;
            }
        }
        int startIndex = COMMANDS_PER_PAGE * (pageNumber - 1);
        sender.sendMessage(ChatColor.GOLD + "<-------<| " + ChatColor.DARK_PURPLE + "Commands: Page " + pageNumber + " " + ChatColor.GOLD + "|>------->");
        for (Command helpCommand : commands.subList(startIndex, Math.min(startIndex + COMMANDS_PER_PAGE, commandAmount))) {
            sender.sendMessage(ChatColor.DARK_PURPLE + ((DPCommand) helpCommand).getCommandUsage());
            sender.sendMessage(ChatColor.GRAY + "-" + ((DPCommand) helpCommand).getDescription());
        }
    }
}
