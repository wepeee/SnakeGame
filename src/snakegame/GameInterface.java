package snakegame;

import java.util.Random;
import javax.swing.Timer;


public abstract interface GameInterface { //Abstract & Interface
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (int) ((double)(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE);//Casting / Convertion
    static final int DELAY = 75;
}
