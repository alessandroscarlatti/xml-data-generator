package com.scarlatti.xmlmutator;

import org.testng.annotations.Test;
import org.w3c.dom.Element;

import java.nio.file.Paths;

import static com.scarlatti.xmlmutator.XmlUtils.parseXml;
import static com.scarlatti.xmlmutator.XmlUtils.serialize;

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
}
