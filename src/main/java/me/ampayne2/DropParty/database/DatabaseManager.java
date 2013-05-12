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
package me.ampayne2.DropParty.database;

import com.alta189.simplesave.Database;
import com.alta189.simplesave.DatabaseFactory;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.alta189.simplesave.mysql.MySQLConfiguration;

import me.ampayne2.DropParty.DropParty;
import me.ampayne2.DropParty.database.tables.DPChestsTable;
import me.ampayne2.DropParty.database.tables.DPItemPointsTable;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;
import me.ampayne2.DropParty.database.tables.DPSettingsTable;
import me.ampayne2.DropParty.database.tables.DPTeleportsTable;

public class DatabaseManager {

	private static Database db;

	public DatabaseManager(DropParty plugin) throws TableRegistrationException, ConnectionException {
		MySQLConfiguration config = new MySQLConfiguration();
		config.setHost(plugin.getConfig().getString("mysql.host"));
		config.setUser(plugin.getConfig().getString("mysql.username"));
		config.setPassword(plugin.getConfig().getString("mysql.password"));
		config.setDatabase(plugin.getConfig().getString("mysql.database"));
		config.setPort(plugin.getConfig().getInt("mysql.port"));
		config.setPrefix(plugin.getConfig().getString("mysql.prefix"));
		db = DatabaseFactory.createNewDatabase(config);
		db.registerTable(DPChestsTable.class);
		db.registerTable(DPItemPointsTable.class);
		db.registerTable(DPTeleportsTable.class);
		db.registerTable(DPSettingsTable.class);
		db.registerTable(DPPartiesTable.class);
		db.connect();
	}

	public static Database getDatabase() {
		return db;
	}
}
