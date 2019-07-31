package com.scarlatti.mappingdemo.util;

import org.w3c.dom.Element;

import java.util.List;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public interface NodeListVisitor {
    void visitElementSet(List<Element> nodes);
}
