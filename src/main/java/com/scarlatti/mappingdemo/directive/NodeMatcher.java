package com.scarlatti.mappingdemo.directive;

import com.scarlatti.mappingdemo.factory.FactoryDependent;
import com.scarlatti.mappingdemo.factory.NodeFactory;
import com.scarlatti.mappingdemo.util.NodeUtils;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
interface NodeMatcher {
    Set<Ref> getMatchingRefs(Node node);

    class SimpleMatchParentNodeMatcher implements NodeMatcher {

        private Ref parentRef;
        private Ref childRef;

        public SimpleMatchParentNodeMatcher(Ref childRef) {
            this.childRef = childRef;
            this.parentRef = childRef.parent();
        }

        @Override
        public Set<Ref> getMatchingRefs(Node node) {
            if (Ref.fromNode(node).equals(parentRef))
                return new HashSet<>(singletonList(childRef));
            else
                return new HashSet<>();
        }
    }

    class RegexNodeMatcher implements NodeMatcher, FactoryDependent {
        private String regex;
        private NodeFactory nodeFactory;

        public RegexNodeMatcher(String regex) {
            this.regex = regex;

        }

        @Override
        public Set<Ref> getMatchingRefs(Node node) {

            // get an example schema node from the factory.
            // try to match each child.
            Node exampleNode = nodeFactory.getExampleNode(Ref.fromNode(node).getRefString());

            Set<Ref> matches = new LinkedHashSet<>();
            NodeUtils.visitChildNodesGroupByName(exampleNode, nodes -> {
                Ref ref = Ref.fromNode(nodes.get(0));
                if (Pattern.matches(regex, ref.getRefString()))
                    matches.add(ref);
            });

            return matches;
        }

        @Override
        public FactoryDependent setNodeFactory(NodeFactory nodeFactory) {
            this.nodeFactory = nodeFactory;
            return this;
        }
    }

    class CompositeNodeMatcher implements NodeMatcher, FactoryDependent {
        private List<NodeMatcher> matchers = new ArrayList<>();

        public CompositeNodeMatcher(List<NodeMatcher> matchers) {
            this.matchers = matchers;
        }

        @Override
        public Set<Ref> getMatchingRefs(Node node) {
            Set<Ref> refs = new LinkedHashSet<>();
            for (NodeMatcher matcher : matchers) {
                refs.addAll(matcher.getMatchingRefs(node));
            }
            return refs;
        }

        @Override
        public FactoryDependent setNodeFactory(NodeFactory nodeFactory) {
            for (NodeMatcher matcher : matchers) {
                if (matcher instanceof FactoryDependent) {
                    ((FactoryDependent) matcher).setNodeFactory(nodeFactory);
                }
            }

            return this;
        }
    }

    class NeverMatchNodeMatcher implements NodeMatcher {
        @Override
        public Set<Ref> getMatchingRefs(Node node) {
            return emptySet();
        }
    }
}
