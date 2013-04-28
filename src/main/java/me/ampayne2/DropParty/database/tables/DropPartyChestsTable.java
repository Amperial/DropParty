package me.ampayne2.DropParty.database.tables;


import com.alta189.simplesave.Field;
import com.alta189.simplesave.Id;
import com.alta189.simplesave.Table;

@Table("DropPartyChest")
public class DropPartyChestsTable {

	@Id
	public int id;

	@Field
	public double x;

	@Field
	public double y;

	@Field
	public double z;

	public String toString() {
		return x + ":" + y + ":" + z;
	}
}
