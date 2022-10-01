package io.ztlpay.routes.dtos

import io.ztlpay.routes.dtos.CoindirectCountry.Document
import java.math.BigInteger

data class Country(
    val name: String,
    val currency: String,
    val requiredDocumentation: List<Document>,
    val maxWithdrawal: BigInteger?,
) {
    constructor(country: CoindirectCountry) : this(
        name = country.name,
        currency = country.defaultCurrency ?: "",
        requiredDocumentation = country.documents,
        maxWithdrawal = country.options?.withdrawalMaximum?.toBigInteger(),
    )
}
