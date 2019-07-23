package com.scarlatti.mappingdemo;

import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class BuildXDirective implements Directive {

    private Ref2 buildXRef;
    private Ref2 parentRef;
    private int count;
    private NodeFactory nodeFactory;

    public BuildXDirective(Ref2 ref, int count, NodeFactory nodeFactory) {
        this.buildXRef = ref;
        this.nodeFactory = nodeFactory;
        this.parentRef = buildXRef.parent();
        this.count = count;
    }

    @Override
    public boolean applyTo(Node node) {
        if (Ref2.fromNode(node).equals(parentRef)) {
            // ^^this node should contain the X nodes

            // so now add them!
            for (int i = 0; i < count; i++) {
                Node newNode = nodeFactory.get(buildXRef.getRefString());
                NodeUtils.addNodeInSlot(newNode, node, nodeFactory.getSchemaNode(parentRef.getRefString()));
            }

            return true;
        } else {
            return false;
        }
    }
}
