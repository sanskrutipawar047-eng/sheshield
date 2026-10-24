package edunet.sheshield_app.service;

import edunet.sheshield_app.dto.EvidenceDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/** Evidence vault: encrypted storage + integrity check. */
public interface EvidenceService {
    List<EvidenceDto> getEvidence(Long userId);
    EvidenceDto upload(Long userId, MultipartFile file);
    boolean verifyIntegrity(Long userId, Long evidenceId);
}