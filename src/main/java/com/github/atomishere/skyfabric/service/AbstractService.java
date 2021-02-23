/*
 *     Copyright (C) 2021  Archie O'Connor
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.github.atomishere.skyfabric.service;

import com.github.atomishere.skyfabric.SkyFabric;
import com.github.atomishere.skyfabric.exceptions.ServiceStateConflictException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractService implements IService {
    protected final SkyFabric mod;

    private boolean started = false;

    @Override
    public void start() {
        if(started) {
            throw new ServiceStateConflictException(getName(), ServiceStateConflictException.State.STARTED);
        }

        // TODO: Config System

        onStart();
        started = true;
    }

    @Override
    public void stop() {
        if(!started) {
            throw new ServiceStateConflictException(getName(), ServiceStateConflictException.State.STOPPED);
        }

        onStop();
        // TODO: Config System
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    public abstract void onStart();
    public abstract void onStop();
}
