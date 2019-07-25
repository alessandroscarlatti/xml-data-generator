package com.scarlatti.mappingdemo.directive;

import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public interface Directive {

    /**
     * Apply the directive to the given node.
     * A directive can decide whether or not it will apply itself to the given node.
     * The directive is free to modify the given node.
     *
     * @param node the node to inspect.
     * @return true if the directive was applied, false if it was not applied.
     */
    boolean applyTo(Node node);
}
