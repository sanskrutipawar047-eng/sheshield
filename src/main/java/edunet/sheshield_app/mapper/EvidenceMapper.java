package edunet.sheshield_app.mapper;

import edunet.sheshield_app.dto.EvidenceDto;
import edunet.sheshield_app.entity.Evidence;

/** Converts Evidence entity -> EvidenceDto. */
public class EvidenceMapper {
    public static EvidenceDto toDto(Evidence e) {
        return new EvidenceDto(e.getEvidenceId(), e.getOriginalFileName(),
                e.getFileType(), e.getSha256Hash(), e.getCapturedAt());
    }
}