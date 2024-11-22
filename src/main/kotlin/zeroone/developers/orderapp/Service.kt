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
        TODO("Not yet implemented")
    }

    override fun update(id: Long, request: UserUpdateRequest) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun update(id: Long, request: CategoryUpdateRequest) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun update(id: Long, request: ProductUpdateRequest) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun update(id: Long, request: OrderUpdateRequest) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun update(id: Long, request: OrderItemUpdateRequest) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        orderItemRepository.trash(id) ?: throw OrderItemNotFoundException()
    }
}


