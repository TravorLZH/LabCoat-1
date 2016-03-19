package com.commit451.gitlab.util;

import android.content.Context;
import android.util.TypedValue;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.Config;
import com.commit451.gitlab.R;

public class AppThemeUtil {

    public static void setupDefaultConfigs(Context context) {
        if (!ATE.config(context, "light_theme").isConfigured(0)) {
            ATE.config(context, "light_theme")
                    .activityTheme(R.style.AppThemeLight)
                    .primaryColorRes(R.color.primary_default)
                    .accentColorRes(R.color.accent_default)
                    .coloredNavigationBar(false)
                    .commit();
        }
        if (!ATE.config(context, "dark_theme").isConfigured(0)) {
            ATE.config(context, "dark_theme")
                    .activityTheme(R.style.AppTheme)
                    .primaryColorRes(R.color.primary_default)
                    .accentColorRes(R.color.accent_default)
                    .coloredNavigationBar(true)
                    .commit();
        }
    }

    public static String resolveThemeKey(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.ate_key, typedValue, true);
        return (String) typedValue.coerceToString();
    }

    public static int resolvePrimaryColor(Context context) {
        return Config.primaryColor(context, resolveThemeKey(context));
    }

    public static int resolvePrimaryColorDark(Context context) {
        return Config.primaryColorDark(context, resolveThemeKey(context));
    }

    public static int resolveAccentColor(Context context) {
        return Config.accentColor(context, resolveThemeKey(context));
    }
}
