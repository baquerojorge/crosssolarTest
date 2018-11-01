package com.crossover.techtrial.repository;

import com.crossover.techtrial.dto.DailyElectricity;
import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * HourlyElectricity Repository is for all operations for HourlyElectricity.
 * @author Crossover
 */
@RestResource(exported = false)
public interface HourlyElectricityRepository 
    extends PagingAndSortingRepository<HourlyElectricity,Long> {
  Page<HourlyElectricity> findAllByPanelIdOrderByReadingAtDesc(Long panelId,Pageable pageable);
  
  @Query("SELECT new com.crossover.techtrial.dto.DailyElectricity(DATE(readingAt), SUM(generatedElectricity),"
          + " AVG(generatedElectricity), MIN(generatedElectricity), MAX(generatedElectricity)) "
          + "FROM HourlyElectricity WHERE panel = ?1 GROUP BY DATE(readingAt) ")
  List<DailyElectricity> retrieveDailyHistorical(Panel panel);
  
  
  void deleteAllByPanelId(Long panelId);
}
