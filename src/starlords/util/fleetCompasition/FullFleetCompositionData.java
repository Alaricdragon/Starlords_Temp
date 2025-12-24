package starlords.util.fleetCompasition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullFleetCompositionData {
    private FleetCompositionData combat;
    private FleetCompositionData cargo;
    private FleetCompositionData fuel;
    private FleetCompositionData personal;
    private FleetCompositionData tug;
}
