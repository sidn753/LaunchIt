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

import java.util.Date;

/**
 * Created by Arne Augenstein on 2/24/14.
 */
public class AppMetaData {
    private String packageName;
    private Date lastStarted;
    private int priorityCounter;

    public AppMetaData(String packageName, Date lastStarted, int priorityCounter) {
        this.packageName = packageName;
        this.lastStarted = lastStarted;
        this.priorityCounter = priorityCounter;
    }

}
