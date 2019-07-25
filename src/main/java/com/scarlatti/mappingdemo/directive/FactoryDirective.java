package com.scarlatti.mappingdemo.directive;

import com.scarlatti.mappingdemo.factory.NodeFactory;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 *
 * A directive that needs a node factory
 */
public interface FactoryDirective {

    /**
     * Set the node factory for this directive.
     * @param nodeFactory the node factory to inject.
     */
    void setNodeFactory(NodeFactory nodeFactory);
}
