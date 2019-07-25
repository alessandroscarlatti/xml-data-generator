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
public interface NodeFinder {
    Set<Ref> getMatchingRefs(Node node);

}
