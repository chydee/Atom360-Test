package com.chydee.atom360testapp.data.pojo


import com.google.gson.annotations.SerializedName

class Summary : ArrayList<Summary.SummaryItem>() {
    data class SummaryItem(
        val updated: Long, // 1589712171318
        val country: String, // Zimbabwe
        val countryInfo: CountryInfo,
        val cases: Int, // 44
        val todayCases: Int, // 2
        val deaths: Int, // 4
        val todayDeaths: Int, // 0
        val recovered: Int, // 17
        val active: Int, // 23
        val critical: Int, // 0
        val casesPerOneMillion: Double, // 3
        val deathsPerOneMillion: Double, // 0.3
        val tests: Int, // 27059
        val testsPerOneMillion: Int, // 1824
        val population: Int, // 14835308
        val continent: String, // Africa
        val activePerOneMillion: Double, // 1.55
        val recoveredPerOneMillion: Double, // 1.15
        val criticalPerOneMillion: Double // 0
    ) {
        data class CountryInfo(
            @SerializedName("_id")
            val id: Int, // 716
            val iso2: String, // ZW
            val iso3: String, // ZWE
            val lat: Double, // -20
            val long: Double, // 30
            val flag: String // https://disease.sh/assets/img/flags/zw.png
        )
    }
}