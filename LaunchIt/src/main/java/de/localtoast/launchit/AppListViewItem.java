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

/**
 * Created by Arne Augenstein on 2/16/14.
 */
public class AppListViewItem {
    private String appName;
    private String packageName;

    public AppListViewItem(String appName, String packageName) {
        this.appName = appName == null ? "" : appName;
        this.packageName = packageName == null ? "" : packageName;

    }

    @Override
    public String toString() {
        return appName;
    }

    @Override
    public int hashCode() {
        return appName.hashCode() + packageName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof AppListViewItem) {
            AppListViewItem other = (AppListViewItem) o;
            return other.getPackageName().equals(packageName) && other.getAppName().equals(appName);
        }
        return false;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }
}
