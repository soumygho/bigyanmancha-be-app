package com.vigyanmancha.backend.domain.postgres;

import com.vigyanmancha.backend.domain.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_details")
public class UserDetails extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kendra_id", referencedColumnName = "id")
    private VigyanKendraDetails  vigyanKendraDetails;

    private String name;
}
