package io.ztlpay.requests

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.ztlpay.routes.dtos.CoindirectCountry
import io.ztlpay.routes.dtos.CoindirectCountry.Document
import io.ztlpay.routes.dtos.CoindirectCountry.Options
import io.ztlpay.routes.dtos.Country
import io.ztlpay.routes.requests.CoindirectCountryRequest
import io.ztlpay.routes.requests.CoindirectCountryRequest.Companion.SortBy
import io.ztlpay.routes.requests.CoindirectCountryRequest.Companion.SortDirection
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Test class for CoindirectCountryRequest
 */
class CoindirectCountryRequestTest {
    companion object {
        /**
         * Generates test CoindirectCountry objects
         */
        private fun createTestCoinCountries(number: Int = 10) = (1..number).map {
            CoindirectCountry(
                id = it,
                name = "test-$it",
                allowRegistration = false,
                code = "code-$it",
                defaultCurrency = if (it % 3 == 0 && it > 6) "currency-$${number - it}" else null,
                documents = listOf(),
                options = if (it % 3 == 0) Options(withdrawalMaximum = if (it > 5) "${it * 10_000}" else null) else null,
                region = null
            )
        }

        /**
         * Builds a test request
         */
        private fun createRequest() =
            Request.Builder()
                .url("http://test-url")
                .build()

        /**
         * Builds a test response
         */
        private fun createResponse(request: Request, code: Int = 200, bodyString: String? = null) =
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(code)
                .message("Test code: $code")
                .body(bodyString?.toResponseBody("application/json".toMediaType()))
                .build()

        /**
         * Creates a mock OkHttpClient
         */
        fun createMockOkHttpClient(): OkHttpClient {
            val okHttpClient = mockk<OkHttpClient>()
            val call = mockk<Call>()
            every { okHttpClient.newCall(any()) } returns call
            every { call.request() } returns createRequest()
            every { call.execute() } returns createResponse(call.request(), 200, responseJson.trimIndent())
            return okHttpClient
        }
    }

    @Test
    fun `it should sort results correctly`() {
        CoindirectCountryRequest(OkHttpClient(), jacksonObjectMapper()).apply {
            val testCountries = createTestCoinCountries(100).map(::Country).shuffled()

            val sortedByNameAsc = testCountries.sortResults(SortBy.NAME, SortDirection.ASC)
            val sortedByNameDesc = testCountries.sortResults(SortBy.NAME, SortDirection.DESC)
            val sortedByCurrencyAsc = testCountries.sortResults(SortBy.CURRENCY, SortDirection.ASC)
            val sortedByCurrencyDesc = testCountries.sortResults(SortBy.CURRENCY, SortDirection.DESC)

            assertTrue { sortedByNameAsc.first().name < sortedByNameAsc.last().name }
            assertTrue { sortedByNameDesc.first().name > sortedByNameDesc.last().name }
            assertTrue { sortedByCurrencyAsc.first().currency < sortedByCurrencyAsc.last().currency }
            assertTrue { sortedByCurrencyDesc.first().currency > sortedByCurrencyDesc.last().currency }

        }
    }

    @Test
    fun `it should filter incomplete values`() {
        CoindirectCountryRequest(OkHttpClient(), jacksonObjectMapper()).apply {
            val testCountries = createTestCoinCountries().filterIncompleteElements()
            assertEquals(1, testCountries.size)
        }
    }

    @Test
    fun `it should parse response correctly`() {
        CoindirectCountryRequest(OkHttpClient(), jacksonObjectMapper()).apply {
            val (andorra, angola) = createResponse(request, 200, responseJson.trimIndent()).parseResponse()
            assertEquals("Andorra", andorra.name)
            assertEquals("EUR", andorra.defaultCurrency)
            assertEquals("15000", andorra.options?.withdrawalMaximum)
            assertEquals(listOf(394, 395, 396), andorra.documents.map(Document::id))

            assertEquals("Angola", angola.name)
            assertEquals("AOA", angola.defaultCurrency)
            assertEquals("4875000", angola.options?.withdrawalMaximum)
            assertEquals(listOf(151, 152, 153), angola.documents.map(Document::id))
        }
    }

    @Test
    fun `it should throw exception when response has code 400 or above`() {
        CoindirectCountryRequest(OkHttpClient(), jacksonObjectMapper()).apply {
            assertFailsWith<Exception>(
                message = "Received code 400 from external service.",
                block = { createResponse(request, 400).parseResponse() }
            )
            assertFailsWith<Exception>(
                message = "Received code 500 from external service.",
                block = { createResponse(request, 500).parseResponse() }
            )
        }
    }

    @Test
    fun `it should return available countries`() {
        CoindirectCountryRequest(createMockOkHttpClient(), jacksonObjectMapper()).apply {
            val countries = fetchCountries(sortBy = SortBy.NAME, sortDirection = SortDirection.ASC)
            assertEquals(3, countries.size)
        }
    }

    @Test
    fun `it should return available currencies`() {
        CoindirectCountryRequest(createMockOkHttpClient(), jacksonObjectMapper()).apply {
            val currencies = fetchCurrencies(sortDirection = SortDirection.ASC)
            assertEquals(2, currencies.size)
            assertEquals(1, currencies.first().countries.size)
            assertEquals(2, currencies.last().countries.size)
        }
    }

}
