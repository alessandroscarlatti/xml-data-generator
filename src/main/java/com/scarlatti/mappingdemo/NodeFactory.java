package com.scarlatti.mappingdemo;

import groovy.util.Node;
import groovy.util.NodeList;

import java.util.*;

import static com.scarlatti.mappingdemo.NodeUtils.*;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class NodeFactory {

    private Map<String, Node> factoryNodes = new LinkedHashMap<>();
    private List<Ref2> plurals = new ArrayList<>();
    private int defaultPluralCount = 0;

    private NodeFactory() {}

    public static NodeFactory fromExample(Node example) {
        NodeFactory nodeFactory = new NodeFactory();

        nodeFactory.factoryNodes = new LinkedHashMap<>();
        nodeFactory.plurals = getPlurals(example);
        buildFactoryNode(cloneNode(example), nodeFactory.factoryNodes);

        return nodeFactory;
    }

    private static void buildFactoryNode(Node node, Map<String, Node> factoryMap) {
        walkNode(node, new NodeUtils.NodeWalkerAdapter() {
            @Override
            public void walkBeanNode(Node node) {
                visitNodeSets(node, nodes -> {
                    if (nodes.size() > 1) {
                        // only keep the last instance
                        for (int i = 0; i < nodes.size() - 1; i++) {
                            removeNode(nodes.get(i));
                        }
                    }

                    Node factoryNode = last(nodes);
                    factoryMap.put(Ref2.fromNode(factoryNode).getRefString(), factoryNode);
                });

                if (!factoryMap.containsKey(node.name())) {
                    Node factoryNode = cloneNode(node);
                    factoryMap.put(Ref2.fromNode(node).getRefString(), factoryNode);
                }

                super.walkBeanNode(node);
            }
        });
    }

    public Node get(String path) {
        return (Node) factoryNodes.get(path).clone();
    }
}
