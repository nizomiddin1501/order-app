package zeroone.developers.orderapp
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.util.*
import jakarta.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID", example = "1")
    var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @CreatedBy var createdBy: Long? = null,
    @LastModifiedBy var lastModifiedBy: Long? = null,
    //@Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false
    @Column(nullable = false) var deleted: Boolean = false
)


@Table
@Entity(name = "users")
@Schema(description = "User information")
class User(

    @Column(unique = true, nullable = false)
    @Schema(description = "Unique username", example = "nizomiddin097")
    var username: String,

    @Column(nullable = false)
    @Schema(description = "User's password", example = "root123")
    var password: String,

    @Schema(description = "User role", example = "USER")
    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER
) : BaseEntity()


@Table
@Entity(name = "category")
@Schema(description = "Category of the product")
class Category(

    @Column(nullable = false)
    @Schema(description = "Category name", example = "Electronics")
    var name: String
) : BaseEntity()


@Table
@Entity(name = "product")
@Schema(description = "Product details")
class Product(

    @Column(nullable = false)
    @Schema(description = "Product name", example = "Smartphone")
    var name: String,

    @Column(nullable = false)
    @Schema(description = "Product stock count", example = "50")
    var stockCount: Int,

    @Column(nullable = false)
    @Schema(description = "Product price", example = "299.99")
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "Product category", example = "Category ID: 3")
    val category: Category
) : BaseEntity()


@Table
@Entity(name = "orders")
@Schema(description = "Order information")
class Order(

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User who made the order", example = "User ID: 3")
    val user: User,

    @Enumerated(EnumType.STRING)
    @Schema(description = "Order status", example = "PENDING")
    var status: OrderStatus = OrderStatus.PENDING,

    @Column(nullable = false)
    @Schema(description = "Order total price", example = "150.00")
    var totalPrice: BigDecimal// = BigDecimal.ZERO
) : BaseEntity()


@Table
@Entity(name = "order_items")
@Schema(description = "Details of items in an order")
class OrderItem(

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @Schema(description = "User who made the order item", example = "Product ID: 7")
    val product: Product,

    @Column(nullable = false)
    @Schema(description = "Quantity of the product in the order", example = "2")
    var quantity: Int,

    @Column(nullable = false)
    @Schema(description = "Unit price of the product", example = "49.99")
    var unitPrice: BigDecimal,

    @Column(nullable = false)
    @Schema(description = "Total price of the order item", example = "99.98")
    var totalPrice: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(description = "Order of the item", example = "Order ID: 3")
    val order: Order
) : BaseEntity()












