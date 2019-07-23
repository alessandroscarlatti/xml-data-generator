package com.scarlatti.mappingdemo;

import groovy.util.Node;
import groovy.util.NodeList;

import java.util.*;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class NodeUtils {
    public static void addNodeInSlot(Node addNode, Node toNode, Node exampleNode) {
        // get the order from the example node
        List<String> order = new ArrayList<>();
        for (Node child : childNodes(exampleNode)) {
            if (!order.contains(child.name())) {
                order.add((String) child.name());
            }
        }

        // now we know the order
        Map<String, List<Node>> nodes = new LinkedHashMap<>();
        for (String key : order) {
            nodes.put(key, nodes((NodeList) toNode.get(key)));
            if (key.equals(addNode.name())) {
                nodes.get(key).add(addNode);
            }
        }

        // now assemble the nodes.
        toNode.setValue("");
        for (String key : nodes.keySet()) {
            for (Node node : nodes.get(key)) {
                toNode.append(node);
            }
        }
    }

    public static boolean isTextNode(NodeList nodeList) {
        for (Object objChild : nodeList) {
            if (objChild instanceof Node) {
                return false;
            }
        }
        return true;
    }

    public static List<Node> childNodes(Node node) {
        List<Node> nodes = new ArrayList<>();
        for (Object objNode : node.children()) {
            if (objNode instanceof Node) {
                nodes.add((Node) objNode);
            }
        }
        return nodes;
    }

    public static List<Node> nodes(NodeList nodeList) {
        List<Node> nodes = new ArrayList<>();
        for (Object objNode : nodeList) {
            if (objNode instanceof Node) {
                nodes.add((Node) objNode);
            }
        }
        return nodes;
    }

    public static List<Ref2> getPlurals(Node node) {
        List<Ref2> plurals = new ArrayList<>();
        getPlurals(node, plurals);
        return plurals;
    }

    // list the plurals
    public static void getPlurals(Node current, List<Ref2> pluralRefs) {
        Set<String> seen = new HashSet<>();
        Set<String> plurals = new HashSet<>();
        for (Object objNode : current.children()) {
            if (objNode instanceof Node) {
                Node node = (Node) objNode;
                if (seen.contains(node.name()) && !plurals.contains(node.name())) {
                    plurals.add((String) node.name());
                    pluralRefs.add(Ref2.fromNode(node));
                } else {
                    seen.add((String) node.name());
                }

                // get the children
                getPlurals(node, pluralRefs);
            }
        }
    }

    public static void removePlurals(Node current) {
        Set<String> seen = new HashSet<>();
        Set<String> plurals = new HashSet<>();
        for (Object objNode : current.children()) {
            if (objNode instanceof Node) {
                Node node = (Node) objNode;
                if (seen.contains(node.name()) && !plurals.contains(node.name())) {
                    plurals.add((String) node.name());
                } else {
                    seen.add((String) node.name());
                }

                // get the children
                removePlurals(node);
            }
        }

        for (String plural : plurals) {
            for (Node node : nodes((NodeList) current.get(plural))) {
                current.remove(node);
            }
        }
    }
}
