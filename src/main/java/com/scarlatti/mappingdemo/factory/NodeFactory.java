package com.scarlatti.mappingdemo.factory;

import com.scarlatti.mappingdemo.util.NodeWalkerAdapter;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

import java.util.*;

import static com.scarlatti.mappingdemo.util.NodeUtils.*;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class NodeFactory {

    private Map<String, Node> factoryNodes = new LinkedHashMap<>();
    private List<Ref> plurals = new ArrayList<>();
    private int defaultPluralCount = 0;

    private NodeFactory() {}

    public static NodeFactory fromExample(Node example) {
        NodeFactory nodeFactory = new NodeFactory();

        nodeFactory.factoryNodes = new LinkedHashMap<>();
        nodeFactory.plurals = getPlurals(example);
        buildFactoryNodes(cloneNode(example), nodeFactory.factoryNodes);

        return nodeFactory;
    }

    private static void buildFactoryNodes(Node node, Map<String, Node> factoryMap) {
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
                    factoryMap.put(Ref.fromNode(last(nodes)).getRefString(), factoryNode);
                });

                if (!factoryMap.containsKey(Ref.fromNode(node).getRefString())) {
                    Node factoryNode = cloneNode(node);
                    factoryMap.put(Ref.fromNode(node).getRefString(), factoryNode);
                }

                super.walkBeanNode(node);
            }
        });
    }

    // Todo this factory method can apply a directive to produce a default repetition of plurals
    public Node getFactoryNode(String path) {
        Node node = cloneNode(factoryNodes.get(path));
        removePlurals(node);
        return node;
    }

    public Node getExampleNode(String path) {
        return cloneNode(factoryNodes.get(path));
    }
}
