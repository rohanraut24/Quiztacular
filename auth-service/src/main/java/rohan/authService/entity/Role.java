package rohan.authService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 20)
    private RoleType name;

    public enum RoleType {
        ROLE_USER,
        ROLE_ADMIN,
        ROLE_MODERATOR
    }

    public Role(RoleType name) {
        this.name = name;
    }
}