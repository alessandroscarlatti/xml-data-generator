package com.scarlatti.xmlmutator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.scarlatti.xmlmutator.XmlUtils.getChildren;
import static com.scarlatti.xmlmutator.XmlUtils.walkNode;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class RecursiveXmlVisitor implements ElementVisitor {

    @Override
    public void visitAttributeNode(Node node) {
        // nothing to do here
    }

    @Override
    public void visitValueElement(Element node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ATTRIBUTE_NODE)
                visitAttributeNode(child);
        }
    }

    @Override
    public void visitBeanElement(Element node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ATTRIBUTE_NODE)
                visitAttributeNode(child);
        }
        for (Element child : getChildren(node)) {
            walkNode(child, this);
        }
    }
}
