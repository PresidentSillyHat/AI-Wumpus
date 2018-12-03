package wumpusworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Derek Wallace
 */
public class WumpusClauses {
    
    public Node[] world; //agents map
    public Node loc; //agent location
    public Node start;//home
    public boolean hasArrow=true;
    public int width;//used for drawing
    public String direction; //direction agent is facing
    List<String> dirs=new ArrayList<>(); //directions list for movement
    public boolean holdingGold=false;
    
    public WumpusClauses(){
        world=CreateWumpusWorld.tiles;
        width=CreateWumpusWorld.RealWorld.length;
        seedWorld(); 
        
        String[] dirTemp={"up","right","down","left"};
        dirs.add(dirTemp[0]);
        dirs.add(dirTemp[1]);
        dirs.add(dirTemp[2]);
        dirs.add(dirTemp[3]);
        direction=dirs.get(0);
        dirs.remove(0);
    }
    
    
    /**
     * The loop that runs the agent
     * @throws IOException
     * @throws InterruptedException
     */
    public void mainLoop() throws IOException, InterruptedException{
        //check if you have gold, percept move forward, go home heads back to 1,1
        int score=0;
        loc.prev=loc;
        while(holdingGold==false){
            Thread.sleep(100);
            if(loc.hasHole || wumpus(loc)){System.out.println("Killed"); return;}
            drawTile(loc,8);
            loc.safe=true;
            percept(smelly(loc),breezy(loc),glitter(loc));
            score--;
        }
        DrawWorld.goHome();
        //loop to get home
        while(loc.number!=start.number){
            //navigate to start with search method?
            //set agent color to yellow for better visual
            Thread.sleep(250);
            homing(loc,direction);
        }
        score+=1000;
        System.out.println("Performance measure: "+score);
        
    }
    
    /**
     *
     * @param location
     * @return true if location is smelly
     */
    public boolean smelly(Node location){
        return location.smelly;
    }

    /**
     *
     * @param location
     * @return true if location is breezy
     */
    public boolean breezy(Node location){
        return location.breezy;
    }

    /**
     *
     * @param location
     * @return true if location has gold
     */
    public boolean glitter(Node location){
        return location.hasGold;
    }
    
    /**
     * Checks if an object is at a specific location
     * @param obj 
     * @param loc
     * @return true if object is at location
     */
    public boolean at(Node a, Node b){
        
        return a.number==b.number;
    }
    
    /**
     * Move to safe spot
     * @param location
     * @param dir
     */
    public void move(Node location, String dir) throws IOException, InterruptedException{

        //attempt 2 for looping
        int count=0;
        boolean left=false,right=false,up=false,down=false;
        if(exists(location.up) && location.up.safe){count++;up=true;}
        if(exists(location.right) && location.right.safe){count++;right=true;}
        if(exists(location.down) && location.down.safe){count++;down=true;}
        if(exists(location.left) && location.left.safe){count++;left=true;}
        
        //leads to weird movement
        switch(count){
            case 1:
                //only one so take it
                if(left){dir="left";}
                if(right){dir="right";}
                if(down){dir="down";}
                if(up){dir="up";}
                break;
            default:
                //multiple options, check which have been taken
                if(up && location.up.lCheck!=true && loc.prev.number!=loc.up.number){dir="up";location.up.lCheck=true;break;}
                if(right&& location.right.lCheck!=true && loc.prev.number!=loc.right.number){dir="right";location.right.lCheck=true;break;}
                if(down && location.down.lCheck!=true && loc.prev.number!=loc.down.number){dir="down";location.down.lCheck=true;break;}
                if(left && location.left.lCheck!=true && loc.prev.number!=loc.left.number){dir="left";location.left.lCheck=true;break;}
                break;
        }
        //move agent in appropriate direction
        if(count>1 && exists(loc.inDirection(dir)) && loc.prev.number==loc.inDirection(dir).number){turn();return;}
        if(!exists(location.inDirection(dir)) || (exists(location.inDirection(dir)) && !location.inDirection(dir).safe)){turn();return;}
        
        location.hasAgent=false;
        location.inDirection(dir).prev=location;
        drawTile(location,5);//5 for safe
        loc=location.inDirection(dir);
        loc.hasAgent=true;
        drawTile(loc,2);//2 for agent
    }
    
    /**
     * Agent goes home along safe squares
     * @param location
     * @param dir
     * @throws IOException
     */
    public void homing(Node location, String dir) throws IOException{
        //attempt 2 for looping
        int count=0;
        boolean left=false,right=false,up=false,down=false;
        if(exists(location.up) && location.up.safe){count++;up=true;}
        if(exists(location.right) && location.right.safe){count++;right=true;}
        if(exists(location.down) && location.down.safe){count++;down=true;}
        if(exists(location.left) && location.left.safe){count++;left=true;}
        
        //leads to weird movement
        switch(count){
            case 1:
                //only one so take it
                if(left){dir="left";}
                if(right){dir="right";}
                if(down){dir="down";}
                if(up){dir="up";}
                break;
            default:
                //multiple options, check which have been taken
                if(down && loc.prev.number!=loc.down.number){dir="down";location.down.lCheck=true;break;}
                if(left && loc.prev.number!=loc.left.number){dir="left";location.left.lCheck=true;break;}
                if(up && loc.prev.number!=loc.up.number){dir="up";location.up.lCheck=true;break;}
                if(right&& loc.prev.number!=loc.right.number){dir="right";location.right.lCheck=true;break;}
                break;
        }
        //if you are looking at spot you think is wumpus and have arrow, fire
        if(exists(location.inDirection(dir))){
            if(hasArrow){shoot(location.inDirection(dir));return;}
        }
        //move agent in appropriate direction
        if(count>1 && exists(loc.inDirection(dir)) && loc.prev.number==loc.inDirection(dir).number){turn();return;}
        if(!exists(location.inDirection(dir)) || (exists(location.inDirection(dir)) && !location.inDirection(dir).safe)){turn();return;}
        
        location.hasAgent=false;
        location.inDirection(dir).prev=location;
        drawTile(location,5);//5 for safe
        loc=location.inDirection(dir);
        loc.hasAgent=true;
        drawTile(loc,2);//2 for agent
    
    }
    public void shoot(Node a) throws IOException{
        if(a.hasWumpus){
            System.out.println("Wumpus Killed");
            safe(a);
        }
        else{
            System.out.println("Miss");
        }
        hasArrow=false;
        
    }
    
