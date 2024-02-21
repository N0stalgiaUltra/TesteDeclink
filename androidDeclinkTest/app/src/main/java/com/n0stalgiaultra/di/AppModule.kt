package com.n0stalgiaultra.di

import androidx.room.Room
import com.n0stalgiaultra.database.AppDatabase
import com.n0stalgiaultra.database.dao.PhotoEntityDAO
import com.n0stalgiaultra.database.repository.LocalDataSourceImpl
import com.n0stalgiaultra.database.repository.LocalRepositoryImpl
import com.n0stalgiaultra.domain.repository.LocalDataSource
import com.n0stalgiaultra.domain.repository.LocalRepository
import com.n0stalgiaultra.domain.repository.RemoteDataSource
import com.n0stalgiaultra.domain.repository.RemoteRepository
import com.n0stalgiaultra.domain.usecases.GetAllPhotoDataUseCase
import com.n0stalgiaultra.domain.usecases.GetPhotoDataUseCase
import com.n0stalgiaultra.domain.usecases.InsertPhotoDataUseCase
import com.n0stalgiaultra.viewModel.PhotoDataViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
//    factory <Interceptor>{
//        Interceptor{
//                chain ->
//            val request  = chain.request()
//                .newBuilder()
//                .build()
//            chain.proceed(request)
//        }
//    }
//    //http logging in
//    factory <HttpLoggingInterceptor>{
//        HttpLoggingInterceptor(
//            HttpLoggingInterceptor.Logger.DEFAULT
//        ).setLevel(
//            HttpLoggingInterceptor.Level.HEADERS
//        )
//    }
//    //okhttp
//    factory {
//        OkHttpClient.Builder().apply {
//            addInterceptor( get<Interceptor>() )
//            addInterceptor(get<HttpLoggingInterceptor>())
//        }.build()
//    }
//    //retrofit
//    single {
//        Retrofit.Builder()
//            .client(get())
//            .baseUrl(CepAPI.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .build()
//    }

//    //instancia da API
//    single(createdAtStart = false){
//        get<Retrofit>().create(CepAPI::class.java)
//    }

    //Room
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = AppDatabase.DATABASE_NAME
        ).build()

    }

    //Dao
    single {
        get<AppDatabase>().getDao()
    }

    //RemoteDataSource
//    single<RemoteDataSource>{
//        RemoteDataSourceImpl(api = get<CepAPI>())
//    }

    //LocalDataSource
    single<LocalDataSource>{
        LocalDataSourceImpl(dao = get<PhotoEntityDAO>())
    }

    //Repositories
    single <LocalRepository>{
        LocalRepositoryImpl(
            localDataSource = get<LocalDataSource>(),
        )
    }
//    single <RemoteRepository>{
//        RemoteRepositoryImpl(
//            remoteDataSource = get<RemoteDataSource>(),
//        )
//    }

    //UseCases
    factory { GetAllPhotoDataUseCase(get<LocalRepository>()) }
    factory { InsertPhotoDataUseCase(get<LocalRepository>()) }
    factory { GetPhotoDataUseCase(get<LocalRepository>()) }


    //ViewModel
    viewModel {
        PhotoDataViewModel(
            get<InsertPhotoDataUseCase>(),
            get<GetPhotoDataUseCase>(),
            get<GetAllPhotoDataUseCase>(),
        )
    }
}