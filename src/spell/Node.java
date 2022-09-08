package spell;

public class Node implements INode{

    int count; //Gives the count for each word ending with this node
    Node[] children = new Node[26];

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        count++;
    }

    @Override
    public Node[] getChildren() {
        return children;
    }

    /** Need a way to add more children nodes to the array
     *      when new words are added
     */
    public void addChildren() {

    }


}
