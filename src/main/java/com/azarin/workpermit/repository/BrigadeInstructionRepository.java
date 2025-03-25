/*
Стандартные интерфейсы Spring Data JPA для работы с БД.

Состоит из:
Наследование от JpaRepository<Entity, Long>
Автоматическое создание методов поиска, сохранения, удаления

Функции:
Позволяют сервису получать и сохранять сущности без SQL
Используются внутри service-классов
 */
package com.azarin.workpermit.repository;

import com.azarin.workpermit.model.BrigadeInstruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrigadeInstructionRepository extends JpaRepository<BrigadeInstruction, Long> {

}
