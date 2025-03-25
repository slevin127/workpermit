/*
Хранит сведения о прохождении инструктажа членами бригады.
Состоит из:
ФИО, профессия, дата, отметка о подписи
@ManyToOne → WorkPermit
Функции:
Подтверждение инструктажа перед началом работ
Важно для соблюдения требований охраны труда
 */
package com.azarin.workpermit.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrigadeInstruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String profession;
    private LocalDate date;
    private Boolean signed;

    @ManyToOne
    @JoinColumn(name = "work_permit_id")
    private WorkPermit workPermit;
}
