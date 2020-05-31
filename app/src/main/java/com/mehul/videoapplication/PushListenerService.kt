package com.mehul.videoapplication

import android.content.Intent
import android.os.Bundle

import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient.CampaignPushResult
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationDetails
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class PushListenerService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(
            TAG,
            "Registering push notifications token: $token"
        )
        AuthenticatorActivity.getPinpointManager(applicationContext)?.notificationClient
            ?.registerDeviceToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message: " + remoteMessage.data)
        val notificationClient: NotificationClient? =
            AuthenticatorActivity.getPinpointManager(applicationContext)?.notificationClient
        val notificationDetails = NotificationDetails.builder()
            .from(remoteMessage.from)
            .mapData(remoteMessage.data)
            .intentAction(NotificationClient.FCM_INTENT_ACTION)
            .build()
        val pushResult =
            notificationClient?.handleCampaignPush(notificationDetails)
        if (CampaignPushResult.NOT_HANDLED != pushResult) {
            /**
             * The push message was due to a Pinpoint campaign.
             * If the app was in the background, a local notification was added
             * in the notification center. If the app was in the foreground, an
             * event was recorded indicating the app was in the foreground,
             * for the demo, we will broadcast the notification to let the main
             * activity display it in a dialog.
             */
            if (CampaignPushResult.APP_IN_FOREGROUND == pushResult) {
                /* Create a message that will display the raw data of the campaign push in a dialog. */
                val dataMap =
                    HashMap(remoteMessage.data)
                broadcast(remoteMessage.from, dataMap)
            }
            return
        }
    }

    private fun broadcast(
        from: String?,
        dataMap: HashMap<String, String>
    ) {
        val intent = Intent(ACTION_PUSH_NOTIFICATION)
        intent.putExtra(INTENT_SNS_NOTIFICATION_FROM, from)
        intent.putExtra(INTENT_SNS_NOTIFICATION_DATA, dataMap)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        val TAG = PushListenerService::class.java.simpleName

        // Intent action used in local broadcast
        const val ACTION_PUSH_NOTIFICATION = "push-notification"

        // Intent keys
        const val INTENT_SNS_NOTIFICATION_FROM = "from"
        const val INTENT_SNS_NOTIFICATION_DATA = "data"

        /**
         * Helper method to extract push message from bundle.
         *
         * @param data bundle
         * @return message string from push notification
         */
        fun getMessage(data: Bundle): String {
            return (data["data"] as HashMap<*, *>).toString()
        }
    }
}