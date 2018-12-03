package wumpusworld;


/**
 *
 * @author Derek Wallace
 */
public class Node {
    
    public Node left;
    public Node right;
    public Node down;
    public Node up;
    public Node prev;
    //debugging
    public int number; //used for debugging
    public int occupant; //debugging
    //agent info
    public boolean hasGold=false; //subgoal check
    public boolean hasWumpus=false;//for death check
    public boolean hasHole=false; //for death check
    public boolean hasAgent=false; //for clauses
    public boolean safe=false; //used by agent, also inferences
    public int possibleWumpus=0; //used for inferring, more than 1 ensures wumpus
    public boolean possibleHole=false; //used for inferring
    public boolean smelly=false; //state
    public boolean breezy=false; //state
    
    //to prevent looping
    public boolean lCheck=false;
    public boolean uCheck=false;
    public boolean rCheck=false;
    public boolean dCheck=false;
    
    
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
    public Node inDirection(String dir){
        switch (dir) {
            case "left":
                return left;
            case "up":
                return up;
            case "down":
                return down;
            default:
                return right;
        }
        
    }
}
