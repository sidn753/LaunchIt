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

package de.localtoast.launchit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arne Augenstein on 2/17/14.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "launchit.db";

    // apps table
    private static final String TABLE_APPS = "apps";

    private static final String APPS_KEY_ID = "id";
    private static final String APPS_PACKAGE_NAME = "packageName";
    private static final String APPS_LAUNCH_COUNT = "launchCount";

    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createAppTable = "create table " + TABLE_APPS + " (" + APPS_KEY_ID +
            " integer primary key autoincrement, " + APPS_PACKAGE_NAME + " text, " +
            APPS_LAUNCH_COUNT + " integer)";

        db.execSQL(createAppTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO proper update handling
    }

    public int getLaunchCounter(String appPackageName) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select " + APPS_LAUNCH_COUNT + " from " + TABLE_APPS +
            " where " + APPS_PACKAGE_NAME + " = ?", new String[]{appPackageName});

        if (cursor.moveToFirst()) {

            // TODO error handling for more than one result
            // TODO crash here
            return cursor.getInt(0);
        }

        return 0;
    }

    /**
     * @return the complete list of apps, sorted by launch count.
     */
    public List<String> getAllApps() {
        List<String> apps = new ArrayList<String>();
        String query = "select " + APPS_PACKAGE_NAME + " from " + TABLE_APPS + " order by " +
            APPS_LAUNCH_COUNT + " desc";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            apps.add(cursor.getString(0));
        }

        return apps;
    }

    public void incrementLaunchCounter(String appPackageName) {
        int launchCounter = getLaunchCounter(appPackageName);
        if (launchCounter == 0) {
            addApp(appPackageName);
        } else {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(APPS_PACKAGE_NAME, appPackageName);
            values.put(APPS_LAUNCH_COUNT, launchCounter + 1);

            db.update(TABLE_APPS, values, APPS_PACKAGE_NAME + " = ?", new String[]{appPackageName});

            db.close();
        }
    }

    private void addApp(String appPackageName) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(APPS_PACKAGE_NAME, appPackageName);
        values.put(APPS_LAUNCH_COUNT, 1);

        db.insert(TABLE_APPS, null, values);

        db.close();
    }
}
