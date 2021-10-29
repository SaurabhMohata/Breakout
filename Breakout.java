import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BasicStroke;

class BrickMaker
{
    public int brick_store[][];
    public int b_width;
    public int b_height;
    public BrickMaker(int row , int col){
        brick_store = new int[row][col];
        for(int i = 0; i < brick_store.length; i++){
            for(int j = 0;j < brick_store[0].length; j++){
                brick_store[i][j] = 1;
            }
        }
        b_width = 540/col;
        b_height = 150/row;
    }
    public void make(Graphics2D g) {
        for (int i = 0; i < brick_store.length; i++) {
            for (int j = 0; j < brick_store[0].length; j++) {
                if (brick_store[i][j] > 0) {
                    g.setColor(Color.red);
                    g.fillRect(j * b_width + 80, i * b_height + 50, b_width, b_height);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * b_width + 80, i * b_height + 50, b_width, b_height);

                }
            }

        }
    }
    public void bricks_initialise(int value,int row,int col)
    {
        brick_store[row][col] = value;

    }
}

class start_game extends JPanel implements KeyListener, ActionListener {

    private int playerX = 310;
    private int b_posX = 120;
    private int b_posY = 350;
    private int b_Xdir = -1;
    private int b_Ydir = -2;
    private int bricks_total = 21;
    private Timer Timer;
    private int delay = 8;
    private boolean play = false;
    private int score = 0;
    private BrickMaker brick_store;

    public start_game() {
        brick_store = new BrickMaker(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        Timer = new Timer(delay, this);
        Timer.start();
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        brick_store.make((Graphics2D) g);

        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        g.setColor(Color.yellow);
        g.fillRect(playerX, 550, 100, 8);

        //ball
        g.setColor(Color.GREEN);
        g.fillOval(b_posX, b_posY, 20, 20);

        if (b_posY > 570) {
            play = false;
            b_Xdir = 0;
            b_Ydir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("    Game Over Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("   Press Enter to Restart", 190, 340);
        }
        if(bricks_total == 0){
            play = false;
            b_Ydir = -2;
            b_Xdir = -1;
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("    Game Over: "+score,190,300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("   Press Enter to Restart", 190, 340);


        }

        g.dispose();


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Timer.start();

        if (play) {
            if (new Rectangle(b_posX, b_posY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                b_Ydir = -b_Ydir;
            }

            A:
            for (int i = 0; i < brick_store.brick_store.length; i++) {
                for (int j = 0; j < brick_store.brick_store[0].length; j++) {
                    if (brick_store.brick_store[i][j] > 0) {
                        int b_x = j * brick_store.b_width + 80;
                        int b_y = i * brick_store.b_height + 50;
                        int b_width = brick_store.b_width;
                        int b_height = brick_store.b_height;

                        Rectangle rect = new Rectangle(b_x, b_y, b_width, b_height);
                        Rectangle b_rect = new Rectangle(b_posX, b_posY, 20, 20);
                        Rectangle brickrect = rect;

                        if (b_rect.intersects(brickrect)) {
                            brick_store.bricks_initialise(0, i, j);
                            bricks_total--;
                            score += 5;
                            if (b_posX + 19 <= brickrect.x || b_posX + 1 >= brickrect.x + b_width) {
                                b_Xdir = -b_Xdir;
                            } else {
                                b_Ydir = -b_Ydir;
                            }
                            break A;
                        }
                    }


                }
            }


            b_posX += b_Xdir;
            b_posY += b_Ydir;
            if (b_posX < 0) {
                b_Xdir = -b_Xdir;
            }
            if (b_posY < 0) {
                b_Ydir = -b_Ydir;
            }
            if (b_posX > 670) {
                b_Xdir = -b_Xdir;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                b_posX = 120;
                b_posY = 350;
                b_Xdir = -1;
                b_Ydir = -2;
                score = 0;
                playerX = 310;
                bricks_total = 21;
                brick_store = new BrickMaker(3, 7);

                repaint();
            }
        }
    }

    public void moveRight ()
    {
        play = true;
        playerX += 20;
    }
    public void moveLeft ()
    {
        play = true;
        playerX -= 20;
    }

}

public class Breakout {
    public static void main(String[] args)
    {
        JFrame obj = new JFrame();
        start_game  obj_start= new start_game();
        obj.setBounds(10,10,700,600);
        obj.setTitle("Breakout");
        obj.setResizable(false);

        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        obj.add(obj_start);

    }
}