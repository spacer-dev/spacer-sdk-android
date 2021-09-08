package com.spacer.sdk.data.api

import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val requestInterceptor =
        Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", userAgent)
                .build()
            return@Interceptor chain.proceed(request)
        }

    private val userAgent: String
        get() {
            return String.format(
                "SpacerSDK/%s (kotlin %s; %s; %s)",
                Build.VERSION.RELEASE,
                Build.MODEL,
                Build.BRAND,
                Build.DEVICE
            )
        }

    private val httpBuilder = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .addInterceptor(loggingInterceptor)

    private val retrofit = Retrofit.Builder()
        .baseUrl("${APIConst.BaseURL}/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpBuilder.build())
        .build()

    val key: IKeyAPI by lazy { retrofit.create(IKeyAPI::class.java) }
    val myLocker: IMyLockerAPI by lazy { retrofit.create(IMyLockerAPI::class.java) }
    val sprLocker: ISPRLockerAPI by lazy { retrofit.create(ISPRLockerAPI::class.java) }
}

val api = APIClient()