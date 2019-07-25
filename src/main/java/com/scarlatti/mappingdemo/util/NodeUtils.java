package com.scarlatti.mappingdemo.util;

import groovy.util.Node;
import groovy.util.NodeList;

import java.util.*;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class NodeUtils {

    /**
     * Insert a node into another node in the right order, given an example order by an example node.
     * @param addNode the node to add
     * @param toNode the node that will accept the new node
     * @param exampleNode the node that contains the example order of tags
     */
    public static void insertNodeByExample(Node addNode, Node toNode, Node exampleNode) {
        // getFactoryNode the order from the example node
        List<String> order = new ArrayList<>();
        for (Node child : getChildren(exampleNode)) {
            if (!order.contains((String) child.name())) {
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
        // first remove all nodes, then add them back
        toNode.setValue("");
        for (String key : nodes.keySet()) {
            for (Node node : nodes.get(key)) {
                toNode.append(node);
            }
        }
    }

    /**
     * Determine whether or not the given node represents a text value, or a node containing other values.
     * @param node the node to inspect.
     * @return true if the given node represents a single text value only.
     */
    private static boolean isValueNode(Node node) {
        for (Object objChild : node.children()) {
            if (objChild instanceof Node) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convert a NodeList into a List of Nodes.
     * @param nodeList the node list to convert
     * @return the list of nodes
     */
    private static List<Node> nodes(NodeList nodeList) {
        List<Node> nodes = new ArrayList<>();
        for (Object objNode : nodeList) {
            if (objNode instanceof Node) {
                nodes.add((Node) objNode);
            }
        }
        return nodes;
    }

    /**
     * Walk the given node and call the given walker based on the data in the node.
     * @param node the node to walk
     * @param walker the walker to call
     */
    public static void walkNode(Node node, NodeWalker walker) {
        if (isValueNode(node)) {
            walker.walkValueNode(node);
        } else {
            walker.walkBeanNode(node);
        }
    }

    /**
     * Visit each group of child nodes.  The visitor is called once per group of child nodes.
     * If a tag has only one occurrence, it will be in a group by itself.
     * If a tag has multiple occurrences, it will be in a group with the other occurrences, in order of appearance.
     * @param node the node to query
     * @param visitor the visitor to callback for each group
     */
    public static void visitChildNodesGroupByName(Node node, NodeListVisitor visitor) {
        for (String childName : getUniqueChildrenNames(node)) {
            List<Node> children = getChildren(node, childName);
            visitor.visitNodeSet(children);
        }
    }

    /**
     * Get the last item in a list.
     * @param list the list to query
     * @param <T> the type of item in the list
     * @return the last item in the list.
     */
    public static <T> T last(List<T> list) {
        return list.get(list.size() - 1);
    }

    /**
     * Get all the child nodes of the given node, in order
     * @param node the node to query
     * @return all the child nodes
     */
    public static List<Node> getChildren(Node node) {
        List<Node> nodes = new ArrayList<>();
        for (Object objNode : node.children()) {
            if (objNode instanceof Node) {
                nodes.add((Node) objNode);
            }
        }
        return nodes;
    }

    /**
     * Get all the child nodes of the given name from the given node.
     * @param node the node to query
     * @param name the tag name to serach for
     * @return all the children with that tag name, in order of appearance
     */
    public static List<Node> getChildren(Node node, String name) {
        List<Node> nodes = new ArrayList<>();
        for (Object objNode : (NodeList) node.get(name)) {
            if (objNode instanceof Node) {
                nodes.add((Node) objNode);
            }
        }
        return nodes;
    }

    /**
     * Get the unique names of child nodes in the given node.
     * If a tag occurs twice, its name is recorded only once.
     * @param node the node to query
     * @return ordered set of unique child tag names
     */
    public static Set<String> getUniqueChildrenNames(Node node) {
        Set<String> names = new LinkedHashSet<>();
        List<Node> nodes = getChildren(node);
        for (Node child : nodes) {
            names.add((String) child.name());
        }
        return names;
    }

    /**
     * Remove the node from its parent.
     * Assumes that the node has a parent.
     * @param node the node to remove from its parent.
     */
    public static void removeNode(Node node) {
        if (node.parent() != null)
            node.parent().remove(node);
    }

    /**
     * Get refs to each child tag occurring more than once (plural).
     * @param node the node to search
     * @return refs to each plural child tag.
     */
    public static List<Ref> getPlurals(Node node) {
        Set<Ref> plurals = new LinkedHashSet<>();
        NodeUtils.walkNode(node, new NodeWalkerAdapter() {
            @Override
            public void walkBeanNode(Node node) {
                Set<String> childrenNames = NodeUtils.getUniqueChildrenNames(node);
                for (String childName : childrenNames) {
                    List<Node> children = getChildren(node, childName);
                    if (children.size() > 1)
                        plurals.add(Ref.fromNode(children.get(0)));
                }

                super.walkBeanNode(node);
            }
        });
        return new ArrayList<>(plurals);
    }

    /**
     * Remove duplicate child tags from a node.
     * @param node the node in which to remove duplicate child tags.
     */
    public static void removePlurals(Node node) {
        List<Ref> plurals = NodeUtils.getPlurals(node);

        NodeUtils.walkNode(node, new NodeWalkerAdapter() {
            @Override
            public void walkValueNode(Node node) {
                if (plurals.contains(Ref.fromNode(node)))
                    removeNode(node);
            }

            @Override
            public void walkBeanNode(Node node) {
                if (plurals.contains(Ref.fromNode(node)))
                    removeNode(node);
                else
                    super.walkBeanNode(node);
            }
        });
    }

    /**
     * Deep-clone a node, preserving parent-child details.
     * Groovy 2.5.7 Node class clone method does not do this.
     * @param node the node to clone
     * @return return the cloned node
     */
    public static Node cloneNode(Node node) {
        return cloneNodeRecursive(node, null);
    }

    @SuppressWarnings("unchecked")
    private static Node cloneNodeRecursive(Node node, Node parent) {
        Object newValue = node.value();
        Node newNode = new Node(parent, node.name(), new HashMap(node.attributes()));
        if (node.value() instanceof NodeList) {
            newValue = cloneNodeList((NodeList) node.value(), newNode);
        }
        newNode.setValue(newValue);
        return newNode;
    }

    @SuppressWarnings("unchecked")
    private static NodeList cloneNodeList(NodeList nodeList, Node parent) {
        NodeList result = new NodeList(nodeList.size());
        for (Object node : nodeList) {
            if (node instanceof Node) {
                result.add(cloneNodeRecursive((Node) node, parent));
            } else {
                result.add(node);
            }
        }
        return result;
    }
}
