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
package me.ampayne2.DropParty;

import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;

import me.ampayne2.DropParty.command.CommandController;
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
		manager.registerEvents(new DropPartyChestListener(), this);
		manager.registerEvents(new DropPartyGlowstoneListener(), this);
		getConfig().options().copyDefaults(true);
		saveConfig();

		//We load the command
		getCommand("dropparty").setExecutor(new CommandController());

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
