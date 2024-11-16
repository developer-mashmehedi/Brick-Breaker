import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreaker extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 5;
    private int playerX = 310;
    private int ballPosX = 120, ballPosY = 350, ballXDir = -2, ballYDir = -3;
    private int[][] map = new int[3][7];
    private int brickWidth = 540 / 7, brickHeight = 150 / 3;

    public BrickBreaker() {
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                map[i][j] = 1;

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                if (map[i][j] > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        if (totalBricks <= 0 || ballPosY > 570) {
            play = false;
            g.setColor(Color.red);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString(ballPosY > 570 ? "Game Over!" : "You Won!", 260, 300);
            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8)))
                ballYDir = -ballYDir;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j] > 0) {
                        int brickX = j * brickWidth + 80, brickY = i * brickHeight + 50;
                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(brickRect)) {
                            map[i][j] = 0;
                            totalBricks--;
                            score += 5;
                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickWidth)
                                ballXDir = -ballXDir;
                            else
                                ballYDir = -ballYDir;
                            break;
                        }
                    }
                }
            }
            ballPosX += ballXDir;
            ballPosY += ballYDir;
            if (ballPosX < 0 || ballPosX > 670) ballXDir = -ballXDir;
            if (ballPosY < 0) ballYDir = -ballYDir;
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < 600) playerX += 20;
        if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 10) playerX -= 20;
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !play) {
            play = true;
            ballPosX = 120;
            ballPosY = 350;
            ballXDir = -2;
            ballYDir = -3;
            playerX = 310;
            score = 0;
            totalBricks = 21;
            for (int i = 0; i < map.length; i++)
                for (int j = 0; j < map[0].length; j++)
                    map[i][j] = 1;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        BrickBreaker gamePanel = new BrickBreaker();
        frame.setBounds(10, 10, 700, 600);
        frame.setTitle("Brick Breaker");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.setVisible(true);
    }
}
