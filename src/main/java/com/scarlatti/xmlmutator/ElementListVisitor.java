package com.scarlatti.xmlmutator;

import org.w3c.dom.Element;

import java.util.List;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public interface ElementListVisitor {
    void visitElementList(List<Element> nodes);
}
