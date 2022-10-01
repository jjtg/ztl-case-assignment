package io.ztlpay.routes.dtos

data class CoindirectCountry(
    val id: Int,
    val name: String,
    val allowRegistration: Boolean? = false,
    val code: String,
    val defaultCurrency: String?,
    val documents: List<Document>,
    val options: Options?,
    val region: Region?
) {
    data class Document(
        val id: Int,
        val code: String,
        val description: String,
        val required: Boolean
    )

    data class Options(
        val minimumOrderValue: Int? = null,
        val limitValue: Int? = null,
        val minimumTransferValue: Int? = null,
        val minimumPartnerValue: Int? = null,
        val requireProofOfPayment: String? = null,
        val supportInstantTransfer: String? = null,
        val orderMinimumValue: String? = null,
        val unverifiedMaximum: String? = null,
        val withdrawalMaximum: String? = null,
        val withdrawalThreshold: String? = null,
        val transferMinimumValue: String? = null,
        val defaultMinimumAvailable: String? = null,
        val riskLevel: String? = null
    )

    data class Region(
        val id: Int,
        val code: String,
        val name: String
    )

}
