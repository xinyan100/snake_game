import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNITE_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNITE_SIZE;
    static final int DELAY = 75;
    final int x_cor[] = new int[GAME_UNITS];
    final int y_cor[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    Color backgroundColor = new Color(255, 182, 193);
    Color snakeBodyColor = new Color(255, 105, 180);
    Color snakeHeadColor = new Color(255, 20, 147);


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(backgroundColor);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter() {});
        startGame();
    }

    public void startGame(){

        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        draw(g);
    }

    public void draw(Graphics g) {

        if(running){
            for (int i = 0; i < SCREEN_HEIGHT/UNITE_SIZE; i++) {
                g.setColor(Color.WHITE);
                g.drawLine(i*UNITE_SIZE, 0, i*UNITE_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNITE_SIZE, SCREEN_WIDTH, i*UNITE_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNITE_SIZE, UNITE_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(snakeHeadColor);
                    g.fillRect(x_cor[i], y_cor[i], UNITE_SIZE, UNITE_SIZE);
                } else {
                    g.setColor(snakeBodyColor);
                    g.fillRect(x_cor[i], y_cor[i], UNITE_SIZE, UNITE_SIZE);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Futura", Font.BOLD, 40));
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - fm.stringWidth("Score:" + applesEaten))/2,
                    g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }

    public void newApple() {

        appleX = random.nextInt(SCREEN_WIDTH/UNITE_SIZE)*UNITE_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNITE_SIZE)*UNITE_SIZE;

    }

    public void move(){

        for (int i = bodyParts; i > 0; i--) {
            x_cor[i] = x_cor[i - 1];
            y_cor[i] = y_cor[i - 1];
        }

        switch (direction) {
            case 'U':
                y_cor[0] = y_cor[0] - UNITE_SIZE;
                break;
            case 'D':
                y_cor[0] = y_cor[0] + UNITE_SIZE;
                break;
            case 'L':
                x_cor[0] = x_cor[0] - UNITE_SIZE;
                break;
            case 'R':
                x_cor[0] = x_cor[0] + UNITE_SIZE;
                break;
        }
    }

    public void checkApple(){

        if ((x_cor[0] == appleX) && (y_cor[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){

        for (int i = bodyParts; i > 0; i--) {
            if((x_cor[0] == x_cor[i]) && (y_cor[0] == y_cor[i])){
                running = false;
            }
            if(x_cor[0] < 0){
                running = false;
            }
            if(x_cor[0] > SCREEN_WIDTH){
                running = false;
            }
            if(y_cor[0] < 0){
                running = false;
            }
            if(y_cor[0] > SCREEN_HEIGHT){
                running = false;
            }
        }
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g){

        g.setColor(Color.RED);
        g.setFont(new Font("Impact", Font.BOLD, 40));
        FontMetrics fm2 = getFontMetrics(g.getFont());
        g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - fm2.stringWidth("Score:" + applesEaten))/2,
                SCREEN_HEIGHT/2 + 2 * g.getFont().getSize());


        g.setColor(Color.RED);
        g.setFont(new Font("Impact", Font.BOLD, 100));
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - fm.stringWidth("Game Over"))/2,
                SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
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
            }
        }
    }
}
