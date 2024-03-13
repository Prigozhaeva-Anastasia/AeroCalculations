package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.RushHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RushHourRepository extends JpaRepository<RushHour, Long> {
    List<RushHour> findRushHoursByWeekDay(int weekDay);
}
