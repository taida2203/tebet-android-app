package com.tebet.mojual.common.util

import co.common.event.NotificationEvent
import com.tebet.mojual.R

/**
 * Created by heo on 3/6/18.
 */

object NotificationFactory {
    private const val WELCOME = "WELCOME"
    const val TRIAL_CLASS_BOOKING_REMINDER = "TRIAL_CLASS_BOOKING_REMINDER"
    private const val TRIAL_CLASS_EXPIRED_REMINDER = "TRIAL_CLASS_EXPIRED_REMINDER"
    private const val TRIAL_CLASS_AFTER_BOOKING = "TRIAL_CLASS_AFTER_BOOKING"
    private const val PACKAGE_ACTIVATING = "PACKAGE_ACTIVATING"
    private const val TRIAL_CLASS_1_DAY_BEFORE = "TRIAL_CLASS_1_DAY_BEFORE"
    private const val CLASS_TODAY = "CLASS_TODAY"
    private const val CLASS_STARTING = "CLASS_STARTING"
    private const val PACKAGE_PAYMENT_REMINDER = "PACKAGE_PAYMENT_REMINDER"
    private const val INVOICE_EXPIRED_TODAY = "INVOICE_EXPIRED_TODAY"
    private const val INVOICE_EXPIRED_1_DAY = "INVOICE_EXPIRED_1_DAY"
    private const val INVOICE_EXPIRED_30_DAYS = "INVOICE_EXPIRED_30_DAYS"
    private const val PAYMENT_SUCCESS = "PAYMENT_SUCCESS"
    const val PROFILE = "PROFILE"

    enum class PAGE {
        HOME,
        BOOKING,
        UPCOMING_CLASS,
        MY_PACKAGE,
        MY_INVOICE,
        BUY,
        RTC,
        PROFILE,
        UNKNOWN
    }

    fun colorFrom(notificationEvent: NotificationEvent?): Int {
        return when (notificationEvent?.eventCode) {
            WELCOME, TRIAL_CLASS_BOOKING_REMINDER, TRIAL_CLASS_AFTER_BOOKING, PACKAGE_ACTIVATING, TRIAL_CLASS_1_DAY_BEFORE, PACKAGE_PAYMENT_REMINDER, PAYMENT_SUCCESS -> R.color.statusSuccess
            TRIAL_CLASS_EXPIRED_REMINDER, CLASS_TODAY, INVOICE_EXPIRED_TODAY -> R.color.statusWarn
            CLASS_STARTING, INVOICE_EXPIRED_1_DAY, INVOICE_EXPIRED_30_DAYS -> R.color.statusDanger
            // Unsupported Code
            else -> R.color.statusInfo
        }
    }

    fun pageFrom(eventCode: String?): PAGE {
        return when (eventCode) {
            TRIAL_CLASS_BOOKING_REMINDER, TRIAL_CLASS_EXPIRED_REMINDER, PAYMENT_SUCCESS -> NotificationFactory.PAGE.BOOKING
            INVOICE_EXPIRED_1_DAY, INVOICE_EXPIRED_30_DAYS -> NotificationFactory.PAGE.BUY
            WELCOME -> NotificationFactory.PAGE.HOME
            CLASS_STARTING -> NotificationFactory.PAGE.RTC
            PACKAGE_PAYMENT_REMINDER, INVOICE_EXPIRED_TODAY -> NotificationFactory.PAGE.MY_INVOICE
            PACKAGE_ACTIVATING -> NotificationFactory.PAGE.MY_PACKAGE
            PROFILE -> NotificationFactory.PAGE.PROFILE
            TRIAL_CLASS_AFTER_BOOKING, TRIAL_CLASS_1_DAY_BEFORE, CLASS_TODAY -> NotificationFactory.PAGE.UPCOMING_CLASS
            else -> NotificationFactory.PAGE.UNKNOWN
        }
    }

    fun pageFrom(notificationEvent: NotificationEvent): PAGE {
        return pageFrom(notificationEvent.eventCode)
    }
}
