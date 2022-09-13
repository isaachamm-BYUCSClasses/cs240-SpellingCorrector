package spell;

public class Trie implements ITrie{
    private Node root = new Node();
    private int wordCount = 0;
    private int nodeCount = 1; //start with one for root

    public void resetRoot() {
        root = null;
        root = new Node();
    }
    public Node getRoot() {
        return root;
    }
    @Override
    public void add(String word) {

        Node[] childrenArray = root.getChildren();
        int charIndex;
        Node findCheck = find(word);

        if (findCheck == null) {
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
        else {
            findCheck.incrementValue();
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
                    childrenArray[charIndex] != null &&
                    childrenArray[charIndex].count > 0) {
                foundNode = childrenArray[charIndex];
                return foundNode;
            }
            childrenArray = childrenArray[charIndex].getChildren(); // reassigned to check for the next char
        }
        return null;

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

        if(o == null) return false;
        if(o == this) return true;
        if(o.getClass() != this.getClass()) return false;

        Trie t = (Trie) o;

        if(t.wordCount != this.wordCount ||
            t.nodeCount != this.nodeCount) return false;

        return equalsHelper(this.getRoot(), t.getRoot());
    }
    public boolean equalsHelper(Node thisTrie, Node toCompare) {
        if(thisTrie.getValue() != toCompare.getValue()) return false;

        Node[] thisTrieChildren = thisTrie.getChildren();
        Node[] toCompareChildren = toCompare.getChildren();

        for(int i = 0; i < 26; i++) { //26 because of the size of the array
            if (thisTrieChildren[i] != null && toCompareChildren[i] != null) {
                if (!equalsHelper(thisTrieChildren[i], toCompareChildren[i])) return false;
            } else if (thisTrieChildren[i] != null && toCompareChildren[i] == null) {
                return false;
            } else if (thisTrieChildren[i] == null && toCompareChildren[i] != null) {
                return false;
            }
        }
        return true;

    }
    @Override
    public int hashCode() {
        int index = 0;
        for (int i = 0; i < 26; i++) {
            if (root.getChildren()[i] != null) {
                index = i;
            }
        }
        return wordCount * nodeCount * index;
    }
    @Override
    public String toString() {

        StringBuilder output = new StringBuilder(); // this is just for collecting all output
        StringBuilder currWord = new StringBuilder(); // to keep track of the path down to each node

        toStringHelper(root, currWord, output);
        return output.toString();
    }
    public void toStringHelper(Node n, StringBuilder currWord, StringBuilder output) {

        if(n.getValue() > 0) {
            output.append(currWord).append("\n");
        }

        for(int i = 0; i < 26; i++) {
            Node child = n.getChildren()[i];

            if(child != null) {
                char childChar = (char) (i + 'a');
                currWord.append(childChar);
                toStringHelper(child, currWord, output);
                currWord.deleteCharAt(currWord.length() - 1);
            }
        }

    }

}
