package com.scarlatti.mappingdemo;

import groovy.util.Node;
import groovy.util.NodeList;

import static com.scarlatti.mappingdemo.NodeUtils.addNodeInSlot;
import static com.scarlatti.mappingdemo.NodeUtils.nodes;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class CaseBuilder {

    public static void buildCase(Node base, NodeFactory factory, Directive directive, int tokenIndex) {
        // traverse the node to the next token in the ref.
        // if it does not exist in the base node, get it from the factory.
        String currentKey = directive.ref.getTokens().get(tokenIndex);
        NodeList nodeList = ((NodeList) base.get(currentKey));
        if (tokenIndex == directive.ref.getTokens().size() - 1) {
            // we are ready to do business finally!

            for (Node node : nodes(nodeList)) {
                // remove all of these
                base.remove(node);
            }

            // now add from the factory the number required.
            // try to match the order of the fields with the order in the node from the factory
            for (int i = 0; i < directive.count; i++) {
                Node nextNode = (Node) factory.get(directive.ref.getRefString()).clone();
//                base.append(nextNode);
                addNodeInSlot(nextNode, base, factory.get(Ref2.fromNode(base).getRefString()));
            }
        } else {
            // we are still traversing.
            // check to see if we need to add from the factory.
            if (nodeList.isEmpty()) {
                Node nextBaseNode;
                String childRef = new Ref2(directive.ref.getTokens().subList(0, tokenIndex + 1)).getRefString();
                nextBaseNode = (Node) factory.get(childRef).clone();
                addNodeInSlot(nextBaseNode, base, factory.get(Ref2.fromNode(base).getRefString()));
                buildCase(nextBaseNode, factory, directive, tokenIndex + 1);
            } else {
                for (Node node : nodes(nodeList)) {
                    buildCase(node, factory, directive, tokenIndex + 1);
                }
            }
        }
    }
}
