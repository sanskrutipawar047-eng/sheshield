package edunet.sheshield_app.dto;

import java.time.LocalDateTime;

/** Evidence data shown on the vault page. The file path is never sent to the page. */
public record EvidenceDto(Long evidenceId, String originalFileName, String fileType,
                          String sha256Hash, LocalDateTime capturedAt) { }