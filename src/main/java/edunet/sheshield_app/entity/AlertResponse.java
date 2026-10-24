package edunet.sheshield_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/** Records who was notified about an alert and what happened. */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AlertResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @ManyToOne
    @JoinColumn(name = "alert_id", nullable = false)
    private Alert alert;

    @Enumerated(EnumType.STRING)
    private ResponderType responderType;

    private String action;             // e.g. "SMS sent to Mom"
    private LocalDateTime respondedAt;
}
