import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int DOT_SIZE = 10;
    private static final int DELAY = 140;

    private ArrayList<Point> snake;
    private Point food;
    private char direction;
    private boolean running;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                }
            }
        });

        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        direction = 'R';
        running = true;

        Timer timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    move();
                    checkCollision();
                    checkFood();
                    repaint();
                }
            }
        });
        timer.start();

        spawnFood();
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'U':
                newHead.translate(0, -1);
                break;
            case 'D':
                newHead.translate(0, 1);
                break;
            case 'L':
                newHead.translate(-1, 0);
                break;
            case 'R':
                newHead.translate(1, 0);
                break;
        }

        snake.add(0, newHead);
        if (!newHead.equals(food)) {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= WIDTH / DOT_SIZE || head.y < 0 || head.y >= HEIGHT / DOT_SIZE || snake.subList(1, snake.size()).contains(head)) {
            running = false;
            JOptionPane.showMessageDialog(this, "Game Over! Your score: " + (snake.size() - 1));
            System.exit(0);
        }
    }

    private void checkFood() {
        if (snake.get(0).equals(food)) {
            spawnFood();
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH / DOT_SIZE);
        int y = rand.nextInt(HEIGHT / DOT_SIZE);
        food = new Point(x, y);
        while (snake.contains(food)) {
            x = rand.nextInt(WIDTH / DOT_SIZE);
            y = rand.nextInt(HEIGHT / DOT_SIZE);
            food = new Point(x, y);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.RED);
        g.fillRect(food.x * DOT_SIZE, food.y * DOT_SIZE, DOT_SIZE, DOT_SIZE);

        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * DOT_SIZE, point.y * DOT_SIZE, DOT_SIZE, DOT_SIZE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeGame().setVisible(true));
    }
}