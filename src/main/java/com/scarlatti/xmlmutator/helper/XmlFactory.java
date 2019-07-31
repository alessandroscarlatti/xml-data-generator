package com.scarlatti.xmlmutator.helper;

import org.w3c.dom.Element;

/**
 * @author Alessandro Scarlatti
 * @since Tuesday, 7/30/2019
 */
public class XmlFactory {

    /**
     * Include the specified xpath in the default XML.
     *
     * @param xpath the xpath selector to include
     */
    public void includeDefault(String xpath) {

    }

    /**
     * Exclude the specified xpath in the default XML.
     *
     * @param xpath the xpath selector to exclude
     */
    public void excludeDefault(String xpath) {

    }

    /**
     * Get an element by example for the specified hypothetical index.
     *
     * @param xpath             the xpath to the base element, assumed to point to a single node, not multiple nodes.
     * @param hypotheticalIndex the hypothetical index for this element.
     *                          This allows the factory to increment values, or vary values based on index.
     *                          This would be particularly useful for lists.
     * @return the newly created element.  This element is a deep clone of the base node.
     * It is then modified according to any rules specified for this factory.
     * The node will need to be imported into the new document in order to use.
     */
    public Element getElementByExample(String xpath, int hypotheticalIndex) {
        return null;
    }
}
