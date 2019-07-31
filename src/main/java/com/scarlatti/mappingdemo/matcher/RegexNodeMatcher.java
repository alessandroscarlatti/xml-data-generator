package com.scarlatti.mappingdemo.matcher;

import com.scarlatti.mappingdemo.factory.FactoryDependent;
import com.scarlatti.mappingdemo.factory.NodeFactory;
import com.scarlatti.mappingdemo.util.NodeUtils;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static com.scarlatti.mappingdemo.util.Ref.ref;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class RegexNodeMatcher implements NodeFinder, FactoryDependent {
    private String regex;
    private NodeFactory nodeFactory;

    public RegexNodeMatcher(String regex, NodeFactory nodeFactory) {
        this.regex = regex;
        this.nodeFactory = nodeFactory;
    }

    @Override
    public Set<Ref> getMatchingRefs(Node node) {

        // get an example schema node from the factory.
        // try to match each child.
        // todo this does not work because it the refs it creates are wrong since they don't attach to the parent
        Node exampleNode = nodeFactory.getExampleNode(ref(node));

        Set<Ref> matches = new LinkedHashSet<>();
        NodeUtils.visitChildNodesGroupByName(exampleNode, nodes -> {
//            Ref ref = ref(node.parent()).resolve(ref(nodes.get(0)));
//            if (Pattern.matches(regex, ref.getRefString()))
//                matches.add(ref);
        });

        return matches;
    }

    @Override
    public FactoryDependent setNodeFactory(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        return this;
    }
}
