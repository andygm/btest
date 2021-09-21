package com.test.btest.app.ports.out

import com.test.btest.app.entity.Phone

interface PhoneQuery {
    suspend fun allPhones(): List<Phone>
    suspend fun findPhone(id: Int): Phone?
}