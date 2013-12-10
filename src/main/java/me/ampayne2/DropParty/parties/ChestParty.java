package me.ampayne2.dropparty.parties;

import me.ampayne2.dropparty.DPChest;
import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.config.ConfigType;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChestParty extends Party {
    private Set<DPChest> chests = new HashSet<>();

    public ChestParty(DropParty dropParty, String partyName, Location teleport) {
        super(dropParty, partyName, PartyType.CHEST_PARTY,teleport);
    }

    public void addChest(DPChest chest) {
        chests.add(chest);
        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        String path = "Parties." + partyName + ".chests";
        List<String> dpChests = partyConfig.getStringList(path);
        dpChests.add(chest.toConfig());
        partyConfig.set(path, dpChests);
    }

    public void removeChest(Chest chest) {
        for (DPChest dpChest : new HashSet<>(chests)) {
            if (dpChest.getChest().equals(chest)) {
                chests.remove(dpChest);
                FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
                String path = "Parties." + partyName + ".chests";
                List<String> dpChests = partyConfig.getStringList(path);
                dpChests.remove(dpChest.toConfig());
                partyConfig.set(path, dpChests);
            }
        }
    }

    public Set<DPChest> getChests() {
        return chests;
    }

    @Override
    public boolean dropNext() {
        if (chests.size() > 0 && itemPoints.size() > 0) {
            ItemStack itemStack = null;
            for (DPChest chest : chests) {
                itemStack = chest.getNextItemStack();
                if (itemStack != null) {
                    break;
                }
            }
            return itemStack != null && dropItemStack(itemStack);
        } else {
            return false;
        }
    }

    @Override
    public void save(ConfigurationSection section) {
        super.save(section);
        List<String> dpChests = new ArrayList<>();
        for (DPChest chest : chests) {
            dpChests.add(chest.toConfig());
        }
        section.set("chests", dpChests);
    }
}
