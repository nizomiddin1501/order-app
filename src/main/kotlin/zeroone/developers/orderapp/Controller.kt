package zeroone.developers.orderapp

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionHandler(private val errorMessageSource: ResourceBundleMessageSource) {

    @ExceptionHandler(BillingExceptionHandler::class)
    fun handleAccountException(exception: BillingExceptionHandler): ResponseEntity<BaseMessage> {
        return ResponseEntity.badRequest().body(exception.getErrorMessage(errorMessageSource))
    }
}

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
class UserController(val service: UserService) {


    @Operation(summary = "Get all users", description = "Fetches all users from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all users"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all users with pagination", description = "Fetches users with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated users"))
    @GetMapping("/page")
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ) =
        service.getAll(page, size)


    @Operation(summary = "Get user by ID", description = "Fetches a single user based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully fetched the user"),
        ApiResponse(responseCode = "404", description = "User not found")
    )
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new user", description = "Creates a new user record.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "User successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data")
    )
    @PostMapping
    fun create(@RequestBody @Valid request: UserCreateRequest) = service.create(request)


    @Operation(summary = "Update existing user", description = "Updates an existing user based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "User successfully updated"),
        ApiResponse(responseCode = "404", description = "User not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data")
    )
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: UserUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete user by ID", description = "Deletes a user based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "User successfully deleted"),
        ApiResponse(responseCode = "404", description = "User not found")
    )
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
class CategoryController(val service: CategoryService) {

    @Operation(summary = "Get all categories", description = "Fetches all categories from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all categories"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(
        summary = "Get all categories with pagination",
        description = "Fetches categories with pagination support."
    )
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated categories"))
    @GetMapping("/page")
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ) =
        service.getAll(page, size)


    @Operation(
        summary = "Get category by ID",
        description = "Fetches a single category based on the provided category ID."
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully fetched the category"),
        ApiResponse(responseCode = "404", description = "Category not found")
    )
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new category", description = "Creates a new category record.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Category successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data")
    )
    @PostMapping
    fun create(@RequestBody @Valid request: CategoryCreateRequest,
               @RequestParam role: UserRole) = service.create(request,role)


    @Operation(
        summary = "Update existing category",
        description = "Updates an existing category based on the provided ID."
    )
    @ApiResponses(ApiResponse(responseCode = "200", description = "Category successfully updated"),
             ApiResponse(responseCode = "404", description = "Category not found"),
             ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: CategoryUpdateRequest,
               @RequestParam role: UserRole) = service.update(id, request, role)


    @Operation(summary = "Delete category by ID", description = "Deletes a category based on the provided category ID.")
    @ApiResponses(ApiResponse(responseCode = "204", description = "Category successfully deleted"),
            ApiResponse(responseCode = "404", description = "Category not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long,
               @RequestParam role: UserRole) = service.delete(id, role)
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
class ProductController(val service: ProductService) {


    @Operation(summary = "Get all products", description = "Fetches all products from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all products"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all products with pagination", description = "Fetches products with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated products"))
    @GetMapping("/page")
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ) =
        service.getAll(page, size)


    @Operation(
        summary = "Get product by ID",
        description = "Fetches a single product based on the provided product ID."
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully fetched the product"),
        ApiResponse(responseCode = "404", description = "Product not found")
    )
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new product", description = "Creates a new product record.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Product successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data")
    )
    @PostMapping
    fun create(@RequestBody @Valid request: ProductCreateRequest,
               @RequestParam role: UserRole) = service.create(request,role)


    @Operation(
        summary = "Update existing product",
        description = "Updates an existing product based on the provided ID."
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Product successfully updated"),
        ApiResponse(responseCode = "404", description = "Product not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data")
    )
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: ProductUpdateRequest,
               @RequestParam role: UserRole) = service.update(id, request,role)


    @Operation(summary = "Delete product by ID", description = "Deletes a product based on the provided product ID.")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Product successfully deleted"),
        ApiResponse(responseCode = "404", description = "Product not found")
    )
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long,
               @RequestParam role: UserRole) = service.delete(id,role)
}



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
class OrderController(private val service: OrderService,
                      private val userService: UserService) {

    @Operation(summary = "Create a new order", description = "Allows a user to create an order with items.")
    @ApiResponses(ApiResponse(responseCode = "201", description = "Order created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun createOrder(
        @RequestBody @Valid request: OrderCreateRequest,
        @RequestParam userId: Long
    ): OrderResponse {
        return service.createOrder(userId, request.items)
    }


    @Operation(summary = "Get user orders", description = "Fetches all orders for a specific user.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Orders fetched successfully"))
    @GetMapping("/{userId}")
    fun getUserOrders(@PathVariable userId: Long): List<OrderResponse> =
        service.getUserOrders(userId)


    @Operation(summary = "Cancel an order", description = "Allows a user to cancel a pending order.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Order canceled successfully"),
             ApiResponse(responseCode = "400", description = "Order cannot be canceled"))
    @PutMapping("/{userId}/cancel/{orderId}")
    fun cancelOrder(@PathVariable userId: Long, @PathVariable orderId: Long): Boolean =
        service.cancelOrder(userId, orderId)


    @Operation(summary = "Update order status", description = "Allows an admin to update the status of an order.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            ApiResponse(responseCode = "403", description = "Permission denied"))
    @PutMapping("/{orderId}/status")
    fun updateOrderStatus(
        @PathVariable orderId: Long,
        @RequestParam status: OrderStatus,
        @RequestParam userId: Long
    ): OrderResponse {
        val user = userService.getUserEntity(userId)
        return service.updateOrderStatus(orderId, status, user)
    }
}




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
class PaymentController(private val service: PaymentService) {

