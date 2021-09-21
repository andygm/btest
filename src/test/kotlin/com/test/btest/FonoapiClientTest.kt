package com.test.btest

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.test.btest.adapters.http.FonoapiClient
import com.test.btest.app.entity.EnricherValues.Bands2g
import com.test.btest.app.entity.EnricherValues.Bands3g
import com.test.btest.app.entity.EnricherValues.Bands4g
import com.test.btest.app.entity.EnricherValues.Technology
import com.test.btest.app.entity.Phone
import com.test.btest.app.values.Availability
import com.test.btest.app.values.Brand
import com.test.btest.app.values.Device
import com.test.btest.app.values.PhoneId
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FonoapiClientTest {
    private val server: MockWebServer = MockWebServer()
    private lateinit var client: FonoapiClient
    private val token = "key"
    private lateinit var url: String
    private val searchPath = "device"

    @BeforeEach
    fun beforeAll() {
        server.start()
        url = server.url("/").toString()
        client = FonoapiClient(
            apiKey =token,
            url = url,
            searchPath = searchPath
        )
    }

    @AfterEach
    fun afterAll() {
        server.shutdown()
    }

    @Test
    fun `fonoapi client enrich`() {
        val brand = "Nokia"
        val device = "3310"
        val technology = "Latest"
        val bands2g = "1,2,3"
        val bands3g = "4,5,6"
        val bands4g = "7,8,9"

        val response = FonoapiTestData(
            device = device,
            brand = brand,
            technology = technology,
            bands2g = bands2g,
            bands3g = bands3g,
            bands4g = bands4g,
            bluetooth = "5",
            gps = "c"
        )

        val phone = Phone.create(
            id = PhoneId(10),
            brand = Brand(brand),
            device = Device(device),
            available = Availability(true),
            lastBooked = null,
            lastBookedPerson = null,
            enricher = null
        )
        runBlocking {
            createResponse(200, response)
            val enricher = client.enrich(phone)
            assertThat(enricher).isNotNull
            assertThat(phone.isEnriched()).isFalse

            // Enriched phone
            val enrichedPhone = phone.enrich(enricher!!)
            assertThat(enrichedPhone.isEnriched()).isTrue
            assertThat(enrichedPhone.enricher!!.technology).isEqualTo(Technology(technology))
            assertThat(enrichedPhone.enricher!!.bands2g).isEqualTo(Bands2g(bands2g))
            assertThat(enrichedPhone.enricher!!.bands3g).isEqualTo(Bands3g(bands3g))
            assertThat(enrichedPhone.enricher!!.bands4g).isEqualTo(Bands4g(bands4g))

            // Request tests
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.requestUrl?.pathSegments?.get(0)).isEqualTo(searchPath)
            assertThat(request.requestUrl?.queryParameter("token")).isEqualTo(token)
            assertThat(request.requestUrl?.queryParameter("brand")).isEqualTo(brand)
            assertThat(request.requestUrl?.queryParameter("device")).isEqualTo(device)
        }
    }

    @Test
    fun `fonoapi fail connection`() {
        val phone = Phone.create(
            id = PhoneId(10),
            brand = Brand("Nokia"),
            device = Device("device"),
            available = Availability(true),
            lastBooked = null,
            lastBookedPerson = null,
            enricher = null
        )
        createResponse(400, "")
        runBlocking {
            val enricher = client.enrich(phone)
            assertThat(enricher).isNull()
        }
    }

    private fun <B : Any> createResponse(responseCode: Int, body: B) {
        val mapper = ObjectMapper()
        val mockResponse = MockResponse().setResponseCode(responseCode)
            .setBody(mapper.writeValueAsString(body))
            .addHeader("Content-Type", "application/json")
        server.enqueue(mockResponse)
    }

    private data class FonoapiTestData(
        @get:JsonProperty("DeviceName") val device: String,
        @get:JsonProperty("Brand") val brand: String,
        @get:JsonProperty("technology") val technology: String,
        @get:JsonProperty("_2g_bands") val bands2g: String,
        @get:JsonProperty("_3g_bands") val bands3g: String,
        @get:JsonProperty("_4g_bands") val bands4g: String,
        @get:JsonProperty("bluetooth") val bluetooth: String,
        @get:JsonProperty("gps") val gps: String
    )

}