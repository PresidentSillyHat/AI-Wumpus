package wumpusworld;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Derek Wallace
 */
public class Node {
    
    public Node left;
    public Node right;
    public Node down;
    public Node up;
    public int number; //used for debugging
    public int color;
    
    public Node(int num){
        number=num;
    }
    public String toString(){
        return "Node number: "+number;
    }
    public int getNumber(){
        return number;
    }
    public boolean equalNode(Node other){
        return number==other.number;
    }
}
