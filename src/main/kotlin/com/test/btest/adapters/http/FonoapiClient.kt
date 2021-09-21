package com.test.btest.adapters.http

import com.test.btest.app.entity.Enricher
import com.test.btest.app.entity.EnricherValues.Bands2g
import com.test.btest.app.entity.EnricherValues.Bands3g
import com.test.btest.app.entity.EnricherValues.Bands4g
import com.test.btest.app.entity.EnricherValues.Technology
import com.test.btest.app.entity.Phone
import com.test.btest.app.ports.out.EnricherQuery
import com.test.btest.app.values.Brand
import com.test.btest.app.values.Device
import kotlinx.coroutines.reactor.awaitSingleOrNull
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import java.net.URI

@Component
class FonoapiClient(
    @Value("\${btest.fonoapi.key}") private val apiKey: String,
    @Value("\${btest.fonoapi.url}") private val url: String,
    @Value("\${btest.fonoapi.search}") private val searchPath: String
) : EnricherQuery {
    private val log = KotlinLogging.logger {}

    override suspend fun enrich(phone: Phone): Enricher? {
        return WebClient.create(url)
            .method(HttpMethod.GET)
            .uri { buildURI(it, phone.device, phone.brand) }
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(FonoapiData::class.java)
            .map { fonoapiToEnricher(it) }
            .onErrorResume { err ->
                log.error(err.message)
                Mono.empty()
            }.awaitSingleOrNull()
    }

    private fun buildURI(builder: UriBuilder, device: Device, brand: Brand): URI {
        return builder.path(searchPath)
            .queryParam("token", apiKey)
            .queryParam("device", device.value)
            .queryParam("brand", brand.value)
            .build()
    }

    private fun fonoapiToEnricher(data: FonoapiData): Enricher {
        return Enricher.create(
            technology = Technology(data.technology),
            bands2g = Bands2g(data.bands2g),
            bands3g = Bands3g(data.bands3g),
            bands4g = Bands4g(data.bands4g)
        )
    }

}

