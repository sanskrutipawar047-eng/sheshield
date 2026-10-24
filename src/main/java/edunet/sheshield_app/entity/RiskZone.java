package edunet.sheshield_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** An area with a risk score, shown in the risk heatmap table. */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class RiskZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zoneId;

    private String areaName;
    private double latitude;
    private double longitude;
    private String timeBand;           // e.g. "Night (10 PM - 5 AM)"
    private double riskScore;          // 0 to 100
}