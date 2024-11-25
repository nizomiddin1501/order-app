package zeroone.developers.orderapp


enum class UserRole {
    ADMIN, USER
}

enum class OrderStatus {
    PENDING, DELIVERED, FINISHED, CANCELLED
}

enum class PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    BANK_TRANSFER,
    E_WALLET,
    CASH
}


enum class ErrorCodes(val code:Int) {

    USER_NOT_FOUND(100),
    USER_ALREADY_EXISTS(101),

    CATEGORY_NOT_FOUND(200),
    CATEGORY_ALREADY_EXISTS(201),

    PRODUCT_NOT_FOUND(300),
    PRODUCT_ALREADY_EXISTS(301),

    ORDER_NOT_FOUND(400),
    ORDER_ALREADY_EXISTS(401),

    ORDER_ITEM_NOT_FOUND(500),
    ORDER_ITEM_ALREADY_EXISTS(501)










}


