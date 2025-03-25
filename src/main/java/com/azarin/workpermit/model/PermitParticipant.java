/*
Описывает одного участника наряда (выдавший, руководитель, член бригады и т.п.)
Состоит из:
ФИО, должность, роль, смена, дата подписи
@ManyToOne → WorkPermit (обратная связь с нарядом)
Функции:
Связан с конкретным нарядом через внешний ключ
Может быть использован для отображения/подписей
 */
package com.azarin.workpermit.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermitParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String position;
    private String role;
    private Integer shiftNumber;
    private Boolean isBrigadeMember;
    private LocalDateTime signedAt;

    @ManyToOne
    @JoinColumn(name = "work_permit_id")
    private WorkPermit workPermit;
}
