package com.scarlatti.mappingdemo;

import groovy.util.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.scarlatti.mappingdemo.NodeUtils.childNodes;
import static java.util.Arrays.asList;

public class Ref2 {
    private List<String> tokens;

    public Ref2(String token) {
        tokens = new ArrayList<>();
        tokens.add(token);
    }

    public Ref2(List<String> tokens) {
        this.tokens = tokens;
    }

    public Ref2(Ref2 other) {
        this.tokens = new ArrayList<>(other.tokens);
    }

    @Override
    public String toString() {
        return "Ref2{" +
            "tokens=" + tokens +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ref2 ref2 = (Ref2) o;
        return Objects.equals(tokens, ref2.tokens);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tokens);
    }

    public static Ref2 fromString(String xpath) {
        if (xpath.startsWith("/"))
            xpath = xpath.substring(1, xpath.length());
        return new Ref2(asList(xpath.split("/")));
    }

    public static Ref2 fromNode(Node node) {
        List<String> tokens = new ArrayList<>();
        tokens.add(0, (String) node.name());
        while (node.parent() != null) {
            tokens.add(0, (String) node.parent().name());
            node = node.parent();
        }

        return new Ref2(tokens);
    }

//    public Node resolveAll(Node base) {
//        if (tokens.size() == 0)
//            return base;
//        for (int i = 1; i < tokens.size(); i++) {
//            String token = tokens.get(i);
//            List<Node> nodes = childNodes(base);
//        }
//    }

    public String getRefString() {
        return "/" + String.join("/", tokens);
    }

    public List<String> getTokens() {
        return Collections.unmodifiableList(tokens);
    }
}