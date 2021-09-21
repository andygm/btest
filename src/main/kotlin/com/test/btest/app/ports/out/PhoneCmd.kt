package com.test.btest.app.ports.out

import com.test.btest.app.entity.Phone

interface PhoneCmd {
    suspend fun enrich(phone: Phone): Phone?
    suspend fun extract(phone: Phone): Phone?
    suspend fun returnPhone(phone: Phone): Phone?
    suspend fun updateData(phone: Phone): Phone?
}