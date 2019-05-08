/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.view.Display;

import androidx.appcompat.view.menu.MenuBuilder;

import net.ddns.andrewnetwork.ludothornsoundbox.R;

import java.util.ArrayList;
import java.util.List;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.icons.SettingsIconActivity.LAUNCHER_ICON_1;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.icons.SettingsIconActivity.LAUNCHER_ICON_2;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.icons.SettingsIconActivity.LAUNCHER_ICON_3;


/**
 * Created by janisharali on 24/05/17.
 */

public final class AppUtils {

    public static final String LUDO_THORN_DOPPIAGGIO = "https://www.youtube.com/user/LudoThornDoppiaggio";
    public static final String MAIL_ANDREA = "andrea.de-matteis@outlook.com";
    public static final String MAIL_LUDO = "miticludus@gmail.com";
    public static final String MAIL_SEGNALAZIONI = "chemerdo@gmail.com";
    public static final String PATREON_LINK = "https://www.patreon.com/LudoThorn";
    public static final String MERCHANDISE_LINK = "https://www.moteefe.com/store/ludothorn";
    public static int DAYS_BEFORE_ASKING_FEEDBACK = 10;
    public static int DAYS_LATER_ASKING_FEEDBACK = 5;
    public static String LINK_ASKING_FEEDBACK = "https://play.google.com/store/apps/details?id=net.ddns.andrewnetwork.ludothornsoundbox&hl=it";

    private static final String ALIAS_1 = "MainActivity1";
    private static final String ALIAS_2 = "MainActivity2";
    private static final String ALIAS_3 = "MainActivity3";

    private AppUtils() {
        // This class is not publicly instantiable
    }

    public static void openPlayStoreForApp(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_market_link) + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_google_play_store_link) + appPackageName)));
        }
    }

    public static boolean isTablet(Context context) {
        if(context instanceof Activity) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            return width > 1500;
        }

        return false;
    }

    public static void changeIcon(Context context, int position) {
        deleteIcon(context, position);
        putIcon(context, position);
    }

    private static void deleteIcon(Context context, int position) {
        String packageName = context.getPackageName();
        List<String> otherIcons = new ArrayList<>();
        switch (position) {
            default:
            case LAUNCHER_ICON_1:
                otherIcons.add(ALIAS_2);
                otherIcons.add(ALIAS_3);
                break;
            case LAUNCHER_ICON_2:
                otherIcons.add(ALIAS_1);
                otherIcons.add(ALIAS_3);
                break;
            case LAUNCHER_ICON_3:
                otherIcons.add(ALIAS_1);
                otherIcons.add(ALIAS_2);
                break;
        }

        for (String className : otherIcons) {
            className = packageName + "." + className;
            context.getPackageManager().setComponentEnabledSetting(
                    new ComponentName(packageName, className),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }

    private static void putIcon(Context context, int position) {
        String packageName = context.getPackageName();
        String className = packageName + ".";
        switch (position) {
            default:
            case LAUNCHER_ICON_1:
                className += ALIAS_1;
                break;
            case LAUNCHER_ICON_2:
                className += ALIAS_2;
                break;
            case LAUNCHER_ICON_3:
                className += ALIAS_3;
                break;
        }
        context.getPackageManager().setComponentEnabledSetting(
                new ComponentName(packageName, className),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public static boolean canWriteSettings(Context context) {
        boolean settingsCanWrite = true;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            settingsCanWrite = Settings.System.canWrite(context);

            if (!settingsCanWrite) {
                CommonUtils.showDialog(context, context.getString(R.string.necessary_write_settings_permission), (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    context.startActivity(intent);
                    dialog.dismiss();
                }, true);
            }
        }
        return settingsCanWrite;
    }
}
