package br.com.tiozinnub.mcpathfinder;

import br.com.tiozinnub.mcpathfinder.entity.RobotEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

public class McPathfinderMod implements ModInitializer {
	public static final EntityType<RobotEntity>  ROBOT = Registry.register(Registry.ENTITY_TYPE, new Identifier("mcpathfinder", "robot"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, RobotEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

	@Override
	public void onInitialize() {
		GeckoLib.initialize();
		GeckoLibMod.DISABLE_IN_DEV = true;

		FabricDefaultAttributeRegistry.register(ROBOT, RobotEntity.createMobAttributes());
	}
}
