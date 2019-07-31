package com.scarlatti.xmlmutator;

import org.testng.annotations.Test;
import org.w3c.dom.Element;

import java.nio.file.Paths;

import static com.scarlatti.xmlmutator.XmlUtils.*;

/**
 * @author Alessandro Scarlatti
 * @since Tuesday, 7/30/2019
 */
public class XmlUtilsTest {

    @Test
    public void testXmlUtils() {
        Element xml = parseXml(Paths.get("sandbox/penguin.xml"));

        System.out.println(serialize(xml));
    }

    @Test
    public void testRemovePlurals() {
        Element xml = parseXml(Paths.get("sandbox/penguin.xml"));
        removePlurals(xml);
        System.out.println(serialize(xml));
    }

    @Test
    public void testVisitElements() {
        Element xml = parseXml(Paths.get("sandbox/penguin.xml"));
        walkNode(xml, new RecursiveXmlVisitor() {
            @Override
            public void visitValueElement(Element node) {
                System.out.println("Value: " + Path.path(node));
            }

            @Override
            public void visitBeanElement(Element node) {
                System.out.println("Bean: " + Path.path(node));
                super.visitBeanElement(node);
            }
        });
    }
}
