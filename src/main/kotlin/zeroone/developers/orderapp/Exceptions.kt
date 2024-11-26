package zeroone.developers.orderapp

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource

sealed class BillingExceptionHandler() : RuntimeException() {
    abstract fun errorCode(): ErrorCodes
    open fun getArguments(): Array<Any?>? = null

    fun getErrorMessage(resourceBundleMessageSource: ResourceBundleMessageSource): BaseMessage {
        val message = try {
            resourceBundleMessageSource.getMessage(
                errorCode().name, getArguments(), LocaleContextHolder.getLocale()
            )
        } catch (e: Exception) {
            e.message
        }
        return BaseMessage(errorCode().code, message)
    }
}

class UserAlreadyExistsException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.USER_ALREADY_EXISTS
    }
}

class UserNotFoundException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.USER_NOT_FOUND
    }
}

class ProductAlreadyExistsException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.PRODUCT_ALREADY_EXISTS
    }
}

class ProductNotFoundException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.PRODUCT_NOT_FOUND
    }
}


class CategoryAlreadyExistsException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.CATEGORY_ALREADY_EXISTS
    }
}

class CategoryNotFoundException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.CATEGORY_NOT_FOUND
    }
}

class OrderAlreadyExistsException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.ORDER_ALREADY_EXISTS
    }
}

class OrderNotFoundException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.ORDER_NOT_FOUND
    }
}


class OrderItemAlreadyExistsException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.ORDER_ITEM_ALREADY_EXISTS
    }
}

class OrderItemNotFoundException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.ORDER_ITEM_NOT_FOUND
    }
}


class UserAccessDeniedException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.ROLE_ACCESS_DENIED
    }
}

class CannotCancelOrderException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.CANNOT_CANCEL_ORDER
    }
}

class InvalidOrderStatusException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.INVALID_ORDER_STATUS
    }
}

class InvalidPaymentMethodException : BillingExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.PAYMENT_METHOD_NOT_FOUND
    }
}





