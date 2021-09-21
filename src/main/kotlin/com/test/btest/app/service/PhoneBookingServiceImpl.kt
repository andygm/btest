package com.test.btest.app.service

import com.test.btest.app.entity.Phone
import com.test.btest.app.ports.`in`.PhoneBookingService
import com.test.btest.app.ports.out.EnricherQuery
import com.test.btest.app.ports.out.PhoneCmd
import com.test.btest.app.ports.out.PhoneQuery
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PhoneBookingServiceImpl(
    private val phoneQuery: PhoneQuery,
    private val phoneCmd: PhoneCmd,
    private val enricherQuery: EnricherQuery
) : PhoneBookingService {
    private val log = KotlinLogging.logger {}

    override suspend fun extractPhone(phoneId: Int, person: String): Phone {
        return getPhone(phoneId)
            .extract(person)
            .also { phoneCmd.updateData(it) }
    }

    override suspend fun returnPhone(phoneId: Int): Phone {
        return getPhone(phoneId)
            .returnPhone()
            .also { phoneCmd.updateData(it) }
    }

    override suspend fun allPhones(): List<Phone> = phoneQuery.allPhones()

    override suspend fun phone(id: Int): Phone = getPhone(id)

    private suspend fun getPhone(id: Int): Phone {
        val phone = phoneQuery.findPhone(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Phone don't exist")
        return if (phone.isEnriched()) phone
        else enricherQuery.enrich(phone)
                ?.let { phone.enrich(it) }
                ?.let { phoneCmd.updateData(it) }
                ?: run {
                    log.error("Bad update with phone enricher")
                    return phone
                }
    }

}