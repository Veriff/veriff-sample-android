package com.veriff.demo.service;

import mobi.lab.veriff.data.VeriffConstants;
import mobi.lab.veriff.service.VeriffStatusUpdatesService;

public class MyVeriffStatusUpdatesService extends VeriffStatusUpdatesService {

    @Override
    protected void onStatusChanged(String sessionToken, int statusCode) {
        if (statusCode == VeriffConstants.STATUS_USER_FINISHED) {
            //user finished whatever he/she was asked to do, there might be other callbacks coming after this one (for example if the images are still being uploaded in the background)
        } else if (statusCode == VeriffConstants.STATUS_ERROR_NO_IDENTIFICATION_METHODS_AVAILABLE) {
            //there are no identifications methods currently available
        } else if (statusCode == VeriffConstants.STATUS_ERROR_SETUP) {
            //issue with the provided vendor data
        } else if (statusCode == VeriffConstants.STATUS_ERROR_UNKNOWN) {
            //unidentified error
        } else if (statusCode == VeriffConstants.STATUS_ERROR_NETWORK) {
            //network unavailable
        } else if (statusCode == VeriffConstants.STATUS_USER_CANCELED) {
            //user closed SDK
        } else if (statusCode == VeriffConstants.STATUS_UNABLE_TO_ACCESS_CAMERA) {
            //we are unable to access phone camera (either access denied or there are no usable cameras)
        } else if (statusCode == VeriffConstants.STATUS_SUBMITTED) {
            //SelfID photos were successfully uploaded
        } else if (statusCode == VeriffConstants.STATUS_ERROR_SESSION) {
            //invalid sessionToken was passed to the SDK
        } else if (statusCode == VeriffConstants.STATUS_DONE) {
            //verification specialist declined the session
        }
    }

}
