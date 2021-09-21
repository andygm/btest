package com.test.btest.app.entity

import com.test.btest.app.values.*
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

data class Phone(
    val id: PhoneId,
    val brand: Brand,
    val device: Device,
    val available: Availability = Availability(true),
    val enricher: Enricher? = null,
    val lastBooked: ExtractedDate? = null,
    val lastBookedPerson: Person? = null
) {
    private val log = KotlinLogging.logger {}

    fun extract(person: String): Phone {
        if (!available.value) throw throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone already extracted")
        return createExtracted(this, person)
            .also { log.info(PhoneMsg.Extracted(id, it.lastBookedPerson!!, it.lastBooked!!).toString()) }
    }

    fun returnPhone(): Phone {
        return createReturned(this)
            .also { log.info(PhoneMsg.Returned(id).toString()) }
    }

    fun enrich(enricher: Enricher): Phone {
        return createEnriched(this, enricher)
            .also { log.info(PhoneMsg.Enriched(id).toString()) }
    }

    fun isEnriched(): Boolean = enricher != null

    sealed interface PhoneMsg {
        data class Extracted(val phone: PhoneId, val person: Person, val date: ExtractedDate) : PhoneMsg
        data class Returned(val phone: PhoneId) : PhoneMsg
        data class Enriched(val phone: PhoneId) : PhoneMsg
    }

    companion object {
        fun create(id: PhoneId,
                   brand: Brand,
                   device: Device,
                   available: Availability,
                   lastBooked: ExtractedDate?,
                   lastBookedPerson: Person?,
                   enricher: Enricher?) =
            Phone(id, brand, device, available, enricher, lastBooked, lastBookedPerson)

        fun createExtracted(basePhone: Phone, person: String) =
            basePhone.copy(
                lastBookedPerson = Person(person),
                lastBooked = ExtractedDate(LocalDateTime.now()),
                available = Availability(false)
            )

        fun createReturned(basePhone: Phone) =
            basePhone.copy(available = Availability(true))

        fun createEnriched(basePhone: Phone, enricher: Enricher) =
            basePhone.copy(enricher = enricher)
    }
}

