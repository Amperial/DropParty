package me.ampayne2.DropParty.command.commands.set;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;
import me.ampayne2.DropParty.database.tables.DPSettingsTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandSetFireworkAmount implements DPCommand {

	public static int defaultFireworkAmount;

	public static void getDefaults(Plugin plugin) {
		defaultFireworkAmount = plugin.getConfig().getInt("defaultpartysettings.fireworkamount");
	}

	public static int getDefaultFireworkAmount() {
		return defaultFireworkAmount;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String dpid;
		int fireworkamount;
		if (!sender.hasPermission("dropparty.set.fireworkamount") && !sender.hasPermission("dropparty.set.*") && !sender.hasPermission("dropparty.*")) {
			return;
		}
		if (args.length != 2) {
			return;
		}
		if (args[0].equals("default")) {
			fireworkamount = defaultFireworkAmount;
			dpid = args[1];
		} else {
			try {
				fireworkamount = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "'" + args[0] + "'" + " is not a positive integer above zero.");
				return;
			}
			if (fireworkamount <= 0) {
				sender.sendMessage(ChatColor.RED + "'" + args[0] + "'" + " is not a positive integer above zero.");
				return;
			}
			dpid = args[1];
		}
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null
				|| !DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne().dpid.equals(dpid)) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartydoesntexist"), dpid);
			return;
		}
		DPSettingsTable table = new DPSettingsTable();
		table.dpid = dpid;
		table.fireworkamount = fireworkamount;
		if (DatabaseManager.getDatabase().select(DPSettingsTable.class).where().equal("dpid", dpid).execute().findOne() == null) {
			DatabaseManager.getDatabase().save(table);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpsetfireworkamount"), dpid);
			return;
		} else {
			DPSettingsTable entry = DatabaseManager.getDatabase().select(DPSettingsTable.class).where().equal("dpid", dpid).execute().findOne();
			table.id = entry.id;
			table.itemdelay = entry.itemdelay;
			table.maxstack = entry.maxstack;
			table.maxlength = entry.maxlength;
			table.fireworkdelay = entry.fireworkdelay;
		}
		DatabaseManager.getDatabase().save(table);
		DPMessageController.sendMessage(player, DPMessageController.getMessage("dpsetfireworkamount"), dpid);
	}

}
