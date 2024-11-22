package zeroone.developers.orderapp

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nonnull
import jakarta.persistence.*
import java.math.BigDecimal

data class BaseMessage(val code : Int, val message : String?)


@Schema(description = "Data transfer object for User createRequest")
data class UserCreateRequest(

    @Schema(description = "Unique username", example = "nizomiddin097")
    @field:Nonnull var username: String,

    @Schema(description = "User's password", example = "root123")
    var password: String,

    @Schema(description = "User role", example = "USER")
    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER
){
    fun toEntity(): User {
        return User(username,password,role)
    }
}


@Schema(description = "Data transfer object for User createRequest")
data class UserResponse(

    @Schema(description = "User ID", example = "1")
    val id: Long?,

    @Schema(description = "Unique username", example = "nizomiddin097")
    var username: String?,

    @Schema(description = "User's password", example = "root123")
    var password: String?,

    @Schema(description = "User role", example = "USER")
    @Enumerated(EnumType.STRING)
    var role: UserRole?
){
    companion object{
        fun toResponse(user: User): UserResponse{
            user.run {
                return UserResponse(id!!,username,password,role)
            }
        }
    }
}


@Schema(description = "Data transfer object for User createRequest")
data class UserUpdateRequest(

    @Column(unique = true, nullable = false)
    @Schema(description = "Unique username", example = "nizomiddin097")
    var username: String,

    @Column(nullable = false)
    @Schema(description = "User's password", example = "root123")
    var password: String,

    @Schema(description = "User role", example = "USER")
    @Enumerated(EnumType.STRING)
    var role: UserRole
)


////


@Schema(description = "Data transfer object for Category createRequest")
data class CategoryCreateRequest(

    @Schema(description = "Category name", example = "Electronics")
    @field:Nonnull var name: String
){
    fun toEntity(): Category {
        return Category(name)
    }
}

@Schema(description = "Data transfer object for Category response")
data class CategoryResponse(

    @Schema(description = "Category ID", example = "1")
    val id: Long?,

    @Column(nullable = false)
    @Schema(description = "Category name", example = "Electronics")
    var name: String?
){
    companion object{
        fun toResponse(category: Category): CategoryResponse{
            category.run {
                return CategoryResponse(id!!,name)
            }
        }
    }
}

@Schema(description = "Data transfer object for Category updateRequest")
data class CategoryUpdateRequest(

    @Schema(description = "Category name", example = "Electronics")
    @field:Nonnull var name: String
)

////

@Schema(description = "Data transfer object for Product createRequest")
data class ProductCreateRequest(

    @Schema(description = "Product name", example = "Smartphone")
    @field:Nonnull var name: String,

    @Schema(description = "Product stock count", example = "50")
    var stockCount: Int,

    @Schema(description = "Product price", example = "299.99")
    var price: BigDecimal,

    @Schema(description = "Category ID for the product", example = "3")
    @field:Nonnull val categoryId: Long
){
    fun toEntity(category: Category): Product {
        return Product(name,stockCount,price,category)
    }
}


@Schema(description = "Data transfer object for Product response")
data class ProductResponse(

    @Schema(description = "Product ID", example = "1")
    val id: Long?,

    @Schema(description = "Product name", example = "Smartphone")
    var name: String?,

    @Schema(description = "Product stock count", example = "50")
    var stockCount: Int?,

    @Schema(description = "Product price", example = "299.99")
    var price: BigDecimal?,

    @Schema(description = "Category name for the product", example = "Smartphone")
    val categoryName: String?
){
    companion object{
        fun toResponse(product: Product): ProductResponse{
            product.run {
                return ProductResponse(id!!,name,stockCount,price,category.name)
            }
        }
    }
}


@Schema(description = "Data transfer object for Product updateRequest")
data class ProductUpdateRequest(

    @Schema(description = "Product name", example = "Smartphone")
    @field:Nonnull var name: String,

    @Schema(description = "Product stock count", example = "50")
    var stockCount: Int,

    @Schema(description = "Product price", example = "299.99")
    var price: BigDecimal,
)

////

@Schema(description = "Data transfer object for Order createRequest")
data class OrderCreateRequest(

    @Schema(description = "User ID for the order", example = "3")
    @field:Nonnull val userId: Long,

    @Enumerated(EnumType.STRING)
    @Schema(description = "Order status", example = "PENDING")
    var status: OrderStatus = OrderStatus.PENDING,

    @Schema(description = "Order total price", example = "150.00")
    var totalPrice: BigDecimal
){
    fun toEntity(user: User): Order {
        return Order(user,status,totalPrice)
    }
}


@Schema(description = "Data transfer object for Order response")
data class OrderResponse(

    @Schema(description = "Order ID", example = "1")
    val id: Long?,

    @Schema(description = "User name for the order", example = "nizomiddin")
    @field:Nonnull val username: String?,

    @Enumerated(EnumType.STRING)
    @Schema(description = "Order status", example = "PENDING")
    var status: OrderStatus ?,

    @Schema(description = "Order total price", example = "150.00")
    var totalPrice: BigDecimal?
){
    companion object{
        fun toResponse(order: Order): OrderResponse{
            order.run {
                return OrderResponse(id!!,user.username,status,totalPrice)
            }
        }
    }
}


@Schema(description = "Data transfer object for Order updateRequest")
data class OrderUpdateRequest(

    @Enumerated(EnumType.STRING)
    @Schema(description = "Order status", example = "PENDING")
    var status: OrderStatus,

    @Schema(description = "Order total price", example = "150.00")
    var totalPrice: BigDecimal
)

////

@Schema(description = "Data transfer object for Order Item createRequest")
data class OrderItemCreateRequest(

    @Schema(description = "Product ID for the item", example = "3")
    @field:Nonnull val productId: Long,

    @Schema(description = "Quantity of the product in the order", example = "2")
    var quantity: Int,

    @Schema(description = "Unit price of the product", example = "49.99")
    var unitPrice: BigDecimal,

    @Schema(description = "Total price of the order item", example = "99.98")
    var totalPrice: BigDecimal,

    @Schema(description = "Order ID for the item", example = "3")
    @field:Nonnull val orderId: Long
){
    fun toEntity(product: Product, order: Order): OrderItem {
        return OrderItem(product,quantity,unitPrice,totalPrice,order)
    }
}


@Schema(description = "Data transfer object for Order Item response")
data class OrderItemResponse(

    @Schema(description = "OrderItem ID", example = "1")
    val id: Long?,

    @Schema(description = "Product name for the item", example = "Galaxy S ultra")
    val productName: String?,

    @Schema(description = "Quantity of the product in the order", example = "2")
    var quantity: Int?,

    @Schema(description = "Unit price of the product", example = "49.99")
    var unitPrice: BigDecimal?,

    @Schema(description = "Total price of the order item", example = "99.98")
    var totalPrice: BigDecimal?,

    @Schema(description = "Order status for the item", example = "PENDING")
    val orderStatus: String?
){
    companion object{
        fun toResponse(orderItem: OrderItem): OrderItemResponse{
            orderItem.run {
                return OrderItemResponse(id!!,product.name,quantity,unitPrice,totalPrice, order.status.toString())
            }
        }
    }
}


@Schema(description = "Data transfer object for Order Item updateRequest")
data class OrderItemUpdateRequest(

    @Schema(description = "Quantity of the product in the order", example = "2")
    var quantity: Int,

    @Schema(description = "Unit price of the product", example = "49.99")
    var unitPrice: BigDecimal,

    @Schema(description = "Total price of the order item", example = "99.98")
    var totalPrice: BigDecimal,
)








