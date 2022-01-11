package com.yol.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.yol.network.RestApiInterface
import com.yol.utils.Constants
import com.yol.utils.MyApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val networkModule = module {

    // Provide Gson
    single<Gson> {
        GsonBuilder().create()
    }

    // Provide HttpLoggingInterceptor
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Provide OkHttpClient
    single {
        val access_token =
            MyApplication.tinyDB.getString(Constants.SharedPref.LOGGED_IN_ACCESS_TOKEN)
                ?: ""
        val cacheDir = File((get<Context>() as MyApplication).cacheDir, "http")
        val cache = Cache(
            cacheDir,
            20 * 1024 * 1024
        )

        OkHttpClient.Builder()
            .cache(cache)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor { chain ->
                val request =
                    chain.request().newBuilder().addHeader("Authorization", access_token).build()
                chain.proceed(request)
            }
            .build()
    }

    // Provide Retrofit
    single<Retrofit> {

        Retrofit.Builder()
            .client(get())
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    // Provide GithubApi
    single {
        get<Retrofit>().create(RestApiInterface::class.java)
    }

    single<FirebaseAuth> {
        FirebaseAuth.getInstance()
    }

    single {
        FirebaseDatabase.getInstance().reference
    }

    single {
        FirebaseStorage.getInstance().reference
    }

}