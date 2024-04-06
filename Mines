using System;
using System.Drawing;
using System.IO;
using System.Windows.Forms;
using System.Timers;

public class Minesweeper : Form
{
    private Panel buttonPanel;
    private Button[,] buttons;
    private Button reset;
    private MenuStrip menuBar;
    private ToolStripMenuItem beginner, intermediate, expert;
    private Image[] numbers = new Image[8];
    private Image flag, mine, smile, win, dead, wait;
    private bool firstClick = true;
    private bool gameOver = false;
    private System.Timers.Timer timer;
    private TextBox timeField;
    private int numMines = 10, clickCount = 0;
    private int clickR, clickC;
    private int selectedCount = 0;
    private int timePassed = 0;

    public Minesweeper()
    {
        this.Size = new Size(1000, 1000);
        // Set up image paths for numbers
        for (int x = 0; x < 8; x++)
        {
            numbers[x] = Image.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Data Structures", "Minesweeper Images", (x + 1) + ".png"));
            numbers[x] = new Bitmap(numbers[x], new Size(60, 60));
        }
        flag = Image.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Data Structures", "Minesweeper Images", "flag2.png"));
        flag = new Bitmap(flag, new Size(60, 60));
        mine = Image.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Data Structures", "Minesweeper Images", "mine0.png"));
        mine = new Bitmap(mine, new Size(60, 60));
        smile = Image.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Data Structures", "Minesweeper Images", "smile1.png"));
        smile = new Bitmap(smile, new Size(60, 60));
        win = Image.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Data Structures", "Minesweeper Images", "win1.png"));
        win = new Bitmap(win, new Size(60, 60));
        dead = Image.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Data Structures", "Minesweeper Images", "dead1.png"));
        dead = new Bitmap(dead, new Size(60, 60));
        wait = Image.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Data Structures", "Minesweeper Images", "wait1.png"));
        wait = new Bitmap(wait, new Size(60, 60));

        menuBar = new MenuStrip();
        beginner = new ToolStripMenuItem("Beginner");
        beginner.Click += DifficultyMenuItem_Click;
        intermediate = new ToolStripMenuItem("Intermediate");
        intermediate.Click += DifficultyMenuItem_Click;
        expert = new ToolStripMenuItem("Expert");
        expert.Click += DifficultyMenuItem_Click;
        beginner.DropDownItems.Add(intermediate);
        beginner.DropDownItems.Add(expert);
        menuBar.Items.Add(beginner);

        reset = new Button();
        reset.Text = "Reset";
        reset.Click += ResetButton_Click;

        timeField = new TextBox();
        timeField.Text = "  " + timePassed;
        timeField.Font = new Font(FontFamily.GenericSansSerif, 18);
        timeField.BackColor = Color.Black;
        timeField.ForeColor = Color.Green;

        SetGrid(9, 9, 10);

        Controls.Add(menuBar);
        Controls.Add(timeField);
        Controls.Add(reset);

        this.FormClosing += Minesweeper_FormClosing;
        this.Visible = true;
    }

