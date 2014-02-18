/*
 * This file is part of Launch It!.
 * Copyright (c) 2014.
 *
 * Launch It! is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Launch It! is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Launch It!.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.localtoast.launchit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;

import de.localtoast.launchit.db.SQLiteHelper;

/**
 * Created by Arne Augenstein on 2/15/14.
 */
public class AppListService extends Service {
    private AppListView listView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WindowManager wm =
            (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params =
            new WindowManager.LayoutParams(225, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        listView = new AppListView(this);
        wm.addView(listView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (listView != null) {
            WindowManager wm =
                (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(listView);
        }
    }

    public void startApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        new SQLiteHelper(this).incrementLaunchCounter(packageName);
        stopSelf();
    }
}
