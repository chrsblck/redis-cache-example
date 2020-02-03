package com.example.rediscacheexample

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import javax.annotation.PostConstruct
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@SpringBootApplication
@EnableCaching
class RedisCacheExampleApplication

fun main(args: Array<String>) {
    runApplication<RedisCacheExampleApplication>(*args)
}

@Configuration
class RedisCacheConfig {

    @Autowired
    private lateinit var redisConnectionFactory: RedisConnectionFactory

    @Primary
    @Bean
    fun contactCacheManager(): CacheManager {
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).initialCacheNames(
                setOf("contactCache")).build()
    }
}

@RestController
@RequestMapping("/contact")
class Endpoint(private val userService: UserService) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long) = userService.get(id)
}

@Service
@CacheConfig(cacheNames = ["contactCache"], cacheManager = "contactCacheManager")
class UserService(val cacheManager: CacheManager, val contactRepository: ContactRepository) {

    @Cacheable(key = "#id")
    fun get(id: Long) = contactRepository.findById(id)

    @PostConstruct
    fun clearCacheOnExit() {
        cacheManager.cacheNames.forEach {
            println("clearing cache: $it")
            cacheManager.getCache(it)?.clear()
        }
    }
}

@Entity
@Table
data class Contact(
        @Id
        val id: Long? = null,
        val name: String? = null,
        val email: String? = null
) : Serializable

@Repository
interface ContactRepository : JpaRepository<Contact, Long>
