package br.com.tiozinnub.mcpathfinder.entity;

import br.com.tiozinnub.mcpathfinder.pathfinder.Node;
import br.com.tiozinnub.mcpathfinder.pathfinder.NodeType;
import br.com.tiozinnub.mcpathfinder.pathfinder.Path;
import br.com.tiozinnub.mcpathfinder.pathfinder.Pathfinder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class PathingEntity extends MobEntity {
    private final Pathfinder pathfinder;
    private Path currentPath;
    private int currentNodeIndex;
    private int currentNodeTime;
    private BlockPos currentTarget;
    private int waitTicks;

    protected PathingEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        pathfinder = new Pathfinder(world, this);
    }

    private static Vec3d getMiddle(BlockPos pos) {
        return new Vec3d(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
    }

    protected void setTargetPos(BlockPos pos) {
        try {
            currentPath = pathfinder.findPath(getBlockPos(), pos, 10000);
        } catch (Exception ex) {
            // failed to find path
            return;
        }

        currentTarget = pos;
        currentNodeIndex = 1;
        currentNodeTime = 0;
    }

    private Node getNode() {
        return currentPath.nodes.get(currentNodeIndex);
    }

    private Node getNextNode() {
        return currentNodeIndex + 1 < currentPath.nodes.size() ? currentPath.nodes.get(currentNodeIndex + 1) : null;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        currentNodeTime++;
        if (currentPath == null) return;

        Node node = getNode();
        Vec3d target = getMiddle(node.pos);

        if (node.getType().canAnticipate()) {
            Node next = getNextNode();
            if (next != null) {
                target = getMidpoint(target, getMiddle(next.pos));
            }
        }

        double speed = 0.35d;
        double margin = node.getType().canAnticipate() ? 0.75d * (1 + speed / 2) : 0.25d;

        double distanceToTarget = target.distanceTo(getPos());

        if (currentNodeIndex == currentPath.nodes.size() - 1) {
            margin = 0.1d;
            speed = speed * MathHelper.clamp(distanceToTarget, 0.1d, 1d);
        }


        if (getNextNode() != null && getNextNode().getType() == NodeType.CARDINAL_LEAP) {
            margin = 0.1d;

            target = target.add(getMiddle(getNextNode().pos).relativize(target).negate().multiply(0.5));
        }

        NodeType type = node.getType();
        if (type == NodeType.CARDINAL_WALK) {
            this.walkTo(target, speed);
        } else if (type == NodeType.CARDINAL_JUMP) {
            this.walkTo(target, speed);
            if (this.isOnGround() && this.getPos().y < target.y) this.jump();
        } else if (type == NodeType.CARDINAL_DROP_1) {
            this.walkTo(target, speed * 0.75d);
        } else if (type == NodeType.CARDINAL_DROP_2) {
            this.walkTo(target, speed * 0.75d);
        } else if (type == NodeType.CARDINAL_DROP_3) {
            this.walkTo(target, speed * 0.5d);
        } else if (type == NodeType.DIAGONAL_WALK) {
            this.walkTo(target, speed);
        } else if (type == NodeType.DIAGONAL_JUMP) {
            this.walkTo(target, speed);
            if (this.isOnGround() && this.getPos().y < target.y) this.jump();
        } else if (type == NodeType.DIAGONAL_DROP_1) {
            this.walkTo(target, speed * 0.75d);
        } else if (type == NodeType.DIAGONAL_DROP_2) {
            this.walkTo(target, speed * 0.75d);
        } else if (type == NodeType.DIAGONAL_DROP_3) {
            this.walkTo(target, speed * 0.5d);
        } else if (type == NodeType.CARDINAL_LEAP) {
            this.walkTo(target, this.isOnGround() ? speed * 0.4 : speed);
            if (this.isOnGround() && this.getPos().y < target.y) this.jump();
        } else if (type == NodeType.CARDINAL_LEAP_LONG) {
            this.walkTo(target, speed);
            if (this.isOnGround() && this.getPos().y < target.y) this.jump();
        } else if (type == NodeType.NONE) {
        }

        if (distanceToTarget < margin && isOnGround()) {
            currentNodeIndex++;
            currentNodeTime = 0;

            if (currentNodeIndex >= currentPath.nodes.size())
                currentPath = null;
        }

        if (currentNodeTime > 40) {
            setTargetPos(currentTarget);
        }
    }

    private void walkTo(Vec3d pos, double speed) {
        this.getMoveControl().moveTo(pos.getX(), pos.getY(), pos.getZ(), speed);
    }

    private Vec3d getMidpoint(Vec3d p1, Vec3d p2) {
        return p1.add(p2).multiply(0.5d);
    }
}
