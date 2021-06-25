package br.com.tiozinnub.mcpathfinder.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RobotEntityModel extends AnimatedGeoModel<RobotEntity> {
    @Override
    public Identifier getModelLocation(RobotEntity object) {
        return new Identifier("mcpathfinder", "geo/entity/robot/robot.geo.json");
    }

    @Override
    public Identifier getTextureLocation(RobotEntity object) {
        return new Identifier("mcpathfinder", "textures/entity/robot/robot.png");
    }

    @Override
    public Identifier getAnimationFileLocation(RobotEntity animatable) {
        return new Identifier("mcpathfinder", "animations/entity/robot/robot.animation.json");
    }
}
