package br.com.tiozinnub.mcpathfinder.pathfinder;

import net.minecraft.util.math.BlockPos;

import static br.com.tiozinnub.mcpathfinder.pathfinder.NodeType.*;

public class Node {
    public final Long id;
    public BlockPos pos;
    public Node parent;

    public Node(long id) {
        this.id = id;
    }

    public static double getCostForType(NodeType type) {
        if (type == CARDINAL_WALK) return 1d;
        if (type == CARDINAL_JUMP) return 1.1d;
        if (type == CARDINAL_DROP_1) return 0.9d;
        if (type == CARDINAL_DROP_2) return 0.9d;
        if (type == CARDINAL_DROP_3) return 1.3d;
        if (type == DIAGONAL_WALK) return Math.sqrt(1d * 1d * 2d);
        if (type == DIAGONAL_JUMP) return Math.sqrt(1.1d * 1.1d * 2d);
        if (type == DIAGONAL_DROP_1) return Math.sqrt(0.9d * 0.9d * 2d);
        if (type == DIAGONAL_DROP_2) return Math.sqrt(0.9d * 0.9d * 2d);
        if (type == DIAGONAL_DROP_3) return Math.sqrt(1.3d * 1.3d * 2d);
        if (type == CARDINAL_LEAP) return 0.8d * 2d;
        if (type == CARDINAL_LEAP_LONG) return 0.7d * 3d;

        return 0d;
    }

    public double getEnterCost() {
        return getCostForType(getType());
    }

    public NodeType getType() {
        if (parent == null) return NONE;
        return getNodeType(parent.pos);
    }

    private NodeType getNodeType(BlockPos parentPos) {
        BlockPos pN = parentPos.north();
        BlockPos pE = parentPos.east();
        BlockPos pS = parentPos.south();
        BlockPos pW = parentPos.west();
        if (pN.equals(pos) || pE.equals(pos) || pS.equals(pos) || pW.equals(pos)) return NodeType.CARDINAL_WALK;
        if (pN.north().equals(pos) || pE.east().equals(pos) || pS.south().equals(pos) || pW.west().equals(pos)) return NodeType.CARDINAL_LEAP;
        if (pN.north().north().equals(pos) || pE.east().east().equals(pos) || pS.south().south().equals(pos) || pW.west().west().equals(pos)) return NodeType.CARDINAL_LEAP_LONG;
        if (pN.up().equals(pos) || pE.up().equals(pos) || pS.up().equals(pos) || pW.up().equals(pos)) return NodeType.CARDINAL_JUMP;
        if (pN.down().equals(pos) || pE.down().equals(pos) || pS.down().equals(pos) || pW.down().equals(pos)) return NodeType.CARDINAL_DROP_1;
        if (pN.down().down().equals(pos) || pE.down().down().equals(pos) || pS.down().down().equals(pos) || pW.down().down().equals(pos)) return NodeType.CARDINAL_DROP_2;
        if (pN.down().down().down().equals(pos) || pE.down().down().down().equals(pos) || pS.down().down().down().equals(pos) || pW.down().down().down().equals(pos)) return NodeType.CARDINAL_DROP_3;
        if (pN.east().equals(pos) || pS.east().equals(pos) || pS.west().equals(pos) || pN.west().equals(pos)) return NodeType.DIAGONAL_WALK;
        if (pN.east().down().equals(pos) || pS.east().down().equals(pos) || pS.west().down().equals(pos) || pN.west().down().equals(pos)) return NodeType.DIAGONAL_DROP_1;
        if (pN.east().down().down().equals(pos) || pS.east().down().down().equals(pos) || pS.west().down().down().equals(pos) || pN.west().down().down().equals(pos)) return NodeType.DIAGONAL_DROP_2;
        if (pN.east().down().down().down().equals(pos) || pS.east().down().down().down().equals(pos) || pS.west().down().down().down().equals(pos) || pN.west().down().down().down().equals(pos)) return NodeType.DIAGONAL_DROP_3;
        if (pN.east().up().equals(pos) || pS.east().up().equals(pos) || pS.west().up().equals(pos) || pN.west().up().equals(pos)) return NodeType.DIAGONAL_JUMP;

        return NodeType.NONE;
    }

    public double getCostToTarget(BlockPos target) {
        return Math.sqrt(pos.getSquaredDistance(target));
    }
}
