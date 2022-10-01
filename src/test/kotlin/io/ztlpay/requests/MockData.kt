package io.ztlpay.requests

internal const val responseJson: String = """
[{
    "allowRegistration": true,
    "code": "AD",
    "defaultCurrency": "EUR",
    "documents": [
        {
            "code": "idPassport",
            "description": "ID or Passport",
            "id": 394,
            "required": true
        },
        {
            "code": "idSelfie",
            "description": "ID Selfie",
            "id": 395,
            "required": true
        },
        {
            "code": "proofOfAddress",
            "description": "Proof of Address",
            "id": 396,
            "required": true
        }
    ],
    "id": 1,
    "name": "Andorra",
    "options": {
        "minimumOrderValue": 35,
        "limitValue": 1000,
        "minimumTransferValue": 20,
        "minimumPartnerValue": 0,
        "requireProofOfPayment": "TRUE",
        "supportInstantTransfer": null,
        "orderMinimumValue": "35",
        "unverifiedMaximum": "1000",
        "withdrawalMaximum": "15000",
        "withdrawalThreshold": "1500",
        "transferMinimumValue": "20",
        "defaultMinimumAvailable": "35",
        "riskLevel": "low"
    },
    "region": null
},
{
    "allowRegistration": true,
    "code": "AO",
    "defaultCurrency": "AOA",
    "documents": [
        {
            "code": "idPassport",
            "description": "ID or Passport",
            "id": 151,
            "required": true
        },
        {
            "code": "idSelfie",
            "description": "ID Selfie",
            "id": 152,
            "required": true
        },
        {
            "code": "proofOfAddress",
            "description": "Proof of Address",
            "id": 153,
            "required": true
        }
    ],
    "id": 9,
    "name": "Angola",
    "options": {
        "minimumOrderValue": 11375,
        "limitValue": 325000,
        "minimumTransferValue": 6500,
        "minimumPartnerValue": 0,
        "requireProofOfPayment": "TRUE",
        "supportInstantTransfer": null,
        "orderMinimumValue": "11375",
        "unverifiedMaximum": "325000",
        "withdrawalMaximum": "4875000",
        "withdrawalThreshold": "2437500",
        "transferMinimumValue": "6500",
        "defaultMinimumAvailable": "11375"
    },
    "region": null
},
{
    "allowRegistration": true,
    "code": "AT",
    "defaultCurrency": "EUR",
    "documents": [
        {
            "code": "idSelfie",
            "description": "ID Selfie",
            "id": 103,
            "required": true
        },
        {
            "code": "idPassport",
            "description": "ID or Passport",
            "id": 104,
            "required": true
        },
        {
            "code": "proofOfAddress",
            "description": "Proof of Address",
            "id": 105,
            "required": true
        }
    ],
    "id": 13,
    "name": "Austria",
    "options": {
        "minimumOrderValue": 35,
        "limitValue": 1000,
        "minimumTransferValue": 20,
        "minimumPartnerValue": 0,
        "requireProofOfPayment": "TRUE",
        "supportInstantTransfer": null,
        "orderMinimumValue": "35",
        "unverifiedMaximum": "1000",
        "withdrawalMaximum": "15000",
        "withdrawalThreshold": "1500",
        "transferMinimumValue": "20",
        "defaultMinimumAvailable": "35",
        "riskLevel": "low"
    },
    "region": null
}]
"""
