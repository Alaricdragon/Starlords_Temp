package starlords.util.fleetCompasition;

import lombok.Getter;
import lombok.Setter;
import starlords.util.ScriptedValues.SV_Double;
import starlords.util.ScriptedValues.SV_String;

@Getter
@Setter
public class FullFleetCompositionData {
    private SV_String portraitDefault;
    private SV_String nameDefault;
    private SV_Double genderDefault;

    private FleetCompositionData combat;
    private FleetCompositionData cargo;
    private FleetCompositionData fuel;
    private FleetCompositionData personal;
    private FleetCompositionData tug;
}
