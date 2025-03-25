/*
Учёт смен — фиксирует факт допуска и окончания работ в определённую смену.
Состоит из:
Дата, смена, время начала/окончания
ФИО допускающего и производителя
Признаки начала/окончания работ
@ManyToOne → WorkPermit
Функции:
Позволяет отслеживать хронологию выполнения работ
Используется для последующего анализа и отчётности
 */
package com.azarin.workpermit.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String shift;
    private Integer teamSize;

    private String producerName;
    private String allowerName;

    private LocalTime startTime;
    private LocalTime endTime;

    private boolean started;
    private boolean finished;

    @ManyToOne
    @JoinColumn(name = "work_permit_id")
    private WorkPermit workPermit;
}
