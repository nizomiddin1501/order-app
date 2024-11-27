package zeroone.developers.orderapp

import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import jakarta.persistence.EntityManager
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDateTime

interface UserService {
    fun getAll(page: Int, size: Int): Page<UserResponse>
    fun getAll(): List<UserResponse>
    fun getOne(id: Long): UserResponse
    fun getUserEntity(id: Long): User
    fun create(request: UserCreateRequest)
    fun update(id: Long, request: UserUpdateRequest)
    fun delete(id: Long)
}

interface CategoryService {
    fun getAll(page: Int, size: Int): Page<CategoryResponse>
    fun getAll(): List<CategoryResponse>
    fun getOne(id: Long): CategoryResponse
    fun create(request: CategoryCreateRequest, role: UserRole)
    fun update(id: Long, request: CategoryUpdateRequest, role: UserRole)
    fun delete(id: Long, role: UserRole)
}

interface ProductService {
    fun getAll(page: Int, size: Int): Page<ProductResponse>
    fun getAll(): List<ProductResponse>
    fun getOne(id: Long): ProductResponse
    fun create(request: ProductCreateRequest, role: UserRole)
    fun update(id: Long, request: ProductUpdateRequest, role: UserRole)
    fun delete(id: Long, role: UserRole)
}

interface OrderService {
    fun createOrder(userId: Long, items: List<OrderItemCreateRequest>): OrderResponse
//    fun getUserOrders(userId: Long): List<OrderWithProductResponse>
    fun getUserOrders(userId: Long): List<OrderResponse>
    fun getOrderEntity(orderId: Long): Order
    fun cancelOrder(userId: Long, orderId: Long): Boolean
    fun updateOrderStatus(orderId: Long, status: OrderStatus, user: User): OrderResponse
}

interface PaymentService {
    fun createPayment(orderId: Long, createRequest: PaymentCreateRequest): PaymentResponse
    fun getUserPayments(userId: Long): List<PaymentResponse>
}

interface OrderItemService {
    fun createOrderItem(createRequest: OrderItemCreateRequest, order: Order): OrderItemResponse
    fun getOrderItemsByOrderId(orderId: Long): List<OrderItemResponse>
    fun cancelOrderItem(orderId: Long, productId: Long): Boolean
    fun getUserOrderItems(userId: Long): List<OrderItemResponse>
    fun getUserPayments(userId: Long): List<PaymentResponse>
    fun getUserOrdersByMonth(userId: Long, month: Int, year: Int): OrderStatistics
    fun getUserOrderStatisticsByPeriod(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<ProductOrderStatistics>
    fun getProductOrderCount(productId: Long): Int


}

interface CompleteOrderService{
    fun processOrder(userId: Long, request: FullOrderRequest): FullOrderResponse
}

interface FileDownloadService {

    @Throws(IOException::class, DocumentException::class)
    fun generatePDF(userId: Long, response: HttpServletResponse)

    @Throws(IOException::class)
    fun generateExcel(userId: Long, response: HttpServletResponse)

