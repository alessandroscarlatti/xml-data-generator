package com.scarlatti.mappingdemo;

import groovy.util.Node;
import groovy.util.XmlParser;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.scarlatti.mappingdemo.NodeUtils.*;
import static groovy.xml.XmlUtil.serialize;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class NodeUtilsTest {

    @Test
    public void testNodeUtils() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());

        NodeUtils.walkNode(xml, new NodeUtils.NodeWalkerAdapter() {

            @Override
            public void walkValueNode(Node node) {
                System.out.println("Found Value @: " + Ref2.fromNode(node) + node.text());
                super.walkValueNode(node);
            }

            @Override
            public void walkBeanNode(Node node) {
                System.out.println("Found Bean @: " + Ref2.fromNode(node));
                // could transform here...
                super.walkBeanNode(node);
            }
        });
    }

    @Test
    public void testGetPlurals() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());

        Set<Ref2> plurals = new LinkedHashSet<>();
        NodeUtils.walkNode(xml, new NodeUtils.NodeWalkerAdapter() {
            @Override
            public void walkBeanNode(Node node) {
                List<String> childrenNames = NodeUtils.getChildrenNames(node);
                for (String childName : childrenNames) {
                    List<Node> children = getChildren(node, childName);
                    if (children.size() > 1)
                        plurals.add(Ref2.fromNode(children.get(0)));
                }

                super.walkBeanNode(node);
            }
        });

        plurals.forEach(System.out::println);
    }

    @Test
    public void testRemovePlurals() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
        List<Ref2> plurals = NodeUtils.getPluralsFromNode(xml);

        NodeUtils.walkNode(xml, new NodeUtils.NodeWalkerAdapter() {
            @Override
            public void walkValueNode(Node node) {
                if (plurals.contains(Ref2.fromNode(node)))
                    removeNode(node);
            }

            @Override
            public void walkBeanNode(Node node) {
                if (plurals.contains(Ref2.fromNode(node)))
                    removeNode(node);

                super.walkBeanNode(node);
            }
        });

        System.out.println(serialize(xml));
    }

    @Test
    public void testBuildFactoryNode() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
        NodeFactory nodeFactory = NodeFactory.fromExample(xml);

        System.out.println(nodeFactory);
    }

    @Test
    public void applyDirective() throws Exception {
        Node example = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
        NodeFactory nodeFactory = NodeFactory.fromExample(example);

        Directive directive1 = new BuildXDirective(Ref2.fromString("/Penguin/Toy"), 3, nodeFactory);
        Directive directive2 = new BuildXDirective(Ref2.fromString("/Penguin/Pet"), 3, nodeFactory);
        Directive directive3 = new BuildXDirective(Ref2.fromString("/Penguin/Pet/Toy"), 5, nodeFactory);

        Node base = cloneNode(example);
        removePluralsFromNode(base);

        NodeBuilder.applyDirective(base, directive1);
        NodeBuilder.applyDirective(base, directive2);
        NodeBuilder.applyDirective(base, directive3);

        System.out.println(serialize(base));
    }

    @Test
    public void testClone() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
        Node clone = cloneNode(xml);

        System.out.println(clone);
    }

}
