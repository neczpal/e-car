package game;

import org.lwjgl.input.Mouse;

/**
 *
 * @author neczpal
 */
public class MainMenu implements Panel{
    
    private int szint;
    
    public MainMenu(){
        this(-1);
    }
    public MainMenu(int szint){
        this.szint = szint;
    }

    @Override
    public void mouseEvent() {
        if(Mouse.isButtonDown(0) && 125 <= Mouse.getX() && Mouse.getX() <= 225){
            if(167 <= Mouse.getY() && Mouse.getY() <= 217){
                Window.window.changePanel(new Game());
            }
            else if(97 <= Mouse.getY() && Mouse.getY() <= 147){
                Window.window.close();
            }
        }
    }

    @Override
    public void keyboardEvent() {
    }

    @Override
    public void draw() {
        Window.drawRect(0, 0, 350, 500, Window.ut);
        
        if(szint != -1)
            Window.drawString(160, 470, Integer.toString(szint), 16);
        
        Window.drawRect(125, 167, 100, 50, Window.gomb);
        Window.drawRect(125, 97, 100, 50, Window.exit);
    }
}
