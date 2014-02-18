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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ListView;

import de.localtoast.launchit.applistview.AppListView;
import de.localtoast.launchit.db.SQLiteHelper;

/**
 * Created by Arne Augenstein on 2/15/14.
 */
public class AppListService extends Service {
    private FrameLayout parentLayout;

    private static final int FADING_DURATION_MS = 280;
    private AppListView listView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = getBaseContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params =
            new WindowManager.LayoutParams(225, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.RIGHT;

        parentLayout = new FrameLayout(context);
        wm.addView(parentLayout, params);
        listView = new AppListView(this);
        parentLayout.addView(listView);
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*
                 * Close the app list, when element outside of list is touched.
                 */
                closeAppList();
                return false;
            }
        });

        // TODO move this animation stuff in some sort of gui class or directly to the view
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        animation.setDuration(FADING_DURATION_MS);
        listView.startAnimation(animation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (parentLayout != null) {
            WindowManager wm =
                (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(parentLayout);
        }
    }

    public void startApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        new SQLiteHelper(this).incrementLaunchCounter(packageName);
        closeAppList();
    }

    public void closeAppList() {
        if (listView != null) {
            Animation animation =
                AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_out);
            animation.setDuration(FADING_DURATION_MS);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // do nothing
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // list fade out complete
                    stopSelf();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // do nothing
                }
            });
            listView.startAnimation(animation);
            listView.setVisibility(ListView.GONE);
        }
    }
}
