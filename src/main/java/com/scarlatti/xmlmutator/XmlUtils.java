package com.scarlatti.xmlmutator;

import com.scarlatti.mappingdemo.util.NodeListVisitor;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.*;

/**
 * @author Alessandro Scarlatti
 * @since Monday, 7/22/2019
 */
public class XmlUtils {

    public static Element parseXml(java.nio.file.Path file) {
        try {
            return parseXml(new String(Files.readAllBytes(file), "utf-8"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing xml from file " + file, e);
        }
    }

    public static Element parseXml(String xml) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
            return document.getDocumentElement();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing xml.", e);
        }
    }

    public static String serialize(Element xml) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Node node = document.importNode(xml.cloneNode(true), true);
            document.appendChild(node);
            return serialize(document);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing xml.", e);
        }
    }

    public static String serialize(Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (TransformerException e) {
            throw new RuntimeException("Error serializing xml.", e);
        }
    }

    /**
     * Insert a node into another node in the right order, given an example order by an example node.
     *
     * @param addNode     the node to add
     * @param toNode      the node that will accept the new node
     * @param exampleNode the node that contains the example order of tags
     */
    public static void insertNodeByExample(Element addNode, Element toNode, Element exampleNode) {
        // getFactoryNode the order from the example node
        List<String> order = new ArrayList<>();
        for (Node child : getChildren(exampleNode)) {
            if (!order.contains(child.getLocalName())) {
                order.add(child.getLocalName());
            }
        }

        // now we know the order
        Map<String, List<Element>> nodes = new LinkedHashMap<>();
        for (String key : order) {
            nodes.put(key, getChildrenOfName(toNode, key));
            if (key.equals(addNode.getLocalName())) {
                nodes.get(key).add(addNode);
            }
        }

        removeAllChildren(toNode);

        // now assemble the nodes.
        // first remove all nodes, then add them back
        for (String key : nodes.keySet()) {
            for (Node node : nodes.get(key)) {
                toNode.appendChild(node);
            }
        }
    }

    /**
     * Remove all child elements from this node.
     * @param element
     */
    public static void removeAllChildren(Element element) {
        for (Element child : getChildren(element)) {
            removeNode(child);
        }
    }

    /**
     * Determine if this node contains other tags, or just text
     *
     * @param node
     * @return
     */
    public static boolean isBeanNode(Element node) {
        return !isValueNode(node);
    }

    /**
     * Determine whether or not the given node represents a text value, or a node containing other values.
     *
     * @param node the node to inspect.
     * @return true if the given node represents a single text value only.
     */
    public static boolean isValueNode(Element node) {
        org.w3c.dom.NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
                return false;
        }
        return true;
    }

    /**
     * Convert a NodeList into a List of Nodes.
     *
     * @param nodeList the node list to convert
     * @return the list of nodes
     */
    private static List<Node> toList(NodeList nodeList) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i));
        }
        return nodes;
    }

    /**
     * Walk the given node and call the given walker based on the data in the node.
     *
     * @param node   the node to walk
     * @param walker the walker to call
     */
    public static void walkNode(Element node, ElementVisitor walker) {
        if (isValueNode(node)) {
            walker.visitValueElement(node);
        } else {
            walker.visitBeanElement(node);
        }
    }

    /**
     * Visit each group of child nodes.  The visitor is called once per group of child nodes.
     * If a tag has only one occurrence, it will be in a group by itself.
     * If a tag has multiple occurrences, it will be in a group with the other occurrences, in order of appearance.
     *
     * @param node    the node to query
     * @param visitor the visitor to callback for each group
     */
    public static void visitChildNodesGroupByName(Element node, NodeListVisitor visitor) {
        for (String childName : getUniqueChildrenNames(node)) {
            List<Element> children = getChildrenOfName(node, childName);
//            visitor.visitElementSet(children);
        }
    }

    /**
     * Get the last item in a list.
     *
     * @param list the list to query
     * @param <T>  the type of item in the list
     * @return the last item in the list.
     */
    public static <T> T last(List<T> list) {
        return list.get(list.size() - 1);
    }

    /**
     * Get the index of the current element within the parent, if it exists.
     *
     * @param element the element to find the index for, assumed to be within a parent.
     * @return the index of this element within its parent, else -1.
     */
    public static int getIndex(Element element) {
        if (element.getParentNode() == null)
            return -1;

        int i = 0;
        for (Element child : getChildren(((Element) element.getParentNode()))) {
            if (child == element) {
                return i;
            }
            i++;
        }

        return -1;
    }

    /**
     * Get all the child nodes of the given node, in order
     *
     * @param node the node to query
     * @return all the child nodes
     */
    public static List<Element> getChildren(Element node) {
        List<Element> childElements = new ArrayList<>();
        org.w3c.dom.NodeList childNodeList = node.getChildNodes();
        for (int i = 0; i < childNodeList.getLength(); i++) {
            org.w3c.dom.Node child = childNodeList.item(i);
            if (child instanceof Element)
                childElements.add(((Element) child));
        }
        return childElements;
    }

    /**
     * Get all the child nodes of the given name from the given node.
     *
     * @param node the node to query
     * @param name the tag name to serach for
     * @return all the children with that tag name, in order of appearance
     */
    public static List<Element> getChildrenOfName(Element node, String name) {
        List<Element> nodes = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);
            if (child instanceof Element && child.getLocalName().equals(name)) {
                nodes.add(((Element) child));
            }
        }

        return nodes;
    }

    /**
     * Get the unique names of child nodes in the given node.
     * If a tag occurs twice, its name is recorded only once.
     *
     * @param node the node to query
     * @return ordered set of unique child tag names
     */
    public static Set<String> getUniqueChildrenNames(Element node) {
        Set<String> names = new LinkedHashSet<>();
        List<Element> nodes = getChildren(node);
        for (Element child : nodes) {
            names.add(child.getLocalName());
        }
        return names;
    }

    /**
     * Remove the node from its parent.
     * Assumes that the node has a parent.
     *
     * @param node the node to remove from its parent.
     */
    public static void removeNode(Element node) {
        if (node.getParentNode() != null)
            node.getParentNode().removeChild(node);
    }

    /**
     * Get refs to each child tag occurring more than once (plural).
     *
     * @param node the node to search
     * @return refs to each plural child tag.
     */
    public static List<Path> getPlurals(Element node) {
        Set<Path> plurals = new LinkedHashSet<>();
        XmlUtils.walkNode(node, new RecursiveXmlVisitor() {
            @Override
            public void visitBeanElement(Element node) {
                visitChildNodesGroupByName(node, nodes -> {
//                    if (nodes.size() > 1)
//                        plurals.add(Path.path(nodes.get(0)));
                });

                super.visitBeanElement(node);
            }
        });
        return new ArrayList<>(plurals);
    }

    /**
     * Remove duplicate child tags from a node.
     *
     * @param node the node in which to remove duplicate child tags.
     */
    public static void removePlurals(Element node) {
        List<Path> plurals = XmlUtils.getPlurals(node);

        XmlUtils.walkNode(node, new RecursiveXmlVisitor() {
            @Override
            public void visitValueElement(Element node) {
                if (plurals.contains(Path.path(node)))
                    removeNode(node);
            }

            @Override
            public void visitBeanElement(Element node) {
                if (plurals.contains(Path.path(node)))
                    removeNode(node);
                else
                    super.visitBeanElement(node);
            }
        });
    }
}
