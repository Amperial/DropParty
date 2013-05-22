package me.ampayne2.DropParty;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPFireworkPointsTable;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class DPFirework extends BukkitRunnable{
	
	CommandSender sender = null;
	String party = null;
	Long delay = null;
	int amount = 0;
	int camount = 0;
	
	public DPFirework(CommandSender ssender, String dpid, Long fireworkdelay, int fireworkamount, int currentamount){
		sender = ssender;
		party = dpid;
		delay = fireworkdelay;
		amount = fireworkamount;
		camount = currentamount;
	}

	public static void spawnFirework(CommandSender sender, String dpid, Long delay, int amount, int camount) {
		if(amount == 0){
			return;
		}
		Location[] fireworkPoints = DPFirework.getFireworkPoints(sender, dpid);
		if(fireworkPoints == null){
			return;
		}
		createFirework(fireworkPoints);
		if(camount != amount){
			camount++;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DropParty.getInstance(), new DPFirework(sender, dpid, delay, amount, camount), delay);
		}
	}
	
	@Override
	public void run() {
		spawnFirework(sender, party, delay, amount, camount);
	}

	public static void createFirework(Location[] fireworkPoints) {
		Random generator = new Random();
		int fireworkpoint = generator.nextInt(fireworkPoints.length);
		World world = fireworkPoints[fireworkpoint].getWorld();
		Firework fw = (Firework) world.spawnEntity(fireworkPoints[fireworkpoint], EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		Random r = new Random();

		int rt = r.nextInt(5) + 1;
		Type type = Type.BALL;
		if (rt == 1)
			type = Type.BALL;
		if (rt == 2)
			type = Type.BALL_LARGE;
		if (rt == 3)
			type = Type.BURST;
		if (rt == 4)
			type = Type.CREEPER;
		if (rt == 5)
			type = Type.STAR;
		int r1i = r.nextInt(17) + 1;
		int r2i = r.nextInt(17) + 1;
		Color c1 = getColor(r1i);
		Color c2 = getColor(r2i);
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}

	private static Color getColor(int i) {
		Color c = null;
		if (i == 1) {
			c = Color.AQUA;
		}
		if (i == 2) {
			c = Color.BLACK;
		}
		if (i == 3) {
			c = Color.BLUE;
		}
		if (i == 4) {
			c = Color.FUCHSIA;
		}
		if (i == 5) {
			c = Color.GRAY;
		}
		if (i == 6) {
			c = Color.GREEN;
		}
		if (i == 7) {
			c = Color.LIME;
		}
		if (i == 8) {
			c = Color.MAROON;
		}
		if (i == 9) {
			c = Color.NAVY;
		}
		if (i == 10) {
			c = Color.OLIVE;
		}
		if (i == 11) {
			c = Color.ORANGE;
		}
		if (i == 12) {
			c = Color.PURPLE;
		}
		if (i == 13) {
			c = Color.RED;
		}
		if (i == 14) {
			c = Color.SILVER;
		}
		if (i == 15) {
			c = Color.TEAL;
		}
		if (i == 16) {
			c = Color.WHITE;
		}
		if (i == 17) {
			c = Color.YELLOW;
		}

		return c;
	}

	public static Location[] getFireworkPoints(CommandSender sender, String dpid) {
		List<DPFireworkPointsTable> list = DatabaseManager.getDatabase().select(DPFireworkPointsTable.class).where().equal("dpid", dpid).execute().find();
		ListIterator<DPFireworkPointsTable> li = list.listIterator();
		Location[] fireworkPoints = new Location[list.size()];
		if (list.size() == 0) {
			return null;
		}
		int id = 0;
		while (li.hasNext()) {
			DPFireworkPointsTable entry = li.next();
			World tworld = Bukkit.getServer().getWorld(entry.world);
			Location fireworkPoint = new Location(tworld, entry.x, entry.y, entry.z);
			if (fireworkPoint.getBlock().getType() == Material.AIR) {
				fireworkPoints[id] = fireworkPoint;
				id++;
			} else {
				DatabaseManager.getDatabase().remove(DatabaseManager.getDatabase().select(DPFireworkPointsTable.class).where().equal("dpid", dpid).execute().find().get(id));
			}
		}
		if (id == 0) {
			return null;
		}
		Location[] newFireworkPoints = new Location[id];
		System.arraycopy(fireworkPoints, 0, newFireworkPoints, 0, newFireworkPoints.length);
		return newFireworkPoints;
	}
}
