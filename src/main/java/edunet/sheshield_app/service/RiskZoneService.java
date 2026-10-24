package edunet.sheshield_app.service;

import edunet.sheshield_app.dto.RiskZoneDto;
import java.util.List;

/** Risk zones for the heatmap table. */
public interface RiskZoneService {
    List<RiskZoneDto> getAllZones();
}
