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
    private val entityManager: EntityManager,
    private val userMapper: UserMapper
) : UserService {

    override fun getAll(pageable: Pageable): Page<UserResponse> {
        return userRepository.findAllNotDeletedForPageable(pageable).map {
            userMapper.toDto(it)
        }
    }

    override fun getAll(): List<UserResponse> {
        return userRepository.findAllNotDeleted().map {
            userMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): UserResponse {
        userRepository.findByIdAndDeletedFalse(id)?.let {
            return userMapper.toDto(it)
        } ?: throw UserNotFoundException()
    }

    override fun create(request: UserCreateRequest) {
        val existingUser = userRepository.findByUsernameAndDeletedFalse(request.username)
        if (existingUser != null) throw UserAlreadyExistsException()
        userRepository.save(userMapper.toEntity(request))
    }

    override fun update(id: Long, request: UserUpdateRequest) {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()
        userRepository.findByUsername(id, request.username)?.let { throw UserAlreadyExistsException() }

        val updateUser = userMapper.updateEntity(user, request)
        userRepository.save(updateUser)
    }

    override fun delete(id: Long) {
        userRepository.trash(id) ?: throw UserNotFoundException()
    }
}


@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val entityManager: EntityManager,
    private val categoryMapper: CategoryMapper
) : CategoryService {

    override fun getAll(pageable: Pageable): Page<CategoryResponse> {
        return categoryRepository.findAllNotDeletedForPageable(pageable).map {
            categoryMapper.toDto(it)
        }
    }

    override fun getAll(): List<CategoryResponse> {
        return categoryRepository.findAllNotDeleted().map {
            categoryMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): CategoryResponse {
        categoryRepository.findByIdAndDeletedFalse(id)?.let {
            return categoryMapper.toDto(it)
        } ?: throw CategoryNotFoundException()
    }

    override fun create(request: CategoryCreateRequest) {
        val category = categoryRepository.findByNameAndDeletedFalse(request.name)
        if (category != null) throw CategoryAlreadyExistsException()
        categoryRepository.save(categoryMapper.toEntity(request))
    }

    override fun update(id: Long, request: CategoryUpdateRequest) {
        val category = categoryRepository.findByIdAndDeletedFalse(id) ?: throw CategoryNotFoundException()
        categoryRepository.findByName(id, request.name)?.let { throw CategoryAlreadyExistsException() }
        val updateCategory = categoryMapper.updateEntity(category, request)
        categoryRepository.save(updateCategory)
    }

    override fun delete(id: Long) {
        categoryRepository.trash(id) ?: throw CategoryNotFoundException()
    }
}


@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper,
    private val entityManager: EntityManager
) : ProductService {

    override fun getAll(pageable: Pageable): Page<ProductResponse> {
        return productRepository.findAllNotDeletedForPageable(pageable).map {
            productMapper.toDto(it)
        }
    }

    override fun getAll(): List<ProductResponse> {
        return productRepository.findAllNotDeleted().map {
            productMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): ProductResponse {
        productRepository.findByIdAndDeletedFalse(id)?.let {
            return productMapper.toDto(it)
        } ?: throw ProductNotFoundException()
    }

    override fun create(request: ProductCreateRequest) {
        val existingProduct = productRepository.findByNameAndDeletedFalse(request.name)
        if (existingProduct != null) throw ProductAlreadyExistsException()
        val referenceCategory = entityManager.getReference(
            Category::class.java, request.categoryId
        )
        productRepository.save(productMapper.toEntity(request, referenceCategory))
    }

    override fun update(id: Long, request: ProductUpdateRequest) {
        val product = productRepository.findByIdAndDeletedFalse(id) ?: throw ProductNotFoundException()
        request.name.let {
            val findByName = productRepository.findByName(id, it)
            if (findByName != null) throw ProductAlreadyExistsException()
            product.name = it
        }
        val updateOrder = productMapper.updateEntity(product, request)
        productRepository.save(updateOrder)
    }

    override fun delete(id: Long) {
        productRepository.trash(id) ?: throw ProductNotFoundException()
    }
}


@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val entityManager: EntityManager,
    private val orderMapper: OrderMapper
) : OrderService {

    override fun getAll(pageable: Pageable): Page<OrderResponse> {
        return orderRepository.findAllNotDeletedForPageable(pageable).map {
            orderMapper.toDto(it)
        }
    }

    override fun getAll(): List<OrderResponse> {
        return orderRepository.findAllNotDeleted().map {
            orderMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): OrderResponse {
        orderRepository.findByIdAndDeletedFalse(id)?.let {
            return orderMapper.toDto(it)
        } ?: throw OrderNotFoundException()
    }

    override fun create(request: OrderCreateRequest) {
        val userExists = orderRepository.existsByUserId(request.userId)
        if (!userExists) throw UserNotFoundException()
        val referenceUser = entityManager.getReference(
            User::class.java, request.userId
        )
        orderRepository.save(orderMapper.toEntity(request, referenceUser))
    }

    override fun update(id: Long, request: OrderUpdateRequest) {
        val order = orderRepository.findByIdAndDeletedFalse(id) ?: throw OrderNotFoundException()

        val updateOrder = orderMapper.updateEntity(order, request)
        orderRepository.save(updateOrder)
    }

    override fun delete(id: Long) {
        orderRepository.trash(id) ?: throw OrderNotFoundException()
    }
}


@Service
class OrderItemServiceImpl(
    private val orderItemRepository: OrderItemRepository,
    private val orderItemMapper: OrderItemMapper,
    private val entityManager: EntityManager
) : OrderItemService {

    override fun getAll(pageable: Pageable): Page<OrderItemResponse> {
        return orderItemRepository.findAllNotDeletedForPageable(pageable).map {
            orderItemMapper.toDto(it)
        }
    }

    override fun getAll(): List<OrderItemResponse> {
        return orderItemRepository.findAllNotDeleted().map {
            orderItemMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): OrderItemResponse {
        orderItemRepository.findByIdAndDeletedFalse(id)?.let {
            return orderItemMapper.toDto(it)
        } ?: throw OrderItemNotFoundException()
    }

    override fun create(request: OrderItemCreateRequest) {
        val productExists = orderItemRepository.existsByProductId(request.productId)
        if (!productExists) throw ProductNotFoundException()
        val orderExists = orderItemRepository.existsByOrderId(request.orderId)
        if (!orderExists) throw OrderNotFoundException()

        val product = entityManager.getReference(
            Product::class.java, request.productId)
        val order = entityManager.getReference(
            Order::class.java, request.orderId)
        orderItemRepository.save(orderItemMapper.toEntity(request, product, order))
    }

    override fun update(id: Long, request: OrderItemUpdateRequest) {
        val orderItem = orderItemRepository.findByIdAndDeletedFalse(id) ?: throw OrderItemNotFoundException()

        val updateOrderItem = orderItemMapper.updateEntity(orderItem, request)
        orderItemRepository.save(updateOrderItem)
    }

    override fun delete(id: Long) {
        orderItemRepository.trash(id) ?: throw OrderItemNotFoundException()
    }
}


