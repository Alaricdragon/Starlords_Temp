package starlords.util.memoryUtils.Compressed.hTypes;

public class MemCompressed_R_Double_Static extends MemCompressed_R_Double_Base {
    private double value;
    public MemCompressed_R_Double_Static(double value){
        this.value = value;
    }

    @Override
    public double getRandom(Object linkedObject) {
        return value;
    }
}
