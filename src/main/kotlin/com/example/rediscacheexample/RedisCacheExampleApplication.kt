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
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct

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
    fun endpointCacheManager(): CacheManager {
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).initialCacheNames(
                setOf("endpointCache")).build()
    }
}

@RestController
class Endpoint(private val service: EndpointService) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = service.get(id)
}

@Service
@CacheConfig(cacheNames = ["endpointCache"], cacheManager = "endpointCacheManager")
class EndpointService(val endpointCache: CacheManager) {

    @Cacheable(key = "#id")
    fun get(id: String): String {
        println("in EndpointService.get()")
        return "foobar"
    }

    @PostConstruct
    fun clearCacheOnExit() {
        endpointCache.cacheNames.forEach {
            println("clearing cache: $it")
            endpointCache.getCache(it)?.clear()
        }
    }
}
