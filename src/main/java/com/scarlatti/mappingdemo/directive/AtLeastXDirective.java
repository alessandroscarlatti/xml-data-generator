package com.scarlatti.mappingdemo.directive;

import com.scarlatti.mappingdemo.factory.NodeFactory;
import com.scarlatti.mappingdemo.factory.NodeNotFoundException;
import com.scarlatti.mappingdemo.util.NodeUtils;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

import static com.scarlatti.mappingdemo.util.NodeUtils.getChildren;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class AtLeastXDirective implements Directive, FactoryDirective {

    private Ref atLeastXRef;
    private Ref parentRef;
    private int atLeastCount;
    private NodeFactory nodeFactory;

    public AtLeastXDirective(Ref ref, int atLeastCount, NodeFactory nodeFactory) {
        this.atLeastXRef = ref;
        this.nodeFactory = nodeFactory;
        this.parentRef = atLeastXRef.parent();
        this.atLeastCount = atLeastCount;
    }

    @Override
    public boolean applyTo(Node node) {
        if (Ref.fromNode(node).equals(parentRef)) {
            // ^^this node should contain the X nodes

            int currentCount = getChildren(node, atLeastXRef.last()).size();
            if (currentCount < atLeastCount) {
                int numToAdd = atLeastCount - currentCount;

                // so now add them!
                for (int i = 0; i < numToAdd; i++) {
                    try {
                        Node newNode = nodeFactory.getFactoryNode(atLeastXRef.getRefString());
                        NodeUtils.insertNodeByExample(newNode, node, nodeFactory.getExampleNode(parentRef.getRefString()));
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setNodeFactory(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }
}
