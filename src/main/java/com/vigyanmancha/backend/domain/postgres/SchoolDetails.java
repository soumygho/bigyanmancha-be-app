package com.vigyanmancha.backend.domain.postgres;

import com.vigyanmancha.backend.domain.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school_details")
public class SchoolDetails extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name of the school

    @ManyToOne
    @JoinColumn(name = "kendra_id", referencedColumnName = "id")
    private VigyanKendraDetails vigyanKendraDetails; // Many-to-one relationship with VigyanKendra

    @ManyToOne
    @JoinColumn(name = "exam_centre_id")
    private ExaminationCentreDetails examinationCentre; // One-to-many relationship with Examination Centre
}

