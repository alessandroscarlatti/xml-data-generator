package com.scarlatti.mappingdemo;

import com.scarlatti.mappingdemo.directive.Directive;
import com.scarlatti.mappingdemo.directive.DirectiveFactory;
import com.scarlatti.mappingdemo.directive.DirectiveUtils;
import com.scarlatti.mappingdemo.factory.IncrementingValueDirective;
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
public class XmlUtilsTest {

    @Test
    public void testNodeWalker() throws Exception {
        Node xml = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());

        NodeUtils.walkNode(xml, new NodeWalkerAdapter() {

            @Override
            public void walkValueNode(Node node) {
                System.out.println("Found Value @: " + Ref.ref(node) + node.text());
                super.walkValueNode(node);
            }

            @Override
            public void walkBeanNode(Node node) {
                System.out.println("Found Bean @: " + Ref.ref(node));
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
        NodeFactory nodeFactory = NodeFactory.fromExample(example);
        DirectiveFactory df = new DirectiveFactory(nodeFactory);

        // now add some directives to the factory...

        List<Directive> directives = Arrays.asList(
            df.add(3, ref("/Penguin/Toy"))
            , df.add(3, ref("/Penguin/Pet"))
            , df.add(5, ref("/Penguin/Pet/Toy"))
            , df.atLeast(3, ref("/Penguin/Pet/Toy"))  // should have no effect
            , df.atLeast(1, ref("/Penguin/Pet/Toy"))  // should add 1
        );

        Node base = cloneNode(example);
        removePlurals(base);

        directives.forEach(directive -> DirectiveUtils.applyDirective(base, directive));

        System.out.println(serialize(base));
    }

    @Test
    public void applyDirectiveWithRegex() throws Exception {
        Node example = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
        NodeFactory nodeFactory = NodeFactory.fromExample(example);
        DirectiveFactory df = new DirectiveFactory(nodeFactory);

        // now add some directives to the factory...

        List<Directive> directives = Arrays.asList(
            df.atLeast(1, ref("/Penguin/Pet")),
            df.atLeast(3, ".*/Toy")
        );

        Node base = cloneNode(example);
        removePlurals(base);

        directives.forEach(directive -> DirectiveUtils.applyDirective(base, directive));

        System.out.println(serialize(base));
    }

    @Test
    public void applyDirectiveToFactory() throws Exception {
        Node example = new XmlParser().parse(Paths.get("sandbox/penguin.xml").toFile());
        NodeFactory nodeFactory = NodeFactory.fromExample(example);
        DirectiveFactory df = new DirectiveFactory(nodeFactory);

        // now add some directives to the factory...
        List<Directive> factoryDirectives = Arrays.asList(
            new IncrementingValueDirective()
        );
        nodeFactory.setFactoryDirectives(factoryDirectives);

        List<Directive> directives = Arrays.asList(
            df.atLeast(1, ref("/Penguin/Pet")),
            df.atLeast(3, ".*/Toy"),
            df.atLeast(2, ".*/Gadget")
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

    @Test
    public void testSyntax() throws Exception {
        // how can I create multiple objects, then modify values in certain objects, by index?

        // can I add an entire xml object?
        // I suppose so, HOWEVER, it may or may not work out well.
        // Because for regex searching I am using example objects.
        // I suppose, though, the purpose is limited modification by example.

        // that would be something like...
        // df can be a builder
        node.apply (
            df.atLeast(1, ref("/Penguin/Pet"));

            df.setValue("/Penguin/Name@value", "Asdf");
            df.setValue("/Penguin/Pet[0]/Name@value", "Asdf");


            df.select("/Penguin/Name@value").replace("asdf(.+)qwe", "$1wer");
            df.select(".*/Toy").repeat(3);
            df.select("/Penguin/Pet/Name").remove();
            df.select("/Penguin/Pet[0]/Name@value").value("qwer");
            df.select("/Penguin/Pet/Name@value");

            testSuite.test("NoWidgets", df -> {
                df.replace("/Penguin/Name@value", "asdf(.+)qwe", "$1wer");
                df.repeat(".*/Toy").repeat(3);
                df.remove("/Penguin/Pet/Name");
                df.value("/Penguin/Pet[0]/Name@value", "qwer");
            });

            testSuite.test("WithWidgets", df -> {
                df.replace("/Penguin/Name@value", "asdf(.+)qwe", "$1wer");
                df.repeat(".*/Toy").repeat(3);
                df.remove("/Penguin/Pet/Name");
                df.value("/Penguin/Pet[0]/Name@value", "qwer");
            });

            testSuite.test("AllCheckboxes", df -> {
                df.replace("/Penguin/Name@value", "asdf(.+)qwe", "$1wer");

                for (int i = 0; i < 99; i++) {
                    df.value(".*/Question" + i + "@value", "true");
                }
            });







            df.atLeast(".*/Toy", 3);
            df.atLeast(".*/Gadget", 2);
        );
    }

}
