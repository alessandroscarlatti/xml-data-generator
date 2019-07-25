package com.scarlatti.mappingdemo.util;

import groovy.util.Node;

import java.util.List;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public interface NodeListVisitor {
    void visitNodeSet(List<Node> nodes);
}
