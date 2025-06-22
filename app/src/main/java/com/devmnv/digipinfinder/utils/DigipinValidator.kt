package com.devmnv.digipinfinder.utils

sealed class DigipinValidationResult {
    data class Valid(val digipin: String) : DigipinValidationResult()
    object Invalid : DigipinValidationResult()
}

object DigipinValidator {
    private val DIGIPIN_REGEX = Regex("""^[A-Za-z0-9]{3}-[A-Za-z0-9]{3}-[A-Za-z0-9]{4}$""")

    fun validate(input: String): DigipinValidationResult {
        val normalized = input.trim()

        // Case 1: Direct digipin match
        if (DIGIPIN_REGEX.matches(normalized)) {
            return DigipinValidationResult.Valid(normalized)
        }

        // Case 2: Indian Post URL
        val indiaPostPrefix = "https://dac.indiapost.gov.in/mydigipin/home/"
        if (normalized.startsWith(indiaPostPrefix)) {
            val extracted = normalized.removePrefix(indiaPostPrefix).trim()
            if (DIGIPIN_REGEX.matches(extracted)) {
                return DigipinValidationResult.Valid(extracted)
            }
        }

        // 🔜 Future: Add more URL formats here

        return DigipinValidationResult.Invalid
    }
}
