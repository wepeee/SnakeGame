package snakegame;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, GameInterface{
    
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    private int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    

//    Constructor
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
       newApple();
       running = true;
       timer = new Timer(DELAY, this);
       timer.start();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void draw(Graphics g) throws IOException{
        
        if(running){
            /*
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            */
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }else{
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
//            g.setColor(Color.red);
//            g.setFont(new Font("Ink Free", Font.BOLD, 40));
//            FontMetrics metrics = getFontMetrics(g.getFont());
//            g.drawString("Score: "+getApplesEaten(), (SCREEN_WIDTH - metrics.stringWidth("Score: "+getApplesEaten()))/2, g.getFont().getSize());
        }else{
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
        }
        
    }
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            setApplesEaten(getApplesEaten() + 1);
            newApple();
        }

    }
    public void checkCollisions(){
//        checks if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
//        check if head touches left border
        if(x[0] < 0){
            running = false;
        }
//        check if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
//        check if head touches top border
        if(y[0] < 0){
            running = false;
        }
//        check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }
        
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g) throws IOException{
//        Score Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+getApplesEaten(), (SCREEN_WIDTH - metrics1.stringWidth("Score: "+getApplesEaten()))/2, g.getFont().getSize());
//        Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
//        Restart text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press space to restart", (SCREEN_WIDTH - metrics3.stringWidth("Press space to restart"))/2, SCREEN_HEIGHT/2+50);
             
        
//        Exception Handling
            try{
            // Input & Output
        File file = new File("highscore.txt");
        
        file.createNewFile();
        
        PrintWriter pw = new PrintWriter(file);   
	                        
        pw.println(applesEaten);

        pw.close();
        } catch(IOException e){
                e.printStackTrace();
            }
    }
    
    public void restart(){
        if (!running) {
            setVisible(false);
            new GameFrame();
        }
    }
    
//    public String showHighScore () throws IOException{
//     
//     BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
//     String currentLine = reader.readLine();
//     reader.close();
//    
//    return currentLine;
//    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    restart();  
                    break;
            }
        }
    }

    /**
     * @return the applesEaten
     */
    public int getApplesEaten() {
        return applesEaten;
    }

    /**
     * @param applesEaten the applesEaten to set
     */
    public void setApplesEaten(int applesEaten) {
        this.applesEaten = applesEaten;
    }
    
}
