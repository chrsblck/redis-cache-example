package com.example.rediscacheexample.controller

import com.example.rediscacheexample.service.ContactService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contact")
class ContactController(private val contactService: ContactService) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long) = contactService.get(id)
}