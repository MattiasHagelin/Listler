package com.math3249.listler

import android.app.Application
import androidx.annotation.StringRes
import com.math3249.listler.data.ListlerDatabase

class App: Application() {
    companion object {
        lateinit var instance: App private set
    }
    val database: ListlerDatabase by lazy { ListlerDatabase.getDatabase(this)}

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}