package com.vigyanmancha.backend.domain.postgres;

import com.vigyanmancha.backend.domain.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "examination_centre_details")
public class ExaminationCentreDetails extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Name of the examination center

    @OneToOne
    @JoinColumn(name = "school_id")  // One-to-one relationship with SchoolDetails
    private SchoolDetails details;

    @ManyToOne
    @JoinColumn(name = "vigyankendra_id")
    private VigyanKendraDetails vigyanKendraDetails;

    @OneToMany(mappedBy = "examinationCentre", cascade = CascadeType.ALL)
    private List<SchoolDetails> schools;  // One-to-many relationship with SchoolDetails
}

