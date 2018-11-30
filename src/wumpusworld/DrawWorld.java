package wumpusworld;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Derek Wallace
 */
public class DrawWorld extends JFrame{

    private static BufferedImage drawer;
    private static JPanel boardPanel;
    private static DrawWorld inst=null;
    public static int height;
    public static int width;

    //using static to ease repainting, personal preference
    public static DrawWorld getInstance() throws IOException{
        if (inst==null){
            inst = new DrawWorld();}
        inst.repaint();	//updates board when needed
        return inst;
    }
    
    //window stuff
    public DrawWorld() throws IOException{	//The GUI Frame
        super("Wumpus World with Derek and Dillon");
 
        drawer=new BufferedImage(1500,1000,BufferedImage.TYPE_INT_ARGB);

        //this.setLayout(new GridBagLayout());
        boardPanel=new JPanel(){
        
            @Override
            protected void paintComponent(Graphics g) {
                
                super.paintComponent(g);
                g.drawImage(drawer, 0, 0, null);	
                
            }
        };
        boardPanel.setSize(1500, 1000);
        this.add(boardPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500,1250);	//Good size for window bc of board
        this.setVisible(true);
    
    }
    
    //pass the initial numberedMap to GUI so it can draw layout
    public static void setBoard(int [][] newMap) throws IOException{

        int h=newMap.length,w=newMap[0].length;
        int tileHeight=1000/h, tileWidth=1250/w,x=0,y=0;
        height=h;width=w;
        
        Graphics g=drawer.getGraphics();
        for(int i=0;i<h;i++){
            for(int j=0;j<w;j++){
            //logic to set up board
                x=j*tileWidth;
                y=i*tileHeight;
                g.setColor(new Color(200,200,200)); 
                g.fillRect(x, y, tileWidth, tileHeight); //make all blackground grey
                switch (newMap[i][j]) {
        
                    case 0:
                        g.setColor(Color.black); //Wall
                        break;
                    case 1:
                        g.setColor(Color.blue); //wumpus
                        break;
                    case 2:
                        g.setColor(Color.cyan); //agent
                        break;
                    case 3:
                        g.setColor(Color.black); //hole
                        break;
                    case 4:
                        g.setColor(Color.yellow); //gold
                        break;
                    default:
                        //empty
                        break;
                }
                g.fillRect(x, y, tileWidth, tileHeight); //take out once objects are drawn
                g.setColor(new Color(0,0,0));
                g.drawRect(x, y, tileWidth, tileHeight); //outline for visibility
            }
        }
        DrawWorld.getInstance();	//Repaint board to show changes
        g.dispose();	//Free up space from Graphics g
        
    }
    
    //update board, format is numberedMap[x0][y0]
    public static void updateBoard(int x0, int y0,int type) throws IOException{
        
        int tileHeight=1000/height, tileWidth=1250/width,x,y;
        Graphics g=drawer.getGraphics();        
        x=x0*tileWidth;
        y=y0*tileHeight;
        switch (type) {
            case 0:
                g.setColor(Color.black); //empty
                break;
            case 1:
                g.setColor(Color.blue); //wumpus
                break;
            case 2:
                g.setColor(Color.cyan); //agent
                break;
            case 3:
                //hole
                break;
            case 4:
                //gold
                break;
            default:
                g.setColor(new Color(200+type*20,200+type*20,200+type*20));   //grey
                break;
        }

        g.fillRect(x+tileWidth/4, y+tileHeight/4, tileWidth/2, tileHeight/2);
        g.setColor(new Color(128,128,128));
        g.drawRect(x, y, tileWidth, tileHeight); //outline for visibility

        DrawWorld.getInstance();	//Repaint board to show changes
        g.dispose();	//Free up space from Graphics g

    }
    
    public static void drawWumpus(int x, int y){
        
    }
    public static void drawCharacter(int x, int y){
        
    }
    public static void drawGold(int x, int y){
        
    }
    
}
