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
package me.ampayne2.DropParty.command.commands;

import me.ampayne2.DropParty.DropParty;
import me.ampayne2.DropParty.DropPartyDrop;
import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;

import org.bukkit.command.CommandSender;

public class CommandStart implements DropPartyCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		DropPartyDrop.setSender(sender);
		DropParty.toggleRunning(sender.getName(), sender);
		DropPartyDrop.dropItems(sender);
	}
}
