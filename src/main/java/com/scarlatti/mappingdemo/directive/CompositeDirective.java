package com.scarlatti.mappingdemo.directive;

import com.scarlatti.mappingdemo.factory.FactoryDependent;
import com.scarlatti.mappingdemo.factory.NodeFactory;
import groovy.util.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class CompositeDirective implements Directive, FactoryDependent {

    private List<Directive> directives = new ArrayList<>();

    public CompositeDirective(List<Directive> directives) {
        this.directives = directives;
    }

    @Override
    public boolean applyTo(Node node) {
        boolean anyApplied = false;
        for (Directive directive : directives) {
            boolean applied = directive.applyTo(node);
            if (applied)
                anyApplied = true;
        }

        return anyApplied;
    }

    /**
     * Set the node factory for any child directives that need it.
     * @param nodeFactory the node factory to inject.
     */
    @Override
    public CompositeDirective setNodeFactory(NodeFactory nodeFactory) {
        for (Directive directive : directives) {
            if (directive instanceof FactoryDependent) {
                ((FactoryDependent) directive).setNodeFactory(nodeFactory);
            }
        }

        return this;
    }
}
