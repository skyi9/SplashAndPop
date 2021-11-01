package com.StriringMind.splashandpop

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_id"
const val channelName = "notification_name"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.getNotification() != null) {
            generateNotification(remoteMessage.notification!!.title!! , remoteMessage.notification!!.body!!)
        }
}

    //attaching notification with the custom layout
    private fun getRemoteView(title : String, description: String) : RemoteViews{
        val remoteView = RemoteViews("com.StriringMind.splashandpop" , R.layout.custom_layout)

        remoteView.setTextViewText( R.id.title , title)
        remoteView.setTextViewText(R.id.description , description)
        remoteView.setImageViewResource(R.id.chidiya , R.drawable.bird)

        return remoteView
    }
    private fun generateNotification(title : String, description: String){
        val intent = Intent(this , Point::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this , 0 , intent , PendingIntent.FLAG_ONE_SHOT)

        //creating notification
        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext , channelId)
            .setSmallIcon(R.drawable.bird)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title , description))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0 , builder.build())
    }
}