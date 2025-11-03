package starlords.PMC;

public class Faction extends PMC {
    private static final String FACTION_ID_ADDON = "STARLORD_FACTION_";
    public Faction(String id){
        super(FACTION_ID_ADDON+id);
    }
}
