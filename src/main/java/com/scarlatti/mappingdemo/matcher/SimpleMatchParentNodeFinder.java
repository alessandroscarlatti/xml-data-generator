package com.scarlatti.mappingdemo.matcher;

import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singletonList;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class SimpleMatchParentNodeFinder implements NodeFinder {

    private Ref parentRef;
    private Ref childRef;

    public SimpleMatchParentNodeFinder(Ref childRef) {
        this.childRef = childRef;
        this.parentRef = childRef.parent();
    }

    @Override
    public Set<Ref> getMatchingRefs(Node node) {
        if (Ref.ref(node).equals(parentRef))
            return new HashSet<>(singletonList(childRef));
        else
            return new HashSet<>();
    }
}