    @Throws(IOException::class)
    fun generateCSV(userId: Long, response: HttpServletResponse)
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val entityManager: EntityManager,
    private val userMapper: UserMapper
) : UserService {

    override fun getAll(page: Int, size: Int): Page<UserResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val usersPage = userRepository.findAllNotDeletedForPageable(pageable)
        return usersPage.map { userMapper.toDto(it) }
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

    //meta method
    override fun getUserEntity(id: Long): User {
        return userRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException()
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

    override fun getAll(page: Int, size: Int): Page<CategoryResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val categoriesPage = categoryRepository.findAllNotDeletedForPageable(pageable)
        return categoriesPage.map { categoryMapper.toDto(it) }
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

    override fun create(request: CategoryCreateRequest, role: UserRole) {
        checkAdminRole(role)
        val category = categoryRepository.findByNameAndDeletedFalse(request.name)
        if (category != null) throw CategoryAlreadyExistsException()
        categoryRepository.save(categoryMapper.toEntity(request))
    }

    override fun update(id: Long, request: CategoryUpdateRequest, role: UserRole) {
        checkAdminRole(role)
        val category = categoryRepository.findByIdAndDeletedFalse(id) ?: throw CategoryNotFoundException()
        categoryRepository.findByName(id, request.name)?.let { throw CategoryAlreadyExistsException() }
        val updateCategory = categoryMapper.updateEntity(category, request)
        categoryRepository.save(updateCategory)
    }

    override fun delete(id: Long, role: UserRole) {
        checkAdminRole(role)
        categoryRepository.trash(id) ?: throw CategoryNotFoundException()
    }

    fun checkAdminRole(role: UserRole) {
        if (role != UserRole.ADMIN) {
            throw UserAccessDeniedException()
        }
    }
}

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper,
    private val categoryRepository: CategoryRepository,
    private val entityManager: EntityManager
) : ProductService {

    override fun getAll(page: Int, size: Int): Page<ProductResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val productsPage = productRepository.findAllNotDeletedForPageable(pageable)
        return productsPage.map { productMapper.toDto(it) }
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

    override fun create(request: ProductCreateRequest, role: UserRole) {
        checkAdminRole(role)
        val existingProduct = productRepository.findByNameAndDeletedFalse(request.name)
        if (existingProduct != null) throw ProductAlreadyExistsException()
        val existsByCategoryId = categoryRepository.existsByCategoryId(request.categoryId)
        if (!existsByCategoryId) throw CategoryNotFoundException()
        val referenceCategory = entityManager.getReference(
            Category::class.java, request.categoryId
        )
        productRepository.save(productMapper.toEntity(request, referenceCategory))
    }

    override fun update(id: Long, request: ProductUpdateRequest, role: UserRole) {
        checkAdminRole(role)
        val product = productRepository.findByIdAndDeletedFalse(id) ?: throw ProductNotFoundException()
        request.name.let {
            val findByName = productRepository.findByName(id, it)
            if (findByName != null) throw ProductAlreadyExistsException()
            product.name = it
        }
        val updateOrder = productMapper.updateEntity(product, request)
        productRepository.save(updateOrder)
    }

    override fun delete(id: Long, role: UserRole) {
        checkAdminRole(role)
        productRepository.trash(id) ?: throw ProductNotFoundException()
    }

    fun checkAdminRole(role: UserRole) {
        if (role != UserRole.ADMIN) {
            throw UserAccessDeniedException()
        }
    }

}











@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val userService: UserService,
    private val orderItemService: OrderItemService,
    private val orderMapper: OrderMapper,
) : OrderService {

    override fun getOrderEntity(orderId: Long): Order {
        return orderRepository.findByIdAndDeletedFalse(orderId)
            ?: throw OrderNotFoundException()
    }

    //process 1.
    @Transactional
    override fun createOrder(userId: Long, items: List<OrderItemCreateRequest>): OrderResponse {
        val user = userService.getUserEntity(userId)

        val totalPrice = items.sumOf { it.totalPrice }
        val order = Order(user = user, totalPrice = totalPrice, status = OrderStatus.PENDING)
        orderRepository.save(order)
        items.forEach { item ->
            orderItemService.createOrderItem(item, order)
        }
        return orderMapper.toDto(order)
    }

//    override fun getUserOrders(userId: Long): List<OrderWithProductResponse> {
//        return orderRepository.findOrdersWithProductNames(userId)
//    }

    override fun getUserOrders(userId: Long): List<OrderResponse> {
        return orderRepository.findAllByUserId(userId).map { orderMapper.toDto(it) }
    }

    override fun cancelOrder(userId: Long, orderId: Long): Boolean {
        val order = orderRepository.findById(orderId).orElseThrow {
            throw OrderNotFoundException()
        }
        if (order.user.id != userId || order.status != OrderStatus.PENDING) {
            throw CannotCancelOrderException()
        }
        order.status = OrderStatus.CANCELLED
        orderRepository.save(order)
        return true
    }

    //process 4.
    @Transactional
    override fun updateOrderStatus(orderId: Long, status: OrderStatus, user: User): OrderResponse {
        if (user.role != UserRole.ADMIN) {
            throw UserAccessDeniedException()
        }
        val order = orderRepository.findByIdAndDeletedFalse(orderId)
            ?: throw OrderNotFoundException()
        if (status == OrderStatus.CANCELLED) {
            throw CannotCancelOrderException()
        }
        order.status = status
        orderRepository.save(order)
        return orderMapper.toDto(order)
    }
}


