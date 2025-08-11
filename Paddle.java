import java.awt.*;

public class Paddle {
    public int x;
    public final int y;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveRight() {
        x += GameConstants.PADDLE_SPEED;
    }

    public void moveLeft() {
        x -= GameConstants.PADDLE_SPEED;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
    }
}
