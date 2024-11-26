package zeroone.developers.orderapp

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun findByIdAndDeletedFalse(id: Long): T?
    fun trash(id: Long): T?
    fun trashList(ids: List<Long>): List<T?>
    fun findAllNotDeleted(): List<T>
    fun findAllNotDeleted(pageable: Pageable): List<T>
    fun findAllNotDeletedForPageable(pageable: Pageable): Page<T>
    fun saveAndRefresh(t: T): T
}

class BaseRepositoryImpl<T : BaseEntity>(
    entityInformation: JpaEntityInformation<T, Long>,
    private val entityManager: EntityManager
) : SimpleJpaRepository<T, Long>(entityInformation, entityManager), BaseRepository<T> {

    val isNotDeletedSpecification = Specification<T> { root, _, cb -> cb.equal(root.get<Boolean>("deleted"), false) }

    override fun findByIdAndDeletedFalse(id: Long) = findByIdOrNull(id)?.run { if (deleted) null else this }

    @Transactional
    override fun trash(id: Long): T? = findByIdOrNull(id)?.run {
        deleted = true
        save(this)
    }

    override fun findAllNotDeleted(): List<T> = findAll(isNotDeletedSpecification)
    override fun findAllNotDeleted(pageable: Pageable): List<T> = findAll(isNotDeletedSpecification, pageable).content
    override fun findAllNotDeletedForPageable(pageable: Pageable): Page<T> =
        findAll(isNotDeletedSpecification, pageable)

    override fun trashList(ids: List<Long>): List<T?> = ids.map { trash(it) }

    @Transactional
    override fun saveAndRefresh(t: T): T {
        return save(t).apply { entityManager.refresh(this) }
    }
}

@Repository
interface UserRepository : BaseRepository<User> {
    fun findByUsernameAndDeletedFalse(username: String): User?

    @Query("""
        select u from users u
        where u.id != :id
        and u.username = :username
        and u.deleted = false 
    """)
    fun findByUsername(id: Long, username: String): User?

}

interface PaymentRepository : JpaRepository<Payment, Long> {

//    // Foydalanuvchiga tegishli to'lovlar ro'yxatini olish
//    fun findByUser(user: User): List<Payment>

    fun findByOrderUserId(userId: Long): List<Payment>
}


@Repository
interface CategoryRepository : BaseRepository<Category> {
    @Query(value = "select count(*) > 0 from category c where c.name = :name", nativeQuery = true)
    fun existsByName(@Param("name") name: String): Boolean

    fun findByNameAndDeletedFalse(ame: String): Category?

    @Query("""
        select c from category c
        where c.id != :id
        and c.name = :name
        and c.deleted = false 
    """)
    fun findByName(id: Long, name: String): Category?

    // User ID exists check
    @Query(value = "select count(*) > 0 from category c where c.id = :id", nativeQuery = true)
    fun existsByCategoryId(@Param("id") id: Long?): Boolean

}


@Repository
interface ProductRepository : BaseRepository<Product> {

    fun findByNameAndDeletedFalse(ame: String): Product?

    @Query("""
        select p from product p
        where p.id != :id
        and p.name = :name
        and p.deleted = false 
    """)
    fun findByName(id: Long, name: String): Product?

}



@Repository
interface OrderRepository : BaseRepository<Order> {

    fun findAllByUserId(userId: Long): List<Order>

    @Query("SELECT o FROM orders o WHERE o.user.id = :userId AND FUNCTION('MONTH', o.createdDate) = :month AND FUNCTION('YEAR', o.createdDate) = :year")
    fun findByUserIdAndMonthAndYear(userId: Long, month: Int, year: Int): List<Order>

    @Query("SELECT o FROM orders o WHERE o.user.id = :userId AND o.createdDate BETWEEN :startDate AND :endDate")
    fun findAllByUserIdAndDateRange(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<Order>


}


@Repository
interface OrderItemRepository : BaseRepository<OrderItem> {

    fun findByOrderId(orderId: Long?): List<OrderItem>

    fun findByOrderIdAndProductId(orderId: Long, productId: Long): Optional<OrderItem>

    fun countByProductId(productId: Long): Int


}








