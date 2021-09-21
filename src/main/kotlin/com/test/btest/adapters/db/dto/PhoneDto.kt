package com.test.btest.adapters.db.dto

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("phones")
data class PhoneDto(
    @Id val id: Int,
    val brand: String,
    val device: String,
    val technology: String?,
    val bands2g: String?,
    val bands3g: String?,
    val bands4g: String?,
    val available: Boolean,
    val lastBooked: LocalDateTime?,
    val lastBookedPerson: String?
)