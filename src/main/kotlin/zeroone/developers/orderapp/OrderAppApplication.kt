package zeroone.developers.orderapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl::class)
class OrderAppApplication

fun main(args: Array<String>) {
    runApplication<OrderAppApplication>(*args)
}