@Service
class PaymentServiceImpl(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val paymentMapper: PaymentMapper,
    private val userRepository: UserRepository
) : PaymentService {

    //process 3.
    override fun createPayment(orderId: Long, createRequest: PaymentCreateRequest): PaymentResponse {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException() }
        if (order.status != OrderStatus.PENDING) {
            throw InvalidOrderStatusException()
        }
        val paymentMethod = try {
            PaymentMethod.valueOf(createRequest.paymentMethod.toString())
        } catch (e: IllegalArgumentException) {
            throw InvalidPaymentMethodException()
        }
        val user = order.user ?: throw UserNotFoundException()
        if (user.balance < order.totalPrice) {
            throw InsufficientBalanceException()
        }
        user.balance = user.balance.subtract(order.totalPrice)
        userRepository.save(user)

        val payment = paymentMapper.toEntity(createRequest.copy(paymentMethod = paymentMethod), order)
        val savedPayment = paymentRepository.save(payment)
        order.status = OrderStatus.DELIVERED
        orderRepository.save(order)
        return paymentMapper.toDto(savedPayment)
    }

    override fun getUserPayments(userId: Long): List<PaymentResponse> {
        return paymentRepository.findByOrderUserId(userId)
            .map { paymentMapper.toDto(it) }
    }


}


@Service
class OrderItemServiceImpl(
    private val orderItemRepository: OrderItemRepository,
    private val productRepository: ProductRepository,
    private val orderItemMapper: OrderItemMapper,
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val paymentMapper: PaymentMapper
) : OrderItemService {


    //process 2.
    @Transactional
    override fun createOrderItem(request: OrderItemCreateRequest, order: Order): OrderItemResponse {
        val product = productRepository.findById(request.productId)
            .orElseThrow { ProductNotFoundException() }
        val orderItem = orderItemMapper.toEntity(request, product, order)
        val savedOrderItem = orderItemRepository.save(orderItem)
        return orderItemMapper.toDto(savedOrderItem)
    }


    override fun getOrderItemsByOrderId(orderId: Long): List<OrderItemResponse> {
        return orderItemRepository.findByOrderId(orderId)
            .map { orderItemMapper.toDto(it) }
    }

    override fun cancelOrderItem(orderId: Long, productId: Long): Boolean {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException() }
        if (order.status != OrderStatus.PENDING) {
            throw InvalidOrderStatusException()
        }
        val orderItem = orderItemRepository.findByOrderIdAndProductId(orderId, productId)
            .orElseThrow { OrderItemNotFoundException() }

        orderItemRepository.delete(orderItem)
        return true
    }

    // Statistika
    override fun getUserOrderItems(userId: Long): List<OrderItemResponse> {
        val orders = orderRepository.findAllByUserId(userId)
        val orderItems = orders.flatMap { order ->
            orderItemRepository.findByOrderId(order.id).map { orderItemMapper.toDto(it) }
        }
        return orderItems
    }

    override fun getUserPayments(userId: Long): List<PaymentResponse> {
        val payments = paymentRepository.findByOrderUserId(userId)
        return payments.map { paymentMapper.toDto(it) }
    }

    override fun getUserOrdersByMonth(userId: Long, month: Int, year: Int): OrderStatistics {
        val orders = orderRepository.findByUserIdAndMonthAndYear(userId, month, year)
        val totalOrders = orders.size
        val totalAmount = orders.sumOf { it.totalPrice.toDouble() }
        return OrderStatistics(totalOrders, totalAmount)
    }

    override fun getUserOrderStatisticsByPeriod(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<ProductOrderStatistics> {
        val orders = orderRepository.findAllByUserIdAndDateRange(userId, startDate, endDate)
        val productOrders = orders.flatMap { order ->
            orderItemRepository.findByOrderId(order.id)
        }
        return productOrders.groupBy { it.product }
            .map { (product, items) ->
                val totalQuantity = items.sumOf { it.quantity }
                val totalPrice = items.sumOf { it.totalPrice.toDouble() }
                ProductOrderStatistics(product.name, totalQuantity, totalPrice)
            }
    }

    override fun getProductOrderCount(productId: Long): Int {
        return orderItemRepository.countByProductId(productId)
    }
}



