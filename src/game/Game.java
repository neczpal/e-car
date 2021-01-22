package game;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL10;

/**
 *
 * @author neczpal
 */
public class Game extends Thread implements Panel{
    
    private int car;//0/1/2
    private ArrayList<int[]> oils;
    private ArrayList<Integer> savok;
    private boolean stopped;
    private int speed = 200;
    private int szint;
    private final int[] szintek = new int[]{1,2,3,4,5,6,7,10,20,35,67,100,166,250,400};
    private int c;
    public Game(){
        
        savok = new ArrayList<>();
        oils = new ArrayList<>();
    }
    
    public void stopGame(){
        stopped = true;
    }
    public void run(){
        
        stopped = false;
        speed = 16;
        c = 0;
        szint = 0;
        savok.add(100);
        savok.add(300);
        savok.add(500);
        while(!stopped){
            try {
                //SAVOK
                for(int i=0; i < savok.size(); i++){
                    savok.set(i, savok.get(i)-2);
                    if(savok.get(i) < -100){
                       savok.add(500); 
                       savok.remove(i);
                       
                       oils.add(new int[]{(int)(Math.random()*3), 500});
                       ++szint;
                       if(c < szintek.length && szint > szintek[c]){
                           speed-=1;
                           c++;
                       }
                    }
                }
                //OILS
                for(int i=0; i < oils.size(); i++){
                    //ütközik e a carral -> main menu
                    if(oils.get(i)[0] == car && 30 <= oils.get(i)[1] && oils.get(i)[1]<= 110){
                        stopGame();
                        Window.window.changePanel(new MainMenu(szint));
                    }
                        
                        
                    oils.set(i, new int[]{oils.get(i)[0], oils.get(i)[1]-2});
                    
                    if(oils.get(i)[1] < -100){
                       oils.remove(i);
                    }
                }
                
                
                sleep(Math.max(speed,1));
            } catch (InterruptedException ex) {
                
            }
        }
    }
    @Override
    public void mouseEvent() {
    }

    @Override
    public void keyboardEvent() {
        while(Keyboard.next()){
            if(Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()){
                if(car > 0)
                    car--;
            }else if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()){
                if(car < 2)
                    car++;
            }
        }
    }

    @Override
    public void draw() {
        Window.drawRect(0, 0, 350, 500, Window.utg);
        Window.drawString(160, 470, Integer.toString(szint), 16);
        for(int i=0; i < savok.size(); i++){
            Window.drawRect(110, savok.get(i), 10, 100, Window.sav);
            Window.drawRect(225, savok.get(i), 10, 100, Window.sav);
        }
        for(int i=0; i < oils.size(); i++){
            Window.drawRect(20+oils.get(i)[0]*120, oils.get(i)[1]+25, 50, 60, Window.oil);
        }
        Window.drawRect(15+car*115, 10, 90, 150, Window.car);
    }
}
