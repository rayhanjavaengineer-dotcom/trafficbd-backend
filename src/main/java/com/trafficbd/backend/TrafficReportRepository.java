
package com.trafficbd.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficReportRepository
        extends JpaRepository<TrafficReport, Long> {
}
