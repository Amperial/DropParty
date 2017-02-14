/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2014 <http://dev.bukkit.org/server-mods/dropparty//>
 *
 * DropParty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DropParty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DropParty.  If not, see <http://www.gnu.org/licenses/>.
 */
package ninja.amp.dropparty;

import ninja.amp.dropparty.parties.Party;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

/**
 * A drop party firework point.
 */
public class DPFireworkPoint {

    private final DropParty dropParty;
    private final Party party;
    private final Location location;
    private static final Random RANDOM = new Random();

    /**
     * Creates a new firework point.
     *
     * @param dropParty The DropParty instance.
     * @param party     The party of the firework point.
     * @param location  The location of the firework point.
     */
    public DPFireworkPoint(DropParty dropParty, Party party, Location location) {
        this.dropParty = dropParty;
        this.party = party;
        this.location = location;
    }

    /**
     * Gets the firework point's party.
     *
     * @return The firework point's party.
     */
    public Party getParty() {
        return party;
    }

    /**
     * Gets the firework point's location.
     *
     * @return The firework point's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Spawns a firework at the firework point.
     */
    public void spawnFirework() {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);

        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(randomFireworkEffect());
        fireworkMeta.setPower(RANDOM.nextInt(2) + 1);

        firework.setFireworkMeta(fireworkMeta);
    }

    /**
     * Converts the firework point into a string for storage in a config.
     *
     * @return The string representation of the firework point.
     */
    public String toConfig() {
        return party.getName() + ";" + DPUtils.locationToString(location);
    }

    /**
     * Converts a string represnetation of a firework point into a firework point.
     *
     * @param dropParty The DropParty instance.
     * @param party     The party of the firework point.
     * @param string    The string representation of the firework point.
     * @return The firework point.
     */
    public static DPFireworkPoint fromConfig(DropParty dropParty, Party party, String string) {
        try {
            String[] parts = string.split(";");
            if (parts.length == 2) {
                String partyName = parts[0];
                if (partyName.equals(party.getName())) {
                    return new DPFireworkPoint(dropParty, party, DPUtils.stringToLocation(parts[1]));
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets a random firework effect.
     *
     * @return The random firework effect.
     */
    public static FireworkEffect randomFireworkEffect() {
        FireworkEffect.Type type = DPUtils.randomEnum(FireworkEffect.Type.class);
        Color c1 = DPUtils.randomColor();
        Color c2 = DPUtils.randomColor();

        return FireworkEffect.builder().flicker(RANDOM.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(RANDOM.nextBoolean()).build();
    }

}
