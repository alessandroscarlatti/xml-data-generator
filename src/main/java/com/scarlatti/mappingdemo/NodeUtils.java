package com.scarlatti.mappingdemo;

import com.sun.org.apache.xpath.internal.NodeSet;
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

    public static boolean isValueNode(Node node) {
        for (Object objChild : node.children()) {
            if (objChild instanceof Node) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValueNode(NodeList nodeList) {
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

    public static void walkNode(Node node, NodeWalker walker) {
        if (isValueNode(node)) {
            walker.walkValueNode(node);
        } else {
            walker.walkBeanNode(node);
        }
    }

    public static void visitNodeSets(Node node, NodeSetVisitor visitor) {
        for (String childName : getChildrenNames(node)) {
            List<Node> children = getChildren(node, childName);
            visitor.visitNodeSet(children);
        }
    }

    public static <T> T last(List<T> list) {
        return list.get(list.size() - 1);
    }

    public interface NodeWalker {
        void walkValueNode(Node node);
        void walkBeanNode(Node node);
    }

    public interface NodeSetVisitor {
        void visitNodeSet(List<Node> nodes);
    }

    public static class NodeSetVisitorAdapter implements NodeSetVisitor {
        @Override
        public void visitNodeSet(List<Node> nodes) {
            // nothing to do here
        }
    }

    public static class NodeWalkerAdapter implements NodeWalker {
        @Override
        public void walkValueNode(Node node) {
            // nothing to do here
        }

        @Override
        public void walkBeanNode(Node node) {
            for (Node child : getChildren(node)) {
                walkNode(child, this);
            }
        }
    }

    public static List<Node> getChildren(Node node) {
        List<Node> nodes = new ArrayList<>();
        for (Object objNode : node.children()) {
            if (objNode instanceof Node) {
                nodes.add((Node) objNode);
            }
        }
        return nodes;
    }

    public static List<Node> getChildren(Node node, String name) {
        List<Node> nodes = new ArrayList<>();
        for (Object objNode : (NodeList) node.get(name)) {
            if (objNode instanceof Node) {
                nodes.add((Node) objNode);
            }
        }
        return nodes;
    }

    public static List<String> getChildrenNames(Node node) {
        List<String> names = new ArrayList<>();
        List<Node> nodes = getChildren(node);
        for (Node child : nodes) {
            names.add((String) child.name());
        }
        return names;
    }

    public static void removeNode(Node node) {
        if (node.parent() != null)
            node.parent().remove(node);
    }

    public static List<Ref2> getPluralsFromNode(Node node) {
        Set<Ref2> plurals = new LinkedHashSet<>();
        NodeUtils.walkNode(node, new NodeUtils.NodeWalkerAdapter() {
            @Override
            public void walkBeanNode(Node node) {
                List<String> childrenNames = NodeUtils.getChildrenNames(node);
                for (String childName : childrenNames) {
                    List<Node> children = getChildren(node, childName);
                    if (children.size() > 1)
                        plurals.add(Ref2.fromNode(children.get(0)));
                }

                super.walkBeanNode(node);
            }
        });
        return new ArrayList<>(plurals);
    }

    public static void removePluralsFromNode(Node node) {
        List<Ref2> plurals = NodeUtils.getPluralsFromNode(node);

        NodeUtils.walkNode(node, new NodeUtils.NodeWalkerAdapter() {
            @Override
            public void walkValueNode(Node node) {
                if (plurals.contains(Ref2.fromNode(node)))
                    removeNode(node);
            }

            @Override
            public void walkBeanNode(Node node) {
                if (plurals.contains(Ref2.fromNode(node)))
                    removeNode(node);
                else
                    super.walkBeanNode(node);
            }
        });
    }
}
