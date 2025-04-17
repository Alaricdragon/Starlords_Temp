package starlords.util.memoryUtils;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class DataHolder {
    protected HashMap<String,String> strings = new HashMap<>();
    protected HashMap<String,Boolean> booleans = new HashMap<>();
    protected HashMap<String,Integer> integers = new HashMap<>();
}
