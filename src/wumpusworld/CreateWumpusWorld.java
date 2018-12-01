package wumpusworld;

import java.util.ArrayList;
import java.util.List;
import  java.lang.Math;

/**
 *
 * @author Derek Wallace
 */
public class CreateWumpusWorld {
    public static Node[] tiles;
    public static Node start;

    public CreateWumpusWorld() throws Exception{
        int[][] numberedMap=addMap(10); //change the map size here. 
        tiles=linkNodes(numberedMap);
        DrawWorld.setBoard(numberedMap);
    }

    public static int[][]addMap(int size){
        boolean goldflag=false;
        boolean wumpflag = false;
        String endcap = "";
        while(endcap.length()<size+2)
        {
            endcap+="%";
        }



        List<String> line=new ArrayList();
        line.add(endcap);
        for(int y=0;y<size;y++) {
            String linestring = "%";
            for (int x = 0; x < size; x++) {
                double rand = Math.random();
                //System.out.println(rand);
                if (y==size-1 && x==0) {
                    linestring+="A";
                }
                else if(rand<.2){
                    linestring+="H";
                }
                else{
                    linestring+=" ";
                }
            }

           linestring+="%";
            //System.out.println(linestring);
            line.add(linestring);
        }

        line.add(endcap);
       // System.out.println(line);
        while(!goldflag) {
            int goldx = (int) (Math.random() * ((size) + 1));
            int goldy = (int) (Math.random() * ((size) + 1));
            String temp = line.get(goldy);
            char letter = temp.charAt(goldx);
            if(letter==' '){
                char[]linechars = temp.toCharArray();
                linechars[goldx]='G';
                temp=String.valueOf(linechars);
                line.set(goldy,temp);
                goldflag=true;
            }
        }
        while(!wumpflag) {
            int wumpx = (int) (Math.random() * ((size) + 1));
            int wumpy = (int) (Math.random() * ((size) + 1));
            String temp = line.get(wumpy);
            char letter = temp.charAt(wumpx);
            if(letter==' '){
                char[]linechars = temp.toCharArray();
                linechars[wumpx]='W';
                temp=String.valueOf(linechars);
                line.set(wumpy,temp);
                wumpflag=true;
            }
        }
        for(int x = 0; x<line.size();x++)
        {
            System.out.println(line.get(x));
        }

        return fillMap(line);
    }

    //fill the array representing map with numbers
    public static int[][] fillMap(List<String> lines){
        int[][] map=new int[lines.size()][lines.get(0).length()];
        //find values of each spot
        for(int i=0;i<lines.size();i++){
            for(int j=0;j<lines.get(i).length();j++){
                char c=lines.get(i).charAt(j);
                switch (c) {
                    case '%': //wall
                        map[i][j]=0;
                        break;
                    case ' ': //empty
                        map[i][j]=-1;
                        break;
                    case 'W': //wumpus
                        map[i][j]=1;
                        break;
                    case 'A': //agent or start
                        map[i][j]=2;
                        break;
                    case 'H': //hole
                        map[i][j]=3;
                        break;
                    case 'G': //gold
                        map[i][j]=4;
                        break;
                    default:
                        map[i][j]=5; //shouldn't happen
                        break;
                }
            }
        }
        return map;
    }
    //link nodes together properly so navigation is possible
    public static Node[] linkNodes(int[][] map){
        Node[] Maze=new Node[map.length*map[0].length];
        int nodeNum=0;
        //may i have some loops brother
        for(int i=0;i<Maze.length;i++){Maze[i]=new Node(i);}
        //connect left/right
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){

                if(nodeNum+1<Maze.length && (nodeNum+1)%(map[0].length)!=0){
                    Maze[nodeNum].right=Maze[nodeNum+1];
                    Maze[nodeNum+1].left=Maze[nodeNum];
                }
                if(map[i][j]>0){Maze[nodeNum].color=map[i][j];} //setting source colors
                nodeNum++;
            }
        }
        nodeNum=0;
        //connect up/down
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                //both spots aren't walls
                if(nodeNum+map[0].length<Maze.length){
                    Maze[nodeNum].down=Maze[nodeNum+map[0].length];
                    Maze[nodeNum+map[0].length].up=Maze[nodeNum];
                }

                nodeNum++;
            }
        }

        return Maze;
    }
}
