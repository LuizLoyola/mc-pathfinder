package br.com.tiozinnub.mcpathfinder.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RobotEntityRenderer extends GeoEntityRenderer<RobotEntity> {
    public RobotEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RobotEntityModel());
        this.shadowRadius = 0.25f;
    }
}
