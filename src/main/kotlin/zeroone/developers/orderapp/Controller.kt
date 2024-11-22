package zeroone.developers.orderapp

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.data.domain.Pageable
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
    @GetMapping
    fun getAll(pageable: Pageable) = service.getAll(pageable)


    @Operation(summary = "Get user by ID", description = "Fetches a single user based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched the user"),
        ApiResponse(responseCode = "404", description = "User not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new user", description = "Creates a new user record.")
    @ApiResponses(ApiResponse(responseCode = "201", description = "User successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: UserCreateRequest) = service.create(request)


    @Operation(summary = "Update existing user", description = "Updates an existing user based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "User successfully updated"),
        ApiResponse(responseCode = "404", description = "User not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: UserUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete user by ID", description = "Deletes a user based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "204", description = "User successfully deleted"),
        ApiResponse(responseCode = "404", description = "User not found"))
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


    @Operation(summary = "Get all categories with pagination", description = "Fetches categories with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated categories"))
    @GetMapping
    fun getAll(pageable: Pageable) = service.getAll(pageable)


    @Operation(summary = "Get category by ID", description = "Fetches a single category based on the provided category ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched the category"),
        ApiResponse(responseCode = "404", description = "Category not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new category", description = "Creates a new category record.")
    @ApiResponses(ApiResponse(responseCode = "201", description = "Category successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: CategoryCreateRequest) = service.create(request)



    @Operation(summary = "Update existing category", description = "Updates an existing category based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Category successfully updated"),
        ApiResponse(responseCode = "404", description = "Category not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: CategoryUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete category by ID", description = "Deletes a category based on the provided category ID.")
    @ApiResponses(ApiResponse(responseCode = "204", description = "Category successfully deleted"),
        ApiResponse(responseCode = "404", description = "Category not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
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
    @GetMapping
    fun getAll(pageable: Pageable) = service.getAll(pageable)


    @Operation(summary = "Get product by ID", description = "Fetches a single product based on the provided product ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched the product"),
        ApiResponse(responseCode = "404", description = "Product not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new product", description = "Creates a new product record.")
    @ApiResponses(ApiResponse(responseCode = "201", description = "Product successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: ProductCreateRequest) = service.create(request)



    @Operation(summary = "Update existing product", description = "Updates an existing product based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Product successfully updated"),
        ApiResponse(responseCode = "404", description = "Product not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: ProductUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete product by ID", description = "Deletes a product based on the provided product ID.")
    @ApiResponses(ApiResponse(responseCode = "204", description = "Product successfully deleted"),
        ApiResponse(responseCode = "404", description = "Product not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
class OrderController(val service: OrderService) {


    @Operation(summary = "Get all orders", description = "Fetches all orders from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all orders"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all orders with pagination", description = "Fetches orders with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated orders"))
    @GetMapping
    fun getAll(pageable: Pageable) = service.getAll(pageable)


    @Operation(summary = "Get order by ID", description = "Fetches a single order based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched the order"),
        ApiResponse(responseCode = "404", description = "Order not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new order", description = "Creates a new order record.")
    @ApiResponses(ApiResponse(responseCode = "201", description = "Order successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: OrderCreateRequest) = service.create(request)



    @Operation(summary = "Update existing order", description = "Updates an existing order based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Order successfully updated"),
        ApiResponse(responseCode = "404", description = "Order not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: OrderUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete order by ID", description = "Deletes a order based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "204", description = "Order successfully deleted"),
        ApiResponse(responseCode = "404", description = "Order not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-items")
class OrderItemController(val service: OrderItemService) {


    @Operation(summary = "Get all orderItems", description = "Fetches all orderItems from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all orderItems"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all orderItems with pagination", description = "Fetches orderItems with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated orderItems"))
    @GetMapping
    fun getAll(pageable: Pageable) = service.getAll(pageable)


    @Operation(summary = "Get orderItem by ID", description = "Fetches a single orderItem based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched the orderItem"),
        ApiResponse(responseCode = "404", description = "OrderItem not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new orderItem", description = "Creates a new orderItem record.")
    @ApiResponses(ApiResponse(responseCode = "201", description = "OrderItem successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: OrderItemCreateRequest) = service.create(request)



    @Operation(summary = "Update existing orderItem", description = "Updates an existing orderItem based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "OrderItem successfully updated"),
        ApiResponse(responseCode = "404", description = "OrderItem not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: OrderItemUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete orderItem by ID", description = "Deletes a orderItem based on the provided ID.")
    @ApiResponses(ApiResponse(responseCode = "204", description = "OrderItem successfully deleted"),
        ApiResponse(responseCode = "404", description = "OrderItem not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}



