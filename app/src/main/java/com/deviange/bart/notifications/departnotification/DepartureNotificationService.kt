package com.deviange.bart.notifications.departnotification

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.deviange.bart.R
import com.deviange.bart.base.date.TimeFormatter
import com.deviange.bart.notifications.NotifChannel
import dagger.android.DaggerService
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

@ExperimentalTime
class DepartureNotificationService : DaggerService() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        NotifChannel.DEPARTURE.registerChannel(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent ?: return Service.START_NOT_STICKY

        val departureTime = intent.getLongExtra(DEPARTURE_TIME_MS_KEY, 0)
        val now = System.currentTimeMillis()
        if (now - LEAVING_TIME_SLOP > departureTime) {
            return Service.START_NOT_STICKY
        }

        val builder = NotificationCompat.Builder(this, NotifChannel.DEPARTURE.channelId)
            .setSmallIcon(R.drawable.ic_star_24px)
            .setContentText("Your train departs at " + TimeFormatter.formatShortTime(departureTime))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        startForeground(id, builder.build())

        return Service.START_NOT_STICKY
    }

    companion object {
        const val id = 100

        private const val DEPARTURE_TIME_MS_KEY = "departure_time"

        private val LEAVING_TIME_SLOP = 2.minutes.inMilliseconds

        fun createIntent(context: Context, departureTimeMs: Long) =
            Intent(context, DepartureNotificationService::class.java).apply {
                putExtra(DEPARTURE_TIME_MS_KEY, departureTimeMs)
            }
    }
}