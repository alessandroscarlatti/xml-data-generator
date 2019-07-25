package com.scarlatti.mappingdemo.factory;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 * <p>
 * A directive that needs a node factory
 */
public interface FactoryDependent {

    /**
     * Set the node factory for this directive.
     *
     * @param nodeFactory the node factory to inject.
     * @return the directive, for chaining
     */
    FactoryDependent setNodeFactory(NodeFactory nodeFactory);
}
