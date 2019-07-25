package com.scarlatti.mappingdemo.util;

import groovy.util.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class Ref {
    private List<String> tokens;

    public Ref(String token) {
        tokens = new ArrayList<>();
        tokens.add(token);
    }

    public Ref(List<String> tokens) {
        this.tokens = tokens;
    }

    public Ref(Ref other) {
        this.tokens = new ArrayList<>(other.tokens);
    }

    @Override
    public String toString() {
        return "Ref{" +
            "tokens=" + tokens +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ref ref = (Ref) o;
        return Objects.equals(tokens, ref.tokens);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tokens);
    }

    public Ref parent() {
        if (tokens.size() == 0 || tokens.size() == 1)
            return null;

        return new Ref(tokens.subList(0, tokens.size() - 1));
    }

    public String last() {
        if (tokens.size() == 0)
            return null;

        return tokens.get(tokens.size() - 1);
    }

    public static Ref ref(String xpath) {
        if (xpath.startsWith("/"))
            xpath = xpath.substring(1, xpath.length());
        return new Ref(asList(xpath.split("/")));
    }

    /**
     * Combine two refs
     * @param ref the second ref
     * @return the first ref path followed by the second ref path
     */
    public Ref resolve(Ref ref) {
        List<String> tokens = new ArrayList<>(getTokens());
        tokens.addAll(ref.getTokens());
        return new Ref(tokens);
    }

    public static Ref ref(Node node) {
        if (node == null)
            return new Ref(emptyList());

        List<String> tokens = new ArrayList<>();
        tokens.add(0, (String) node.name());
        while (node.parent() != null) {
            tokens.add(0, (String) node.parent().name());
            node = node.parent();
        }

        return new Ref(tokens);
    }

    public String getRefString() {
        return "/" + String.join("/", tokens);
    }

    public List<String> getTokens() {
        return Collections.unmodifiableList(tokens);
    }
}