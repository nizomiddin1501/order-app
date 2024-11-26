package zeroone.developers.orderapp

import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDto(user: User): UserResponse {
        return user.run {
            UserResponse(
                id = this.id,
                username = this.username,
                password = this.password,
                role = this.role,
                balance = this.balance
            )
        }
    }

    fun toEntity(createRequest: UserCreateRequest): User {
        return createRequest.run {
            User(
                username = this.username,
                password = this.password,
                role = this.role,
                balance = this.balance
            )
        }
    }

    fun updateEntity(user: User, updateRequest: UserUpdateRequest): User {
        return updateRequest.run {
            user.apply {
                updateRequest.username.let { this.username = it }
                updateRequest.password.let { this.password = it }
                updateRequest.role.let { this.role = it }
                updateRequest.balance.let { this.balance = it }
            }
        }
    }
}

@Component
class CategoryMapper {
    fun toDto(category: Category): CategoryResponse {
        return category.run {
            CategoryResponse(
                id = this.id,
                name = this.name,
            )
        }
    }

    fun toEntity(createRequest: CategoryCreateRequest): Category {
        return createRequest.run {
            Category(
                name = this.name,
            )
        }
    }

    fun updateEntity(category: Category, updateRequest: CategoryUpdateRequest): Category {
        return updateRequest.run {
            category.apply {
                updateRequest.name.let { this.name = it }
            }
        }
    }
}


@Component
class ProductMapper {

    fun toDto(product: Product): ProductResponse {
        return product.run {
            ProductResponse(
                id = this.id,
                name = this.name,
                stockCount = this.stockCount,
                price = this.price,
                categoryName = this.category.name
            )
        }
    }

    fun toEntity(createRequest: ProductCreateRequest, category: Category): Product {
        return createRequest.run {
            Product(
                name = this.name,
                stockCount = this.stockCount,
                price = this.price,
                category = category
            )
        }
    }

    fun updateEntity(product: Product, updateRequest: ProductUpdateRequest): Product {
        return updateRequest.run {
            product.apply {
                updateRequest.name.let { this.name = it }
                updateRequest.stockCount.let { this.stockCount = it }
                updateRequest.price.let { this.price = it }
            }
        }
    }
}


@Component
class OrderMapper {

    fun toDto(order: Order): OrderResponse {
        return order.run {
            OrderResponse(
                id = this.id,
                username = this.user.username,
                status = this.status,
                totalPrice = this.totalPrice,
            )
        }
    }

    fun toEntity(createRequest: OrderCreateRequest, user: User): Order {
        return createRequest.run {
            Order(
                user = user,
                status = this.status,
                totalPrice = this.totalPrice
            )
        }
    }

    fun updateEntity(order: Order, updateRequest: OrderUpdateRequest): Order {
        return updateRequest.run {
            order.apply {
                updateRequest.status.let { this.status = it }
                updateRequest.totalPrice.let { this.totalPrice = it }
            }
        }
    }
}

@Component
class OrderItemMapper {

    fun toDto(orderItem: OrderItem): OrderItemResponse {
        return orderItem.run {
            OrderItemResponse(
                id = this.id,
                productName = this.product.name,
                quantity = this.quantity,
                unitPrice = this.unitPrice,
                totalPrice = this.totalPrice,
                orderStatus = this.order.status.toString()
            )
        }
    }

    fun toEntity(createRequest: OrderItemCreateRequest, product: Product, order: Order): OrderItem {
        return createRequest.run {
            OrderItem(
                product = product,
                quantity = this.quantity,
                unitPrice = this.unitPrice,
                totalPrice = this.totalPrice,
                order = order
            )
        }
    }

    fun updateEntity(orderItem: OrderItem, updateRequest: OrderItemUpdateRequest): OrderItem {
        return updateRequest.run {
            orderItem.apply {
                updateRequest.quantity.let { this.quantity = it }
                updateRequest.unitPrice.let { this.unitPrice = it }
                updateRequest.totalPrice.let { this.totalPrice = it }
            }
        }
    }
}


@Component
class PaymentMapper {

    fun toDto(payment: Payment): PaymentResponse {
        return payment.run {
            PaymentResponse(
                id = this.id,
                orderStatus = this.order.status.toString(),
                amount = this.amount,
                paymentDate = this.paymentDate,
                paymentMethod = this.paymentMethod
            )
        }
    }

    fun toEntity(createRequest: PaymentCreateRequest, order: Order): Payment {
        return createRequest.run {
            Payment(
                order = order,
                amount = this.amount,
                paymentDate = this.paymentDate,
                paymentMethod = this.paymentMethod
            )
        }
    }

    fun updateEntity(payment: Payment, updateRequest: PaymentUpdateRequest): Payment {
        return updateRequest.run {
            payment.apply {
                updateRequest.amount.let { this.amount = it }
                updateRequest.paymentDate.let { this.paymentDate = it }
                updateRequest.paymentMethod.let { this.paymentMethod = it }
            }
        }
    }
}





