package net.neczpal.ecar;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class Window extends Thread{
    private String name;
    private final int width,height;
    private Panel panel;

    private boolean stopped;

    private static int[] font;

    public static int oil,car,sav,ut,utg,gomb,exit;

    public Window(String name, int width, int height){
        super(name);
        this.width = width;
        this.height = height;
        this.panel = new MainMenu();
    }

    public final static void drawRect(int x,int y,int w,int h, int tex){
        int wx=w+x,wy=h+y;

        glBindTexture(GL_TEXTURE_2D, tex);
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 1);
            glVertex2i(x, y);
            glTexCoord2f(1, 1);
            glVertex2i(wx, y);
            glTexCoord2f(1, 0);
            glVertex2i(wx, wy);
            glTexCoord2f(0, 0);
            glVertex2i(x, wy);
        }
        glEnd();
    }

    public final static void drawString(int x, int y, String s, int fs){

        for(int i=0; i<s.length(); ++i){
            drawRect(x+i*fs, y, fs, fs, font[s.charAt(i)]);
        }
    }
    public void changePanel(Panel p){
        panel = p;
        if(p instanceof Game){
            ((Game)p).start();
        }
    }

    public void close(){
        stopped = true;
    }

    public void run() {
        initDisplay();
        initGL();

        Loader.setUseCache(false);
        font = Loader.loadTextures("res/font.png", 8, 8);
        oil = Loader.loadTexture("res/oil.png");
        car = Loader.loadTexture("res/car.png");
        sav = Loader.loadTexture("res/line.png");
        ut = Loader.loadTexture("res/menu.png");
        utg = Loader.loadTexture("res/road.png");
        gomb = Loader.loadTexture("res/new_game.png");
        exit = Loader.loadTexture("res/exit.png");

        stopped = false;

        while(!Display.isCloseRequested() && !stopped){
            glClear(GL_COLOR_BUFFER_BIT);

            mouseEvent();
            keyboardEvent();

            draw();

            Display.sync(80);
            Display.update();
        }
        clean();

        System.exit(0);
    }


    private void initDisplay(){
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle(name);
            Display.create();
            Keyboard.create();
            Mouse.create();

        } catch (LWJGLException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void initGL(){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
        glMatrixMode(GL_MODELVIEW);

        glClearColor(0, 0, 0, 1);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
    private void draw(){
        panel.draw();
    }
    private void mouseEvent(){
        panel.mouseEvent();
    }
    private void keyboardEvent(){
        panel.keyboardEvent();
    }

    private void clean(){
        if(panel instanceof Game)
            ((Game)panel).stopGame();

        Display.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }


    public static Window window;
    public static void main(String[] args){
        window = new Window("Game",350,500);
        window.start();
    }
}
