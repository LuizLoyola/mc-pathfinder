package br.com.tiozinnub.mcpathfinder.entity;

import br.com.tiozinnub.mcpathfinder.pathfinder.Node;
import br.com.tiozinnub.mcpathfinder.pathfinder.NodeType;
import br.com.tiozinnub.mcpathfinder.pathfinder.Path;
import br.com.tiozinnub.mcpathfinder.pathfinder.Pathfinder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RobotEntity extends PathingEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private Pathfinder pathfinder;
    public Path currentPath;
    public int currentPathStep = 0;

    public RobotEntity(EntityType<? extends PathingEntity> entityType, World world) {
        super(entityType, world);
        this.setMovementSpeed(0.1f);

        setNoGravity(true);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    protected PlayState predicate(AnimationEvent<RobotEntity> event) {
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public float getMovementSpeed() {
        return 0.4f;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.world.isClient && hand == Hand.MAIN_HAND) {
            BlockPos target = new BlockPos(294, 75, -57);

            setTargetPos(target);
        }

        return super.interactMob(player, hand);
    }

    private void navigatePath() {
        Node node = currentPath.nodes.get(currentPathStep);

        NodeType type = node.getType();

        if (type == NodeType.CARDINAL_WALK) {
        } else if (type == NodeType.CARDINAL_JUMP) {
        } else if (type == NodeType.CARDINAL_DROP_1) {
        } else if (type == NodeType.CARDINAL_DROP_2) {
        } else if (type == NodeType.CARDINAL_DROP_3) {
        } else if (type == NodeType.DIAGONAL_WALK) {
        } else if (type == NodeType.DIAGONAL_JUMP) {
        } else if (type == NodeType.DIAGONAL_DROP_1) {
        } else if (type == NodeType.DIAGONAL_DROP_2) {
        } else if (type == NodeType.DIAGONAL_DROP_3) {
        } else if (type == NodeType.CARDINAL_LEAP) {
        } else if (type == NodeType.CARDINAL_LEAP_LONG) {
        } else if (type == NodeType.NONE) {
        }

        currentPathStep++;
    }

    @Override
    public void mobTick() {
//        if (this.world.isClient()) return;
//
//        if (currentPath == null) return;
//        boolean finishedStep = getBlockPos().equals(currentPath.nodes.get(currentPathStep).pos);
//        if (finishedStep)
//            currentPathStep++;
//
//        if (currentPath.nodes.size() <= currentPathStep) {
//            currentPath = null;
//            return;
//        }
//
//        Node node = currentPath.nodes.get(currentPathStep);
//        BlockPos diff = getBlockPos().subtract(node.pos);
//        this.travel(new Vec3d(diff.getX(), diff.getY(), diff.getZ()));
    }
}
