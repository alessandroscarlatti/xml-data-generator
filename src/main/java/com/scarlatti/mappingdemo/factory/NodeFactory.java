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
 * <p>
 * Node factory by example.
 * The directives will each be applied in order to each new factory object.
 */
public class NodeFactory {

    private Map<String, Node> exampleNodes = new LinkedHashMap<>();
    private List<Directive> factoryDirectives = new ArrayList<>();

    private NodeFactory() {
    }

    /**
     * Create a node factory by example.
     *
     * @param example the example node to use for all factory objects
     * @return the new factory instance
     */
    public static NodeFactory fromExample(Node example) {
        NodeFactory nodeFactory = new NodeFactory();

        nodeFactory.exampleNodes = new LinkedHashMap<>();
        buildExampleNodes(cloneNode(example), nodeFactory.exampleNodes);

        // todo set the factory for the directives...

        return nodeFactory;
    }

    public void setFactoryDirectives(List<Directive> directives) {
        this.factoryDirectives = unmodifiableList(directives);
    }

    /**
     * Build the example nodes.  Each unique Path path will have an example node.
     * The last node from a group of identical tag names will be used as the example.
     *
     * @param node       the base node to use as an example.
     * @param exampleMap the map to add examples to.
     */
    private static void buildExampleNodes(Node node, Map<String, Node> exampleMap) {
//        walkNode(node, new NodeWalkerAdapter() {
//            @Override
//            public void walkBeanNode(Node node) {
//                visitChildNodesGroupByName(node, nodes -> {
//                    if (nodes.size() > 1) {
//                        // only keep the last instance
//                        for (int i = 0; i < nodes.size() - 1; i++) {
//                            removeNode(nodes.get(i));
//                        }
//                    }
//
//                    Node factoryNode = cloneNode(last(nodes));
//                    exampleMap.put(Ref.ref(last(nodes)).getRefString(), factoryNode);
//                });
//
//                if (!exampleMap.containsKey(Ref.ref(node).getRefString())) {
//                    Node factoryNode = cloneNode(node);
//                    exampleMap.put(Ref.ref(node).getRefString(), factoryNode);
//                }
//
//                super.walkBeanNode(node);
//            }
//        });
    }

    public Node getFactoryNode(Ref ref) {
        return getFactoryNode(ref, 0);
    }

    /**
     * Get a new copy of the example node stored in this factory.
     * The new node will be cloned.  All plurals will be removed.
     * Then any directives in this factory will be applied.
     *
     * @param ref   the path of the node to created
     * @param index the index of the object within its parent. 0 will return the default object.
     * @return the node constructed
     * @throws NodeNotFoundException if the given path is not found.
     */
    public Node getFactoryNode(Ref ref, int index) {
        if (!exampleNodes.containsKey(ref.getRefString()))
            throw new NodeNotFoundException("Factory does not contain an example node for path " + ref, ref);
        Node node = cloneNode(exampleNodes.get(ref.getRefString()));
        removePlurals(node);

        // now apply any directives for this factory
        for (Directive directive : factoryDirectives) {
            if (directive instanceof FactoryDirective) {
                Node exampleNode = getExampleNode(ref);
                ((FactoryDirective) directive).applyTo(node, exampleNode, index);
            } else {
                directive.applyTo(node);
            }
        }

        return node;
    }

    public Node getExampleNode(Ref ref) {
        return cloneNode(exampleNodes.get(ref.getRefString()));
    }
}
