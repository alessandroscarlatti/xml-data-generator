package com.scarlatti.mappingdemo.factory;

import com.scarlatti.mappingdemo.directive.Directive;
import com.scarlatti.mappingdemo.util.NodeWalkerAdapter;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

import java.util.*;

import static com.scarlatti.mappingdemo.util.NodeUtils.*;
import static java.util.Collections.unmodifiableList;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class NodeFactory {

    private Map<String, Node> exampleNodes = new LinkedHashMap<>();
    private List<Directive> factoryDirectives = new ArrayList<>();

    private NodeFactory() {
    }

    /**
     * Create a NodeFactory.
     * @param example the example node to use for all factory objects
     * @return the new factory instance
     */
    public static NodeFactory fromExample(Node example) {
        return fromExample(example, Arrays.asList());
    }

    /**
     * Create a node factory by example, with the given directives.
     * The directives will each be applied in order to each new factory object.
     *
     * @param example the example node to use for all factory objects
     * @return the new factory instance
     */
    public static NodeFactory fromExample(Node example, List<Directive> directives) {
        NodeFactory nodeFactory = new NodeFactory();

        nodeFactory.exampleNodes = new LinkedHashMap<>();
        buildExampleNodes(cloneNode(example), nodeFactory.exampleNodes);
        nodeFactory.factoryDirectives = unmodifiableList(directives);

        return nodeFactory;
    }

    /**
     * Build the example nodes.  Each unique Ref path will have an example node.
     * The last node from a group of identical tag names will be used as the example.
     *
     * @param node       the base node to use as an example.
     * @param exampleMap the map to add examples to.
     */
    private static void buildExampleNodes(Node node, Map<String, Node> exampleMap) {
        walkNode(node, new NodeWalkerAdapter() {
            @Override
            public void walkBeanNode(Node node) {
                visitChildNodesGroupByName(node, nodes -> {
                    if (nodes.size() > 1) {
                        // only keep the last instance
                        for (int i = 0; i < nodes.size() - 1; i++) {
                            removeNode(nodes.get(i));
                        }
                    }

                    Node factoryNode = cloneNode(last(nodes));
                    exampleMap.put(Ref.fromNode(last(nodes)).getRefString(), factoryNode);
                });

                if (!exampleMap.containsKey(Ref.fromNode(node).getRefString())) {
                    Node factoryNode = cloneNode(node);
                    exampleMap.put(Ref.fromNode(node).getRefString(), factoryNode);
                }

                super.walkBeanNode(node);
            }
        });
    }

    /**
     * Get a new copy of the example node stored in this factory.
     * The new node will be cloned.  All plurals will be removed.
     * Then any directives in this factory will be applied.
     *
     * @param path the path of the node to created
     * @return the node constructed
     * @throws NodeNotFoundException if the given path is not found.
     */
    public Node getFactoryNode(String path) {
        if (!exampleNodes.containsKey(path))
            throw new NodeNotFoundException("Factory does not contain an example node for ref " + path, path);
        Node node = cloneNode(exampleNodes.get(path));
        removePlurals(node);

        // now apply any directives for this factory
        for (Directive directive : factoryDirectives) {
            directive.applyTo(node);
        }

        return node;
    }

    public Node getExampleNode(String path) {
        return cloneNode(exampleNodes.get(path));
    }
}
