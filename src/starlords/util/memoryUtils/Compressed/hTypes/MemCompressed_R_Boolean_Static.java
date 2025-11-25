package starlords.util.memoryUtils.Compressed.hTypes;

public class MemCompressed_R_Boolean_Static extends MemCompressed_R_Boolean_Base{
    private boolean odds;
    public MemCompressed_R_Boolean_Static(boolean data){
        this.odds = odds;
    }

    @Override
    public boolean getRandom(Object linkedObject) {
        return odds;
    }
}