@Service
class CompleteOrderServiceImpl(
    private val userService: UserService,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val orderItemRepository: OrderItemRepository,
    private val paymentRepository: PaymentRepository,
    private val orderMapper: OrderMapper,
    private val orderItemMapper: OrderItemMapper,
    private val paymentMapper: PaymentMapper,
    private val userRepository: UserRepository
) : CompleteOrderService {

    override fun processOrder(userId: Long, request: FullOrderRequest): FullOrderResponse {
        val user = userService.getUserEntity(userId)
        val totalPrice = request.items.sumOf { it.totalPrice }

        if (user.balance < totalPrice) {
            throw InsufficientBalanceException()
        }

        val order = Order(user = user, totalPrice = totalPrice, status = OrderStatus.PENDING)
        orderRepository.save(order)

        request.items.forEach { item ->
            val product = productRepository.findById(item.productId)
                .orElseThrow { ProductNotFoundException() }
            val orderItem = orderItemMapper.toEntity(item, product, order)
            orderItemRepository.save(orderItem)
        }

        val paymentMethod = try {
            PaymentMethod.valueOf(request.payment.paymentMethod.toString())
        } catch (e: IllegalArgumentException) {
            throw InvalidPaymentMethodException()
        }

        if (order.status != OrderStatus.PENDING) {
            throw InvalidOrderStatusException()
        }
        val payment = paymentMapper.toEntity(request.payment.copy(paymentMethod = paymentMethod), order)
        paymentRepository.save(payment)

        user.balance = user.balance.subtract(totalPrice)
        userRepository.save(user)

        order.status = OrderStatus.DELIVERED
        orderRepository.save(order)
        return FullOrderResponse(
            order = orderMapper.toDto(order),
            payment = paymentMapper.toDto(payment)
        )
    }
}



@Service
class FileDownloadServiceImpl(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
) : FileDownloadService {

    @Throws(IOException::class, DocumentException::class)
    override fun generatePDF(userId: Long, response: HttpServletResponse) {
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }
        val orders = orderRepository.findAllByUserId(userId)

        response.contentType = "application/pdf"
        response.setHeader("Content-Disposition", "attachment; filename=orders_${user.id}.pdf")

        val document = Document()
        PdfWriter.getInstance(document, response.outputStream)
        document.open()

        document.add(Paragraph("Foydalanuvchi: ${user.username} ${user.role}"))
        document.add(Paragraph("Buyurtmalar ro'yxati:"))

        val table = PdfPTable(5)
        table.addCell("Buyurtma ID")
        table.addCell("Holati")
        table.addCell("Mahsulot nomi")
        table.addCell("Narx")
        table.addCell("Kategoriya nomi")

        orders.forEach { order ->
            val orderItems = orderItemRepository.findByOrderId(order.id)
            orderItems.forEach { item ->
                table.addCell(order.id.toString())
                table.addCell(order.status.toString())
                table.addCell(item.product.name)
                table.addCell(item.totalPrice.toString())
                table.addCell(item.product.category.name)
            }
        }

        document.add(table)
        document.close()
    }



    @Throws(IOException::class)
    override fun generateExcel(userId: Long, response: HttpServletResponse) {
        response.reset()
        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.setHeader("Content-Disposition", "attachment; filename=orders_${userId}.xlsx")

        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }
        val orders = orderRepository.findAllByUserId(userId)

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Orders")
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Buyurtma ID")
        headerRow.createCell(1).setCellValue("Holati")
        headerRow.createCell(2).setCellValue("Mahsulot nomi")
        headerRow.createCell(3).setCellValue("Narx")
        headerRow.createCell(4).setCellValue("Kategoriya nomi")

        var rowNum = 1
        orders.forEach { order ->
            val orderItems = orderItemRepository.findByOrderId(order.id)
            orderItems.forEach { item ->
                val row = sheet.createRow(rowNum++)
                row.createCell(0).setCellValue(order.id.toString())
                row.createCell(1).setCellValue(order.status.toString())
                row.createCell(2).setCellValue(item.product.name)
                row.createCell(3).setCellValue(item.totalPrice.toDouble())
                row.createCell(4).setCellValue(item.product.category.name)
            }
        }

        workbook.write(response.outputStream)
        workbook.close()
    }



    @Throws(IOException::class)
    override fun generateCSV(userId: Long, response: HttpServletResponse) {
        response.reset()
        response.contentType = "text/csv"
        response.setHeader("Content-Disposition", "attachment; filename=orders_${userId}.csv")

        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }
        val orders = orderRepository.findAllByUserId(userId)

        val writer = response.writer
        writer.append("Buyurtma ID,Holati,Mahsulot nomi,Narx,Kategoriya nomi\n")

        orders.forEach { order ->
            val orderItems = orderItemRepository.findByOrderId(order.id)
            orderItems.forEach { item ->
                writer.append("${order.id},")
                writer.append("${order.status},")
                writer.append("${item.product.name},")
                writer.append("${item.totalPrice},")
                writer.append("${item.product.category.name}\n")
            }
        }

        writer.flush()
        writer.close()
    }


}

