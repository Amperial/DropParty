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

import java.io.IOException;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;

import me.ampayne2.DropParty.command.CommandController;
import me.ampayne2.DropParty.command.commands.set.CommandSetItemDelay;
import me.ampayne2.DropParty.command.commands.set.CommandSetMaxLength;
import me.ampayne2.DropParty.command.commands.set.CommandSetMaxStack;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.listeners.DPChestListener;
import me.ampayne2.DropParty.listeners.DPGlowstoneListener;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DropParty extends JavaPlugin {

	private DatabaseManager dbManager = null;
	private DPMessageController DPMessageController = null;
	private static DropParty instance;

	public static DropParty getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new DPChestListener(), this);
		manager.registerEvents(new DPGlowstoneListener(), this);
		getConfig().options().copyDefaults(true);
		saveConfig();

		getCommand("dropparty").setExecutor(new CommandController());

		try {
			dbManager = new DatabaseManager(this);
		} catch (TableRegistrationException e) {
			getLogger().severe("A error occured while connecting to the database!");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
			return;
		} catch (ConnectionException e) {
			getLogger().severe("A error occured while connecting to the database!");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		DPMessageController = new DPMessageController(this);
		DPPartyController.getParties();
		CommandSetItemDelay.getDefaults(this);
		CommandSetMaxLength.getDefaults(this);
		CommandSetMaxStack.getDefaults(this);

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
		}

		if (this.getConfig().getBoolean("autoupdater.enabled")) {
			@SuppressWarnings("unused")
			Updater updater = new Updater(this, "dropparty", this.getFile(), Updater.UpdateType.DEFAULT, true);
		}
	}

	public void onDisable() {

	}

	public DatabaseManager getDatabaseManager() {
		return dbManager;
	}

	public DPMessageController getDPMessageController() {
		return DPMessageController;
	}
}
