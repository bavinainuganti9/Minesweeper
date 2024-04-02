import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class Minesweeper extends JFrame implements MouseListener, ActionListener {
    JPanel buttonPanel;
    JToggleButton[][] buttons;
    JButton reset;
    JMenuBar menuBar;
    JMenu difficultyMenu;
    JMenuItem beginner, intermediate, expert;
    ImageIcon[] numbers = new ImageIcon[8];
    ImageIcon flag, mine, smile, win, dead, wait;
    boolean firstClick = true;
    boolean gameOver = false;
    Timer timer;
    JTextField timeField;
    GraphicsEnvironment ge;
    Font clockFont;
    int numMines = 10, clickCount = 0;
    int clickR, clickC;
    int selectedCount = 0;
    int timePassed = 0;

    public static void main(String[] args)
    {
        Minesweeper m = new Minesweeper();
    }

    public Minesweeper()
    {
        this.setSize(1000, 1000);

        for(int x=0;x<8;x++){
            numbers[x] = new ImageIcon("/Users/rithikapathuri/Downloads/Data Structures/Minesweeper Images/"+(x+1)+".png");
            numbers[x] = new ImageIcon(numbers[x].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        }
        flag = new ImageIcon("/Users/rithikapathuri/Downloads/Data Structures/Minesweeper Images/flag2.png");
		flag = new ImageIcon(flag.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
		mine = new ImageIcon("/Users/rithikapathuri/Downloads/Data Structures/Minesweeper Images/mine0.png");
		mine = new ImageIcon(mine.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
		smile = new ImageIcon("/Users/rithikapathuri/Downloads/Data Structures/Minesweeper Images/smile1.png");
		smile = new ImageIcon(smile.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
		win = new ImageIcon("/Users/rithikapathuri/Downloads/Data Structures/Minesweeper Images/win1.png");
		win = new ImageIcon(win.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
		dead = new ImageIcon("/Users/rithikapathuri/Downloads/Data Structures/Minesweeper Images/dead1.png");
		dead = new ImageIcon(dead.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        wait = new ImageIcon("/Users/rithikapathuri/Downloads/Data Structures/Minesweeper Images/wait1.png");
		wait = new ImageIcon(wait.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

        //frame = new JFrame("Minesweeper - The Game");

        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            clockFont=Font.createFont(Font.TRUETYPE_FONT,
                       new File("/Users/rithikapathuri/Downloads/Data Structures/digital-7.ttf"));
            ge.registerFont(clockFont);
       } catch (IOException|FontFormatException e) {

       }

        menuBar = new JMenuBar();
        difficultyMenu = new JMenu("Difficulty");
        beginner = new JMenuItem("Beginner");
        beginner.addActionListener(this);
        intermediate = new JMenuItem("Intermediate");
        intermediate.addActionListener(this);
        expert = new JMenuItem("Expert");
        expert.addActionListener(this);
        difficultyMenu.add(beginner);
        difficultyMenu.add(intermediate);
        difficultyMenu.add(expert);

        reset = new JButton("Reset");
        reset.addActionListener(this);

        timeField = new JTextField("  "+timePassed);
        timeField.setFont(clockFont.deriveFont(18f));
        timeField.setBackground(Color.BLACK);
        timeField.setForeground(Color.GREEN);

        setGrid(9, 9, 10);

        menuBar.add(difficultyMenu);
        menuBar.add(reset);

        this.add(menuBar, BorderLayout.NORTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setGrid(int rows, int cols, int mines)
    {
        if (buttonPanel != null){
            this.remove(buttonPanel);
            this.remove(menuBar);
        }
        numMines = mines;
        gameOver = false;
        this.setSize(cols*40, rows*40);
        buttons = new JToggleButton[rows][cols];
        buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridLayout(rows, cols));
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                buttons[r][c] = new JToggleButton();
                buttons[r][c].addMouseListener(this);
                buttons[r][c].putClientProperty("r", r);
                buttons[r][c].putClientProperty("c", c);
                buttons[r][c].putClientProperty("mineVal", -1);

                buttons[r][c].setFocusable(false);
                buttons[r][c].setFont(new Font("Arial", Font.PLAIN, 18));
                buttonPanel.add(buttons[r][c]);
            }
        }
        this.add(buttonPanel, BorderLayout.CENTER);
        this.revalidate(); //repaint for GUI
    }

    public void dropMines(int rows, int cols)
    {
        int mines = 0;
        while (mines < numMines){
            int r = (int)(Math.random()*rows);
            int c = (int)(Math.random()*cols);
            int state = (int)buttons[r][c].getClientProperty("mineVal");
            if (r != clickR && c != clickC && (int)buttons[r][c].getClientProperty("mineVal") == -1){
                buttons[r][c].putClientProperty("mineVal", 0);
                mines++;
            }
        }
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                int state = (int)buttons[r][c].getClientProperty("mineVal");
                buttons[r][c].setText("" + state);
                if (state!=-1){
                    buttons[r][c].setEnabled(false);
                    int count = 0;
                    for (int rr = r-1; rr<=r+1; rr++){
                        for (int cc = c-1; cc<=c+1; cc++){
                            try{
                                if ((int)buttons[rr][cc].getClientProperty("mineVal") == -1){
                                    count++;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException e){

                            }
                        }
                    }
                    buttons[r][c].putClientProperty("mineVal", count);
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.print(e.getSource());
        clickR=(int)((JToggleButton)e.getComponent()).getClientProperty("r");
        clickC=(int)((JToggleButton)e.getComponent()).getClientProperty("c");
        if(!gameOver){
            if(e.getButton()==MouseEvent.BUTTON1 && buttons[clickR][clickC].isEnabled()){
                if (firstClick){
                    //do stuff
                    timer = new Timer();
                    timer.schedule(new UpdateTimer(), 0, 1000);

                    dropMines(buttons.length, buttons[0].length);
                    firstClick = false;
                }
                int state = (int) buttons[clickR][clickC].getClientProperty("mineVal");;
                if(state == -1){
                    //game lost
                    timer.cancel();
                    gameOver = true;
                    disableBoard();
                    JOptionPane.showMessageDialog(null, "You lose!");
                }
                else{
                    expand(clickR, clickC);
                    clickCount++;
                    if(clickCount == (buttons.length * buttons[0].length - numMines)){
                        JOptionPane.showMessageDialog(null, "You Win!");
                    }
                }
            }
            if(!firstClick && e.getButton()==MouseEvent.BUTTON3){
                if (!buttons[clickR][clickC].isSelected()){
                    if (buttons[clickR][clickC].getIcon() == null){
                        buttons[clickR][clickC].setIcon(flag);
                        buttons[clickR][clickC].setDisabledIcon(flag);
                        buttons[clickR][clickC].setEnabled(false);
                    }
                    else{
                        buttons[clickR][clickC].setIcon(null);
                        buttons[clickR][clickC].setDisabledIcon(null);
                        buttons[clickR][clickC].setEnabled(false);
                    }
                }
            }
        }
    }

    public void expand(int row, int col){
        if(!buttons[row][col].isSelected()){
            buttons[row][col].setSelected(true);
            clickCount++;
        }
        int state = (int) buttons[row][col].getClientProperty("mineVal");
        if(state > 0){
            buttons[row][col].setText(String.valueOf(state));
        }
        else{
            for(int r=row-1;r<=row+1;r++){
                for(int c=col-1;c<=col+1;c++){
                    try{
                        if(!buttons[r][c].isSelected()){
                            expand(r,c);
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){

                    }
                }
            }
        }
    }

    public void disableBoard(){
        for(int r=0;r<buttons.length;r++){
            for(int c=0;c<buttons[r].length;c++){
                ImageIcon icon = (ImageIcon) buttons[r][c].getIcon();
                buttons[r][c].setDisabledIcon(icon);
                buttons[r][c].setEnabled(false);
                int state = (int) buttons[r][c].getClientProperty("mineVal");;
                if(state == -1){
                    buttons[r][c].setIcon(mine);
                    buttons[r][c].setDisabledIcon(mine);
                }
            }
            
        }
    }
    
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == beginner){
            setGrid(9, 9, 10);
            selectedCount = 0;
            firstClick = true;
            if(timer != null){
                timer.cancel();
                timer = new Timer();
            }
            timePassed = 0;
            timeField.setText("  "+timePassed);

        }
        if(e.getSource() == intermediate){
            setGrid(16, 16, 40);
            selectedCount = 0;
            firstClick = true;
            if(timer != null){
                timer.cancel();
                timer = new Timer();
            }
            timePassed = 0;
            timeField.setText("  "+timePassed);
        }
        if(e.getSource() == expert){
            setGrid(16, 40, 99);
            selectedCount = 0;
            firstClick = true;
            if(timer != null){
                timer.cancel();
                timer = new Timer();
            }
            timePassed = 0;
            timeField.setText("  "+timePassed);
        }
        if(e.getSource() == reset){
            timePassed = 0;
            timeField.setText("  "+timePassed);
        }
    }

    public class UpdateTimer extends TimerTask{

        @Override
        public void run() {
            if(!gameOver){
                timePassed++;
                timeField.setText("  "+timePassed);
            }
        }
        
    }
}