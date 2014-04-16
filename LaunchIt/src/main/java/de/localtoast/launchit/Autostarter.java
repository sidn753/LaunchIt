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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.localtoast.launchit.preferences.Settings;

/**
 * Created by Arne Augenstein on 4/16/14.
 */
public class Autostarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Settings settings = new Settings(context);
        if (settings.isAutostartSwitchedOn()) {
            Intent startServiceIntent = new Intent(context, BackgroundService.class);
            context.startService(startServiceIntent);
        }
    }
}
