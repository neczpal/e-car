package net.neczpal.ecar;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

/**
 * @author neczpal
 */
public class Game extends Thread implements Panel {

    private int car;//0/1/2
    private final List<int[]> oils;
    private final List<Integer> lines;
    private boolean stopped;
    private int level;
    private final int[] levels = new int[]{1, 2, 3, 4, 5, 6, 7, 10, 20, 35, 67, 100, 166, 250, 400};

    public Game() {

        lines = new ArrayList<>();
        oils = new ArrayList<>();
    }

    public void stopGame() {
        stopped = true;
    }

    public void run() {

        stopped = false;
        int speed = 16;
        int next_level = 0;
        level = 0;
        lines.add(100);
        lines.add(300);
        lines.add(500);
        while (!stopped) {
            try {
                //Lines
                for (int i = 0; i < lines.size(); i++) {
                    lines.set(i, lines.get(i) - 2);
                    if (lines.get(i) < -100) {
                        lines.add(500);
                        lines.remove(i);

                        oils.add(new int[]{(int) (Math.random() * 3), 500});
                        ++level;
                        if (next_level < levels.length && level > levels[next_level]) {
                            speed -= 1;
                            next_level++;
                        }
                    }
                }
                //OILS
                for (int i = 0; i < oils.size(); i++) {
                    //collide with car -> main menu
                    if (oils.get(i)[0] == car && 30 <= oils.get(i)[1] && oils.get(i)[1] <= 110) {
                        stopGame();
                        Window.window.changePanel(new MainMenu(level));
                    }


                    oils.set(i, new int[]{oils.get(i)[0], oils.get(i)[1] - 2});

                    if (oils.get(i)[1] < -100) {
                        oils.remove(i);
                    }
                }


                sleep(Math.max(speed, 1));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void mouseEvent() {
    }

    @Override
    public void keyboardEvent() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
                if (car > 0)
                    car--;
            } else if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
                if (car < 2)
                    car++;
            }
        }
    }

    @Override
    public void draw() {
        Window.drawRect(0, 0, 350, 500, Window.road);
        Window.drawString(160, 470, Integer.toString(level), 16);
        for (Integer line : lines) {
            Window.drawRect(110, line, 10, 100, Window.line);
            Window.drawRect(225, line, 10, 100, Window.line);
        }
        for (int[] oil : oils) {
            Window.drawRect(20 + oil[0] * 120, oil[1] + 25, 50, 60, Window.oil);
        }
        Window.drawRect(15 + car * 115, 10, 90, 150, Window.car);
    }
}
