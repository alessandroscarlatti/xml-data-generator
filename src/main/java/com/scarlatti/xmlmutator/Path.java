package com.scarlatti.xmlmutator;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.scarlatti.xmlmutator.XmlUtils.getIndex;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class Path {
    private List<Token> tokens = new ArrayList<>();

    private Path(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Path(Path other) {
        if (other.tokens != null)
            this.tokens = cloneTokens(other.tokens);
    }

    private List<Token> cloneTokens(List<Token> source) {
        List<Token> target = new ArrayList<>();
        for (Token token : source) {
            target.add(new Token(token));
        }

        return target;
    }

    @Override
    public String toString() {
        return "Path{" +
            "tokens=" + tokens +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(tokens, path.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens);
    }

    public Path parent() {
        if (tokens.size() == 0 || tokens.size() == 1)
            return null;

        return new Path(tokens.subList(0, tokens.size() - 1));
    }

    public Token last() {
        if (tokens.size() == 0)
            return null;

        return tokens.get(tokens.size() - 1);
    }

    public static Path path(String xpath) {
        // need to parse each slash at a time with indeces for []s.
        if (xpath.startsWith("/"))
            xpath = xpath.substring(1, xpath.length());

        List<String> strTokens = asList(xpath.split("/"));
        List<Token> tokens = new ArrayList<>();
        for (String strToken : strTokens) {
            String regex = "(.+)(\\[)([\\d]+)(])";
            String index = strToken.replaceAll(regex, "$3");
            if (!index.equals(strToken)) {
                // true, since the string value was changed.
                // we have an index, e.g. Penguin[1]
                // now find just the name part
                String name = strToken.replace(regex, "$1");
                Token token = new Token(name, Integer.valueOf(index));
                tokens.add(token);
            } else {
                // there is no index, e.g. Penguin
                Token token = new Token(strToken);
                tokens.add(token);
            }
        }

        return new Path(tokens);
    }

    /**
     * Combine two refs
     *
     * @param path the second path
     * @return the first path path followed by the second path path
     */
    public Path resolve(Path path) {
        List<Token> tokens = new ArrayList<>(getTokens());
        tokens.addAll(path.getTokens());
        return new Path(tokens);
    }

    public static Path path(Element node) {
        if (node == null)
            return new Path(emptyList());

        List<Token> tokens = new ArrayList<>();
        tokens.add(getIndex(node), new Token(node.getLocalName()));
        while (node.getParentNode() != null) {
            tokens.add(getIndex(node), new Token(node.getParentNode().getLocalName()));
            node = (Element) node.getParentNode();
        }

        return new Path(tokens);
    }

    public String getRefString() {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append("/");
            sb.append(token.name);
        }

        return sb.toString();
    }

    public String getPathString() {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append("/");
            sb.append(token.name);
            sb.append("[");
            sb.append(token.index + 1);
            sb.append("]");
        }

        return sb.toString();
    }

    public List<Token> getTokens() {
        return Collections.unmodifiableList(cloneTokens(this.tokens));
    }

    public static class Token {
        String name;
        int index;

        public Token() {
        }

        public Token(String name) {
            this.name = name;
        }

        public Token(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public Token(Token other) {
            this.name = other.name;
            this.index = other.index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Token token = (Token) o;
            return index == token.index &&
                Objects.equals(name, token.name);
        }

        @Override
        public int hashCode() {

            return Objects.hash(name, index);
        }
    }
}