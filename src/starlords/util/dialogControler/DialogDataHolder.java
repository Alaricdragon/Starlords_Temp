package starlords.util.dialogControler;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class DialogDataHolder {
    private String targetID;
    private HashMap<String,String> strings = new HashMap<>();
    private HashMap<String,Boolean> booleans = new HashMap<>();
    private HashMap<String,Integer> integers = new HashMap<>();
}
