package com.scarlatti.mappingdemo.matcher;

import com.scarlatti.mappingdemo.util.Ref;
import groovy.util.Node;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class NeverMatchNodeFinder implements NodeFinder {
    @Override
    public Set<Ref> getMatchingRefs(Node node) {
        return emptySet();
    }
}
