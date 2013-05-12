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

import me.ampayne2.DropParty.command.commands.CommandCreate;
import me.ampayne2.DropParty.command.commands.CommandDelete;
import me.ampayne2.DropParty.command.commands.CommandStart;
import me.ampayne2.DropParty.command.commands.CommandTeleport;
import me.ampayne2.DropParty.command.commands.list.CommandListChests;
import me.ampayne2.DropParty.command.commands.list.CommandListItempoints;
import me.ampayne2.DropParty.command.commands.list.CommandListParties;
import me.ampayne2.DropParty.command.commands.list.CommandListSettings;
import me.ampayne2.DropParty.command.commands.list.CommandListTeleport;
import me.ampayne2.DropParty.command.commands.remove.CommandRemoveChest;
import me.ampayne2.DropParty.command.commands.remove.CommandRemoveItempoint;
import me.ampayne2.DropParty.command.commands.set.CommandSetChest;
import me.ampayne2.DropParty.command.commands.set.CommandSetItempoint;
import me.ampayne2.DropParty.command.commands.set.CommandSetItemDelay;
import me.ampayne2.DropParty.command.commands.set.CommandSetMaxLength;
import me.ampayne2.DropParty.command.commands.set.CommandSetMaxStack;
import me.ampayne2.DropParty.command.commands.set.CommandSetTeleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandController extends JavaPlugin {

	private final SubCommand mainCommand = new SubCommand();

	public CommandController() {

		CommandStart start = new CommandStart();
		mainCommand.addCommand("start", start);

		CommandStart stop = new CommandStart();
		mainCommand.addCommand("stop", stop);
		
		CommandTeleport teleport = new CommandTeleport();
		mainCommand.addCommand("teleport", teleport);
		
		CommandCreate create = new CommandCreate();
		mainCommand.addCommand("create", create);
		
		SubCommand set = new SubCommand();
		set.addCommand("chest", new CommandSetChest());
		set.addCommand("itempoint", new CommandSetItempoint());
		set.addCommand("teleport", new CommandSetTeleport());
		set.addCommand("itemdelay", new CommandSetItemDelay());
		set.addCommand("maxstack", new CommandSetMaxStack());
		set.addCommand("maxlength", new CommandSetMaxLength());
		mainCommand.addCommand("set", set);
		
		SubCommand remove = new SubCommand();
		remove.addCommand("chest", new CommandRemoveChest());
		remove.addCommand("itempoint", new CommandRemoveItempoint());
		mainCommand.addCommand("remove", remove);
		
		SubCommand list = new SubCommand();
		list.addCommand("parties", new CommandListParties());
		list.addCommand("chests", new CommandListChests());
		list.addCommand("itempoints", new CommandListItempoints());
		list.addCommand("settings", new CommandListSettings());
		list.addCommand("teleport", new CommandListTeleport());
		mainCommand.addCommand("list", list);
		
		CommandDelete delete = new CommandDelete();
		mainCommand.addCommand("delete", delete);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!cmd.getName().equalsIgnoreCase("dropparty"))
			return false;

		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
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
