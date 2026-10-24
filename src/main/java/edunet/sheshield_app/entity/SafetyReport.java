package edunet.sheshield_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A community report about an unsafe place (can be anonymous). */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class SafetyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private RiskZone riskZone;

    @ManyToOne
    @JoinColumn(name = "user_id")      // stays null for anonymous reports
    private User user;

    private String category;           // e.g. "Poor lighting", "Stalking"

    @Column(length = 1000)
    private String description;

    private boolean verified;          // admin verifies it later
}