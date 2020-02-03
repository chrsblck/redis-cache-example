package com.example.rediscacheexample.domain

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
data class Contact(
        @Id
        val id: Long? = null,
        val name: String? = null,
        val email: String? = null
) : Serializable