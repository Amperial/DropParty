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
package me.ampayne2.dropparty.command;

import me.ampayne2.dropparty.DropParty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandController implements CommandExecutor {
    private final DropParty dropParty;
    private final SubCommand mainCommand;

    public CommandController(DropParty dropParty) {
        this.dropParty = dropParty;
        mainCommand = new SubCommand(dropParty);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String subCommand = "";
        if (args.length > 0) {
            subCommand = args[0];
        }
        if (!cmd.getName().equalsIgnoreCase("dropparty")) {
            return false;
        } else if (subCommand.equals("") && mainCommand.commandExists(subCommand)) {
            mainCommand.execute(subCommand, sender, args);
        } else if (mainCommand.commandExists(subCommand)) {
            String[] newArgs;
            if (args.length == 1) {
                newArgs = new String[0];
            } else {
                newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
            }
            mainCommand.execute(subCommand, sender, newArgs);
        } else {
            dropParty.getMessage().sendMessage(sender, "commands.invalidsubcommand", "\"" + subCommand + "\"", "\"dropparty\"");
            dropParty.getMessage().sendMessage(sender, "commands.validsubcommands", mainCommand.getSubCommandList());
        }
        return true;
    }
}
