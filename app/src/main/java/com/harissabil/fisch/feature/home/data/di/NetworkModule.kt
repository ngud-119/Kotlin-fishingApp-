package com.harissabil.fisch.feature.home.data.di

import android.content.Context
import com.harissabil.fisch.BuildConfig
import com.harissabil.fisch.core.common.helper.ConnectivityChecker
import com.harissabil.fisch.feature.home.data.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    @Named(ONLINE_INTERCEPTOR)
    fun provideOnlineInterceptor(): Interceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 60 * 60
        response
            .newBuilder()
            .header(CACHE_CONTROL, "public, max-age=$maxAge")
            .removeHeader(PRAGMA)
            .build()
    }

    @Provides
    @Singleton
    @Named(OFFLINE_INTERCEPTOR)
    fun provideOfflineInterceptor(@ApplicationContext mContext: Context): Interceptor =
        Interceptor { chain ->
            var request = chain.request()
            val connectivityChecker = ConnectivityChecker()
            val isConnectivityAvailable = connectivityChecker(mContext)
            if (!isConnectivityAvailable) {
                val maxStale = 60 * 60 * 24 * 7
                request = request
                    .newBuilder()
                    .header(CACHE_CONTROL, "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader(PRAGMA)
                    .build()
            }
            chain.proceed(request)
        }

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache =
        Cache(context.cacheDir, CACHE_SIZE)

    private const val HOST_NAME = "api.open-meteo.com"
    private val certificatePinner = CertificatePinner.Builder()
        .add(HOST_NAME, "sha256/Mg8VP+/mow0f1wW03lfDgVBJ4271IlltuzOgV2Pc6WU=")
        .add(HOST_NAME, "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=")
        .add(HOST_NAME, "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Named(ONLINE_INTERCEPTOR) internetInterceptor: Interceptor,
        @Named(OFFLINE_INTERCEPTOR) offlineInterceptor: Interceptor,
        cache: Cache,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(internetInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .certificatePinner(certificatePinner)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

private const val CACHE_SIZE: Long = (50 * 1024 * 1024).toLong()
private const val ONLINE_INTERCEPTOR = "online_interceptor"
private const val OFFLINE_INTERCEPTOR = "offline_interceptor"
private const val CACHE_CONTROL = "Cache-Control"
private const val PRAGMA = "Pragma"