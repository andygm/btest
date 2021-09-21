package com.test.btest.adapters.http

import com.test.btest.app.entity.Phone
import com.test.btest.app.ports.`in`.PhoneBookingService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/phones")
class PhoneController(
    val phoneBookingService: PhoneBookingService
) {
    @PostMapping("/extract")
    suspend fun bookPhone(@RequestBody dto: ExtractDto): ResponseEntity<Phone> {
        return phoneBookingService.extractPhone(dto.phone, dto.person)
            .let { ResponseEntity.ok(it) }
    }

    @PostMapping("/return")
    suspend fun returnPhone(@RequestBody dto: ReturnDto): ResponseEntity<Phone> {
        return phoneBookingService.returnPhone(dto.phone)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping()
    suspend fun allPhones(): ResponseEntity<List<Phone>> {
        return phoneBookingService.allPhones()
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{phone}")
    suspend fun phone(@PathVariable phone: Int): ResponseEntity<Phone> {
        return phoneBookingService.phone(phone)
            .let { ResponseEntity.ok(it) }
    }

}