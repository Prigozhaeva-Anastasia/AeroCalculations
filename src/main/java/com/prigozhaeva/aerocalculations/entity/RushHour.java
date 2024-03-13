package com.prigozhaeva.aerocalculations.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "rush_hour")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RushHour {
    @Id
    @Column(name="id")
    private Long id;
    @Column(name="from_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime fromTime;
    @Column(name="to_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime toTime;
    @Column(name="week_day")
    private int weekDay;
}
