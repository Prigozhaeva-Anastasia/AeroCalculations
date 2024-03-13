package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.RushHour;
import com.prigozhaeva.aerocalculations.repository.RushHourRepository;
import com.prigozhaeva.aerocalculations.service.RushHourService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RushHourServiceImpl implements RushHourService {
    private RushHourRepository rushHourRepository;

    public RushHourServiceImpl(RushHourRepository rushHourRepository) {
        this.rushHourRepository = rushHourRepository;
    }

    @Override
    public List<RushHour> findRushHoursByWeekDay(int weekDay) {
        return rushHourRepository.findRushHoursByWeekDay(weekDay);
    }

    @Override
    public void removeRushHour(Long id) {
        rushHourRepository.deleteById(id);
    }
}
