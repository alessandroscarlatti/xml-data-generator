package com.scarlatti.mappingdemo.directive;

import com.scarlatti.mappingdemo.util.NodeUtils;
import com.scarlatti.mappingdemo.util.NodeWalkerAdapter;
import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public final class DirectiveUtils {

    public static void applyDirective(Node node, Directive directive) {
        NodeUtils.walkNode(node, new NodeWalkerAdapter() {

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
