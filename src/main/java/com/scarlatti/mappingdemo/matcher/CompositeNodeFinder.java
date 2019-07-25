package com.scarlatti.mappingdemo.matcher;

import com.scarlatti.mappingdemo.factory.FactoryDependent;
import com.scarlatti.mappingdemo.factory.NodeFactory;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class CompositeNodeFinder implements NodeFinder, FactoryDependent {
    private List<NodeFinder> matchers = new ArrayList<>();

    public CompositeNodeFinder(List<NodeFinder> matchers) {
        this.matchers = matchers;
    }

    @Override
    public Set<Ref> getMatchingRefs(Node node) {
        Set<Ref> refs = new LinkedHashSet<>();
        for (NodeFinder matcher : matchers) {
            refs.addAll(matcher.getMatchingRefs(node));
        }
        return refs;
    }

    @Override
    public FactoryDependent setNodeFactory(NodeFactory nodeFactory) {
        for (NodeFinder matcher : matchers) {
            if (matcher instanceof FactoryDependent) {
                ((FactoryDependent) matcher).setNodeFactory(nodeFactory);
            }
        }

        return this;
    }
}
