package com.test.btest.app.ports.out

import com.test.btest.app.entity.Enricher
import com.test.btest.app.entity.Phone

interface EnricherQuery {
    suspend fun enrich(phone: Phone): Enricher?
}