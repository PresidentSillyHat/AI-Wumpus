package wumpusworld;

import javax.swing.JOptionPane;


/**
 *
 * @author Derek Wallace
 */
public class WumpusWorld {


    public static void main(String[] args) throws Exception {
        DrawWorld.getInstance();
        String availableChars[] = {"4x4","5x5","8x8","10x10"};
        int size=4;
        String choice = (String) JOptionPane.showInputDialog(JOptionPane.getRootFrame(),
                    "How big is the wumpus world?",
                    "Choose size",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    availableChars,
                    2);
        if(choice==null)choice="4x4";
        System.out.println(choice);
        try{
            switch(choice){
                case "4x4":
                    size=4;
                    break;
                case "5x5":
                    size=5;
                    break;
                case "8x8":
                    size=8;
                    break;
                default:
                    size=10;
                    break;
            }
                CreateWumpusWorld game=new CreateWumpusWorld(size);
                WumpusClauses wc=new WumpusClauses();
                wc.mainLoop();

        }
        catch(Exception e){System.out.println(e+" "+e.getStackTrace()[0].getLineNumber());}
    }
        
    
    
}
