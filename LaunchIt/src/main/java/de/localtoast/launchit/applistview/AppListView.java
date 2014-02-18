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

package de.localtoast.launchit.applistview;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.localtoast.launchit.AppListHelper;
import de.localtoast.launchit.AppListService;

/**
 * Created by Arne Augenstein on 2/16/14.
 */
public class AppListView extends ListView {

    private AppListService appListService;

    public AppListView(final AppListService appListService) {
        super(appListService);
        this.appListService = appListService;
        setBackgroundColor(0xDD000A30);

        final ArrayList<AppListViewItem> list = AppListHelper.getAppList(appListService);
        final AppsArrayAdapter adapter = new AppsArrayAdapter(appListService, list);
        setAdapter(adapter);

        setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                AppListViewItem item = (AppListViewItem) parent.getItemAtPosition(position);
                appListService.startApp(item.getPackageName());
            }

        });
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
