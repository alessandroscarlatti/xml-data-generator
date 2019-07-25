package com.scarlatti.mappingdemo.factory;

import com.scarlatti.mappingdemo.directive.Directive;
import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class SimpleFactoryDirective implements FactoryDirective {

    private Directive directive;

    public SimpleFactoryDirective(Directive directive) {
        this.directive = directive;
    }

    @Override
    public boolean applyTo(Node node, Node example, int index) {
        return directive.applyTo(node);
    }
}
