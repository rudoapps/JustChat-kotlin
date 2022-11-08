package es.rudo.androidbaseproject.di

// @Module
// @InstallIn(SingletonComponent::class)
// object ApiModule {
//
//    @Singleton
//    @Provides
//    fun providesHttpLoggingInterceptor() =
//        HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//
//    @Singleton
//    @Provides
//    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
//        OkHttpClient
//            .Builder()
//            .addInterceptor(httpLoggingInterceptor)
//            .build()
//
//    @Singleton
//    @Provides
//    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
//        Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(BuildConfig.API_BASE_URL)
//            .client(okHttpClient)
//            .build()
// }
