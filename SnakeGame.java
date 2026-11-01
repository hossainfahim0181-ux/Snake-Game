import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    static final int SIZE = 600;
    static final int UNIT = 25;
    static final int TOTAL_UNITS = (SIZE * SIZE) / (UNIT * UNIT);
    static final int DELAY = 100;

    final int[] x = new int[TOTAL_UNITS];
    final int[] y = new int[TOTAL_UNITS];

    int bodyParts;
    int score;
    int foodX, foodY;
    char direction;      // U, D, L, R
    boolean running;
    Timer timer;
    Random random = new Random();

    SnakeGame() {
        setPreferredSize(new Dimension(SIZE, SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }

    void startGame() {
        bodyParts = 6;
        score = 0;
        direction = 'R';
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        spawnFood();
        running = true;
        if (timer != null) timer.stop();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    void spawnFood() {
        foodX = random.nextInt(SIZE / UNIT) * UNIT;
        foodY = random.nextInt(SIZE / UNIT) * UNIT;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    void draw(Graphics g) {
        if (running) {
            // food
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT, UNIT);

            // snake
            for (int i = 0; i < bodyParts; i++) {
                g.setColor(i == 0 ? Color.GREEN : new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT, UNIT);
            }

            // score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 25);
        } else {
            gameOver(g);
        }
    }

    void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U': y[0] -= UNIT; break;
            case 'D': y[0] += UNIT; break;
            case 'L': x[0] -= UNIT; break;
            case 'R': x[0] += UNIT; break;
        }
    }

    void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            bodyParts++;
            score++;
            spawnFood();
        }
    }

    void checkCollisions() {
        // body te dhakka
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        // wall e dhakka
        if (x[0] < 0 || x[0] >= SIZE || y[0] < 0 || y[0] >= SIZE) {
            running = false;
        }
        if (!running) timer.stop();
    }

    void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        FontMetrics big = getFontMetrics(g.getFont());
        String over = "Game Over";
        g.drawString(over, (SIZE - big.stringWidth(over)) / 2, SIZE / 2 - 20);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics small = getFontMetrics(g.getFont());
        String s = "Score: " + score;
        g.drawString(s, (SIZE - small.stringWidth(s)) / 2, SIZE / 2 + 30);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        small = getFontMetrics(g.getFont());
        String r = "Press SPACE to restart";
        g.drawString(r, (SIZE - small.stringWidth(r)) / 2, SIZE / 2 + 70);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';   // ulta dike jabe na
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
            case KeyEvent.VK_SPACE:
                if (!running) startGame();               // restart
                break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.add(new SnakeGame());
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
