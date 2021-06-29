package br.com.tiozinnub.mcpathfinder.pathfinder;

public enum NodeType {
    CARDINAL_WALK(true),
    CARDINAL_JUMP(false),
    CARDINAL_DROP_1(false),
    CARDINAL_DROP_2(false),
    CARDINAL_DROP_3(false),
    DIAGONAL_WALK(true),
    DIAGONAL_JUMP(false),
    DIAGONAL_DROP_1(false),
    DIAGONAL_DROP_2(false),
    DIAGONAL_DROP_3(false),
    CARDINAL_LEAP(false),
    CARDINAL_LEAP_LONG(false),
    NONE(false);

    private final boolean canAnticipate;

    NodeType(boolean canAnticipate) {
        this.canAnticipate = canAnticipate;
    }

    public boolean canAnticipate() {
        return canAnticipate;
    }

}
