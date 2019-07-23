package com.scarlatti.mappingdemo;

import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public interface Directive {

    boolean applyTo(Node node);
}
