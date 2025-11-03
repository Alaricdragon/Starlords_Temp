package starlords.util.memoryUtils.Compressed.hTypes;

public class MemCompressed_Random_Double_Static extends MemCompressed_Random_Double_Base {
    double value;
    public MemCompressed_Random_Double_Static(double value){
        this.value = value;
    }

    @Override
    public double getRandom(Object linkedObject) {
        return value;
    }
}
