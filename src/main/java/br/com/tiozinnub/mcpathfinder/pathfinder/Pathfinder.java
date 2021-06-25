package br.com.tiozinnub.mcpathfinder.pathfinder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class Pathfinder {
    private final World world;
    private final LivingEntity entity;
    private final BlockPos startPos;
    private final BlockPos targetPos;
    private final int maxDrop = 3;
    public List<Node> nodes;
    public List<Long> open;
    public List<Long> closed;
    private long nextNodeId;

    public Pathfinder(World world, LivingEntity entity, BlockPos startPos, BlockPos targetPos) {
        this.world = world;
        this.entity = entity;
        this.startPos = startPos;
        this.targetPos = targetPos;
        nodes = new ArrayList<>();
        open = new ArrayList<>();
        closed = new ArrayList<>();
        nextNodeId = 0;
        Node node = createNode(startPos, null);

        open.add(node.id);
    }

    public Path tick() {
        Node node;

        node = getCheaperOpenNode(targetPos);
        open.remove(node.id);
        closed.add(node.id);

        if (node.pos.equals(targetPos)) {
            return pathTo(node);
        }

        for (Node neighbor : getNeighbors(node)) {
            if (closed.contains(neighbor.id)) continue;

            if (pathCostTo(node) + neighbor.getEnterCost() < pathCostTo(neighbor) || !open.contains(neighbor.id)) {
                neighbor.parent = node;
                if (!open.contains(neighbor.id))
                    open.add(neighbor.id);
            }
        }

        return null;
    }

    private List<Node> getNeighbors(Node node) {
        BlockPos pos = node.pos;

        List<Node> neighbors = new ArrayList<>();

        Consumer<BlockPos> addNeighbor = (BlockPos neighPos) -> {
            if (neighPos == null) return;
            neighbors.add(createOrGetNode(neighPos, node));
        };

        addNeighbor.accept(getCardinalNeighbor(pos, pos.north()));
        addNeighbor.accept(getCardinalNeighbor(pos, pos.east()));
        addNeighbor.accept(getCardinalNeighbor(pos, pos.south()));
        addNeighbor.accept(getCardinalNeighbor(pos, pos.west()));

        addNeighbor.accept(getDiagonalNeighbor(pos, pos.north(), pos.east(), pos.north().east()));
        addNeighbor.accept(getDiagonalNeighbor(pos, pos.east(), pos.south(), pos.south().east()));
        addNeighbor.accept(getDiagonalNeighbor(pos, pos.south(), pos.west(), pos.south().west()));
        addNeighbor.accept(getDiagonalNeighbor(pos, pos.west(), pos.north(), pos.north().west()));

        addNeighbor.accept(getCardinalLeapNeighbor(pos, pos.north(), pos.north().north()));
        addNeighbor.accept(getCardinalLeapNeighbor(pos, pos.east(), pos.east().east()));
        addNeighbor.accept(getCardinalLeapNeighbor(pos, pos.south(), pos.south().south()));
        addNeighbor.accept(getCardinalLeapNeighbor(pos, pos.west(), pos.west().west()));

        addNeighbor.accept(getCardinalLongLeapNeighbor(pos, pos.north(), pos.north().north(), pos.north().north().north()));
        addNeighbor.accept(getCardinalLongLeapNeighbor(pos, pos.east(), pos.east().east(), pos.east().east().east()));
        addNeighbor.accept(getCardinalLongLeapNeighbor(pos, pos.south(), pos.south().south(), pos.south().south().south()));
        addNeighbor.accept(getCardinalLongLeapNeighbor(pos, pos.west(), pos.west().west(), pos.west().west().west()));

        return neighbors;
    }

    private BlockPos getCardinalNeighbor(BlockPos pos, BlockPos p) {
        if (isPassable(p)) {
            // straight or down
            if (!enoughClearance(p)) return null;  // not enough clearance to go forward at all

            // block immediately down
            if (isSolidGround(p.down())) {
                // can pass
                return p;
            }

            // check size of the drop
            var current = p.down();
            int i;
            for (i = 1; i <= maxDrop; i++) {
                current = current.down();
                if (!isSolidGround(current)) continue;

                // drop is passable
                return current.up();
            }
            // cant pass drop
        } else {
            // up?
            if (!enoughClearance(pos.up())) return null; // cant even jump here

            if (!isPassable(p.up())) return null; // 2 blocks up, cant jump

            // there's air upwards
            if (!enoughClearance(p.up()))
                return null; // theres block and air, but not enough space to stand

            return p.up();
        }

        return null;
    }

    private BlockPos getDiagonalNeighbor(BlockPos pos, BlockPos left, BlockPos right, BlockPos p) {
        if (!isPassable(left) || !isPassable(right) || !enoughClearance(p))
            return null; //cant go diagonal if these are blocked

        if (isPassable(p)) {
            // same or down

            // are cardinals ok with this?
            if (!enoughClearance(left) || !enoughClearance(right)) return null; // nope, not enough clearance

            // block immediately down
            if (isSolidGround(p.down())) {
                // can walk
                return p;
            } else {
                // down

                var currentLeft = left.down();
                var currentRight = right.down();

                if (isPassable(currentLeft) || isPassable(currentRight))
                    return null; // cant go down, these arent clear

                // check size of the drop
                var current = p.down();
                int i;
                for (i = 1; i <= maxDrop; i++) {
                    current = current.down();
                    currentLeft = currentLeft.down();
                    currentRight = currentRight.down();

                    if (!isPassable(currentLeft) || isPassable(currentRight)) return null; // wont check any further

                    if (!isSolidGround(current)) continue; // keep dropping

                    // drop is passable
                    return current.up();
                }
                // cant pass drop
            }
        } else {
            // up
            if (!isPassable(pos.up())) return null; // cant even jump here

            if (!isPassable(p.up())) return null; // 2 blocks up, cant jump

            // there's air upwards
            if (!enoughClearance(p.up())) return null; // theres block and air, but not enough space to stand

            // are cardinals ok?
            if (!enoughClearance(left.up()) || !enoughClearance(right.up())) return null; //nope

            return p.up();
        }

        return null;
    }

    private BlockPos getCardinalLeapNeighbor(BlockPos pos, BlockPos middle, BlockPos p) {
        if (!enoughClearance(pos.up())) return null; // cant even jump
        if (!enoughClearance(middle.down(), 2) || !enoughClearance(middle.up())) return null; //not enough space in the middle
        if (!isPassable(p) || !enoughClearance(p.up())) return null; // not enough clearance when landing
        if (!isSolidGround(p.down())) return null; // there's no ground to land

        return p;
    }

    private BlockPos getCardinalLongLeapNeighbor(BlockPos pos, BlockPos p1, BlockPos p2, BlockPos p) {
        if (!enoughClearance(pos.up())) return null; // cant even jump
        if (!enoughClearance(p1.down(), 2) || !enoughClearance(p1.up())) return null; //not enough space on p1
        if (!enoughClearance(p2.down()) || !enoughClearance(p2.up())) return null; //not enough space on p2
        if (!isPassable(p) || !enoughClearance(p.up())) return null; // not enough clearance when landing
        if (!isSolidGround(p.down())) return null; // there's no ground to land

        return p;
    }

    private boolean enoughClearance(BlockPos pos) {
        return enoughClearance(pos, entity.getHeight());
    }

    private boolean enoughClearance(BlockPos pos, float height) {
        BlockPos curr = pos;

        for (var i = 0; i < height; i++) {
            if (!isPassable(curr)) return false;
            curr = curr.up();
        }

        return true;
    }

    public boolean isSolidGround(BlockPos pos) {
        return world.isTopSolid(pos, entity);
    }

    private boolean isPassable(BlockPos pos) {
        BlockState state = world.getBlockState(pos);


        return state.isAir() || !state.isSolidBlock(world, pos);
    }

    private Node createOrGetNode(BlockPos pos, Node parent) {
        Node node = nodes.stream().filter(n -> n.pos.equals(pos)).findFirst().orElse(null);
        if (node != null) return node;
        return createNode(pos, parent);
    }

    private Node getCheaperOpenNode(BlockPos targetPos) {
        return nodes.stream().filter(n -> open.contains(n.id)).min(Comparator.comparingDouble(o -> (o.getCostToTarget(targetPos) + o.getEnterCost()))).orElse(null);
    }

    private double pathCostTo(Node node) {
        return node != null ? node.getEnterCost() + pathCostTo(node.parent) : 0;
    }

    private Path pathTo(Node node) {
        List<Node> nodes = new ArrayList<>();
        Node current = node;
        while (true) {
            nodes.add(0, current);
            if (current.parent == null) break;
            current = current.parent;
        }

        return new Path(nodes);
    }

    private Node createNode(BlockPos pos, Node parent) {
        Node node = new Node(nextNodeId++);
        node.pos = pos;
        node.parent = parent;
        nodes.add(node);
        return node;
    }
}
