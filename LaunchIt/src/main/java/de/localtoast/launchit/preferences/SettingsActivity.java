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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import de.localtoast.launchit.BackgroundService;
import de.localtoast.launchit.BackgroundServiceInterface;
import de.localtoast.launchit.R;

public class SettingsActivity extends Activity
     {
    BackgroundServiceInterface serviceInterface;

    SharedPreferences.OnSharedPreferenceChangeListener preferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            s = "test";
//            if (key.equals(getString(R.string.prefKey_touchAreaHeight)) ||
//                key.equals(getString(R.string.prefKey_touchAreaWidth)) ||
//                key.equals(getString(R.string.prefKey_touchAreaVerticalPosition)) ||
//                key.equals(getString(R.string.prefKey_touchAreaHorizontalPosition))) {
//
//                if (serviceInterface != null) {
//                    try {
//                        serviceInterface.resizeTouchArea();
//                    } catch (RemoteException e) {
//                        // ignore this error
//                    }
//                }
//            }
        }
    };


    protected ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            serviceInterface = BackgroundServiceInterface.Stub.asInterface(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceInterface = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
            .replace(android.R.id.content, new SettingsFragment()).commit();

        bindService(new Intent(this, BackgroundService.class), serviceConnection,
            Context.BIND_AUTO_CREATE);

        if (serviceInterface != null) {
            try {
                serviceInterface.makeTouchAreaVisible();
            } catch (RemoteException e) {
                // ignore this error
            }
        }
    }



    @Override
    protected void onDestroy() {
        if (serviceInterface != null)
        {
            try {
                serviceInterface.makeTouchAreaInvisible();
            } catch (RemoteException e) {
                // ignore this exception
            }
        }
    }

}
