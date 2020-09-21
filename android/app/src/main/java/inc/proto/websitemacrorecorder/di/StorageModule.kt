package inc.proto.websitemacrorecorder.di

import android.content.Context
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideWebStorage(): WebStorage {
        return WebStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager {
        return CookieManager.getInstance()
    }

    @Provides
    @Singleton
    @Suppress("DEPRECATION")
    fun provideCookieSyncManager(@ApplicationContext applicationContext: Context): CookieSyncManager {
        return CookieSyncManager.createInstance(applicationContext)
    }
}
