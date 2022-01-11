package com.yol.utils

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.yol.di.networkModule
import com.yol.di.repositoryModule
import com.yol.di.vmModule
import com.construction.utils.TinyDB
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {



    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModules)
        }
      // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Fresco.initialize(this);
        val spPrivate = getSharedPreferences("private", MODE_PRIVATE)
        tinyDB = TinyDB(spPrivate)
        instance = this
        mContext = this

    }
    companion object {
        val appModules = listOf(
            repositoryModule,
            networkModule,
            vmModule
        )

        lateinit var tinyDB: TinyDB
        @get:Synchronized
        var instance: MyApplication? = null
            private set
        var mContext:Context? = null

        fun getRealPathFromURI(context:Context,contentURI: Uri?): String {
            val result: String?
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = contentURI?.let { context.contentResolver.query(it, filePathColumn, null, null, null) }
            if (cursor == null) {
                result = contentURI?.path
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(filePathColumn[0])
                result = cursor.getString(idx)
                cursor.close()
            }
            return result.toString()
        }
        fun toast(message: CharSequence)
        {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }
    }



}