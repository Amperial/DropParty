package me.ampayne2.DropParty.command.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;
import me.ampayne2.DropParty.database.tables.DPSettingsTable;

public class CommandSetFireworkDelay implements DPCommand {
	public static Long defaultFireworkDelay = null;

	public static void getDefaults(Plugin plugin) {
		defaultFireworkDelay = plugin.getConfig().getLong("defaultpartysettings.fireworkdelay");
	}

	public static Long getDefaultFireworkDelay() {
		return defaultFireworkDelay;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String dpid;
		Long fireworkdelay;
		if (!sender.hasPermission("dropparty.set.fireworkdelay") && !sender.hasPermission("dropparty.set.*") && !sender.hasPermission("dropparty.*")) {
			return;
		}
		if (args.length != 2) {
			return;
		}
		if (args[0].equals("default")) {
			fireworkdelay = defaultFireworkDelay;
			dpid = args[1];
		} else {
			try {
				fireworkdelay = Long.parseLong(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "'" + args[0] + "'" + " is not a positive integer above zero.");
				return;
			}
			if (fireworkdelay <= 0) {
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
		table.fireworkdelay = fireworkdelay;
		if (DatabaseManager.getDatabase().select(DPSettingsTable.class).where().equal("dpid", dpid).execute().findOne() == null) {
			DatabaseManager.getDatabase().save(table);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpsetfireworkdelay"), dpid);
			return;
		} else {
			DPSettingsTable entry = DatabaseManager.getDatabase().select(DPSettingsTable.class).where().equal("dpid", dpid).execute().findOne();
			table.id = entry.id;
			table.itemdelay = entry.itemdelay;
			table.maxlength = entry.maxlength;
			table.maxstack = entry.maxstack;
			table.fireworkamount = entry.fireworkamount;
		}
		DatabaseManager.getDatabase().save(table);
		DPMessageController.sendMessage(player, DPMessageController.getMessage("dpsetfireworkdelay"), dpid);
	}

}
