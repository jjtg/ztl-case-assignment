package io.ztlpay

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ztlpay.requests.CoindirectCountryRequestTest.Companion.createMockOkHttpClient
import io.ztlpay.routes.configureRouting
import io.ztlpay.routes.dtos.Country
import io.ztlpay.routes.dtos.Currency
import org.junit.Test
import kotlin.test.assertEquals

class ApplicationTest {

    private fun ApplicationTestBuilder.configure() {
        application { configureRouting(
            client = createMockOkHttpClient()
        ) }
    }

    @Test
    fun `should fetch all countries`() = testApplication {
        configure()
        val response = client.get("/countries?sortBy=currency")
        val countries: List<Country> = jacksonObjectMapper().readValue(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(3, countries.size)
        assertEquals(countries.map(Country::name), listOf("Angola", "Andorra", "Austria"))
    }

    @Test
    fun `should fetch all currencies`() = testApplication {
        configure()
        val response = client.get("/currencies?sortDirection=desc")
        val currencies: List<Currency> = jacksonObjectMapper().readValue(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(2, currencies.size)
        assertEquals(currencies.map(Currency::name), listOf("EUR", "AOA"))
    }

}
