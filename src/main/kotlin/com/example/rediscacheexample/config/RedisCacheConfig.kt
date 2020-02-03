package com.example.rediscacheexample.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory

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