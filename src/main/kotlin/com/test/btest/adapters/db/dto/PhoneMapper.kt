package com.test.btest.adapters.db.dto

import com.test.btest.app.entity.Enricher
import com.test.btest.app.entity.EnricherValues.Bands2g
import com.test.btest.app.entity.EnricherValues.Bands3g
import com.test.btest.app.entity.EnricherValues.Bands4g
import com.test.btest.app.entity.EnricherValues.Technology
import com.test.btest.app.entity.Phone
import com.test.btest.app.values.*
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
abstract class PhoneMapper {
    fun phoneToDto(phone: Phone): PhoneDto =
        PhoneDto(
            id = phone.id.value,
            brand = phone.brand.value,
            device = phone.device.value,
            available = phone.available.value,
            lastBooked = phone.lastBooked?.value,
            lastBookedPerson = phone.lastBookedPerson?.value,
            technology = phone.enricher?.technology?.value,
            bands2g = phone.enricher?.bands2g?.value,
            bands3g = phone.enricher?.bands3g?.value,
            bands4g = phone.enricher?.bands4g?.value
        )

    fun dtoToPhone(dto: PhoneDto): Phone =
        Phone.create(
            id = PhoneId(dto.id),
            brand = Brand(dto.brand),
            device = Device(dto.device),
            available = Availability(dto.available),
            lastBooked = dto.lastBooked?.let { ExtractedDate(it) },
            lastBookedPerson = dto.lastBookedPerson?.let { Person(it) },
            enricher = dtoToEnricher(dto)
        )

    private fun dtoToEnricher(dto: PhoneDto): Enricher? {
        dto.technology ?: return null
        dto.bands2g ?: return null
        dto.bands3g ?: return null
        dto.bands4g ?: return null
        return Enricher.create(
            technology = Technology(dto.technology),
            bands2g = Bands2g(dto.bands2g),
            bands3g = Bands3g(dto.bands3g),
            bands4g = Bands4g(dto.bands4g)
        )
    }
}