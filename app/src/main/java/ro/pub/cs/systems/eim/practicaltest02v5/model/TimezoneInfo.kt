package ro.pub.cs.systems.eim.practicaltest02v5.model

import com.google.gson.annotations.SerializedName
import com.google.gson.Gson
import java.util.*

data class TimezoneInfo(
    @SerializedName("abbreviation") val abbreviation: String,
    @SerializedName("client_ip") val clientIp: String,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("day_of_week") val dayOfWeek: Int,
    @SerializedName("day_of_year") val dayOfYear: Int,
    @SerializedName("dst") val dst: Boolean,
    @SerializedName("dst_from") val dstFrom: String,
    @SerializedName("dst_offset") val dstOffset: Int,
    @SerializedName("dst_until") val dstUntil: String,
    @SerializedName("raw_offset") val rawOffset: Int,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("unixtime") val unixTime: Long,
    @SerializedName("utc_datetime") val utcDatetime: String,
    @SerializedName("utc_offset") val utcOffset: String,
    @SerializedName("week_number") val weekNumber: Int
) {
    override fun toString(): String {
        return "abbreviation=$abbreviation,client_ip=$clientIp,datetime=$datetime,day_of_week=$dayOfWeek,day_of_year=$dayOfYear,dst=$dst,dst_from=$dstFrom,dst_offset=$dstOffset,dst_until=$dstUntil,raw_offset=$rawOffset,timezone=$timezone,unixtime=$unixTime,utc_datetime=$utcDatetime,utc_offset=$utcOffset,week_number=$weekNumber"
    }

    companion object {
        fun fromString(data: String): TimezoneInfo {
            val map = data.split(",").associate {
                val (key, value) = it.split("=")
                key to value
            }
            return TimezoneInfo(
                abbreviation = map["abbreviation"]!!,
                clientIp = map["client_ip"]!!,
                datetime = map["datetime"]!!,
                dayOfWeek = map["day_of_week"]!!.toInt(),
                dayOfYear = map["day_of_year"]!!.toInt(),
                dst = map["dst"]!!.toBoolean(),
                dstFrom = map["dst_from"]!!,
                dstOffset = map["dst_offset"]!!.toInt(),
                dstUntil = map["dst_until"]!!,
                rawOffset = map["raw_offset"]!!.toInt(),
                timezone = map["timezone"]!!,
                unixTime = map["unixtime"]!!.toLong(),
                utcDatetime = map["utc_datetime"]!!,
                utcOffset = map["utc_offset"]!!,
                weekNumber = map["week_number"]!!.toInt()
            )
        }
    }

    fun getValue(field: String): Any? {
        return when (field) {
            "abbreviation" -> abbreviation
            "client_ip" -> clientIp
            "datetime" -> datetime
            "day_of_week" -> dayOfWeek
            "day_of_year" -> dayOfYear
            "dst" -> dst
            "dst_from" -> dstFrom
            "dst_offset" -> dstOffset
            "dst_until" -> dstUntil
            "raw_offset" -> rawOffset
            "timezone" -> timezone
            "unixtime" -> unixTime
            "utc_datetime" -> utcDatetime
            "utc_offset" -> utcOffset
            "week_number" -> weekNumber
            else -> null
        }
    }
}
