package com.scarlatti.mappingdemo;

import com.mysql.jdbc.NdbLoadBalanceExceptionChecker;
import groovy.util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alessandro Scarlatti
 * @since Saturday, 7/20/2019
 */
public class Generator {
    // example node
    private Node example;

    // the node each case is based on
    private Node baseNode;

    // each example can have multiples
    private Map<String, Node> baseNodesCardinality2 = new HashMap<>();

    // each example can only occur once
    private Map<String, Node> baseNodesCardinality1 = new HashMap<>();

    /**
     * @return separate cases based on the example node.
     */
    List<Node> generateByExample() {

        // for each

        return new ArrayList<>();
    }
}
