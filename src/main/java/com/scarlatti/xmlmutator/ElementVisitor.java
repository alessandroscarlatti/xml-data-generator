package com.scarlatti.xmlmutator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public interface ElementVisitor {
    void visitAttributeNode(Node node);

    void visitValueElement(Element node);

    void visitBeanElement(Element node);
}
