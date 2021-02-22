package com.github.atomishere.skyfabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class SkyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onStop);
        
    }

    public void onStop(MinecraftServer server) {

    }
}
