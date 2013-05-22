package me.ampayne2.DropParty.command.commands.remove;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPFireworkPointsTable;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;

public class CommandRemoveFireworkpoint implements DPCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String dpid;
		String fireworkpoint;
		if (!sender.hasPermission("dropparty.remove.fireworkpoint") && !sender.hasPermission("dropparty.remove.*") && !sender.hasPermission("dropparty.*")) {
			return;
		}
		if (args.length == 2) {
			dpid = args[1];
			fireworkpoint = args[0];
		} else {
			return;
		}
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null
				|| !DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne().dpid.equals(dpid)) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartydoesntexist"), dpid);
			return;
		}
		List<DPFireworkPointsTable> list = DatabaseManager.getDatabase().select(DPFireworkPointsTable.class).where().equal("dpid", dpid).execute().find();
		int fireworkpointid;
		try {
			fireworkpointid = Integer.parseInt(fireworkpoint);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "'" + fireworkpoint + "'" + " is not a positive integer above zero.");
			return;
		}
		if (fireworkpointid <= 0) {
			sender.sendMessage(ChatColor.RED + "'" + args[0] + "'" + " is not a positive integer above zero.");
			return;
		}
		if (!(list.size() >= fireworkpointid)) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpfireworkpointiddoesntexist"), dpid);
			return;
		}
		DPFireworkPointsTable entry = list.get(fireworkpointid - 1);
		DatabaseManager.getDatabase().remove(entry);
		DPMessageController.sendMessage(player, DPMessageController.getMessage("dpremovefireworkpoint"), dpid);
	}

}
