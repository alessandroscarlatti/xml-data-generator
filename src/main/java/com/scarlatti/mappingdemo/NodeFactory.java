package com.scarlatti.mappingdemo;

import groovy.util.Node;
import groovy.util.NodeList;

import java.util.*;

import static com.scarlatti.mappingdemo.NodeUtils.childNodes;
import static com.scarlatti.mappingdemo.NodeUtils.getPlurals;
import static com.scarlatti.mappingdemo.NodeUtils.isTextNode;

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
        buildFactoryNode((Node) example.clone(), nodeFactory.factoryNodes);

        return nodeFactory;
    }

    private static void buildFactoryNode(Node current, Map<String, Node> factoryMap) {
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
        if (current.value() instanceof NodeList && !isTextNode((NodeList) current.value())) {

            current.setValue("");
            for (Node node : keep.values()) {
                current.append(node);
            }

            // now take each child and do the same process for each.
            for (Node child : childNodes(current)) {
                buildFactoryNode(child, factoryMap);
            }
        }
    }

    public Node get(String path) {
        return (Node) factoryNodes.get(path).clone();
    }
}
