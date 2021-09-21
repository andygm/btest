package com.test.btest

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.test.btest.adapters.http.ExtractDto
import com.test.btest.adapters.http.ReturnDto
import com.test.btest.app.entity.Enricher
import com.test.btest.app.entity.EnricherValues.Bands2g
import com.test.btest.app.entity.EnricherValues.Bands3g
import com.test.btest.app.entity.EnricherValues.Bands4g
import com.test.btest.app.entity.EnricherValues.Technology
import com.test.btest.app.entity.Phone
import com.test.btest.app.ports.out.EnricherQuery
import com.test.btest.app.values.*
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.BDDMockito
import org.mockito.Mockito.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BtestApplicationTests() {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var mapper: ObjectMapper

    @MockkBean
    lateinit var fonoapiClient: EnricherQuery

    @BeforeEach
    fun beforeEach() {
        coEvery { fonoapiClient.enrich(any()) } returns null
    }

    @Test
    fun `all phones`() {
        val entity = restTemplate.getForEntity<List<Phone>>("/phones")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        val phones = entity.body
        assertThat(phones).isNotNull
        assertThat(phones?.count()).isEqualTo(10)
    }

    @Test
    fun `extract phone 1`() {
        val person = "John Doe"
        val id = 1

        val entity = restTemplate.postForEntity<String>("/phones/extract", ExtractDto(phone = id, person = person))
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        val extractedPhone = mapper.readValue(entity.body, Phone::class.java)

        assertThat(extractedPhone).isNotNull
        assertThat(extractedPhone?.id).isEqualTo(PhoneId(id))
        assertThat(extractedPhone?.lastBookedPerson).isEqualTo(Person(person))
        assertThat(extractedPhone?.lastBooked).isNotNull
        assertThat(extractedPhone?.available).isEqualTo(Availability(false))
    }


    @Test
    fun `extract and return phone 2`() {
        val person = "John Doe"
        val id = 2

        val extract = restTemplate.postForEntity<String>("/phones/extract", ExtractDto(phone = id, person = person))
        assertThat(extract.statusCode).isEqualTo(HttpStatus.OK)
        val returnEntity = restTemplate.postForEntity<String>("/phones/return", ReturnDto(phone = id))
        assertThat(returnEntity.statusCode).isEqualTo(HttpStatus.OK)

        val extractedPhone = mapper.readValue(returnEntity.body, Phone::class.java)

        assertThat(extractedPhone).isNotNull
        assertThat(extractedPhone?.id).isEqualTo(PhoneId(id))
        assertThat(extractedPhone?.lastBookedPerson).isEqualTo(Person(person))
        assertThat(extractedPhone?.lastBooked).isNotNull
        assertThat(extractedPhone?.available).isEqualTo(Availability(true))
    }

    @Test
    fun `error when extract not available phone`() {
        val person = "John Doe"
        val id = 3

        val extract1 = restTemplate.postForEntity<String>("/phones/extract", ExtractDto(phone = id, person = person))
        assertThat(extract1.statusCode).isEqualTo(HttpStatus.OK)
        val extract2 = restTemplate.postForEntity<String>("/phones/extract", ExtractDto(phone = id, person = person))
        assertThat(extract2.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `get phone`() {
        runBlocking {
            val phoneId = 1
            val enricher = Enricher.create(
                technology = Technology("t"),
                bands2g = Bands2g("2"),
                bands3g = Bands3g("3"),
                bands4g = Bands4g("4")
            )
            coEvery { fonoapiClient.enrich(any()) } returns enricher

            val entity = restTemplate.getForEntity<String>("/phones/$phoneId")
            assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

            // Enriched test
            val phone = mapper.readValue(entity.body, Phone::class.java)
            assertThat(phone).isNotNull
            assertThat(phone?.isEnriched()).isTrue
            assertThat(phone?.enricher?.technology).isEqualTo(enricher.technology)
            assertThat(phone?.enricher?.bands2g).isEqualTo(enricher.bands2g)
            assertThat(phone?.enricher?.bands3g).isEqualTo(enricher.bands3g)
            assertThat(phone?.enricher?.bands4g).isEqualTo(enricher.bands4g)

        }
    }

}

