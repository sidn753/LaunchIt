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

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import de.localtoast.launchit.BackgroundService;

/**
 * Created by Arne Augenstein on 2/16/14.
 */
public class AppListView extends ListView {

    private BackgroundService service;
    private final AppsArrayAdapter adapter;

    public AppListView(final BackgroundService service) {
        super(service);
        this.service = service;
        setBackgroundColor(0xDD000A30);

        adapter = new AppsArrayAdapter(service);
        setAdapter(adapter);

        setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                AppListViewItem item = (AppListViewItem) parent.getItemAtPosition(position);
                service.startApp(item.getPackageName());
            }

        });
    }

    public void update() {
        adapter.update();
    }
}
