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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.DPPartyController;
import me.ampayne2.DropParty.command.commands.set.CommandSetFireworkAmount;
import me.ampayne2.DropParty.command.commands.set.CommandSetFireworkDelay;
import me.ampayne2.DropParty.command.commands.set.CommandSetItemDelay;
import me.ampayne2.DropParty.command.commands.set.CommandSetMaxLength;
import me.ampayne2.DropParty.command.commands.set.CommandSetMaxStack;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;
import me.ampayne2.DropParty.database.tables.DPSettingsTable;

public class CommandCreate implements DPCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String dpid;
		if (args.length == 1) {
			dpid = args[0];
		} else {
			return;
		}
		if (!sender.hasPermission("dropparty.create") && !sender.hasPermission("dropparty.*")) {
			return;
		}
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null) {
			DPPartiesTable partytable = new DPPartiesTable();
			DPSettingsTable settingstable = new DPSettingsTable();
			partytable.dpid = dpid;
			settingstable.dpid = dpid;
			settingstable.itemdelay = CommandSetItemDelay.getDefaultItemDelay();
			settingstable.maxlength = CommandSetMaxLength.getDefaultMaxLength();
			settingstable.maxstack = CommandSetMaxStack.getDefaultMaxStack();
			settingstable.fireworkdelay = CommandSetFireworkDelay.getDefaultFireworkDelay();
			settingstable.fireworkamount = CommandSetFireworkAmount.getDefaultFireworkAmount();
			DatabaseManager.getDatabase().save(partytable);
			DatabaseManager.getDatabase().save(settingstable);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpcreate"), dpid);
			DPPartyController.getParties();
			return;
		} else {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartyalreadyexists"), dpid);
		}
	}
}
