package com.scarlatti.mappingdemo.factory;

import com.scarlatti.mappingdemo.directive.Directive;
import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public interface FactoryDirective extends Directive {

    @Override
    default boolean applyTo(Node node) {
        return applyTo(node, 0);
    }

    /**
     * Apply a directive to this node.
     * The node is understood to be the hypothetical nth node in a
     * group of nodes of identical name.
     * @param node the node to inspect.
     * @param index the index of this node in a hypothetical grouping of nodes of the same name
     * @return whether or not the directive was applied.
     */
    boolean applyTo(Node node, int index);
}
