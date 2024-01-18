import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CATchYouLater extends JFrame implements KeyListener {

    private int level;
    private int n;
    private int catX, catY, mouseX, mouseY;
    private Timer timer;
    private BufferedImage offScreenImage;


    // Constructor initializes the game with given parameters
    public CATchYouLater(int n, int catX, int catY, int mouseX, int mouseY) {
        this.level = 1;
        this.n = n;
        this.catX = 0;
        this.catY = 0;
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        setTitle("Cat-ch You Later - Level " + level);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);

        // Display welcome message and start the game timer
        offScreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        String welcomeMessage = "Use the arrow keys to move and catch the mouse!\nLevel " + level;
        displayMessage(welcomeMessage);

        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveMouse();
                moveCat();
                repaint();
            }
        });
        timer.start();
    }

    // Moves the mouse randomly and checks if it's caught by the cat
    private void moveMouse() {
    Random random = new Random();
    int mouseMovement = random.nextInt(5) + level; // Increase difficulty with level

    // Increase mouse speed by multiplying movement with a factor
    int speedFactor = 1; // Adjust as needed
    int movement = speedFactor * mouseMovement;

    // Avoid the cat by moving away
    int moveDirection = random.nextInt(4) + 1;
    switch (moveDirection) {
        case 1:
            mouseX = Math.max(0, mouseX - movement); // Move left
            break;
        case 2:
            mouseX = Math.min(n - 1, mouseX + movement); // Move right
            break;
        case 3:
            mouseY = Math.max(0, mouseY - movement); // Move up
            break;
        case 4:
            mouseY = Math.min(n - 1, mouseY + movement); // Move down
            break;
    }

    // Decrease catch distance as the level increases
    int baseCatchDistance = 4; // Adjust as needed
    double catchDistance = Math.max(0.8, baseCatchDistance - level); // Ensure catch distance doesn't go below 0.6

    if (distance(catX, catY, mouseX, mouseY) <= catchDistance) {
        timer.stop();
        displayMessage("You caught the mouse!\nLevel " + level);
        if (level < 5) {
            startNextLevel();
        } else {
            displayMessage("Congratulations! You completed all levels!");
            System.exit(0); // Close the application after completing all levels
        }
    }
}

    // Calculates the distance between two points
    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    // Starts the next game level by updating positions
    private void startNextLevel() {
        level++;
        catX = 0;
        catY = 0;


        // Place the mouse in a new random position avoiding the cat
        do {
            mouseX = randomPosition();
            mouseY = randomPosition();
        } while (distance(catX, catY, mouseX, mouseY) <= level);

        setTitle("Cat-ch You Later - Level " + level);

        String levelMessage = "Get ready for Level " + level + "!";
        displayMessage(levelMessage);

        timer.restart();
    }

    // Generates a random position within the grid
    private int randomPosition() {
        return new Random().nextInt(n);
    }

    private void moveCat() {
        // Cat uses userinput now instead of generating its own
    }

    // Displays a message using a JOptionPane
    private void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }


     // Handles key presses to move the cat
     @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (catY > 0) {
                    catY--;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (catY < n - 1) {
                    catY++;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (catX > 0) {
                    catX--;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (catX < n - 1) {
                    catX++;
                }
                break;
        }
    }

    // Not needed but since implementing keylistener it looks for these

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed 
    }


    // Overrides the paint method to draw the game components
    @Override
    public void paint(Graphics g) {
    Graphics offScreenGraphics = offScreenImage.getGraphics();

    // Load background image
    ImageIcon backgroundImage = new ImageIcon("field.png");

    offScreenGraphics.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);

    int cellSize = 50;
    int catSize = cellSize;
    int mouseSize = (int) (cellSize * 0.8);

    ImageIcon catIcon = new ImageIcon("cat.png");
    ImageIcon mouseIcon = new ImageIcon("mouse.gif");

    // Draw cat image
    offScreenGraphics.drawImage(catIcon.getImage(), catX * cellSize, catY * cellSize, cellSize, cellSize, this);

    // Draw mouse image
    offScreenGraphics.drawImage(mouseIcon.getImage(), mouseX * cellSize, mouseY * cellSize, cellSize, cellSize, this);

    g.drawImage(offScreenImage, 0, 0, this);
}

    // Main method to start the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CATchYouLater game = new CATchYouLater(10, 2, 2, 8, 8);
            game.setVisible(true);
        });
    }
}

