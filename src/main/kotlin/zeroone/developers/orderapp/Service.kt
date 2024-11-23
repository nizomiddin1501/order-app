package zeroone.developers.orderapp

import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface UserService {
    fun getAll(pageable: Pageable): Page<UserResponse>
    fun getAll(): List<UserResponse>
    fun getOne(id: Long): UserResponse
    fun create(request: UserCreateRequest)
    fun update(id: Long, request: UserUpdateRequest)
    fun delete(id: Long)
}

interface CategoryService {
    fun getAll(pageable: Pageable): Page<CategoryResponse>
    fun getAll(): List<CategoryResponse>
    fun getOne(id: Long): CategoryResponse
    fun create(request: CategoryCreateRequest)
    fun update(id: Long, request: CategoryUpdateRequest)
    fun delete(id: Long)
}

interface ProductService {
    fun getAll(pageable: Pageable): Page<ProductResponse>
    fun getAll(): List<ProductResponse>
    fun getOne(id: Long): ProductResponse
    fun create(request: ProductCreateRequest)
    fun update(id: Long, request: ProductUpdateRequest)
    fun delete(id: Long)
}

interface OrderService {
    fun getAll(pageable: Pageable): Page<OrderResponse>
    fun getAll(): List<OrderResponse>
    fun getOne(id: Long): OrderResponse
    fun create(request: OrderCreateRequest)
    fun update(id: Long, request: OrderUpdateRequest)
    fun delete(id: Long)
}

interface OrderItemService {
    fun getAll(pageable: Pageable): Page<OrderItemResponse>
    fun getAll(): List<OrderItemResponse>
    fun getOne(id: Long): OrderItemResponse
    fun create(request: OrderItemCreateRequest)
    fun update(id: Long, request: OrderItemUpdateRequest)
    fun delete(id: Long)
}


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val entityManager: EntityManager
) : UserService {

    override fun getAll(pageable: Pageable): Page<UserResponse> {
        return userRepository.findAllNotDeletedForPageable(pageable).map {
            UserResponse.toResponse(it)
        }
    }

    override fun getAll(): List<UserResponse> {
        return userRepository.findAllNotDeleted().map {
            UserResponse.toResponse(it)
        }
    }

    override fun getOne(id: Long): UserResponse {
        userRepository.findByIdAndDeletedFalse(id)?.let {
            return UserResponse.toResponse(it)
        } ?: throw UserNotFoundException()
    }

    override fun create(request: UserCreateRequest) {
        request.run {
            val user = userRepository.findByUsernameAndDeletedFalse(username)
            if (user != null) throw UserAlreadyExistsException()
            userRepository.save(this.toEntity())
        }
    }

    override fun update(id: Long, request: UserUpdateRequest) {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()
        request.run {
            username.let {
                val findByUsername = userRepository.findByUsername(id, it)
                if (findByUsername != null) throw UserAlreadyExistsException()
                user.username = it
            }
            password.let { user.password = it }
            role.let { user.role = it }
        }
        userRepository.save(user)
    }

    override fun delete(id: Long) {
        userRepository.trash(id) ?: throw UserNotFoundException()
    }
}


@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val entityManager: EntityManager
) : CategoryService {

    override fun getAll(pageable: Pageable): Page<CategoryResponse> {
        return categoryRepository.findAllNotDeletedForPageable(pageable).map {
            CategoryResponse.toResponse(it)
        }
    }

    override fun getAll(): List<CategoryResponse> {
        return categoryRepository.findAllNotDeleted().map {
            CategoryResponse.toResponse(it)
        }
    }

    override fun getOne(id: Long): CategoryResponse {
        categoryRepository.findByIdAndDeletedFalse(id)?.let {
            return CategoryResponse.toResponse(it)
        } ?: throw CategoryNotFoundException()
    }

    override fun create(request: CategoryCreateRequest) {
        request.run {
            val category = categoryRepository.findByNameAndDeletedFalse(name)
            if (category != null) throw CategoryAlreadyExistsException()
            categoryRepository.save(this.toEntity())
        }
    }

    override fun update(id: Long, request: CategoryUpdateRequest) {
        val category = categoryRepository.findByIdAndDeletedFalse(id) ?: throw CategoryNotFoundException()
        request.run {
            name.let {
                val findByName = categoryRepository.findByName(id, it)
                if (findByName != null) throw CategoryAlreadyExistsException()
                category.name = it
            }
        }
        categoryRepository.save(category)
    }

    override fun delete(id: Long) {
        categoryRepository.trash(id) ?: throw CategoryNotFoundException()
    }
}


