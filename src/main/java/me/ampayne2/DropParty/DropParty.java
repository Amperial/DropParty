package me.ampayne2.DropParty;

import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;

import me.ampayne2.DropParty.database.DatabaseManager;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DropParty extends JavaPlugin {

	private DatabaseManager dbManager = null;
	private static DropParty instance;

	public DropParty getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new DropPartyListener(), this);
		getConfig().options().copyDefaults(true);
		saveConfig();
		CommandController command = new CommandController();
		getCommand("dropparty").setExecutor(command);
		try {
			dbManager = new DatabaseManager(this);
		} catch (TableRegistrationException e) {
			getLogger().severe(
					"A error occured while connecting to the database!");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
			return;
		} catch (ConnectionException e) {
			getLogger().severe(
					"A error occured while connecting to the database!");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	public void onDisable() {

	}

	public DatabaseManager getDatabaseManager() {
		return dbManager;
	}

}
