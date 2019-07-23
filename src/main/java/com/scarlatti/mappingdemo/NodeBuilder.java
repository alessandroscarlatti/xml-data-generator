package com.scarlatti.mappingdemo;

import groovy.util.Node;
import groovy.util.NodeList;

import static com.scarlatti.mappingdemo.NodeUtils.addNodeInSlot;
import static com.scarlatti.mappingdemo.NodeUtils.nodes;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class NodeBuilder {

    public static void applyDirective(Node node, Directive directive) {
        NodeUtils.walkNode(node, new NodeUtils.NodeWalkerAdapter() {

            @Override
            public void walkValueNode(Node node) {
                directive.applyTo(node);
            }

            @Override
            public void walkBeanNode(Node node) {
                directive.applyTo(node);
                super.walkBeanNode(node);
            }
        });
    }
}