    private void SetGrid(int rows, int cols, int mines)
    {
        if (buttonPanel != null)
        {
            Controls.Remove(buttonPanel);
            Controls.Remove(menuBar);
        }
        numMines = mines;
        gameOver = false;
        this.Size = new Size(cols * 40, rows * 40);
        buttons = new Button[rows, cols];
        buttonPanel = new Panel();
        buttonPanel.Size = new Size(cols * 40, rows * 40);
        buttonPanel.Location = new Point(0, 0);
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                buttons[r, c] = new Button();
                buttons[r, c].Size = new Size(40, 40);
                buttons[r, c].Location = new Point(c * 40, r * 40);
                buttons[r, c].MouseClick += Button_MouseClick;
                buttons[r, c].Tag = new Point(r, c);
                buttons[r, c].Font = new Font(FontFamily.GenericSansSerif, 18);
                buttonPanel.Controls.Add(buttons[r, c]);
            }
        }
        Controls.Add(buttonPanel);
        this.Refresh();
    }

    private void DropMines(int rows, int cols)
    {
        int mines = 0;
        Random rand = new Random();
        while (mines < numMines)
        {
            int r = rand.Next(rows);
            int c = rand.Next(cols);
            if (r != clickR && c != clickC && (int)buttons[r, c].Tag == -1)
            {
                buttons[r, c].Tag = 0;
                mines++;
            }
        }
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                int state = (int)buttons[r, c].Tag;
                buttons[r, c].Text = "" + state;
                if (state != -1)
                {
                    buttons[r, c].Enabled = false;
                    int count = 0;
                    for (int rr = r - 1; rr <= r + 1; rr++)
                    {
                        for (int cc = c - 1; cc <= c + 1; cc++)
                        {
                            try
                            {
                                if ((int)buttons[rr, cc].Tag == -1)
                                {
                                    count++;
                                }
                            }
                            catch (IndexOutOfRangeException)
                            {
                            }
                        }
                    }
                    buttons[r, c].Tag = count;
                }
            }
        }
    }

    private void Button_MouseClick(object sender, MouseEventArgs e)
    {
        Button clickedButton = (Button)sender;
        Point location = (Point)clickedButton.Tag;
        clickR = location.X;
        clickC = location.Y;
        if (!gameOver)
        {
            if (e.Button == MouseButtons.Left && clickedButton.Enabled)
            {
                if (firstClick)
                {
                    timer = new System.Timers.Timer();
                    timer.Interval = 1000;
                    timer.Elapsed += Timer_Elapsed;
                    timer.Enabled = true;

                    DropMines(buttons.GetLength(0), buttons.GetLength(1));
                    firstClick = false;
                }
                int state = (int)clickedButton.Tag;
                if (state == -1)
                {
                    timer.Stop();
                    gameOver = true;
                    DisableBoard();
                    MessageBox.Show("You lose!");
                }
                else
                {
                    Expand(clickR, clickC);
                    clickCount++;
                    if (clickCount == (buttons.GetLength(0) * buttons.GetLength(1) - numMines))
                    {
                        MessageBox.Show("You Win!");
                    }
                }
            }
            if (!firstClick && e.Button == MouseButtons.Right)
            {
                if (!clickedButton.Checked)
                {
                    if (clickedButton.BackgroundImage == null)
                    {
                        clickedButton.BackgroundImage = flag;
                        clickedButton.Enabled = false;
                    }
                    else
                    {
                        clickedButton.BackgroundImage = null;
                        clickedButton.Enabled = false;
                    }
                }
            }
        }
    }

    private void Expand(int row, int col)
    {
        if (!buttons[row, col].Checked)
        {
            buttons[row, col].Checked = true;
            clickCount++;
        }
        int state = (int)buttons[row, col].Tag;
        if (state > 0)
        {
            buttons[row, col].Text = state.ToString();
        }
        else
        {
            for (int r = row - 1; r <= row + 1; r++)
            {
                for (int c = col - 1; c <= col + 1; c++)
                {
                    try
                    {
                        if (!buttons[r, c].Checked)
                        {
                            Expand(r, c);
                        }
                    }
                    catch (IndexOutOfRangeException)
                    {
                    }
                }
            }
        }
    }

    private void DisableBoard()
    {
        foreach (Control control in buttonPanel.Controls)
        {
            Button button = (Button)control;
            int state = (int)button.Tag;
            button.Enabled = false;
            if (state == -1)
            {
                button.BackgroundImage = mine;
            }
        }
    }

    private void Timer_Elapsed(object sender, ElapsedEventArgs e)
    {
        if (!gameOver)
        {
            timePassed++;
            timeField.Invoke((MethodInvoker)delegate
            {
                timeField.Text = "  " + timePassed;
            });
        }
    }

    private void DifficultyMenuItem_Click(object sender, EventArgs e)
    {
        ToolStripMenuItem clickedItem = (ToolStripMenuItem)sender;
        if (clickedItem == beginner)
        {
            SetGrid(9, 9, 10);
        }
        else if (clickedItem == intermediate)
        {
            SetGrid(16, 16, 40);
        }
        else if (clickedItem == expert)
        {
            SetGrid(16, 40, 99);
        }
        selectedCount = 0;
        firstClick = true;
        if (timer != null)
        {
            timer.Stop();
            timer = new System.Timers.Timer();
        }
        timePassed = 0;
        timeField.Text = "  " + timePassed;
    }

    private void ResetButton_Click(object sender, EventArgs e)
    {
        timePassed = 0;
        timeField.Text = "  " + timePassed;
    }

    private void Minesweeper_FormClosing(object sender, FormClosingEventArgs e)
    {
        if (timer != null)
        {
            timer.Stop();
            timer.Dispose();
        }
    }

    public static void Main()
    {
        Application.Run(new Minesweeper());
    }
}