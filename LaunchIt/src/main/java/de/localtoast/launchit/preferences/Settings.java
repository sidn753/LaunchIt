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

package de.localtoast.launchit.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.localtoast.launchit.R;

/**
 * Created by Arne Augenstein on 4/10/14.
 */
public class Settings {
    SharedPreferences settings;
    private Context context;

    public Settings(Context context) {
        this.context = context;
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void registerPreferenceListener(
        SharedPreferences.OnSharedPreferenceChangeListener listener) {
        settings.registerOnSharedPreferenceChangeListener(listener);
    }

    public boolean isTouchAreaPositionLeftEdge() {
        return settings
            .getBoolean(context.getString(R.string.prefKey_touchAreaHorizontalPosition), false);
    }

    public int getTouchAreaHorizontalPosition() {
        return Integer.valueOf(
            settings.getString(context.getString(R.string.prefKey_touchAreaVerticalPosition), "0"));
    }

    public int getTouchAreaHeight() {
        return Integer.valueOf(
            settings.getString(context.getString(R.string.prefKey_touchAreaHeight), "100"));
    }

    public int getTouchAreaWidth() {
        return Integer
            .valueOf(settings.getString(context.getString(R.string.prefKey_touchAreaWidth), "100"));
    }

}
