package com.scarlatti.mappingdemo.util;

import groovy.util.Node;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public interface NodeWalker {
    void walkValueNode(Node node);

    void walkBeanNode(Node node);
}
