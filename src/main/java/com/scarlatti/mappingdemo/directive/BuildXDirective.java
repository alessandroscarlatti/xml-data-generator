package com.scarlatti.mappingdemo.directive;

import com.scarlatti.mappingdemo.factory.NodeFactory;
import com.scarlatti.mappingdemo.util.NodeUtils;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class BuildXDirective implements Directive {

    private Ref buildXRef;
    private Ref parentRef;
    private int count;
    private NodeFactory nodeFactory;

    public BuildXDirective(Ref ref, int count, NodeFactory nodeFactory) {
        this.buildXRef = ref;
        this.nodeFactory = nodeFactory;
        this.parentRef = buildXRef.parent();
        this.count = count;
    }

    @Override
    public boolean applyTo(Node node) {
        if (Ref.fromNode(node).equals(parentRef)) {
            // ^^this node should contain the X nodes

            // so now add them!
            for (int i = 0; i < count; i++) {
                Node newNode = nodeFactory.getFactoryNode(buildXRef.getRefString());
                NodeUtils.insertNodeByExample(newNode, node, nodeFactory.getExampleNode(parentRef.getRefString()));
            }

            return true;
        } else {
            return false;
        }
    }
}
