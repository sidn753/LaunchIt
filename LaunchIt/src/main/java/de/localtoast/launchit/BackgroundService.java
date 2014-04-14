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
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import de.localtoast.launchit.applistview.AppListView;
import de.localtoast.launchit.db.SQLiteHelper;
import de.localtoast.launchit.preferences.Settings;

/**
 * Created by Arne Augenstein on 2/15/14.
 */
public class BackgroundService extends Service {

    private static final int FADING_DURATION_MS = 280;

    private Timer timer = new Timer();
    private AppListUpdater appListUpdater;

    private LinearLayout touchArea;
    private FrameLayout sidebar;

    boolean sidebarVisible = false;

    int touchAreaColor = 0x00000000;
    int receiveTouchEvents = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

    private AppListView appListView;

    private final BackgroundServiceInterface.Stub binder = new BackgroundServiceInterface.Stub() {
        // TODO make normal class out of this inner class

        @Override
        public void makeTouchAreaInvisible() throws RemoteException {
            touchAreaColor = 0x00000000;
            receiveTouchEvents = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            refreshTouchArea();
        }

        @Override
        public void makeTouchAreaVisible() throws RemoteException {
            touchAreaColor = 0x992DE397;
            receiveTouchEvents = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            refreshTouchArea();
        }

        @Override
        public void resizeTouchArea() throws RemoteException {
            refreshTouchArea();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appListUpdater = new AppListUpdater();
        timer.scheduleAtFixedRate(appListUpdater, 0, 60000);

        initSidebar();
        initTouchArea();
        WindowManager.LayoutParams params = getTouchAreaLayoutParams();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(touchArea, params);
    }

    public void switchToTouchArea() {
        /*
         * Only enter on first run (touchArea == null) or when the sidebar is visible.
         */
        if (touchArea == null || sidebarVisible) {
            sidebarVisible = false;
            if (appListView != null) {
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
                        switchToTouchAreaPostAnimation();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // do nothing
                    }
                });
                appListView.startAnimation(animation);
                appListView.setVisibility(ListView.GONE);
            } else {
                switchToTouchAreaPostAnimation();
            }
        }
    }

    private void switchToTouchAreaPostAnimation() {
        WindowManager.LayoutParams params = getTouchAreaLayoutParams();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        touchArea.setBackgroundColor(touchAreaColor);
        removeView();
        wm.addView(touchArea, params);
    }

    private void removeView() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        try {
            wm.removeView(sidebar);
        } catch (IllegalArgumentException e1) {
            try {
                wm.removeView(touchArea);
            } catch (IllegalArgumentException e2) {
                // if now view was attached, we just do nothing
            }
        }
    }

    private void refreshTouchArea() {
        WindowManager.LayoutParams params = getTouchAreaLayoutParams();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        touchArea.setBackgroundColor(touchAreaColor);
        removeView();
        wm.addView(touchArea, params);
    }

    private WindowManager.LayoutParams getTouchAreaLayoutParams() {
        Settings settings = new Settings(getBaseContext());
        WindowManager.LayoutParams params =
            new WindowManager.LayoutParams(settings.getTouchAreaWidth(),
                settings.getTouchAreaHeight(), WindowManager.LayoutParams.TYPE_PHONE,
                receiveTouchEvents | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        int horizontalPos = Gravity.RIGHT;
        if (settings.isTouchAreaPositionLeftEdge()) {
            horizontalPos = Gravity.LEFT;
        }
        params.gravity = Gravity.TOP | horizontalPos;
        params.y = settings.getTouchAreaHorizontalPosition();
        return params;
    }

    private void initTouchArea() {
        if (touchArea == null) {

            touchArea = new LinearLayout(this);
            touchArea.setBackgroundColor(touchAreaColor);
            touchArea.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switchToSidebar();
                    return true;
                }
            });
        }
    }

    private void switchToSidebar() {
        if (!sidebarVisible) {
            sidebarVisible = true;

            Context context = getBaseContext();

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(225, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT
                );
            params.gravity = Gravity.TOP | Gravity.RIGHT;

            removeView();
            wm.addView(sidebar, params);
            // TODO move this animation stuff in some sort of gui class or directly to the view
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            animation.setDuration(FADING_DURATION_MS);
            appListView.startAnimation(animation);
            appListView.setVisibility(View.VISIBLE);
        }
    }

    private void initSidebar() {
        if (sidebar == null) {
            Context context = getBaseContext();
            sidebar = new FrameLayout(context);
            appListView = new AppListView(this);
            sidebar.addView(appListView);
            sidebar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    /*
                     * Close the app list, when element outside of list is touched.
                     */
                    if (sidebarVisible) {
                        switchToTouchArea();
                    }
                    return false;
                }
            });
        }
    }

    public void startApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

        if (intent == null) {
            Toast toast = Toast.makeText(getBaseContext(),
                "Launch it! does not have the permission to launch this app", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            new SQLiteHelper(this).incrementLaunchCounter(packageName);
            appListUpdater.addNewRunningApp(packageName);
            appListView.update();
            switchToTouchArea();
        }
    }

    @Override
    public void onDestroy() {
        removeView();
        super.onDestroy();
    }

    private class AppListUpdater extends TimerTask {
        private AppListHelper appListHelper;

        public AppListUpdater() {
            appListHelper = new AppListHelper(BackgroundService.this);
        }

        @Override
        public void run() {
            appListHelper.updateRunningTasks();
            if (appListView != null) {
                // The following action has to be run from the GUI thread
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        appListView.update();
                    }
                });
            }
        }

        public void addNewRunningApp(String packageName) {
            appListHelper.addNewRunningApp(packageName);
        }
    }

}
