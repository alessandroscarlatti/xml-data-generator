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
        buildFactoryNodeRecursive((Node) example.clone(), nodeFactory.factoryNodes);

        return nodeFactory;
    }

    public static NodeFactory fromExample2(Node example) {
        NodeFactory nodeFactory = new NodeFactory();

        nodeFactory.factoryNodes = new LinkedHashMap<>();
        nodeFactory.plurals = getPlurals(example);
        buildFactoryNode((Node) example.clone(), nodeFactory.factoryNodes);

        return nodeFactory;
    }

    private static void buildFactoryNode(Node node, Map<String, Node> factoryMap) {
        walkNode(node, new NodeUtils.NodeWalkerAdapter() {
            @Override
            public void walkBeanNode(Node node) {
                visitNodeSets(node, nodes -> {
                    if (nodes.size() > 1) {
                        // iterate all but last instance
                        for (int i = 0; i < nodes.size() - 1; i++) {
                            removeNode(nodes.get(i));
                        }
                    }

                    Node factoryNode = last(nodes);
                    factoryMap.put(Ref2.fromNode(factoryNode).getRefString(), factoryNode);
                });

                super.walkBeanNode(node);
            }
        });
    }

    private static void buildFactoryNodeRecursive(Node current, Map<String, Node> factoryMap) {
        Map<String, Node> keep = new LinkedHashMap<>();
        for (Node child : childNodes(current)) {

            // on this child node, if it's not in keep, add it.
            // if it's already in keep, see if this one has more children.
            // if so, replace the current one in keep.

            // replace the current keep value if this one is bigger.
            if (keep.values().contains(child)) {
                Node keepNode = keep.get(child.name());
                if (keepNode.value() instanceof NodeList) {
                    if (child.children().size() > keepNode.children().size()) {
                        keep.replace((String) child.name(), (Node) child.clone());
                    }
                }
            } else {
                keep.put((String) child.name(), (Node) child.clone());
            }
        }

        // add to factory map
        factoryMap.put(Ref2.fromNode(current).getRefString(), current);

        // now delete all the children and add back only the ones we want to keep
        if (current.value() instanceof NodeList && !isValueNode((NodeList) current.value())) {

            current.setValue("");
            for (Node node : keep.values()) {
                current.append(node);
            }

            // now take each child and do the same process for each.
            for (Node child : childNodes(current)) {
                buildFactoryNodeRecursive(child, factoryMap);
            }
        }
    }

    public Node get(String path) {
        return (Node) factoryNodes.get(path).clone();
    }
}
