package com.yol.di

import com.yol.ui.auth.AuthViewModel
import com.yol.ui.timeline.TimeLineViewModel
import com.yol.ui.event.EventViewModel
import com.yol.viewmodel.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {

    // Auth ViewModel
    viewModel {
        AuthViewModel(
            authRepository = get(),
            firebaseAuth = get(),
            databaseReference = get()
        )
    }
    // Auth ViewModel
    viewModel {
        MainActivityViewModel(
            forecastRepository= get()
           )
    }

    viewModel {
        EventViewModel(
            eventRepository = get()
        )
    }
    viewModel {
        TimeLineViewModel(
            timelineRepository = get()
        )
    }
}