package it.cnr.ilc.lexo.service.conll.core;

import java.util.List;

/**
 * Created by mvojtek on 05/07/2017.
 */
public interface EnhancedDeps {
    List<EnhancedDeps.Dep> getDeps();

    String toStringFormat();

    abstract class RootNode {
        public abstract boolean isRoot();
        public Sentence getRoot() {return null;}
        public Token getNode() {return null;}

        public int getOrd() {
            if (isRoot()) {
                return 0;
            } else {
                return getNode().getOrd();
            }
        }
    }

    class RootNodeDep extends RootNode {

        private final Sentence root;

        public RootNodeDep(Sentence root) {
            this.root = root;
        }

        @Override
        public boolean isRoot() {
            return true;
        }

        @Override
        public Sentence getRoot() {
            return root;
        }
    }

    class NodeDep extends RootNode {

        private final Token node;

        public NodeDep(Token node) {
            this.node = node;
        }

        @Override
        public boolean isRoot() {
            return false;
        }

        @Override
        public Token getNode() {
            return node;
        }
    }

    class Dep {

        private final RootNode head;
        private final String rel;

        public Dep(RootNode head, String rel) {
            this.head = head;
            this.rel = rel;
        }

        public RootNode getHead() {
            return head;
        }

        public String getRel() {
            return rel;
        }
    }
}
