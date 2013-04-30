package me.ampayne2.DropParty.database;

import com.alta189.simplesave.Database;
import com.alta189.simplesave.DatabaseFactory;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.alta189.simplesave.mysql.MySQLConfiguration;

import me.ampayne2.DropParty.DropParty;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;
import me.ampayne2.DropParty.database.tables.DropPartyItempointsTable;

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
		db.registerTable(DropPartyChestsTable.class);
		db.registerTable(DropPartyItempointsTable.class);
		db.connect();
	}

	public static Database getDatabase() {
		return db;
	}
}
