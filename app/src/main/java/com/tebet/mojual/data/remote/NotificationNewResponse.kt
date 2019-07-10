package com.tebet.mojual.data.remote

import android.os.Bundle
import android.text.format.DateUtils
import co.common.event.NotificationEvent
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

open class NotificationNewResponse(
    templateCode: String?,
    title: String? = null,
    content: String? = null,
    extraParams: MutableMap<String, Any?>? = mutableMapOf()
) : NotificationEvent {
    constructor(notificationData: Map<String, Any?>) : this(notificationData["templateCode"] as String?) {
        templateCode = notificationData["templateCode"] as String?
        title = notificationData["title"] as String?
        content = notificationData["content"] as String?
        notificationId = (notificationData["notificationId"] as String?)?.toInt()
        extraParams = try {
            notificationData["extraParams"] as MutableMap<String, Any?>?
        } catch (e: Exception) {
            val stringExtraParams = notificationData["extraParams"] as String?
            val json: JsonObject? = stringExtraParams?.let { Parser().parse(StringBuilder().append(it)) } as JsonObject
            json?.toMutableMap()
        }
    }

    constructor(extras: Bundle?) : this(extras?.get("templateCode") as String?) {
        templateCode = extras?.get("templateCode") as String?
        title = extras?.get("title") as String?
        content = extras?.get("content") as String?
        notificationId = (extras?.get("notificationId") as String?)?.toInt()
        extraParams = try {
            extras?.get("extraParams") as MutableMap<String, Any?>?
        } catch (e: Exception) {
            val stringExtraParams = extras?.get("extraParams") as String?
            val json: JsonObject? = stringExtraParams?.let { Parser().parse(StringBuilder().append(it)) } as JsonObject
            json?.toMutableMap()
        }
    }

    override var message: String?
        get() = content
        set(value) {}
    override var eventCode: String?
        get() = templateCode
        set(value) {
            templateCode = eventCode
        }
    override var notyType: NotificationEvent.NOTY_TYPE
        get() = notyTypeLocal
        set(value) {
            notyTypeLocal = value
        }
    override var map: MutableMap<String, Any?>?
        get() = extraParams
        set(value) {}
    @SerializedName("notificationId")
    @Expose
    var notificationId: Int? = null
    @SerializedName("userId")
    @Expose
    var userId: Int? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("userType")
    @Expose
    var userType: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("link")
    @Expose
    var link: Any? = null
    @SerializedName("notificationStatus")
    @Expose
    var notificationStatus: String? = null
    @SerializedName("sendingStatus")
    @Expose
    var sendingStatus: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("cancelLimit")
    @Expose
    var cancelLimit: Any? = null
    @SerializedName("studyId")
    @Expose
    var studyId: Int? = null
    @SerializedName("templateCode")
    @Expose
    var templateCode: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("extraParams")
    @Expose
    var extraParams: MutableMap<String, Any?>? = null
    //                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

    var notyTypeLocal: NotificationEvent.NOTY_TYPE = NotificationEvent.NOTY_TYPE.SHOW_ALERT

    val isToday: Boolean
        get() {
            try {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                var dateMiliseconds: Long? = null
//                val newTimeZone: TimeZone? = ClassResponse.getUserTimeZone()
                val converter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSz", Locale.US)
//                newTimeZone?.let {
//                    converter.timeZone = it
//                }
//                dateMiliseconds = converter.parse(date!!).time
//                if (dateMiliseconds > cal.timeInMillis) {
//                    return true
//                }
            } catch (e: Exception) {
                Timber.e(e)
            }

            return false
        }

    fun toHumanRead(): String? {
        try {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            var dateMiliseconds: Long? = null
//            val newTimeZone: TimeZone? = ClassResponse.getUserTimeZone()
            val converter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSz", Locale.US)
//            newTimeZone?.let {
//                converter.timeZone = it
//            }
            dateMiliseconds = converter.parse(date!!).time
            val ago = DateUtils.getRelativeTimeSpanString(dateMiliseconds)
            return ago.toString()
            //                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        } catch (e: Exception) {
            Timber.e(e)
        }

        return date
    }
}