package com.test.btest.adapters.db

import com.test.btest.adapters.db.dto.PhoneMapper
import com.test.btest.app.entity.Phone
import com.test.btest.app.ports.out.PhoneCmd
import com.test.btest.app.ports.out.PhoneQuery
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Repository

@Repository
class PhoneBookingRepositoryImpl(
    private val phoneRepository: PhoneRepository,
    private val mapper: PhoneMapper
) : PhoneQuery, PhoneCmd {

    override suspend fun allPhones(): List<Phone> {
        return phoneRepository.findAll()
            .map { mapper.dtoToPhone(it) }
            .collectList()
            .awaitSingle()
    }

    override suspend fun findPhone(id: Int): Phone? {
        return phoneRepository.findById(id)
            .map { mapper.dtoToPhone(it) }
            .awaitSingleOrNull()
    }

    override suspend fun updateData(phone: Phone): Phone? {
        return phone.let { mapper.phoneToDto(it) }
            .let { phoneRepository.save(it) }
            .map { mapper.dtoToPhone(it) }
            .awaitSingleOrNull()
    }

    override suspend fun enrich(phone: Phone): Phone? {
        return phoneRepository.save(mapper.phoneToDto(phone))
            .map { mapper.dtoToPhone(it) }
            .awaitSingleOrNull()
    }

    override suspend fun extract(phone: Phone): Phone? {
        return phoneRepository.save(mapper.phoneToDto(phone))
            .map { mapper.dtoToPhone(it) }
            .awaitSingleOrNull()
    }

    override suspend fun returnPhone(phone: Phone): Phone? {
        return phoneRepository.save(mapper.phoneToDto(phone))
            .map { mapper.dtoToPhone(it) }
            .awaitSingleOrNull()
    }

}