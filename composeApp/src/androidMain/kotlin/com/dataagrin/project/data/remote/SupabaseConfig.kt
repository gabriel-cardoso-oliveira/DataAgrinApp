package com.dataagrin.project.data.remote

import com.dataagrin.project.BuildConfig

actual object SupabaseConfig {
    actual val URL: String = BuildConfig.URL
    actual val API_KEY: String = BuildConfig.API_KEY
}
