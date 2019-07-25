package com.scarlatti.mappingdemo.directive;

import com.scarlatti.mappingdemo.factory.NodeFactory;
import com.scarlatti.mappingdemo.util.Ref;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class DirectiveFactory {

    private NodeFactory nodeFactory;

    public DirectiveFactory(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    public AtLeastXDirective atLeast(int x, Ref... refs) {
        return new AtLeastXDirective(x, nodeFactory).useRefs(refs);
    }

    public AtLeastXDirective atLeast(int x, String... regexes) {
        return new AtLeastXDirective(x, nodeFactory).useRegexes(regexes);
    }
}
