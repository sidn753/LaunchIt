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

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.localtoast.launchit.applistview.AppListViewItem;
import de.localtoast.launchit.db.SQLiteHelper;

/**
 * Created by Arne Augenstein on 2/17/14.
 */
public class AppListHelper {
    protected ActivityManager actManager;

    private static final PRIORITY_DECREASING_DELAY=5;

    private Set<String> recentlyRunningTasks = new HashSet<String>();
    private Context context;
    private static Set<String> appBlacklist =
        new HashSet(Arrays.asList(new String[]{"com.android.systemui"}));

    public AppListHelper(Context context) {
        this.context = context;
        actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void updateRunningTasks() {
        List<ActivityManager.RunningTaskInfo> runningTasks = actManager.getRunningTasks(50);

        Set<String> currentTasks = new HashSet<String>();

        for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
            String packageName = taskInfo.topActivity.getPackageName();

            if (!appBlacklist.contains(packageName)) {
                currentTasks.add(packageName);
            }
        }

        SQLiteHelper dbHelper = new SQLiteHelper(context);
        Set<String> newTasks = new HashSet<String>(currentTasks);

        if (!recentlyRunningTasks.isEmpty()) {
            newTasks.removeAll(recentlyRunningTasks);
        } else {
            /**
             * Launch it! has just recently been started and we don't have a list of the currently
             * running apps yet. So let's get the apps, we already know of from the database and
             * compare them with the running ones.
             */
            newTasks.removeAll(dbHelper.getAllApps());
        }

        for (String newTask : newTasks) {
            dbHelper.incrementLaunchCounter(newTask);
        }

        recentlyRunningTasks = new HashSet<String>(currentTasks);
    }

    // TODO leave it as static? move this method to other class?
    public static ArrayList<AppMetaData> getAppList(Context context) {
        final ArrayList<AppMetaData> list = new ArrayList<AppMetaData>();

        List<AppMetaData> apps = new SQLiteHelper(context).getAllApps();
        PackageManager packageManager = context.getPackageManager();
        for (AppMetaData app : apps) {
            try {
                // TODO sort with decreasing level in mind
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                list.add(new AppListViewItem(
                    packageManager.getApplicationLabel(applicationInfo).toString(), packageName,
                    packageManager.getApplicationIcon(packageName)));
            } catch (PackageManager.NameNotFoundException e) {
                // ignore the app
            }
        }
        return list;
    }

    public void addNewRunningApp(String packageName) {
        recentlyRunningTasks.add(packageName);
    }
}
