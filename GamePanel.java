import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = GameConstants.TOTAL_BRICKS;

    private Timer timer;

    private Paddle paddle;
    private Ball ball;
    private MapGenerator map;

    public GamePanel() {
        map = new MapGenerator(GameConstants.ROWS, GameConstants.COLS);
        paddle = new Paddle(310, 550);
        ball = new Ball(120, 350, -2, -3);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        timer = new Timer(GameConstants.TIMER_DELAY, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, GameConstants.WIDTH - 8, GameConstants.HEIGHT - 8);

        // Bricks
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 3, GameConstants.HEIGHT);
        g.fillRect(0, 0, GameConstants.WIDTH, 3);
        g.fillRect(GameConstants.WIDTH - 7, 0, 3, GameConstants.HEIGHT);

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // Paddle
        paddle.draw(g);

        // Ball
        ball.draw(g);

        // Win
        if (totalBricks <= 0) {
            play = false;
            ball.dx = ball.dy = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!", 260, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // Game over
        if (ball.y > 570) {
            play = false;
            ball.dx = ball.dy = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            // Paddle collision
            if (ball.getBounds().intersects(paddle.getBounds())) {
                ball.reverseY();
            }

            // Brick collision
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

                        if (ball.getBounds().intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ball.x + ball.diameter - 1 <= brickRect.x || ball.x + 1 >= brickRect.x + brickRect.width) {
                                ball.reverseX();
                            } else {
                                ball.reverseY();
                            }
                            break A;
                        }
                    }
                }
            }

            // Move ball
            ball.move();

            // Wall collisions
            if (ball.x < 0) ball.reverseX();
            if (ball.y < 0) ball.reverseY();
            if (ball.x > GameConstants.WIDTH - ball.diameter - 8) ball.reverseX();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (paddle.x < GameConstants.WIDTH - GameConstants.PADDLE_WIDTH - 10) {
                paddle.moveRight();
            }
            play = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (paddle.x > 10) {
                paddle.moveLeft();
            }
            play = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                resetGame();
            }
        }
    }

    private void resetGame() {
        play = true;
        ball = new Ball(120, 350, -2, -3);
        paddle = new Paddle(310, 550);
        score = 0;
        totalBricks = GameConstants.TOTAL_BRICKS;
        map = new MapGenerator(GameConstants.ROWS, GameConstants.COLS);
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
