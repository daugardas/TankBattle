package com.tankbattle.server.utils;

public class Node implements Comparable<Node> {
    public Vector2 position;
    public Node parent;
    public double g; // cost from start to this node
    public double f; // estimated cost from start to end through this node

    public Node(Vector2 position) {
        this.position = position;
    }

    public Node(Vector2 position, Node parent, double g, double h) {
        this.position = position;
        this.parent = parent;
        this.g = g;
        this.f = g + h;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.f, other.f);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return position.equals(node.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
