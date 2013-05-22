package me.ampayne2.DropParty.database.tables;

import com.alta189.simplesave.Field;
import com.alta189.simplesave.Id;
import com.alta189.simplesave.Table;

@Table("DropPartyFireworkpoint")
public class DPFireworkPointsTable {
	@Id
	public int id;

	@Field
	public String dpid;

	@Field
	public String world;

	@Field
	public int x;

	@Field
	public int y;

	@Field
	public int z;
}
