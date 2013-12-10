package me.ampayne2.dropparty.parties;

/**
 * Types of drop parties.
 */
public enum PartyType {
    CHEST_PARTY("Chest", ChestParty.class),
    CUSTOM_PARTY("Custom", CustomParty.class);

    private final String name;
    private final Class<? extends Party> clazz;

    private PartyType(String name, Class<? extends Party> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Party> getPartyClass() {
        return clazz;
    }
}
