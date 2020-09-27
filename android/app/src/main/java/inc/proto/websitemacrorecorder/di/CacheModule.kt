package inc.proto.websitemacrorecorder.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext applicationContext : Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }
}
