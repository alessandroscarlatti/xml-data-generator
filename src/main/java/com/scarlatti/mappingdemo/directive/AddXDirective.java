package com.scarlatti.mappingdemo.directive;

import com.scarlatti.mappingdemo.factory.FactoryDependent;
import com.scarlatti.mappingdemo.factory.NodeFactory;
import com.scarlatti.mappingdemo.util.NodeUtils;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class AddXDirective implements Directive, FactoryDependent {

    private Ref buildXRef;
    private Ref parentRef;
    private int count;
    private NodeFactory nodeFactory;

    public AddXDirective(Ref ref, int count, NodeFactory nodeFactory) {
        this.buildXRef = ref;
        this.nodeFactory = nodeFactory;
        this.parentRef = buildXRef.parent();
        this.count = count;
    }

    @Override
    public boolean applyTo(Node node) {
        if (Ref.ref(node).equals(parentRef)) {
            // ^^this node should contain the X nodes

            // so now add them!
            for (int i = 0; i < count; i++) {
                Node newNode = nodeFactory.getFactoryNode(buildXRef);
                NodeUtils.insertNodeByExample(newNode, node, nodeFactory.getExampleNode(parentRef));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public AddXDirective setNodeFactory(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        return this;
    }
}
