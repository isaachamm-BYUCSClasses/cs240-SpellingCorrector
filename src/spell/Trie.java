package spell;

import java.util.Objects;

public class Trie implements ITrie{
    Node root = new Node();
    int wordCount = 0;
    int nodeCount = 0;
    @Override
    public void add(String word) {

        Node[] childrenArray = root.getChildren();
        int charIndex;

        if(find(word) == null) {
            wordCount++;
            for (int i = 0; i < word.length(); i++) {
                charIndex = word.charAt(i) - 'a';
                if(childrenArray[charIndex] == null) {
                    Node newNode = new Node();
                    childrenArray[charIndex] = newNode;
                    nodeCount++;
                }
                if (i == (word.length() - 1)) {
                    childrenArray[charIndex].count = 1;
                }
                childrenArray = childrenArray[charIndex].getChildren();
            }
        }
    }

    @Override
    public Node find(String word) {
        Node[] childrenArray = root.getChildren();
        Node foundNode = new Node();

        for(int i = 0; i < word.length(); i++) {
            int charIndex;
            charIndex = word.charAt(i) - 'a';
            if(childrenArray[charIndex] == null) {
                return null;
            }
            if(i == (word.length() - 1) &&
                    childrenArray[charIndex] != null) {
                foundNode = childrenArray[charIndex];
                foundNode.incrementValue();
            }
            childrenArray = childrenArray[charIndex].getChildren(); // reassigned to check for the next char
        }
        return foundNode;

    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public boolean equals(Object o) {
        /* this is the function that intellij made
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trie trie = (Trie) o;
        return Objects.equals(root, trie.root);*/

//        Node rootNode = o.root;
//
//        return DFS(this.root, o.root);
        return true;
    }

    public boolean DFS(Node thisTrie, Node toCompare) {
        Node[] thisTrieChildren = thisTrie.getChildren();
        Node[] toCompareChildren = toCompare.getChildren();

        for(int i = 0; i < 26; i++) { //26 because of the size of the array
            if(thisTrieChildren[i] != null && toCompareChildren[i] != null) {
                if(thisTrieChildren[i].getValue() != toCompareChildren[i].getValue()) {
                    return false;
                }
                else { DFS(thisTrieChildren[i], toCompareChildren[i]); }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }

    @Override
    public String toString() {
        return "Trie{" +
                "root=" + root +
                '}';
    }
}
