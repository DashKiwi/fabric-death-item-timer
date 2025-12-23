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
        // Check if the entity is a player
        if (entity instanceof ServerPlayer player) {
            // Player died - tag all items near death location immediately
            if (server != null) {
                // Store death position
                double x = player.getX();
                double y = player.getY();
                double z = player.getZ();
                
                // Tag items at death location (much larger radius to be safe)
                server.getCommands().performPrefixedCommand(
                    player.createCommandSourceStack(),
                    String.format("execute positioned %.2f %.2f %.2f run tag @e[type=item,distance=..2] add death_item", x, y, z)
                );
            }
        }
    }
    
    private void onServerStarted(MinecraftServer server) {
        this.server = server;
        LOGGER.info("Server started, datapack functions should be active");
    }
}