package com.example.rediscacheexample.service

import com.example.rediscacheexample.repository.ContactRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
@CacheConfig(cacheNames = ["contactCache"], cacheManager = "contactCacheManager")
class ContactService(val cacheManager: CacheManager, val contactRepository: ContactRepository) {

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