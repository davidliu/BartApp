package com.deviange.bart.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationManagerCompat.*
import com.deviange.bart.R

@IntDef(
    value = [
        IMPORTANCE_UNSPECIFIED,
        IMPORTANCE_NONE,
        IMPORTANCE_MIN,
        IMPORTANCE_LOW,
        IMPORTANCE_DEFAULT,
        IMPORTANCE_HIGH
    ]
)
@Retention(AnnotationRetention.SOURCE)
annotation class Importance

enum class NotifChannel(
    val channelId: String,
    @StringRes val channelNameResId: Int,
    @StringRes val descriptionResId: Int,
    @Importance val importance: Int = IMPORTANCE_DEFAULT
) {
    DEPARTURE("DEPARTURE", R.string.notification_departures, R.string.notification_departures_description);

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(context: Context) =
        NotificationChannel(
            channelId,
            context.getString(channelNameResId),
            importance
        )
            .apply {
                description = context.getString(descriptionResId)
            }

    fun registerChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createChannel(context)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}