package com.veriff.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import mobi.lab.veriff.data.VeriffConstants;

public class VeriffStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Log.d("VeriffStatusReceiver","Broadcast received");
        if (extras.containsKey(VeriffConstants.INTENT_EXTRA_STATUS)) {
            String sessionToken = extras.getString(VeriffConstants.INTENT_EXTRA_SESSION_URL);
            int status = extras.getInt(VeriffConstants.INTENT_EXTRA_STATUS);
            if (status == VeriffConstants.STATUS_UNABLE_TO_ACCESS_CAMERA) {
                //Veriff broadcast: camera permission missing
                Toast.makeText(context, "Veriff broadcast: camera or permission missing", Toast.LENGTH_LONG).show();
            } else if (status == VeriffConstants.STATUS_USER_FINISHED) {
                //Veriff broadcast: finished
                Toast.makeText(context, "Veriff broadcast: finished", Toast.LENGTH_LONG).show();
            } else if (status == VeriffConstants.STATUS_USER_CANCELED) {
                //Veriff broadcast: canceled
                Toast.makeText(context, "Veriff broadcast: canceled", Toast.LENGTH_LONG).show();
            } else if (status == VeriffConstants.STATUS_SUBMITTED) {
                //Veriff broadcast: submitted
                Toast.makeText(context, "Veriff broadcast: submitted", Toast.LENGTH_LONG).show();
            } else if (status == VeriffConstants.STATUS_ERROR_SESSION) {
                //Veriff broadcast: session expired
                Toast.makeText(context, "Veriff broadcast: session expired", Toast.LENGTH_LONG).show();
            } else if (status == VeriffConstants.STATUS_ERROR_NETWORK) {
                //Veriff broadcast: network error
                Toast.makeText(context, "Veriff broadcast: network error", Toast.LENGTH_LONG).show();
            } else if (status == VeriffConstants.STATUS_ERROR_SETUP) {
                //Veriff broadcast: setup error
                Toast.makeText(context, "Veriff broadcast: setup error", Toast.LENGTH_LONG).show();
            } else if (status == VeriffConstants.STATUS_ERROR_NO_IDENTIFICATION_METHODS_AVAILABLE) {
                //Veriff broadcast: no ident methods available
                Toast.makeText(context, "Veriff broadcast: no ident methods available", Toast.LENGTH_LONG).show();
            } else if (status == VeriffConstants.STATUS_DONE) {
                //Veriff broadcast: done
                Toast.makeText(context, "Veriff broadcast: done", Toast.LENGTH_LONG).show();
            } else if (status == VeriffConstants.STATUS_ERROR_UNKNOWN) {
                //Veriff broadcast: unknown
                Toast.makeText(context, "Veriff broadcast: unknown", Toast.LENGTH_LONG).show();
            }
        }
    }
}
