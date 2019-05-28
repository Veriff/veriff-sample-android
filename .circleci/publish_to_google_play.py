#!/usr/bin/env python3

import sys
from googleapiclient.discovery import build
import httplib2
from oauth2client import service_account

if len(sys.argv) != 7:
    print("publish_to_google_play\n\tPushes an apk into Google Play")
    print("Usage:\n\tadd_artifact.py <service_email> <key_file> <key_password> " +
          "<apk> <package_name> <track=internal|alpha|beta>")
    exit(1)

service_email, key_file, key_password, apk, package_name, track = sys.argv[1:]

creds = service_account.ServiceAccountCredentials.from_p12_keyfile(
    service_email,
    key_file,
    key_password,
    scopes="https://www.googleapis.com/auth/androidpublisher")

http = creds.authorize(httplib2.Http())

service = build("androidpublisher", "v3", http=http)

edit = service.edits().insert(body={}, packageName=package_name)
edit_result = edit.execute()

apk_result = service.edits().apks().upload(
    editId=edit_result['id'],
    packageName=package_name,
    media_body=apk).execute()

track_result = service.edits().tracks().update(
    editId=edit_result['id'],
    track=track,
    packageName=package_name,
    body={u'releases': [{
        u'versionCodes': [str(apk_result['versionCode'])],
        u'status': u'completed',
    }]}).execute()

service.edits().commit(editId=edit_result['id'], packageName=package_name).execute()
