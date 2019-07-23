//package com.scarlatti.mappingdemo;
//
//import groovy.util.Node;
//import groovy.util.XmlParser;
//import groovy.xml.XmlUtil;
//import org.junit.Test;
//
//import java.nio.file.Paths;
//import java.util.List;
//
//import static com.scarlatti.mappingdemo.NodeUtils.getPlurals;
//import static com.scarlatti.mappingdemo.NodeUtils.removePlurals;
//import static java.nio.file.Files.readAllBytes;
//import static org.junit.Assert.assertTrue;
//
///**
// * @author Alessandro Scarlatti
// * @since Saturday, 7/20/2019
// */
//public class XmlParsingDemo {
//
//    @Test
//    public void testGetPlurals() throws Exception {
//        XmlParser xmlParser = new XmlParser();
//        xmlParser.setKeepIgnorableWhitespace(false);
//        Node xml = xmlParser.parseText(new String(readAllBytes(Paths.get("sandbox/penguin.xml"))));
//        List<Ref2> plurals = getPlurals(xml);
//        assertTrue(plurals.size() > 0);
//        assertTrue(plurals.contains(Ref2.fromString("/Penguin/Pet")));
//        assertTrue(plurals.contains(Ref2.fromString("/Penguin/Pet/Toy")));
//        assertTrue(plurals.contains(Ref2.fromString("/Penguin/Toy")));
//        assertTrue(plurals.contains(Ref2.fromString("/Penguin/Toy/Piece")));
//    }
//
//    @Test
//    public void testBuildFactoryNode() throws Exception {
//        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
//        NodeFactory nodeFactory = NodeFactory.fromExample(xml);
//
//        System.out.println(XmlUtil.serialize(xml));
//        System.out.println(nodeFactory);
//    }
//
//    @Test
//    public void testBuildCases() throws Exception {
//        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
//        NodeFactory nodeFactory = NodeFactory.fromExample(xml);
//
//        Directive2 directive1 = new Directive2();
//        directive1.ref = Ref2.fromString("/Penguin/Pet/Toy");
//        directive1.count = 5;
//
//        Directive2 directive2 = new Directive2();
//        directive2.ref = Ref2.fromString("/Penguin/Toy/Piece");
//        directive2.count = 5;
//
//        Node case1 = xml;
//        removePlurals(case1);
//
//        applyDirectiveRecursive(case1, nodeFactory, directive1, 1);
//
//        System.out.println(XmlUtil.serialize(case1));
//
//        applyDirectiveRecursive(case1, nodeFactory, directive2, 1);
//
//        System.out.println(XmlUtil.serialize(case1));
//    }
//
//    @Test
//    public void testBuildCaseWithBadDirective() throws Exception {
//        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
//        NodeFactory nodeFactory = NodeFactory.fromExample(xml);
//
//        Directive2 directive1 = new Directive2();
//        directive1.ref = Ref2.fromString("/Penguin/Toy");
//        directive1.count = 5;
//
//        Node case1 = nodeFactory.get("/Penguin/Toy");
//        removePlurals(case1);
//        applyDirectiveRecursive(case1, nodeFactory, directive1, 1);
//        System.out.println(XmlUtil.serialize(case1));
//    }
//
//    @Test
//    public void buildTestCasesFromPlurals() throws Exception {
//        Node xml = new XmlParser().parseText(new String(readAllBytes(Paths.get("sandbox/penguin.xml"))));
//        NodeFactory nodeFactory = NodeFactory.fromExample(xml);
//        List<Ref2> plurals = getPlurals(xml);
//        Node caseXml = (Node) xml.clone();
//
//        // todo this doesn't work yet, because we need
//        // to modify stuff somewhere so that the factory items return an empty list of plural items.
//        // that way, I can apply directives sequentially.
//        // I think I know what we should do:
//        // use a factory object instead of a map...
//        // make that a setting on the factory...
//        // to be able to know about plurals and return a Node with no nodes in the plurals list.
//        // applyDirectiveRecursive(caseXml, nodeFactory, new Directive2(Ref2.fromString("/Penguin/Toy"), 4), 1);
//        for (Ref2 ref : plurals) {
//            Directive2 directive = new Directive2(ref, 2);
//            applyDirectiveRecursive(caseXml, nodeFactory, directive, 1);
//        }
//
//        System.out.println(XmlUtil.serialize(caseXml));
//    }
//}