    /**
     * Cycle to next direction
     */
    public void turn(){
        dirs.add(direction);
        direction=dirs.get(0);
        dirs.remove(0);
    }

    
    
    /**
     * Returns true if agent and wumpus are in same location
     * @param location
     * @return
     */
    public boolean wumpus(Node location){
        return location.hasAgent && location.hasWumpus;
    }
    public boolean hole(Node location){
        return location.hasAgent && location.hasHole;
    }
    
    /**
     * Mark possible Wumpus
     * @param stinky
     */
    public void stench(Node stinky){
        if(!loc.wCheck){ //if possible wumpus spots havent been marked at this spot yet
            if(exists(stinky.left) && !stinky.left.safe){stinky.left.possibleWumpus++;}
            if(exists(stinky.up) && !stinky.up.safe){stinky.up.possibleWumpus++;}
            if(exists(stinky.right) && !stinky.right.safe){stinky.right.possibleWumpus++;}
            if(exists(stinky.down) && !stinky.down.safe){stinky.down.possibleWumpus++;}
            loc.wCheck=true;
        }
    }

    /**
     * Mark possible holes
     * @param breezy
     */
    public void holey(Node breezy){
        if(exists(breezy.left) && !breezy.left.safe){breezy.left.possibleHole=true;}
        if(exists(breezy.up) && !breezy.up.safe){breezy.up.possibleHole=true;}
        if(exists(breezy.right) && !breezy.right.safe){breezy.right.possibleHole=true;}
        if(exists(breezy.down) && !breezy.down.safe){breezy.down.possibleHole=true;}
    }

    /**
     * No smells, mark appropriate safe
     * @param safety
     * @throws IOException
     */
    public void safe(Node safety) throws IOException{
        if(exists(safety.left) ){safety.left.safe=true;drawTile(safety.left,5);}
        if(exists(safety.up) && !safety.up.safe){safety.up.safe=true; drawTile(safety.up,5);}
        if(exists(safety.right) && !safety.right.safe){safety.right.safe=true;drawTile(safety.right,5);}
        if(exists(safety.down) && !safety.down.safe){safety.down.safe=true;drawTile(safety.down,5);}
    }
    /**
     * Function, choose to do something based on perception
     * @param stench
     * @param breeze
     * @param glitter
     */
    public void percept(boolean stench, boolean breeze, boolean glitter) throws IOException, InterruptedException{
        //if at gold, pick up
        if(glitter){
            holdingGold=true;
            loc.hasGold=false;
            System.out.println("Gold found");
            return; //done exploring, just head back
        }
        //if next to wumpus
        if(stench){
            //mark possible wump spots
            drawTile(loc,6);
            stench(loc);
        }
        //if next to pit
        if(breeze){
            //mark possible pits
            drawTile(loc,7);
            holey(loc);
        }
        //explore
        if(!stench && !breeze){
            safe(loc);
        }
        move(loc,direction);
             
    }
    
    /**
     * Makes appropriate nodes smelly and breezy
     *  
     */
    public void seedWorld(){
        for (Node world1 : world) {
            if (world1.hasWumpus) {
                if (exists(world1.left)) {
                    world1.left.smelly = true;
                }
                if (exists(world1.up)) {
                    world1.up.smelly = true;
                }
                if (exists(world1.right)) {
                    world1.right.smelly = true;
                }
                if (exists(world1.down)) {
                    world1.down.smelly = true;
                }
            } else if (world1.hasHole) {
                if (exists(world1.left)) {
                    world1.left.breezy = true;
                }
                if (exists(world1.up)) {
                    world1.up.breezy = true;
                }
                if (exists(world1.right)) {
                    world1.right.breezy = true;
                }
                if (exists(world1.down)) {
                    world1.down.breezy = true;
                }
            }
            else if(world1.hasAgent){
                loc=world1;
                start=world1;
            }
        }
    }
    
    /**
     * exists: helper method to prevent null pointers
     * @param questioned the dubious node that might cause exception
     * @return true if node exists
     */
    public boolean exists(Node questioned){
        if(questioned==null)return false;
        return true;
    }
    
    public void drawTile(Node tile, int type) throws IOException{
        int[] coords=convertNodeNum(tile.number);
        DrawWorld.updateBoard(coords[0], coords[1], type);
    }
    public int[] convertNodeNum(int NN){
        int[] coords=new int[2];
        coords[0]=NN%width;
        coords[1]=NN/width;
        return coords;
    }
}
