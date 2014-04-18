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

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Created by Arne Augenstein on 2/24/14.
 */
public class AppMetaData implements Comparable<AppMetaData> {
    private String packageName;
    private DateTime lastStarted;
    private int priorityCounter;

    private static final Days PRIORITY_DECREASING_DELAY = Days.days(5);

    public AppMetaData(String packageName, DateTime lastStarted, int priorityCounter) {
        this.packageName = packageName;
        this.lastStarted = lastStarted;
        this.priorityCounter = priorityCounter;
    }

    @Override
    public int compareTo(AppMetaData another) {
        return getEffectivePriority() - another.getEffectivePriority();
    }

    public int getEffectivePriority() {
        int effectivePriority = priorityCounter;

        Days daysBetween = Days.daysBetween(lastStarted, DateTime.now());
        if (daysBetween.isGreaterThan(PRIORITY_DECREASING_DELAY)) {
            effectivePriority =
                effectivePriority - (daysBetween.getDays() - PRIORITY_DECREASING_DELAY.getDays());
            if (effectivePriority < 0) {
                effectivePriority = 0;
            }
        }

        return effectivePriority;
    }

    public String getPackageName() {
        return packageName;
    }
}
