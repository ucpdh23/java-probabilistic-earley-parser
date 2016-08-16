
package org.leibnizcenter.cfg.earleyparser.parse;

import org.leibnizcenter.cfg.Grammar;
import org.leibnizcenter.cfg.category.Category;
import org.leibnizcenter.cfg.earleyparser.chart.ScannedTokenState;
import org.leibnizcenter.cfg.earleyparser.chart.State;

import java.util.LinkedList;
import java.util.List;
//TODO

/**
 * A parse tree that represents the derivation of a string based on the
 * rules in a {@link Grammar}. Parse trees recursively contain
 * {@link #getChildren() other parse trees}, so they can be iterated through to
 * find the entire derivation of a category.
 * <p>
 * Parse trees are essentially partial views of a {@link Chart} from a
 * given {@link State} or {@link Category}. They represent the completed
 * category at a given string index and origin position. The special
 * {@link Category#START} category is not included in a parse tree at the root
 * (only category that are actually specified in the corresponding grammar
 * are represented).
 */
public abstract class ParseTree {
    final Category category;
    final LinkedList<ParseTree> children;

    /**
     * Creates a new parse tree with the specified category and parent parse
     * tree.
     */
    public ParseTree(Category category) {
        this(category, new LinkedList<>());
    }

    /**
     * Creates a new parse tree with the specified category, parent, and
     * child trees.
     *
     * @param category The category of the {@link #getCategory() category} of this parse
     *                 tree.
     * @param children The list of children of this parse tree, in their linear
     *                 order.
     */
    public ParseTree(Category category, LinkedList<ParseTree> children) {
        this.category = category;
        this.children = children;
    }

    /**
     * Gets the category category of this parse tree.
     *
     * @return <code>NP</code> for a subtree <code>NP -> Det N</code>.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Gets the child parse trees of this parse tree, retaining their linear
     * ordering.
     *
     * @return For a subtree <code>NP -> Det N</code>, returns an array
     * that contains parse trees whose {@link #getCategory() node} is
     * <code>Det, N</code> in that order, or <code>null</code> if this parse
     * tree has no children.
     */
    public List<ParseTree> getChildren() {
        return children;
    }


    /**
     * Gets a string representation of this parse tree.
     *
     * @return For the string &quot;the boy left&quot;, possibly something like:
     * <blockquote><code>[S[NP[Det[the]][N[boy]]][VP[left]]]</code></blockquote>
     * (The actual string would depend on the grammar rules in effect for the
     * parse).
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(category.toString());

        // recursively append children
        if (children != null) for (ParseTree child : children) sb.append(child.toString());

        sb.append(']');

        return sb.toString();
    }

    public void addRightMost(ParseTree tree) {
        children.addLast(tree);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParseTree parseTree = (ParseTree) o;
        return category.equals(parseTree.category) && (children != null ? children.equals(parseTree.children) : parseTree.children == null);
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

//    @Deprecated
//    public <E> TokenParseTree mapTokensToLeafnodes(final LinkedList<Token<E>> tokens) {
//        if (!hasChildren()) throw new IssueRequest("This is a bug.");
//        ArrayList<TokenParseTree> newChildren = new ArrayList<>(children.size());
//        for (ParseTree child : children)
//            if (!child.hasChildren()) newChildren.add(new TokenParseTree<>(child.category, tokens.pop()));
//            else newChildren.add(child.mapTokensToLeafnodes(tokens));
//        return new TokenParseTree(this.category, newChildren);
//    }

    private boolean hasChildren() {
        return children == null || children.size() > 0;
    }

    public static class Token<E> extends ParseTree {
        public final org.leibnizcenter.cfg.token.Token<E> token;

        public Token(org.leibnizcenter.cfg.token.Token<E> scannedToken, Category category) {
            super(category, null);
            this.token = scannedToken;
        }

        public Token(ScannedTokenState<E> scannedState) {
            this(scannedState.scannedToken, scannedState.scannedCategory);
        }
    }

    public static class NonToken extends ParseTree {
        public NonToken(Category node) {
            super(node);
        }

        public NonToken(Category node, LinkedList<ParseTree> children) {
            super(node, children);
        }
    }

//    @Deprecated
//    public class TokenParseTree<E> {
//        public final Token<E> token;
//        public final List<TokenParseTree> children;
//        private final Category category;
//
//        public TokenParseTree(Category category, Token<E> token) {
//            this.token = token;
//            this.category = category;
//            this.children = null;
//        }
//
//        public TokenParseTree(Category category, ArrayList<TokenParseTree> children) {
//            this.token = null;
//            this.category = category;
//            this.children = Collections.unmodifiableList(children);
//        }
//    }
}