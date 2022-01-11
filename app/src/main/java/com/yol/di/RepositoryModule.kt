package com.yol.di

import com.yol.network.repositories.AuthRepository
import com.yol.network.repositories.EventRepository
import com.yol.network.repositories.FirebaseRepository
import com.yol.network.repositories.TimelineRepository
import com.yol.network.repositories.ForecastRepository
import org.koin.dsl.module

val repositoryModule = module {

    // Auth Repository
    single {
        AuthRepository(get())
    }
    // Forecast Repository
    single {
        ForecastRepository(get())
    }

    // Event Repository
    single {
        EventRepository(get())
    }

    // Event Repository
    single {
        TimelineRepository(get())
    }
    // Auth Repository
    single {
        FirebaseRepository(
            firebaseAuth = get(),
            databaseReference = get()
        )
    }

}