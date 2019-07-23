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

    public Ref2 parent() {
        if (tokens.size() == 0 || tokens.size() == 1)
            return null;

        return new Ref2(tokens.subList(0, tokens.size() - 1));
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

    public String getRefString() {
        return "/" + String.join("/", tokens);
    }

    public List<String> getTokens() {
        return Collections.unmodifiableList(tokens);
    }
}