package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

public class Main {
    private static final String SIMPLE_CHAR = "V";
    private static final String PARALLEL_CHAR = "H";
    private static final String TERMINAL_CHAR = "E";

    private static final int N = 17;

    private static List<Node> nodes = new ArrayList<>();
    private String[] visited = new String[N];

    private Stack<Node> stack = new Stack<Node>();


    public static void main(String[] args) throws IOException {
        (new Main()).solve();

    }

    private void solve() throws IOException {
        for (int i = 0; i < N; i++) {
            nodes.add(new Node());
        }
        Stream<String> stream = Files.lines(Paths.get("src/var7.txt"));
        stream.forEach(item -> {
            String[] tmp = item.split(" ");
            int from = Integer.parseInt(tmp[0]) - 1;
            int to = Integer.parseInt(tmp[1]) - 1;
            int type = Integer.parseInt(tmp[2]);
            Edge edge = new Edge(from, to, type);
            nodes.get(from).getToEdges().add(edge);
            nodes.get(to).getFromEdges().add(edge);
        });
        bfs();
        for (int i = 0; i < N; i++) {
            System.out.println("Метка вершины №" + (i+1) + ": " + nodes.get(i).toString());

        }
    }

    private void bfs() {
        stack.push(nodes.get(0));
        while (!stack.isEmpty()) {
            Node fromNode = stack.pop();
            List<Node> nodesToPush = markNode(fromNode);
            for (Node node : nodesToPush) {
                if (node.isMarked) continue;
                node.setFromNode(fromNode);
                stack.push(node);
            }
        }
    }

    private List<Node> markNode(Node node) {
        List<Node> answer = new LinkedList<>();
        if (node.isMarked)
            return answer;
        if (nodes.get(7) == node || nodes.get(2) == node) {
            int iiii = 0;
            iiii++;
        }
        node.setType(0);
        if (node.getFromEdges().size() != 0) {
            Node fromNode = node.getFromNode();
            Edge fromEdge = null;
            for (Edge edge : node.getFromEdges()) {
                if (nodes.get(edge.getFrom()) == fromNode) {
                    fromEdge = edge;
                }
            }
            switch (fromEdge.getType()) {
                case 0:
                    node.setDeep(nodes.get(fromEdge.getFrom()).getDeep());
                    break;
                case 1:
                    node.setDeep(nodes.get(fromEdge.getFrom()).getDeep() + 1);
                    break;
                case 2:
                    node.setDeep(nodes.get(fromEdge.getFrom()).getDeep() - 1);
                    break;
            }
        }

        if (node.getToEdges().size() != 0 &&
                node.getToEdges().get(0).getType() == 1) {
            node.setType(1);
            setId(node);

            for (int i = 0; i < node.getToEdges().size(); i++) {
                Edge toEdge = node.getToEdges().get(i);
                if (toEdge.isPassed()) continue;
                toEdge.setPassed();

                Node toNode = nodes.get(toEdge.getTo());
                if (toNode.isMarked) continue;
                toNode.setId(i + 1);
                List<Integer> parentIds = new LinkedList<>(node.getParentIds());
                if (node.getId() != 0) parentIds.add(node.getId());
                toNode.setParentIds(parentIds);
                answer.add(toNode);
            }
        }

        if (node.getFromEdges().size() != 0 &&
                node.getFromEdges().get(0).getType() == 2) {
            node.setType(2);
        }
        if (node.getType() == 0) {
            setId(node);

        }
        if (node.getType() == 0 || node.getType() == 2) {
            for (Edge edge : node.getToEdges())
                if (!edge.isPassed()) {
                    Node toNode = nodes.get(edge.getTo());
                    if (toNode.isMarked) continue;

                    answer.add(toNode);
                    edge.setPassed();
                    toNode.setParentIds(node.getParentIds());
                }
        }
        node.setMarked();

        if (node.getParentIds().size() > node.getDeep()) {
            List<Integer> ids = new LinkedList<>();
            for (int i = 0; i < node.getDeep(); i++) {
                ids.add(node.parentIds.get(i));
            }
            node.setParentIds(ids);
        }

        return answer;
    }

    private void setId(Node node) {
        if (node.getId() == 0) {
            if (node.getFromNode() == null) return;
            int id = node.getFromNode().getId();
            if (id != 0)
                node.setId(id);
            for (Edge edge : node.getToEdges()) {
                if (edge.isPassed()) continue;
                if (id != 0)
                    nodes.get(edge.getTo()).setId(id);
            }
        }
    }

    private static class Edge {
        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public int getType() {
            return type;
        }

        int from;
        int to;
        int type;

        public boolean isPassed() {
            return passed;
        }

        public void setPassed() {
            this.passed = true;
        }

        boolean passed = false;

        public Edge(int from, int to, int type) {
            this.from = from;
            this.to = to;
            this.type = type;
        }
    }

    private static class Node {
        List<Edge> fromEdges, toEdges;
        List<Integer> parentIds;
        String label;
        int deep, id, type;

        public void setMarked() {
            isMarked = true;
        }

        boolean isMarked = false;

        public Node getFromNode() {
            return fromNode;
        }

        private Node fromNode;


        public List<Edge> getFromEdges() {
            return fromEdges;
        }

        public List<Edge> getToEdges() {
            return toEdges;
        }

        public List<Integer> getParentIds() {
            return parentIds;
        }

        public void setParentIds(List<Integer> parentIds) {
            this.parentIds = parentIds;
        }

        public int getDeep() {
            return deep;
        }

        public void setDeep(int deep) {
            this.deep = deep;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Node() {
            this.fromEdges = new LinkedList<>();
            this.toEdges = new LinkedList<>();
            this.parentIds = new LinkedList<>();
            this.deep = 0;
            this.id = 0;
        }


        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            String typeString = "";
            switch (type) {
                case 0:
                    typeString = SIMPLE_CHAR;
                    break;
                case 1:
                    typeString = PARALLEL_CHAR;
                    break;
                case 2:
                    typeString = TERMINAL_CHAR;
                    break;
            }
            String label = deep + "." + typeString;

            String parentIdString = "";
            for (int id : parentIds)
                label += "." + id;
            if (id != 0) label += "." + id;
            return label;
        }

        public void setFromNode(Node fromNode) {
            this.fromNode = fromNode;
        }
    }
}
