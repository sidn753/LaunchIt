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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.localtoast.launchit.R;

/**
 * Created by Arne Augenstein on 2/18/14.
 */
public class AppsArrayAdapter extends ArrayAdapter<AppListViewItem> {
    private final Context context;

    public AppsArrayAdapter(Context context, List<AppListViewItem> items) {
        super(context, R.layout.app_list_view_item, items.toArray(new AppListViewItem[]{}));
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.app_list_view_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.appListLabel);
        ImageView logo = (ImageView) row.findViewById(R.id.appListLogo);

        AppListViewItem item = getItem(position);

        label.setText(item.getAppName());
        logo.setImageDrawable(item.getAppIcon());

        return row;
    }
}
