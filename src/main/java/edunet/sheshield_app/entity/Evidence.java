package edunet.sheshield_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/** An encrypted proof file (audio, video, image) saved in the evidence vault. */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Evidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evidenceId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                 // owner of the evidence

    @ManyToOne
    @JoinColumn(name = "alert_id")     // optional: link to an alert if there is one
    private Alert alert;

    private String originalFileName;
    private String fileType;
    private String storagePath;        // where the ENCRYPTED file is stored
    private String sha256Hash;         // hash of the original file, used to verify integrity
    private LocalDateTime capturedAt;
}