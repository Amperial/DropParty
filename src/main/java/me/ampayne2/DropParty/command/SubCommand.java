/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2013
 *
 * DropParty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DropParty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DropParty.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.DropParty.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.ampayne2.DropParty.command.interfaces.Command;
import me.ampayne2.DropParty.command.interfaces.DPCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SubCommand implements Command {

	private Map<String, Command> commandList = new HashMap<String, Command>();


	public void addCommand(String name, Command command) {
		commandList.put(name, command);
	}

	public boolean commandExist(String name) {
		return commandList.containsKey(name);
	}

	public void execute(String command, CommandSender sender, String[] args) {
		if (commandExist(command)) {
			Command entry = commandList.get(command);
			if (entry instanceof DPCommand) {
				((DPCommand) entry).execute(sender,args);
			} else if (entry instanceof SubCommand) {
				SubCommand subCommand = (SubCommand) entry;

				String subSubCommand = "";
				if (args.length != 0) {
					subSubCommand = args[0];
				}

				if (subCommand.commandExist(subSubCommand)) {
					String[] newArgs;
					if (args.length == 0) {
						newArgs = args;
					} else {
						newArgs = new String[args.length - 1];
						System.arraycopy(args, 1, newArgs, 0, args.length - 1);
					}
					((SubCommand) entry).execute(subSubCommand, sender, newArgs);
				} else {
					sender.sendMessage(ChatColor.RED + "Invalid argument. Valid arguments are: " + subCommand.getSubCommandList());
				}

			}

		}
	}

	public String getSubCommandList() {
		return Arrays.toString(commandList.keySet().toArray());
	}
}
