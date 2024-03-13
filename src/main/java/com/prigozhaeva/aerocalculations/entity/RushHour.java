package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

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
    @Column(name="from")
    private LocalTime from;
    @Column(name="to")
    private LocalTime to;
    @Column(name="week_day")
    private int weekDay;
}
