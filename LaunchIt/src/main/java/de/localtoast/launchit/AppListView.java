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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arne Augenstein on 2/16/14.
 */
public class AppListView extends ListView {

    private AppListService appListService;

    public AppListView(final AppListService appListService) {
        super(appListService);
        this.appListService = appListService;
        setBackgroundColor(0xEE043863);

        ActivityManager actManager =
            (ActivityManager) appListService.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> runningTasks = actManager.getRunningTasks(50);

        PackageManager packageManager = appListService.getPackageManager();

        final ArrayList<AppListViewItem> list = new ArrayList<AppListViewItem>();
        for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
            try {
                String packageName = taskInfo.topActivity.getPackageName();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                list.add(new AppListViewItem(
                    packageManager.getApplicationLabel(applicationInfo).toString(), packageName));
            } catch (PackageManager.NameNotFoundException e) {
                // ignore the app
            }
        }

        final AppArrayArrayAdapter adapter =
            new AppArrayArrayAdapter(appListService, android.R.layout.simple_list_item_1, list);
        setAdapter(adapter);

        setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                AppListViewItem item = (AppListViewItem) parent.getItemAtPosition(position);
                appListService.startApp(item.getPackageName());
            }

        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float rawx = ev.getRawX();
        if (rawx == 0) {
            appListService.stopSelf();
        }

        return super.dispatchTouchEvent(ev);
    }

    private class AppArrayArrayAdapter extends ArrayAdapter<AppListViewItem> {

        HashMap<AppListViewItem, Integer> mIdMap = new HashMap<AppListViewItem, Integer>();

        public AppArrayArrayAdapter(Context context, int textViewResourceId,
                                    List<AppListViewItem> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            AppListViewItem item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
