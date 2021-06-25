package br.com.tiozinnub.mcpathfinder;

import br.com.tiozinnub.mcpathfinder.entity.RobotEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

import static br.com.tiozinnub.mcpathfinder.McPathfinderMod.ROBOT;

public class McPathfinderModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(ROBOT, RobotEntityRenderer::new);
    }
}
