package starlords.PMC;

public class PMC_Faction extends PMC {
    private static final String FACTION_ID_ADDON = "STARLORD_FACTION_";
    public PMC_Faction(String id){
        super(transformID(id));
    }
    public static String transformID(String id){
        return FACTION_ID_ADDON+id;
    }
}
