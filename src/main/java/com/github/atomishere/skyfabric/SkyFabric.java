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

package com.github.atomishere.skyfabric;

import com.github.atomishere.skyfabric.error.ErrorHandler;
import com.github.atomishere.skyfabric.service.ServiceManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class SkyFabric implements ModInitializer {
    private ErrorHandler errorHandler;

    private ServiceManager serviceManager;

    @Override
    public void onInitialize() {
        errorHandler = new ErrorHandler();
        errorHandler.start();

        ServerLifecycleEvents.SERVER_STARTING.register(this::onStart);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onStop);
    }

    public void onStart(MinecraftServer server) {
        serviceManager = new ServiceManager(this);
        // Register services
    }

    public void onStop(MinecraftServer server) {
        serviceManager.stopServices();
    }
}
