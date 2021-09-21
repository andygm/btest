package com.test.btest.app.ports.`in`

import com.test.btest.app.entity.Phone

interface PhoneBookingService {
    suspend fun extractPhone(phoneId: Int, person: String): Phone
    suspend fun returnPhone(phoneId: Int): Phone
    suspend fun allPhones(): List<Phone>
    suspend fun phone(id: Int): Phone
}