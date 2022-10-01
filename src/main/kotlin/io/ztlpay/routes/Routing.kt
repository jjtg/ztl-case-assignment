package io.ztlpay.routes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ztlpay.routes.requests.CoindirectCountryRequest
import io.ztlpay.routes.requests.CoindirectCountryRequest.Companion.SortBy
import io.ztlpay.routes.requests.CoindirectCountryRequest.Companion.SortDirection
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory

/**
 * Attempts to retrieve a valid enum value.
 * source: https://stackoverflow.com/questions/41844080/kotlin-how-to-check-if-enum-contains-a-given-string-without-messing-with-except
 *
 * @return an enum entry with the specified name or `null` if no such entry was found.
 */
inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String?): T? {
    return enumValues<T>().find { it.name == name?.uppercase() }
}

// @todo: these values can either be enforced by throwing 400 or defaulted as below.
/**
 * @return sort by enum if it exists else default value `SortBy.NAME`
 */
fun ApplicationCall.sortBy(default: SortBy = SortBy.NAME) =
    enumValueOfOrNull(request.queryParameters["sortBy"]) ?: default

/**
 * @return sort direction enum if it exists else default value `SortDirection.ASC`
 */
fun ApplicationCall.sortDirection(default: SortDirection = SortDirection.ASC) =
    enumValueOfOrNull(request.queryParameters["sortDirection"]) ?: default

/**
 * @return max value for request from query parameters or default value
 */
fun ApplicationCall.max(default: Int = 250) = try {
    request.queryParameters["max"]?.toInt()  ?: default
} catch (e: Exception) {
    default
}

fun Application.configureRouting(
    client: OkHttpClient = OkHttpClient(),
    mapper: ObjectMapper = jacksonObjectMapper(),
) {
    val log = LoggerFactory.getLogger(this::class.java)

    routing {
        get("/countries") {
            try {
                CoindirectCountryRequest(client, mapper, call.max())
                    .fetchCountries(call.sortBy(), call.sortDirection())
                    .let {
                       call.response.header("Content-Type", "application/json")
                        call.respond(mapper.writeValueAsString(it))
                    }
            } catch (e: Exception) {
                log.error("Failed to fetch country data: ${e.message}", e)
                call.respond(HttpStatusCode.InternalServerError, "Failed to fetch country data from external service.")
            }
        }
        get("/currencies") {
            try {
                CoindirectCountryRequest(client, mapper)
                    .fetchCurrencies(call.sortDirection())
                    .let {
                        call.response.header("Content-Type", "application/json")
                        call.respond(mapper.writeValueAsString(it.take(call.max())))
                    }
            } catch (e: Exception) {
                log.error("Failed to fetch currency data: ${e.message}", e)
                call.respond(HttpStatusCode.InternalServerError, "Failed to fetch currency data from external service.")
            }
        }
    }
}
