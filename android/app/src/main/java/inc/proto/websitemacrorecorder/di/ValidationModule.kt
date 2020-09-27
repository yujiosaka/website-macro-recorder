package inc.proto.websitemacrorecorder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.apache.commons.validator.routines.UrlValidator
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ValidationModule {
    @Provides
    @Singleton
    fun provideUrlValidator(): UrlValidator {
        return UrlValidator.getInstance()
    }
}
