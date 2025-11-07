package starlords.person.templateSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LordFleetData {
    /*todo: additional data required:
          -commander data (for in combat fleet command.)
          -custom scripts (for all upgrades)
          ...
          -additional data:
            right now the only 'additional data' that exsists is Attrubutes. But I will need to have a MemCompresssedHolder to handle the relevant data for this for each starlord. maybe?
            ... no I don't. This can be held in the lords MemCompressedData if only I make it possible to hold and phrase strings (AKA class paths, or other things)
          */
    public LordShipData flagship;
    public List<LordShipData> fleet = new ArrayList<>();
}
