package com.scarlatti.mappingdemo.factory;

import groovy.util.Node;

import static com.scarlatti.mappingdemo.util.NodeUtils.getChildren;
import static com.scarlatti.mappingdemo.util.NodeUtils.isValueNode;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class IncrementingValueDirective implements FactoryDirective {

    @Override
    public boolean applyTo(Node node, Node example, int index) {
        for (Node childNode : getChildren(node)) {
            Node exampleChild = getChildren(example, (String) childNode.name()).get(0);
            if (isValueNode(exampleChild))
                childNode.setValue(childNode.text() + String.valueOf(index));
        }

        return true;
    }
}