@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val entityManager: EntityManager
) : ProductService {

    override fun getAll(pageable: Pageable): Page<ProductResponse> {
        return productRepository.findAllNotDeletedForPageable(pageable).map {
            ProductResponse.toResponse(it)
        }
    }

    override fun getAll(): List<ProductResponse> {
        return productRepository.findAllNotDeleted().map {
            ProductResponse.toResponse(it)
        }
    }

    override fun getOne(id: Long): ProductResponse {
        productRepository.findByIdAndDeletedFalse(id)?.let {
            return ProductResponse.toResponse(it)
        } ?: throw ProductNotFoundException()
    }

    override fun create(request: ProductCreateRequest) {
        request.run {
            val product = productRepository.findByNameAndDeletedFalse(name)
            if (product != null) throw ProductAlreadyExistsException()
            val referenceCategory = entityManager.getReference(
                Category::class.java, categoryId
            )
            productRepository.save(this.toEntity(referenceCategory))

        }
    }

    override fun update(id: Long, request: ProductUpdateRequest) {
        val product = productRepository.findByIdAndDeletedFalse(id)?.let { throw ProductNotFoundException() }
        request.run {
            name.let {
                val findByName = productRepository.findByName(id, it)
                if (findByName != null) throw ProductAlreadyExistsException()
                product.name = it
            }
            stockCount.let { product.stockCount = it }
            price.let { product.price = it }
        }
        product?.let { productRepository.save(it) }
    }

    override fun delete(id: Long) {
        productRepository.trash(id) ?: throw ProductNotFoundException()
    }
}



@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val entityManager: EntityManager
) : OrderService {

    override fun getAll(pageable: Pageable): Page<OrderResponse> {
        return orderRepository.findAllNotDeletedForPageable(pageable).map {
            OrderResponse.toResponse(it)
        }
    }

    override fun getAll(): List<OrderResponse> {
        return orderRepository.findAllNotDeleted().map {
            OrderResponse.toResponse(it)
        }
    }

    override fun getOne(id: Long): OrderResponse {
        orderRepository.findByIdAndDeletedFalse(id)?.let {
            return OrderResponse.toResponse(it)
        } ?: throw OrderNotFoundException()
    }

    override fun create(request: OrderCreateRequest) {
        request.run {
            val userExists = orderRepository.existsByUserId(userId)
            if (!userExists) throw UserNotFoundException()
            val referenceUser = entityManager.getReference(
                User::class.java, userId)
            orderRepository.save(this.toEntity(referenceUser))
        }
    }

    override fun update(id: Long, request: OrderUpdateRequest) {
        val order = orderRepository.findByIdAndDeletedFalse(id) ?: throw OrderNotFoundException()
        request.run {
            status.let { order.status = it }
            totalPrice.let { order.totalPrice = it }
        }
        orderRepository.save(order)
    }

    override fun delete(id: Long) {
        orderRepository.trash(id) ?: throw OrderNotFoundException()
    }
}



@Service
class OrderItemServiceImpl(
    private val orderItemRepository: OrderItemRepository,
    private val entityManager: EntityManager
) : OrderItemService {

    override fun getAll(pageable: Pageable): Page<OrderItemResponse> {
        return orderItemRepository.findAllNotDeletedForPageable(pageable).map {
            OrderItemResponse.toResponse(it)
        }
    }

    override fun getAll(): List<OrderItemResponse> {
        return orderItemRepository.findAllNotDeleted().map {
            OrderItemResponse.toResponse(it)
        }
    }

    override fun getOne(id: Long): OrderItemResponse {
        orderItemRepository.findByIdAndDeletedFalse(id)?.let {
            return OrderItemResponse.toResponse(it)
        } ?: throw OrderItemNotFoundException()
    }

    override fun create(request: OrderItemCreateRequest) {
        request.run {
            val productExists = orderItemRepository.existsByProductId(productId)
            if (!productExists) throw ProductNotFoundException()
            val orderExists = orderItemRepository.existsByOrderId(orderId)
            if (!orderExists) throw OrderNotFoundException()

            val product = entityManager.getReference(
                Product::class.java,productId)
            val order = entityManager.getReference(
                Order::class.java,orderId)
            orderItemRepository.save(this.toEntity(product,order))
        }
    }

    override fun update(id: Long, request: OrderItemUpdateRequest) {
        val orderItem = orderItemRepository.findByIdAndDeletedFalse(id) ?: throw OrderItemNotFoundException()
        request.run {
            quantity.let { orderItem.quantity = it }
            unitPrice.let { orderItem.unitPrice = it }
            totalPrice.let { orderItem.totalPrice = it }
        }
        orderItemRepository.save(orderItem)
    }

    override fun delete(id: Long) {
        orderItemRepository.trash(id) ?: throw OrderItemNotFoundException()
    }
}


