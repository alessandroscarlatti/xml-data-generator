package com.scarlatti.mappingdemo.directive;

import com.scarlatti.mappingdemo.matcher.NodeFinder;
import com.scarlatti.mappingdemo.matcher.CompositeNodeFinder;
import com.scarlatti.mappingdemo.matcher.NeverMatchNodeFinder;
import com.scarlatti.mappingdemo.factory.FactoryDependent;
import com.scarlatti.mappingdemo.factory.NodeFactory;
import com.scarlatti.mappingdemo.factory.NodeNotFoundException;
import com.scarlatti.mappingdemo.matcher.RegexNodeMatcher;
import com.scarlatti.mappingdemo.matcher.SimpleMatchParentNodeFinder;
import com.scarlatti.mappingdemo.util.NodeUtils;
import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.scarlatti.mappingdemo.util.NodeUtils.getChildren;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class AtLeastXDirective implements Directive, FactoryDependent {

    private NodeFinder nodeFinder = new NeverMatchNodeFinder();
    private int atLeastCount;
    private NodeFactory nodeFactory;

    public AtLeastXDirective(int atLeastCount, NodeFactory nodeFactory) {
        this.atLeastCount = atLeastCount;
        this.nodeFactory = nodeFactory;
    }

    public AtLeastXDirective useRefs(Ref... atLeastXRefs) {
        List<NodeFinder> matchers = new ArrayList<>();
        for (Ref ref : atLeastXRefs) {
            matchers.add(new SimpleMatchParentNodeFinder(ref));
        }
        nodeFinder = new CompositeNodeFinder(matchers);
        return this;
    }

    public AtLeastXDirective useRegexes(String... regexes) {
        List<NodeFinder> matchers = new ArrayList<>();
        for (String regex : regexes) {
            matchers.add(new RegexNodeMatcher(regex, nodeFactory));
        }
        nodeFinder = new CompositeNodeFinder(matchers);
        return this;
    }

    @Override
    public boolean applyTo(Node node) {

        Set<Ref> matches = nodeFinder.getMatchingRefs(node);
        // ^^this node should contain the X nodes

        if (matches.size() > 0) {
            for (Ref match : matches) {
                applyForChildRef(node, match);
            }

            return true;
        } else {
            return false;
        }
    }

    private void applyForChildRef(Node node, Ref atLeastXRef) {
            int currentCount = getChildren(node, atLeastXRef.last()).size();
            if (currentCount < atLeastCount) {
                int numToAdd = atLeastCount - currentCount;

                // so now add them!
                for (int i = 0; i < numToAdd; i++) {
                    try {
                        Node newNode = nodeFactory.getFactoryNode(atLeastXRef, i);
                        Ref thisRef = Ref.ref(node);
                        NodeUtils.insertNodeByExample(newNode, node, nodeFactory.getExampleNode(thisRef));
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    @Override
    public AtLeastXDirective setNodeFactory(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        return this;
    }
}
