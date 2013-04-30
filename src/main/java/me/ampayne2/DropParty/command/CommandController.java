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

import me.ampayne2.DropParty.command.commands.CommandListChests;
import me.ampayne2.DropParty.command.commands.CommandListItempoints;
import me.ampayne2.DropParty.command.commands.CommandRemoveChest;
import me.ampayne2.DropParty.command.commands.CommandRemoveItempoint;
import me.ampayne2.DropParty.command.commands.CommandSetChest;
import me.ampayne2.DropParty.command.commands.CommandSetItempoint;
import me.ampayne2.DropParty.command.commands.CommandStart;
import me.ampayne2.DropParty.command.commands.CommandStop;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandController extends JavaPlugin {

	private final SubCommand mainCommand = new SubCommand();

	public CommandController() {

		SubCommand chest = new SubCommand();
		chest.addCommand("set", new CommandSetChest());
		chest.addCommand("remove", new CommandRemoveChest());
		chest.addCommand("list", new CommandListChests());
		mainCommand.addCommand("chest", chest);

		SubCommand item = new SubCommand();
		item.addCommand("set", new CommandSetItempoint());
		item.addCommand("remove", new CommandRemoveItempoint());
		item.addCommand("list", new CommandListItempoints());
		mainCommand.addCommand("item", item);

		SubCommand start = new SubCommand();
		start.addCommand("", new CommandStart());
		mainCommand.addCommand("start", start);

		SubCommand stop = new SubCommand();
		stop.addCommand("", new CommandStop());
		mainCommand.addCommand("stop", stop);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!cmd.getName().equalsIgnoreCase("dropparty"))
			return false;

		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		}

		if (!sender.hasPermission("dropparty.admin")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return true;
		}

		if (args.length == 0) {
			if (mainCommand.commandExist("")) {
				mainCommand.execute("", sender, args);
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid argument. Valid arguments are: " + mainCommand.getSubCommandList());
				return true;
			}
		}

		if (mainCommand.commandExist(args[0])) {
			String[] newArgs;
			if (args.length == 1) {
				newArgs = new String[0];
			} else {
				newArgs = new String[args.length - 1];
				System.arraycopy(args, 1, newArgs, 0, args.length - 1);
			}

			mainCommand.execute(args[0], sender, newArgs);
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "Invalid argument. Valid arguments are: " + mainCommand.getSubCommandList());
			return true;
		}
	}

}
