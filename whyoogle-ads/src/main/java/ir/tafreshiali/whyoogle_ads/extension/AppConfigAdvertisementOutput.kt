package ir.tafreshiali.whyoogle_ads.extension

data class AppConfigAdvertisementOutput(
    val Active: Boolean,
    val Sources: List<Source>
)

data class Source(
    val Key: String,
    val Value: String
)