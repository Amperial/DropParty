package me.ampayne2.dropparty.parties;

import me.ampayne2.dropparty.DropParty;
import org.bukkit.Location;

public class CustomParty extends Party {
    public CustomParty(DropParty dropParty, String partyName, Location teleport) {
        super(dropParty, partyName, PartyType.CUSTOM_PARTY, teleport);
    }

    @Override
    public boolean dropNext() {
        return true;
    }
}
