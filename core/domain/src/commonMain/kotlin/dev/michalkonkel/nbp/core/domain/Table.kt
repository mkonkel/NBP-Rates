package dev.michalkonkel.nbp.core.domain

import kotlinx.serialization.Serializable

/**
 * Enum representing NBP exchange rate table types.
 */
@Serializable
enum class Table(val value: String) {
    TABLE_A("A"),
    TABLE_B("B"),
    TABLE_C("C"),
    ;

    companion object {
        /**
         * Creates Table enum from string value.
         * Defaults to TABLE_A if value is not recognized.
         */
        fun fromString(value: String?): Table {
            return when (value?.uppercase()) {
                "A" -> TABLE_A
                "B" -> TABLE_B
                "C" -> TABLE_C
                else -> throw IllegalStateException("Unknown table type: $value")
            }
        }
    }
}
