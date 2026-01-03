package starlords.util.ScriptedValues;

public class SV_B_C implements SV_Boolean{
    SV_Boolean[] values;
    SV_String[] conditions;
    @Override
    public void init(ScriptedValueController value) {
        int size = (int) value.getNextDouble().getValue(null);
        //total size of values = size / 2 + 1 (3 = 2. 5 = 3)
        //totoal size of condtions = size / 2 (3 = 1. 5 = 2.)
        values = new SV_Boolean[(size / 2) + 1];
        conditions = new SV_String[size / 2];
        int added = 0;
        for (int a = 0; a < values.length; a++){
            values[a] = value.getNextBoolean();
            if (a == values.length-1) break;
            conditions[a] = value.getNextString();
        }
    }

    @Override
    public boolean getValue(Object linkedObject) {
        boolean last = values[0].getValue(linkedObject);
        for (int a = 0; a < conditions.length; a++) last = getNextCheck(linkedObject,last,a);
        return last;
    }
    private boolean getNextCheck(Object linkedObject,boolean lastBool,int checkAt){
        String check = conditions[checkAt].getValue(linkedObject);
        boolean nextBool = values[checkAt+1].getValue(linkedObject);//in all cases, values should be one graeter then conditions.
        switch (check){
            case "AND":
                return lastBool && nextBool;
            case "OR":
                return lastBool || nextBool;
            case "NAND":
                return !(lastBool && nextBool);
            case "NOR":
                return !(lastBool || nextBool);
            case "XOR":
                return (lastBool && !nextBool) || (!lastBool && nextBool);
            default:
                return false;
        }
    }
}
