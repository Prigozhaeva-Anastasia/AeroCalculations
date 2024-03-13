package com.prigozhaeva.aerocalculations.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "rush_hours")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RushHour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
