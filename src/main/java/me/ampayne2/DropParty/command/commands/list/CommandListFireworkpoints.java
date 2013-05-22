package me.ampayne2.DropParty.command.commands.list;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPFireworkPointsTable;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;

public class CommandListFireworkpoints implements DPCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		String dpid;
		Player player = (Player) sender;
		if (args.length == 1) {
			dpid = args[0];
		} else {
			return;
		}
		if (!sender.hasPermission("dropparty.list.fireworkpoints") && !sender.hasPermission("dropparty.list.*") && !sender.hasPermission("dropparty.*")) {
			return;
		}
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null
				|| !DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne().dpid.equals(dpid)) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartydoesntexist"), dpid);
			return;
		}
		List<DPFireworkPointsTable> list = DatabaseManager.getDatabase().select(DPFireworkPointsTable.class).where().equal("dpid", dpid).execute().find();
		ListIterator<DPFireworkPointsTable> li = list.listIterator();
		if (list.size() == 0) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpnofireworkpointsfound"), dpid);
			return;
		} else {
			int id = 0;
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistfireworkpoints"), dpid);
			while (li.hasNext()) {
				DPFireworkPointsTable entry = li.next();
				id++;
				DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistfireworkpoints.fireworkpoint") + id + " World: " + entry.world + " X: " + entry.x + " Y: " + entry.y + " Z: "
						+ entry.z, dpid);
			}
		}
	}

}
