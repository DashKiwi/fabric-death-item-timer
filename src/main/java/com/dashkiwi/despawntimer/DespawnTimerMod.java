package com.dashkiwi.despawntimer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class DespawnTimerMod implements ModInitializer {
    public static final String MOD_ID = "despawntimer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private MinecraftServer server;

    @Override
    public void onInitialize() {
        LOGGER.info("Despawn Timer Mod initialized!");
        
        // Store server reference
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        
        // Register death event - use ServerLivingEntityEvents instead
        ServerLivingEntityEvents.AFTER_DEATH.register(this::onEntityDeath);
    }

    private void onEntityDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity instanceof ServerPlayer player) {
            if (server != null) {
                String fullKey = player.level().dimension().toString();
                String dimensionId = fullKey.contains("/") ? 
                    fullKey.substring(fullKey.lastIndexOf("/") + 1).trim().replace("]", "") : 
                    "minecraft:overworld";
                
                String command = String.format(
                    "execute in %s run tag @e[type=item,x=%.2f,y=%.2f,z=%.2f,distance=..3] add death_item",
                    dimensionId,
                    player.getX(),
                    player.getY(),
                    player.getZ()
                );
                
                server.getCommands().performPrefixedCommand(
                    server.createCommandSourceStack(),
                    command
                );
                
                LOGGER.info("Dimension: " + dimensionId + " | Command: " + command);
            }
        }
    }
    private void onServerStarted(MinecraftServer server) {
        this.server = server;
        LOGGER.info("Server started, datapack functions should be active");
    }
}