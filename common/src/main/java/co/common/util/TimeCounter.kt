package co.common.util

import java.util.*

/**
 * Created by Mochamad Noor Syamsu on 7/30/18.
 */

object TimeCounter {

    private var start = 0L

    fun startTimer() {
        start = getCurrentMillis()
    }

    private fun getCurrentMillis(): Long {
        return GregorianCalendar().timeInMillis
    }

    /**
     * If not started, return 0
     * after calculating time spent, reset counter
     */
    fun getTimeSpent(): Int {
        if (start == 0L) return 0

        val timeSpent = getCurrentMillis() - start
        start = 0L
        return timeSpent.toInt() / 1000
    }

}