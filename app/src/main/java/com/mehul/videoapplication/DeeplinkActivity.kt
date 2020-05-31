package com.mehul.videoapplication

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle


class DeeplinkActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        if (intent.action === Intent.ACTION_VIEW) {
            val data: Uri? = intent.data
            if (data != null) {

                // show an alert with the "custom" param
                AlertDialog.Builder(this)
                    .setTitle("An example of a Deeplink")
                    .setMessage("Found custom param: " + data.getQueryParameter("custom"))
                    .setPositiveButton(
                        R.string.yes,
                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                    .setIcon(R.drawable.ic_dialog_alert)
                    .show()
            }
        }
    }
}
