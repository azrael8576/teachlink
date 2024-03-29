package com.wei.teachlink.core.datastore

import androidx.datastore.core.DataStore
import com.wei.teachlink.core.model.data.DarkThemeConfig
import com.wei.teachlink.core.model.data.LanguageConfig
import com.wei.teachlink.core.model.data.ThemeBrand
import com.wei.teachlink.core.model.data.UserData
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class TlPreferencesDataSource
@Inject
constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData =
        userPreferences.data.map {
            UserData(
                isFirstTimeUser = it.isFirstTimeUser,
                tokenString = it.tokenString,
                userName = it.userName,
                useDynamicColor = it.useDynamicColor,
                themeBrand =
                when (it.themeBrand) {
                    null,
                    ThemeBrandProto.THEME_BRAND_UNSPECIFIED,
                    ThemeBrandProto.UNRECOGNIZED,
                    ThemeBrandProto.THEME_BRAND_DEFAULT,
                    -> ThemeBrand.DEFAULT

                    ThemeBrandProto.THEME_BRAND_ANDROID -> ThemeBrand.ANDROID
                },
                darkThemeConfig =
                when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                    -> DarkThemeConfig.FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> DarkThemeConfig.LIGHT
                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                },
                languageConfig =
                when (it.languageConfig) {
                    null,
                    LanguageConfigProto.LANGUAGE_CONFIG_UNSPECIFIED,
                    LanguageConfigProto.UNRECOGNIZED,
                    LanguageConfigProto.LANGUAGE_CONFIG_FOLLOW_SYSTEM,
                    -> LanguageConfig.FOLLOW_SYSTEM

                    LanguageConfigProto.LANGUAGE_CONFIG_ENGLISH -> LanguageConfig.ENGLISH
                    LanguageConfigProto.LANGUAGE_CONFIG_CHINESE -> LanguageConfig.CHINESE
                },
            )
        }

    suspend fun setTokenString(tokenString: String) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.tokenString = tokenString
                }
            }
        } catch (ioException: IOException) {
            Timber.tag("TlPreferences").e(ioException, "Failed to update user preferences")
        }
    }
}