    @Operation(summary = "Create a payment", description = "Processes a payment for a pending order.")
    @ApiResponses(ApiResponse(responseCode = "201", description = "Payment created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid order or status"))
    @PostMapping("/{orderId}")
    fun createPayment(
        @PathVariable orderId: Long,
        @RequestBody @Valid request: PaymentCreateRequest):
            PaymentResponse = service.createPayment(orderId, request)


    @Operation(summary = "Get user payments", description = "Fetches all payments made by a specific user.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Payments fetched successfully"))
    @GetMapping("/{userId}")
    fun getUserPayments(@PathVariable userId: Long): List<PaymentResponse> =
        service.getUserPayments(userId)
}




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-items")
class OrderItemController(private val service: OrderItemService,
                          private val orderService: OrderService) {

    @Operation(summary = "Create a new order item", description = "Allows adding items to an existing order.")
    @ApiResponses(ApiResponse(responseCode = "201", description = "Order item created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid product or order"))
    @PostMapping("/{orderId}")
    fun createOrderItem(
        @PathVariable orderId: Long,
        @RequestBody @Valid request: OrderItemCreateRequest):
            OrderItemResponse {
        val order = orderService.getOrderEntity(orderId)
        return service.createOrderItem(request, order)
    }


    @Operation(summary = "Get order items", description = "Fetches all items in a specific order.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Order items fetched successfully"))
    @GetMapping("/{orderId}")
    fun getOrderItems(@PathVariable orderId: Long): List<OrderItemResponse> =
        service.getOrderItemsByOrderId(orderId)


    @Operation(summary = "Cancel an order item", description = "Removes an item from an existing order.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Order item canceled successfully"),
            ApiResponse(responseCode = "400", description = "Order cannot be modified"))
    @DeleteMapping("/{orderId}/{productId}")
    fun cancelOrderItem(
        @PathVariable orderId: Long,
        @PathVariable productId: Long):
            Boolean = service.cancelOrderItem(orderId, productId)
}






@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order/files")
class FileDownloadController(val service: FileDownloadService) {


    @GetMapping("/pdf/{id}")
    fun downloadPDF(
        @PathVariable id: Long,
        response: HttpServletResponse) =
        service.generatePDF(id, response)


    @GetMapping("/excel/{id}")
    fun downloadExcel(
        @PathVariable id: Long,
        response: HttpServletResponse) =
        service.generatePDF(id, response)


    @GetMapping("/csv/{id}")
    fun downloadCSV(
        @PathVariable id: Long,
        response: HttpServletResponse) =
        service.generatePDF(id, response)

}




