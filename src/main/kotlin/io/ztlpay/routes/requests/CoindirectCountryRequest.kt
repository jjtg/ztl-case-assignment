package io.ztlpay.routes.requests

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ztlpay.routes.dtos.CoindirectCountry
import io.ztlpay.routes.dtos.Country
import io.ztlpay.routes.dtos.Currency
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class CoindirectCountryRequest(
    private val client: OkHttpClient,
    private val mapper: ObjectMapper,
    max: Int = 250
) {

    val request: Request = Request.Builder()
        .url("$url?max=$max")
        .addHeader("Content-Type", "application/json")
        .build()

    companion object {
        // TODO - url should be an environment variable.
        const val url: String = "https://api.coindirect.com/api/country"
        enum class SortBy { NAME, CURRENCY; }
        enum class SortDirection { ASC, DESC; }
    }

    /**
     * Extends OkHttp Response object in order to parse a response.
     * @return A list containing raw Coindirect data
     * @throws Exception if the request results in code 400 or higher
     */
    fun Response.parseResponse(): List<CoindirectCountry> = when {
        code < 400 -> body?.string()?.let { mapper.readValue(it) } ?: listOf()
        else -> throw Exception("Received code $code from external service.")
    }

    /**
     * Filters CoindirectCountry elements that are missing maximum withdrawal values or default currency values.
     * @return A list containing complete CoindirectCountry elements.
     */
    fun List<CoindirectCountry>.filterIncompleteElements() = filter {
        it.options != null && !it.options.withdrawalMaximum.isNullOrBlank() && !it.defaultCurrency.isNullOrBlank()
    }

    /**
     * Sorts a country list based on specified parameters.
     * @return A sorted list of countries.
     */
    fun List<Country>.sortResults(sortBy: SortBy, sortDirection: SortDirection) =
        when (sortDirection) {
            SortDirection.ASC -> {
                when (sortBy) {
                    SortBy.NAME -> sortedBy { it.name }
                    SortBy.CURRENCY -> sortedBy { it.currency }
                }
            }
            SortDirection.DESC -> {
                when (sortBy) {
                    SortBy.NAME -> sortedByDescending { it.name }
                    SortBy.CURRENCY -> sortedByDescending { it.currency }
                }
            }

        }

    /**
     * Executes Coindirect country request and processes data received.
     * @return A sorted list of Country objects based on remote data from Coindirect or an empty list.
     * @throws Exception if the request results in code 400 or higher
     */
    private fun execute(
        sortBy: SortBy,
        sortDirection: SortDirection,
    ) = client.newCall(request).execute()
        .parseResponse()
        .filterIncompleteElements()
        .map(::Country)
        .sortResults(sortBy, sortDirection)

    /**
     * Fetches country data.
     * @return A sorted list of Country objects.
     * @throws Exception if the request results in code 400 or higher.
     */
    fun fetchCountries(
        sortBy: SortBy,
        sortDirection: SortDirection,
    ) = execute(sortBy, sortDirection)

    /**
     * Fetches country data and aggregates it by currency.
     * @return A sorted list of Currency objects.
     * @throws Exception if the request results in code 400 or higher.
     */
    fun fetchCurrencies(
        sortDirection: SortDirection = SortDirection.ASC
    ) = execute(
        sortBy = SortBy.CURRENCY,
        sortDirection = sortDirection
    ).groupBy { it.currency }
        .map { (name, countries) -> Currency(name, countries.map(Country::name)) }

}
