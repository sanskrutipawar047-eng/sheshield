package edunet.sheshield_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A registered person (user, guardian volunteer or admin). */
@Entity
@Table(name = "users")   // "user" is a reserved word in H2, so we use "users"
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;       // BCrypt hash, never the plain password

    @Enumerated(EnumType.STRING)
    private Role role;

    private String cancelPinHash;      // normal PIN: really cancels the alert
    private String duressPinHash;      // duress PIN: looks cancelled, silent SOS is sent
}