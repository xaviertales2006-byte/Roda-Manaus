package com.turismoacessivel.di

import com.turismoacessivel.data.remote.OverpassApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(
            Interceptor { chain ->
                val requisicao = chain.request().newBuilder()
                    .header("User-Agent", "TurismoAcessivel/1.0 (projeto academico)")
                    .build()
                chain.proceed(requisicao)
            },
        )
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(cliente: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://overpass-api.de/")
        .client(cliente)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideOverpassApi(retrofit: Retrofit): OverpassApi =
        retrofit.create(OverpassApi::class.java)
}
