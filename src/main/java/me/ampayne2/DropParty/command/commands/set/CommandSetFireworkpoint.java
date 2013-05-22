package me.ampayne2.DropParty.command.commands.set;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.commands.remove.CommandRemoveChest;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPFireworkPointsTable;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;

public class CommandSetFireworkpoint implements DPCommand {

	private static Map<String, String> playersSetting = new HashMap<String, String>();

	@Override
	public void execute(CommandSender sender, String[] args) {
		String dpid;
		if (args.length == 1) {
			dpid = args[0];
		} else {
			return;
		}
		if (!sender.hasPermission("dropparty.set.fireworkpoint") && !sender.hasPermission("dropparty.set.*") && !sender.hasPermission("dropparty.*")) {
			return;
		}
		Player player = (Player) sender;
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().find() == null
				|| !DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne().dpid.equals(dpid)) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartydoesntexist"), dpid);
			return;
		}
		if (CommandRemoveChest.isRemoving(player.getName()) && !isSetting(player.getName())) {
			CommandRemoveChest.toggleRemoving(player, dpid);
		}
		if (CommandSetChest.isSetting(player.getName()) && !isSetting(player.getName())) {
			CommandSetChest.toggleSetting(player, dpid);
		}
		if (CommandSetItempoint.isSetting(player.getName()) && !isSetting(player.getName())) {
			CommandSetItempoint.toggleSetting(player, dpid);
		}
		toggleSetting(player, dpid);
	}

	public static void toggleSetting(Player player, String dpid) {
		String playername = player.getName();
		if (isSetting(playername) && getSetting(playername).equals(dpid)) {
			playersSetting.remove(playername);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpsetfireworkpointmodeoff"), dpid);
		} else {
			playersSetting.put(playername, dpid);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpsetfireworkpointmode"), dpid);
		}
	}

	public static boolean isSetting(String playername) {
		if (playersSetting.containsKey(playername)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getSetting(String playername) {
		return playersSetting.get(playername);
	}

	public static void saveFireworkPoint(Player player, String playerName, String dpid, String world, int x, int y, int z) {

		if (DatabaseManager.getDatabase().select(DPFireworkPointsTable.class).where().equal("dpid", dpid).and().equal("x", x).and().equal("y", y).and().equal("z", z).execute().findOne() != null) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpfireworkpointalreadyexists"), dpid);
			return;
		}

		DPFireworkPointsTable table = new DPFireworkPointsTable();
		table.dpid = dpid;
		table.world = world;
		table.x = x;
		table.y = y;
		table.z = z;
		DatabaseManager.getDatabase().save(table);
		DPMessageController.sendMessage(player, DPMessageController.getMessage("dpsetfireworkpoint"), dpid);
	}
}
