package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.RushHour;

import java.util.List;
import java.util.Map;

public interface RushHourService {
    List<RushHour> findRushHoursByWeekDay(int weekDay);
}
