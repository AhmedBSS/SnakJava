import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener {

  private static final int SCREEN_WIDTH = 600;
  private static final int SCREEN_HEIGHT = 600;
  private static final int UNIT_SIZE = 30;
  private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
  private static final int DELAY = 75;

  private final int x[] = new int[GAME_UNITS];
  private final int y[] = new int[GAME_UNITS];
  private int bodyParts = 1;
  private int appelsEaten;
  private int appeleX;
  private int appeleY;
  char direction = 'R';
  private boolean running = false;
  private Timer timer;
  private Random rand;

  public GamePanel() {
    rand = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.BLACK);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());
    startGame();
  }

  public void startGame() {
    newAppele();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    if (running) {
      /* 
      for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
        g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
      }
      */
      g.setColor(Color.RED);
      g.fillOval(appeleX, appeleY, UNIT_SIZE, UNIT_SIZE);

      for (int i = 0; i < bodyParts; i++) {
        if (i == 0) {
          g.setColor(Color.GREEN);
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        } else {
          g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
      }
      g.setColor(Color.RED);
      g.setFont(new Font(null,Font.BOLD,40));
      FontMetrics fm = getFontMetrics(g.getFont());
      g.drawString("SCORE : "+appelsEaten,(SCREEN_WIDTH- fm.stringWidth("SCORE : "+appelsEaten))/2,g.getFont().getSize());
    }
    else{
      gameOver(g);
    }
  }

  public void newAppele() {
    appeleX = rand.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
    appeleY = rand.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

  }

  public void move() {
    for (int i = bodyParts; i > 0; i--) {
      x[i] = x[i - 1];
      y[i] = y[i - 1];
    }

    switch (direction) {
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
        break;
    }
  }

  public void checkAppel() {
    if ((x[0] == appeleX) && (y[0] == appeleY)) {
      bodyParts++;
      appelsEaten++;
      newAppele();
    }
  }

  public void checkCollision() {

    for (int i = bodyParts; i > 0; i--) {
      if ((x[0] == x[i]) && (y[0] == y[i]))
        running = false;
    }
    if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT)
      running = false;
    if (!running)
      timer.stop();
  }

  public void gameOver(Graphics g) {

       g.setColor(Color.RED);
      g.setFont(new Font(null,Font.BOLD,40));
      FontMetrics fm1 = getFontMetrics(g.getFont());
      g.drawString("SCORE : "+appelsEaten,(SCREEN_WIDTH- fm1.stringWidth("SCORE : "+appelsEaten))/2,g.getFont().getSize());


    g.setColor(Color.RED);
    g.setFont(new Font(null,Font.BOLD,75));
    FontMetrics fm = getFontMetrics(g.getFont());
    g.drawString("GAME OVER",(SCREEN_WIDTH- fm.stringWidth("GAME OVER"))/2,SCREEN_HEIGHT/2);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (running) {
      move();
      checkAppel();
      checkCollision();
    }
    repaint();
  }

  public class MyKeyAdapter extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          if (direction != 'R')
            direction = 'L';
          break;
        case KeyEvent.VK_RIGHT:
          if (direction != 'L')
            direction = 'R';
          break;
        case KeyEvent.VK_UP:
          if (direction != 'D')
            direction = 'U';
          break;
        case KeyEvent.VK_DOWN:
          if (direction != 'U')
            direction = 'D';
          break;
      }
    }
  }
}
