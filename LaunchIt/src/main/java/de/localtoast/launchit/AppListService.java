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
import android.view.WindowManager;

/**
 * Created by Arne Augenstein on 2/15/14.
 */
public class AppListService extends Service {

    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        WindowManager.LayoutParams params =
            new WindowManager.LayoutParams(600, 600, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        WindowManager wm =
            (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        wm.addView(new AppListView(this), params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO brauche ich das noch?
        //        if (view != null) {
        //            view.destroy();
        //        }
    }
}