package starlords.util.memoryUtils.Compressed.hTypes;

public class MemCompressed_R_String_Static extends MemCompressed_R_String_Base{
    private String odds;
    public MemCompressed_R_String_Static(String data){
        this.odds = odds;
    }

    @Override
    public String getRandom(Object linkedObject) {
        return odds;
    }
}
