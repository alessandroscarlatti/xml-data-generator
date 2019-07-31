package com.scarlatti.mappingdemo.util;

import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class NodeWalkerAdapter implements NodeVisitor {
    @Override
    public void walkValueNode(Node node) {
        // nothing to do here
    }

    @Override
    public void walkBeanNode(Node node) {
        for (Node child : NodeUtils.getChildren(node)) {
            NodeUtils.walkNode(child, this);
        }
    }
}
