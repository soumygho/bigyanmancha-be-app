package com.vigyanmancha.backend.repository.mysql;

import com.vigyanmancha.backend.domain.mysql.VigyanKendra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VigyanKendraMySqlRepository extends JpaRepository<VigyanKendra, Long> {
}
