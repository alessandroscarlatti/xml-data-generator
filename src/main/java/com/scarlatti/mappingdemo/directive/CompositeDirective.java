package com.scarlatti.mappingdemo.directive;

import groovy.util.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class CompositeDirective implements Directive {

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
}
