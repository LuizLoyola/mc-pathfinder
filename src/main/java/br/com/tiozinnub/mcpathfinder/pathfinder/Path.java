package br.com.tiozinnub.mcpathfinder.pathfinder;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Path {
    public final ImmutableList<Node> nodes;

    public Path(List<Node> nodes) {
        this.nodes = ImmutableList.copyOf(nodes);
    }
}
