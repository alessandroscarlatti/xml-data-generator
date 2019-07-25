package com.scarlatti.mappingdemo;

import com.scarlatti.mappingdemo.directive.AddXDirective;
import com.scarlatti.mappingdemo.directive.Directive;
import com.scarlatti.mappingdemo.directive.DirectiveFactory;
import com.scarlatti.mappingdemo.directive.DirectiveUtils;
import com.scarlatti.mappingdemo.factory.NodeFactory;
import com.scarlatti.mappingdemo.util.NodeUtils;
import com.scarlatti.mappingdemo.util.NodeWalkerAdapter;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;
import groovy.util.XmlParser;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.scarlatti.mappingdemo.util.NodeUtils.cloneNode;
import static com.scarlatti.mappingdemo.util.NodeUtils.removePlurals;
import static com.scarlatti.mappingdemo.util.Ref.ref;
import static groovy.xml.XmlUtil.serialize;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class NodeUtilsTest {

    @Test
    public void testNodeWalker() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());

        NodeUtils.walkNode(xml, new NodeWalkerAdapter() {

            @Override
            public void walkValueNode(Node node) {
                System.out.println("Found Value @: " + Ref.fromNode(node) + node.text());
                super.walkValueNode(node);
            }

            @Override
            public void walkBeanNode(Node node) {
                System.out.println("Found Bean @: " + Ref.fromNode(node));
                // could transform here...
                super.walkBeanNode(node);
            }
        });
    }

    @Test
    public void testGetPlurals() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());

        List<Ref> plurals = NodeUtils.getPlurals(xml);
        plurals.forEach(System.out::println);
    }

    @Test
    public void testRemovePlurals() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
        NodeUtils.removePlurals(xml);
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
        NodeFactory nodeFactory = NodeFactory.fromExample(example, Arrays.asList());

        DirectiveFactory df = new DirectiveFactory(nodeFactory);

        List<Directive> directives = Arrays.asList(
            new AddXDirective(ref("/Penguin/Toy"), 3, nodeFactory)
            , new AddXDirective(ref("/Penguin/Pet"), 3, nodeFactory)
            , new AddXDirective(ref("/Penguin/Pet/Toy"), 5, nodeFactory)
            , df.atLeast(3, ref("/Penguin/Pet/Toy"))  // should have no effect
            , df.atLeast(1, ref("/Penguin/Pet/Toy"))  // should add 1
        );

        Node base = cloneNode(example);
        removePlurals(base);

        directives.forEach(directive -> DirectiveUtils.applyDirective(base, directive));

        System.out.println(serialize(base));
    }

    @Test
    public void testClone() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
        Node clone = cloneNode(xml);

        System.out.println(clone);
    }

}
