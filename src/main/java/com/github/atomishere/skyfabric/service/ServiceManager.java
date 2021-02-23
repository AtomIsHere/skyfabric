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
import com.github.atomishere.skyfabric.exceptions.ServiceAlreadyRegisteredException;
import com.github.atomishere.skyfabric.exceptions.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class ServiceManager {
    private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());

    private final SkyFabric mod;
    private final List<IService> services = new ArrayList<>();

    private boolean started = false;

    public void startServices() {
        if(started) {
            logger.warning("Attempted to start services while they where already started!");
            return;
        }

        services.forEach(s -> {
            logger.info("Starting service: " + s.getName());
            s.start();
        });
        started = true;
    }

    public void stopServices() {
        if(!started) {
            logger.warning("Attempted to stop services while they where already stopped!");
            return;
        }

        Collections.reverse(services);
        services.forEach(s -> {
            logger.info("Stopping service: " + s.getName());
            s.stop();
        });
        started = false;
    }

    public void clearServices() {
        services.clear();
    }

    public void registerService(IService service) {
        if(services.stream().anyMatch(s -> s.getName().equals(service.getName()))) {
            throw new ServiceAlreadyRegisteredException(service.getName());
        }

        services.add(service);
    }

    public <S extends AbstractService> S registerService(Class<S> serviceClass) {
        Constructor<S> con;
        try {
            con = serviceClass.getConstructor(SkyFabric.class);
        } catch(NoSuchMethodException nsme) {
            nsme.printStackTrace();
            return null;
        }

        S inst;
        try {
            inst = con.newInstance(mod);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        registerService(inst);
        return inst;
    }

    @SuppressWarnings("unchecked")
    public <S extends IService> S getServiceFromClass(Class<S> serviceClass) {
        return (S) services.stream()
                .filter(serviceClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new ServiceNotFoundException(serviceClass));
    }

    public IService getServiceFromName(String name) {
        return services.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ServiceNotFoundException(name));
    }
}
