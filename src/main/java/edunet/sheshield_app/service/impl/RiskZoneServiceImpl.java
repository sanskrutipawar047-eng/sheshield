package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.dto.RiskZoneDto;
import edunet.sheshield_app.mapper.RiskZoneMapper;
import edunet.sheshield_app.repository.RiskZoneRepository;
import edunet.sheshield_app.service.RiskZoneService;
import java.util.List;
import org.springframework.stereotype.Service;

/** Business logic for risk zones. */
@Service
public class RiskZoneServiceImpl implements RiskZoneService {

    private final RiskZoneRepository riskZoneRepository;

    public RiskZoneServiceImpl(RiskZoneRepository riskZoneRepository) {
        this.riskZoneRepository = riskZoneRepository;
    }

    /** All zones, most dangerous first. */
    @Override
    public List<RiskZoneDto> getAllZones() {
        return riskZoneRepository.findAllByOrderByRiskScoreDesc()
                .stream()
                .map(RiskZoneMapper::toDto)
                .toList();
    }
}
