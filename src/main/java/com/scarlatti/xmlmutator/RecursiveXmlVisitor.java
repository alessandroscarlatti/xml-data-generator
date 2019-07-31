package com.scarlatti.xmlmutator;

import org.w3c.dom.Element;

import static com.scarlatti.xmlmutator.XmlUtils.getChildren;
import static com.scarlatti.xmlmutator.XmlUtils.walkNode;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class RecursiveXmlVisitor implements ElementVisitor {
    @Override
    public void visitValueElement(Element node) {
        // nothing to do here
    }

    @Override
    public void visitBeanElement(Element node) {
        for (Element child : getChildren(node)) {
            walkNode(child, this);
        }
    }
}
