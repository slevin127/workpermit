/*
Основная сущность — наряд-допуск. Представляет собой "заглавный документ", содержащий общую информацию о работах, условиях, заказчике и т.д.
Состоит из:
Основные поля наряда: номер, заказчик, исполнитель, даты, условия
@OneToMany → participants — список участников (выдавший, допускающий, бригада)
Метки JPA (@Entity) и Lombok (@Data, @Builder и т.д.)
Функции:
Хранит полную информацию по наряду
Связывает наряд с участниками, сменами, инструктажами
 */
package com.azarin.workpermit.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkPermit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private String organizationCustomer;
    private String organizationExecutor;
    private String workplace;
    private String workDescription;
    private String type; // F01, F02, ...

    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;

    private String workMode;       // 1-сменный, 2-сменный и т.п.
    private String documentRefs;   // ППР, инструкции и т.д.
    private String workConditions; // Особые условия труда
    @Lob
    private String safetyMeasures;

    private String status;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "workPermit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PermitParticipant> participants;

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getOrganizationCustomer() {
        return organizationCustomer;
    }

    public String getOrganizationExecutor() {
        return organizationExecutor;
    }

    public String getWorkplace() {
        return workplace;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public String getWorkMode() {
        return workMode;
    }

    public String getDocumentRefs() {
        return documentRefs;
    }

    public String getWorkConditions() {
        return workConditions;
    }

    public String getSafetyMeasures() {
        return safetyMeasures;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<PermitParticipant> getParticipants() {
        return participants;
    }
}
