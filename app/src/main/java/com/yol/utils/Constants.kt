package com.yol.utils

import com.yol.R
import android.provider.MediaStore

import android.provider.DocumentsContract

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri

import android.os.Environment

import android.os.Build
import com.google.android.gms.common.util.CollectionUtils.listOf


class Constants {
    companion object {
      //  val BASE_URL = "https://youronelifes.com/eventapi/"
        val BASE_URL = "http://13.126.24.62:3000/api/"
        val TERMS_AND_CONDITION = "https://youronelifes.com/privacypolicy"

        var category = kotlin.collections.listOf<String>(
            "Ambition", "Commitments", "Confession", "conscience", "Dilemmas",
            "Inhibitions", "Document", "Family", "Friends","health", "outings", "wallet"
        )
        var forecastList = kotlin.collections.listOf<String>(
            "Happy & Productive", "Lazy or Busy", "Not Good", "Life Missed",
        )
        var categoryIcon = kotlin.collections.listOf<Int>(
            R.drawable.target,
            R.drawable.handshake,
            R.drawable.prayer,
            R.drawable.in_love,
            R.drawable.decision_making,
            R.drawable.stop,
            R.drawable.google_docs,
            R.drawable.family,
            R.drawable.friend,
            R.drawable.healthcare,
            R.drawable.mountain,
            R.drawable.wallet,
        )
        val AMBITION_KEY = "Ambition"
        val COMMITMENTS_KEY = "Commitments"
        val CONFESSION_KEY = "Confession"
        val CONSCIENCE_KEY = "conscience"
        val DILEMMAS_KEY = "Dilemmas"
        val INHIBITIONS_KEY = "Inhibitions"
        val DOCUMENT_KEY = "Document"
        val FAMILY_KEY = "Family"
        val FRIENDS_KEY = "Friends"
        val HEALTH_KEY = "health"
        val OUTINGS_KEY = "outings"
        val WALLET_KEY = "wallet"

        val FORECAST_SELFIE_KEY = "forecastSelfie"
        fun getPath(context: Context, uri: Uri): String? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // DocumentProvider
                if (DocumentsContract.isDocumentUri(context, uri)) {
                    // ExternalStorageProvider
                    if (isExternalStorageDocument(uri)) {
                        val docId:String = DocumentsContract.getDocumentId(uri)
                        val split: Array<String> = docId.split(":").toTypedArray()
                        val type = split[0]
                        if ("primary".equals(type)) {
                            return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                        }
                        // TODO handle non-primary volumes
                    } else if (isDownloadsDocument(uri)) {
                        val id = DocumentsContract.getDocumentId(uri)
                        val contentUri: Uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id)
                        )
                        return getDataColumn(context, contentUri, null, null)
                    } else if (isMediaDocument(uri)) {
                        val docId = DocumentsContract.getDocumentId(uri).toString()
                        val split: Array<String> = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        if ("image" == type) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        } else if ("video" == type) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        } else if ("audio" == type) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                }
            } else if ("content".equals(uri.getScheme())) {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )
            } else if ("file".equals(uri.getScheme())) {
                return uri.getPath()
            }
            return null
        }


        fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor =
                    uri?.let { context.contentResolver.query(it, projection, selection, selectionArgs, null) }
                if (cursor != null && cursor.moveToFirst()) {
                    val index: Int = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                if (cursor != null) cursor.close()
            }
            return null
        }

        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.getAuthority()
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.getAuthority()
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.getAuthority()
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.getAuthority()
        }
    }

    interface SharedPref {
        companion object {
            const val LOGGGED_IN = "LOGGGED_IN"
            const val LOGGED_IN_ACCESS_TOKEN = "logged_in_access_token"
            const val USER_PROFILE_DETAIL = "user_profile_details"
            const val EMAIL = "EMAIL"
            const val TXT_EMAIL = "TXTEMAIL"
            const val OTP = "OTP"
            const val ID = "ID_"
            const val USER_ID = "USER_ID"
            const val USER_NAME = "USER_NAME"
            const val USER_AVATAR = "USER_AVATAR"
            const val DEVICE_TOKEN = "DEVICE_TOKEN"
            const val DEVICE_ID = "DEVICE_ID"
            const val ACCESS_TOKEN = "ACCESS_TOKEN"
            const val DIAL_CODE = "DIAL_CODE"
            const val MOBILE = "MOBILE"
            const val SITE_ID = "SITE_ID"
            const val START_DATE = "START_DATE"
            const val END_DATE = "END_DATE"
            const val CANCEL_ID = "CANCEL_ID"
            const val REQUEST_ID = "REQUEST_ID"
            const val CURRENCY = "CURRENCY"
            const val SERVICE_TYPE = "SERVICE_TYPE"
            const val STRIPE_PUBLISHABLE_KEY = "STRIPE_PUBLISHABLE_KEY"
            const val LATITUDE = "LATITUDE"
            const val LONGITUDE = "LONGITUDE"
            const val PICTURE = "PICTURE"
            const val IS_VERIFIED = "IS_VERIFIED"
            const val ROLES = "ROLES"
            const val IMAGE = "IMAGE"
            const val USER_TOKEN = "usertoken"
            const val USER_TIMBER_LIST: String = "user_tribes_list"
            const val USER_FOCECAST_STATUS: String = "user_focecast_status"

        }
    }





}