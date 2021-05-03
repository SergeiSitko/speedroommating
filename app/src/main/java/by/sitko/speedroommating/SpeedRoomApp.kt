package by.sitko.speedroommating

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import by.sitko.speedroommating.di.apiModule
import by.sitko.speedroommating.di.singletonModule
import by.sitko.speedroommating.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class SpeedRoomApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SpeedRoomApp)
            modules(listOf(viewModels, apiModule, singletonModule))
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}