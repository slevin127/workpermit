/*
Инкапсулирует логику взаимодействия с WorkPermitRepository.
Состоит из:
Методы: getAllPermits(), savePermit(...)
Использует @Service, внедряет репозиторий через конструктор

Функции:
Промежуточное звено между контроллером и БД
Позволяет централизованно управлять логикой нарядов
 */
package com.azarin.workpermit.service;

import com.azarin.workpermit.model.WorkPermit;
import com.azarin.workpermit.repository.WorkPermitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkPermitService {

    private final WorkPermitRepository repository;

    public WorkPermitService(WorkPermitRepository repository) {
        this.repository = repository;
    }

    public List<WorkPermit> getAllPermits() {
        return repository.findAll();
    }

    public void savePermit(WorkPermit permit) {
        repository.save(permit);
    }
    public Optional<WorkPermit> getPermitById(Long id) {
        return repository.findById(id);
    }

}
