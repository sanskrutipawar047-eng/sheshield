package edunet.sheshield_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/** One danger event raised for a user. */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alertId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id")      // optional: alert may happen outside a trip
    private Trip trip;

    private double dangerScore;        // 0 to 100

    @Enumerated(EnumType.STRING)
    private AlertLevel alertLevel;

    private LocalDateTime triggeredAt;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    private Boolean falseAlarm;        // null = user has not answered feedback yet
}