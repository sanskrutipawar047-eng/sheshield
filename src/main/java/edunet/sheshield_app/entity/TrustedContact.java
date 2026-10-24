package edunet.sheshield_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A person the user trusts, who gets alerts. */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class TrustedContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                 // owner of this contact

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;
}