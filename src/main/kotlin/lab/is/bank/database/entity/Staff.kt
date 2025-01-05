package lab.`is`.bank.database.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

/**
 * Экземпляр сущности "Сотрудник", то есть банкира. Нужен в основном для логирования
 */

@Entity
class Staff {

    @Id
    lateinit var uuid: UUID

    lateinit var username: String


}