import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;

@SuppressWarnings("removal")
public class Block extends JPanel implements ActionListener, KeyListener//应该是继承JPanel
{
    static Button[] but = new Button[8];
    static Button noStop = new Button("PAUSE");
    static Label scoreLab = new Label("Score:");
    static Label infoLab = new Label("tips:");
    static Label speedLab = new Label("LV:");
    static Label scoreTex = new Label("0");
    static Label infoTex = new Label(" ");
    static Label speedTex = new Label("1");
    static Label registLab = new Label("hold:");
    static Label nextLab = new Label("coming:");
    static JFrame jf = new JFrame();
    static MyTimer timer;
    static ImageIcon icon = new ImageIcon();//icon adress here
    static JMenuBar mb = new JMenuBar();
    static JMenu menuGame = new JMenu("GAME");
    static JMenu menuHelp = new JMenu("HELP");
    static JMenuItem newGame = new JMenuItem("new GAME");
    static JMenuItem exitGame = new JMenuItem("EXIT");
    static JMenuItem about = new JMenuItem("HELP");
    static JDialog dlg_1;
    static JTextArea dlg_1_text = new JTextArea();
    static int startSign = 0;//游戏开始标志 0 未开始 1 开始 2 暂停
    static String[] butLab = {"start", "restart", "levelDOWN", "levelUP", "pause", "exit"};
    static int[][] game_body = new int[20][11];
    static int[] game_sign_x = new int[4];//用于记录4个方格的水平位置
    static int[] game_sign_y = new int[4];//用于记录4个方格的垂直位置
    static boolean downSign = false;//是否落下
    static int[] blockNumber = new int[2];//砖块的编号
    static int gameScore = 0;//游戏分数
    static int speedMark = 1;//speed mark
    static int speedMark_log = 1;//level mark
    static int regNum = 0;//寄存位砖块
    static boolean changeFlag = true;//方块是否可以寄存
    Color blockColor;
    Color holdColor;
    Color nextColor;
    int nextBlock;

    public static void main(String[] args) {
        Block myBlock = new Block();
        mb.add(menuGame);
        mb.add(menuHelp);
        menuGame.add(newGame);
        menuGame.add(exitGame);
        menuHelp.add(about);
        jf.setJMenuBar(mb);

        myBlock.init();
        jf.add(myBlock);
        jf.setSize(565, 501);
        jf.setResizable(false);
        jf.setTitle("Tetris:demo");
        jf.setIconImage(icon.getImage());
        jf.setLocation(200, 100);
        jf.show();
        timer = new MyTimer(myBlock); //启动线程
        timer.setDaemon(true);
        timer.start();
        timer.suspend();
    }

    public void init() {
        setLayout(null);
        for (int i = 0; i < 6; i++) {
            but[i] = new Button(butLab[i]);
            add(but[i]);
            but[i].addActionListener(this);
            but[i].addKeyListener(this);
            but[i].setBounds(360, (240 + 30 * i), 160, 25);
        }

        add(scoreLab);
        add(scoreTex);
        add(speedLab);
        add(speedTex);
        add(infoLab);
        add(infoTex);
        add(registLab);
        add(scoreLab);
        add(nextLab);

        scoreLab.setBounds(320, 15, 35, 20);
        scoreTex.setBounds(360, 15, 160, 20);
        scoreTex.setBackground(Color.white);
        speedLab.setBounds(320, 45, 30, 20);
        speedTex.setBounds(360, 45, 160, 20);
        speedTex.setBackground(Color.white);
        infoLab.setBounds(320, 75, 30, 20);
        infoTex.setBounds(360, 75, 160, 20);
        infoTex.setBackground(Color.white);
        registLab.setBounds(320, 105, 30, 20);
        nextLab.setBounds(410,105,40,20);

        but[1].setEnabled(false);
        but[4].setEnabled(false);
        noStop.setBounds(360, 360, 160, 25);
        noStop.addActionListener(this);
        noStop.addKeyListener(this);
        newGame.addActionListener(this);
        exitGame.addActionListener(this);
        about.addActionListener(this);
        num_csh_game();
        nextBlock=randomNum();
        blockNumber = blockList(nextBlock);
        blockCreator(blockNumber[0]);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == but[0])//开始游戏
        {
            startSign = 1;
            infoTex.setText("START!");
            but[0].setEnabled(false);
            but[1].setEnabled(true);
            but[4].setEnabled(true);
            timer.resume();
        }
        if (e.getSource() == but[1] || e.getSource() == newGame)//重新开始游戏
        {
            startSign = 0;
            gameScore = 0;
            timer.suspend();
            num_csh_restart();
            repaint();
            nextBlock=randomNum();
            blockNumber = blockList(nextBlock);
            blockCreator(blockNumber[0]);
            nextBlock= blockNumber[1];
            scoreTex.setText("0");
            infoTex.setText("NEW GAME!");
            but[0].setEnabled(true);
            but[1].setEnabled(false);
            but[4].setEnabled(false);
        }
        if (e.getSource() == but[2])//降低级数
        {
            infoTex.setText("LV DOWN!");
            speedMark_log--;
            speedMark = speedMap(speedMark_log);
            if (speedMark_log <= 1) {
                speedMark = speedMap(1);
                speedMark_log = 1;
                infoTex.setText("LOWEST!");
            }
            speedTex.setText(speedMark_log + "");
        }
        if (e.getSource() == but[3])//提高级数
        {
            infoTex.setText("LV UP!");
            speedMark_log++;
            speedMark = speedMap(speedMark_log);
            if (speedMark_log >= 10) {
                speedMark = speedMap(10);
                speedMark_log = 10;
                infoTex.setText("HIGHEST!");
            }
            speedTex.setText(speedMark_log + "");
        }
        if (e.getSource() == but[4])//游戏暂停
        {
            this.add(noStop);
            this.remove(but[4]);
            infoTex.setText("PAUSE");
            timer.suspend();
        }
        if (e.getSource() == noStop)//取消暂停
        {
            this.remove(noStop);
            this.add(but[4]);
            infoTex.setText("CONTINUE");
            timer.resume();
        }
        if (e.getSource() == but[5] || e.getSource() == exitGame)//退出游戏
        {
            jf.dispose();
        }
        if (e.getSource() == about)//退出游戏
        {
            dlg_1 = new JDialog(jf, "HELP");
            try {
                FileInputStream io = new FileInputStream("C:/MyJava/BeginnerTesta/outExp/resource/about.txt");//得到路径
                byte[] a = new byte[io.available()];
                io.read(a);
                io.close();
                String str = new String(a);
                dlg_1_text.setText(str);
            } catch (Exception ignored) {
            }
            dlg_1_text.setEditable(false);
            dlg_1.add(dlg_1_text);
            dlg_1.pack();
            dlg_1.setResizable(false);
            dlg_1.setSize(200, 120);
            dlg_1.setLocation(400, 240);
            dlg_1.show();
        }
    }

    public int randomNum() {
        int num;//random block number
        num = (int) (Math.random() * 7) + 1;//产生0~6之间的随机数 attention:from(1)to(7-1)!!
        return num;
    }
    public int[] blockList(int random){
        int[] blockNum=new int[2];
        blockNum[1] = random;
        blockNum[0] = blockNum[1];
        return blockNum;
    }
    public void blockCreator(int num)//随机产生砖块
    {
        switch (num) {
            case 1 -> {
                hero();
                blockNumber = blockList(1);
            }
            case 2 -> {
                smashBoy();
                blockNumber = blockList(2);
            }
            case 3 -> {
                orangeRicky();
                blockNumber = blockList(3);
            }
            case 4 -> {
                teewee();
                blockNumber = blockList(4);
            }
            case 5 -> {
                blueRicky();
                blockNumber = blockList(5);
            }
            case 6 -> {
                clevelandZ();
                blockNumber = blockList(6);
            }
            case 7 -> {
                rhoodeLandZ();
                blockNumber = blockList(7);
            }
        }
        changeFlag = true;
    }
    public int speedMap(int log) {// speed map
        int speed = 1;
        switch (log) {
            case 1 -> {
                speed = 48;
            }
            case 2 -> {
                speed = 52;
            }
            case 3 -> {
                speed = 56;
            }
            case 4 -> {
                speed = 60;
            }
            case 5 -> {
                speed = 64;
            }
            case 6 -> {
                speed = 68;
            }
            case 7 -> {
                speed = 72;
            }
            case 8 -> {
                speed = 78;
            }
            case 9 -> {
                speed = 84;
            }
            case 10 -> {
                speed = 90;
            }
        }
        return speed;
    }

    public void whirl(int blockNumber)//旋转方块
    {
        if (changeFlag) {
            posCheck();
            if (blockNumber == 1 && !downSign)//变换长条2种情况
            {
                if (game_sign_y[0] == game_sign_y[1] && game_sign_y[3] <= 16)//说明长条是横着的
                {
                    if (game_body[game_sign_y[0] - 1][game_sign_x[0] + 1] != 2 && game_body[game_sign_y[3] + 2][game_sign_x[3] - 2] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] - 1][game_sign_x[0] + 1] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1]] = 1;
                        game_body[game_sign_y[2] + 1][game_sign_x[2] - 1] = 1;
                        game_body[game_sign_y[3] + 2][game_sign_x[3] - 2] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_x[0] == game_sign_x[1] && game_sign_x[0] >= 1 && game_sign_x[3] <= 7)//说明长条是竖着的
                {
                    if (game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] != 2 && game_body[game_sign_y[3] - 2][game_sign_x[3] + 2] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1]] = 1;
                        game_body[game_sign_y[2] - 1][game_sign_x[2] + 1] = 1;
                        game_body[game_sign_y[3] - 2][game_sign_x[3] + 2] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
            }
            if (blockNumber == 3 && !downSign)//变换转弯1有4种情况
            {
                if (game_sign_x[0] == game_sign_x[1] && game_sign_x[0] == game_sign_x[2] && game_sign_y[2] == game_sign_y[3] && game_sign_x[0] >= 1) {
                    if (game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] != 2 && game_body[game_sign_y[2] - 1][game_sign_x[2] + 1] != 2 && game_body[game_sign_y[3] - 2][game_sign_x[3]] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1]] = 1;
                        game_body[game_sign_y[2] - 1][game_sign_x[2] + 1] = 1;
                        game_body[game_sign_y[3] - 2][game_sign_x[3]] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_y[1] == game_sign_y[2] && game_sign_y[2] == game_sign_y[3] && game_sign_x[0] == game_sign_x[3] && game_sign_y[1] <= 17) {
                    if (game_body[game_sign_y[0]][game_sign_x[0] - 2] != 2 && game_body[game_sign_y[1] + 1][game_sign_x[1] + 1] != 2 && game_body[game_sign_y[3] - 1][game_sign_x[3] - 1] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0]][game_sign_x[0] - 2] = 1;
                        game_body[game_sign_y[1] + 1][game_sign_x[1] + 1] = 1;
                        game_body[game_sign_y[2]][game_sign_x[2]] = 1;
                        game_body[game_sign_y[3] - 1][game_sign_x[3] - 1] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_x[1] == game_sign_x[2] && game_sign_x[1] == game_sign_x[3] && game_sign_y[0] == game_sign_y[1] && game_sign_x[3] <= 8) {
                    if (game_body[game_sign_y[0] + 2][game_sign_x[0]] != 2 && game_body[game_sign_y[1] + 1][game_sign_x[1] - 1] != 2 && game_body[game_sign_y[3] - 1][game_sign_x[3] + 1] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 2][game_sign_x[0]] = 1;
                        game_body[game_sign_y[1] + 1][game_sign_x[1] - 1] = 1;
                        game_body[game_sign_y[2]][game_sign_x[2]] = 1;
                        game_body[game_sign_y[3] - 1][game_sign_x[3] + 1] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_y[0] == game_sign_y[1] && game_sign_y[1] == game_sign_y[2] && game_sign_x[0] == game_sign_x[3]) {
                    if (game_body[game_sign_y[0] + 1][game_sign_x[0] + 1] != 2 && game_body[game_sign_y[2] - 1][game_sign_x[2] - 1] != 2 && game_body[game_sign_y[3]][game_sign_x[3] + 2] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 1][game_sign_x[0] + 1] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1]] = 1;
                        game_body[game_sign_y[2] - 1][game_sign_x[2] - 1] = 1;
                        game_body[game_sign_y[3]][game_sign_x[3] + 2] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
            }
            if (blockNumber == 4 && !downSign)//变换转弯2有4种情况
            {
                if (game_sign_x[0] == game_sign_x[1] && game_sign_x[0] == game_sign_x[3] && game_sign_y[1] == game_sign_y[2] && game_sign_x[3] <= 7) {
                    if (game_body[game_sign_y[0] + 2][game_sign_x[0]] != 2 && game_body[game_sign_y[1] + 1][game_sign_x[1] + 1] != 2 && game_body[game_sign_y[3]][game_sign_x[3] + 2] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 2][game_sign_x[0]] = 1;
                        game_body[game_sign_y[1] + 1][game_sign_x[1] + 1] = 1;
                        game_body[game_sign_y[2]][game_sign_x[2]] = 1;
                        game_body[game_sign_y[3]][game_sign_x[3] + 2] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_y[1] == game_sign_y[2] && game_sign_y[1] == game_sign_y[3] && game_sign_x[0] == game_sign_x[2]) {
                    if (game_body[game_sign_y[1]][game_sign_x[1] + 2] != 2 && game_body[game_sign_y[2] - 1][game_sign_x[2] + 1] != 2 && game_body[game_sign_y[3] - 2][game_sign_x[3]] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0]][game_sign_x[0]] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1] + 2] = 1;
                        game_body[game_sign_y[2] - 1][game_sign_x[2] + 1] = 1;
                        game_body[game_sign_y[3] - 2][game_sign_x[3]] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_x[0] == game_sign_x[2] && game_sign_x[0] == game_sign_x[3] && game_sign_y[1] == game_sign_y[2] && game_sign_x[0] >= 2) {
                    if (game_body[game_sign_y[0]][game_sign_x[0] - 2] != 2 && game_body[game_sign_y[2] - 1][game_sign_x[2] - 1] != 2 && game_body[game_sign_y[3] - 2][game_sign_x[3]] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0]][game_sign_x[0] - 2] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1]] = 1;
                        game_body[game_sign_y[2] - 1][game_sign_x[2] - 1] = 1;
                        game_body[game_sign_y[3] - 2][game_sign_x[3]] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_y[0] == game_sign_y[1] && game_sign_y[0] == game_sign_y[2] && game_sign_x[1] == game_sign_x[3] && game_sign_y[0] <= 16) {
                    if (game_body[game_sign_y[0] + 2][game_sign_x[0]] != 2 && game_body[game_sign_y[1] + 1][game_sign_x[1] - 1] != 2 && game_body[game_sign_y[2]][game_sign_x[2] - 2] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 2][game_sign_x[0]] = 1;
                        game_body[game_sign_y[1] + 1][game_sign_x[1] - 1] = 1;
                        game_body[game_sign_y[2]][game_sign_x[2] - 2] = 1;
                        game_body[game_sign_y[3]][game_sign_x[3]] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
            }
            if (blockNumber == 5 && !downSign)//变换转弯3有4种情况
            {
                if (game_sign_x[0] == game_sign_x[2] && game_sign_x[2] == game_sign_x[3] && game_sign_y[0] == game_sign_y[1] && game_sign_x[1] >= 2) {
                    if (game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] != 2 && game_body[game_sign_y[1]][game_sign_x[1] - 2] != 2 && game_body[game_sign_y[3] - 1][game_sign_x[3] + 1] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1] - 2] = 1;
                        game_body[game_sign_y[2]][game_sign_x[2]] = 1;
                        game_body[game_sign_y[3] - 1][game_sign_x[3] + 1] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_y[1] == game_sign_y[2] && game_sign_y[2] == game_sign_y[3] && game_sign_x[0] == game_sign_x[1] && game_sign_y[0] <= 16) {
                    if (game_body[game_sign_y[0] + 2][game_sign_x[0]] != 2 && game_body[game_sign_y[1] + 1][game_sign_x[1] + 1] != 2 && game_body[game_sign_y[3] - 1][game_sign_x[3] - 1] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 2][game_sign_x[0]] = 1;
                        game_body[game_sign_y[1] + 1][game_sign_x[1] + 1] = 1;
                        game_body[game_sign_y[2]][game_sign_x[2]] = 1;
                        game_body[game_sign_y[3] - 1][game_sign_x[3] - 1] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_x[0] == game_sign_x[1] && game_sign_x[1] == game_sign_x[3] && game_sign_y[2] == game_sign_y[3]) {
                    if (game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] != 2 && game_body[game_sign_y[2]][game_sign_x[2] + 2] != 2 && game_body[game_sign_y[3] - 1][game_sign_x[3] + 1] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1]] = 1;
                        game_body[game_sign_y[2]][game_sign_x[2] + 2] = 1;
                        game_body[game_sign_y[3] - 1][game_sign_x[3] + 1] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_y[0] == game_sign_y[1] && game_sign_y[1] == game_sign_y[2] && game_sign_x[2] == game_sign_x[3]) {
                    if (game_body[game_sign_y[0] + 1][game_sign_x[0] + 1] != 2 && game_body[game_sign_y[2] - 1][game_sign_x[2] - 1] != 2 && game_body[game_sign_y[3] - 2][game_sign_x[3]] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 1][game_sign_x[0] + 1] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1]] = 1;
                        game_body[game_sign_y[2] - 1][game_sign_x[2] - 1] = 1;
                        game_body[game_sign_y[3] - 2][game_sign_x[3]] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
            }
            if (blockNumber == 6 && !downSign)//变换两层砖块1的2种情况
            {
                if (game_sign_x[0] == game_sign_x[2] && game_sign_x[0] >= 2) {
                    if (game_body[game_sign_y[0]][game_sign_x[0] - 2] != 2 && game_body[game_sign_y[2] - 1][game_sign_x[2] - 1] != 2 && game_body[game_sign_y[3] - 1][game_sign_x[3] + 1] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0]][game_sign_x[0] - 2] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1]] = 1;
                        game_body[game_sign_y[2] - 1][game_sign_x[2] - 1] = 1;
                        game_body[game_sign_y[3] - 1][game_sign_x[3] + 1] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_y[0] == game_sign_y[1] && game_sign_y[3] <= 17) {
                    if (game_body[game_sign_y[0]][game_sign_x[0] + 2] != 2 && game_body[game_sign_y[1] + 1][game_sign_x[1] + 1] != 2 && game_body[game_sign_y[3] + 1][game_sign_x[3] - 1] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0]][game_sign_x[0] + 2] = 1;
                        game_body[game_sign_y[1] + 1][game_sign_x[1] + 1] = 1;
                        game_body[game_sign_y[2]][game_sign_x[2]] = 1;
                        game_body[game_sign_y[3] + 1][game_sign_x[3] - 1] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
            }
            if (blockNumber == 7 && !downSign)//变换两层砖块2的2种情况
            {
                if (game_sign_x[0] == game_sign_x[1] && game_sign_x[0] <= 16) {
                    if (game_body[game_sign_y[0]][game_sign_x[0] + 2] != 2 && game_body[game_sign_y[1] - 1][game_sign_x[1] + 1] != 2 && game_body[game_sign_y[3] - 1][game_sign_x[3] - 1] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0]][game_sign_x[0] + 2] = 1;
                        game_body[game_sign_y[1] - 1][game_sign_x[1] + 1] = 1;
                        game_body[game_sign_y[2]][game_sign_x[2]] = 1;
                        game_body[game_sign_y[3] - 1][game_sign_x[3] - 1] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
                if (game_sign_y[0] == game_sign_y[1] && game_sign_y[2] <= 17) {
                    if (game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] != 2 && game_body[game_sign_y[1]][game_sign_x[1] - 2] != 2 && game_body[game_sign_y[2] + 1][game_sign_x[2] + 1] != 2) {
                        num_csh_game();
                        game_body[game_sign_y[0] + 1][game_sign_x[0] - 1] = 1;
                        game_body[game_sign_y[1]][game_sign_x[1] - 2] = 1;
                        game_body[game_sign_y[2] + 1][game_sign_x[2] + 1] = 1;
                        game_body[game_sign_y[3]][game_sign_x[3]] = 1;
                        infoTex.setText("GAMING!");
                        repaint();
                    }
                }
            }
        }
    }
    public void num_csh_game()//数组清零
    {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 10; j++) {
                if (game_body[i][j] == 2) {
                    game_body[i][j] = 2;
                } else {
                    game_body[i][j] = 0;
                }
            }
        }
    }
    public void num_csh_restart()//重新开始时数组清零
    {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 10; j++) {
                game_body[i][j] = 0;
            }
        }
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S && startSign == 1)//处理下键
        {
            this.down();
        }
        if (e.getKeyCode() == KeyEvent.VK_A && startSign == 1)//处理左键
        {
            this.left();
        }
        if (e.getKeyCode() == KeyEvent.VK_D && startSign == 1)//处理右键
        {
            this.right();
        }
        if (e.getKeyCode() == KeyEvent.VK_W && startSign == 1)//处理上键转换
        {
            this.whirl(blockNumber[0]);
        }
        if (e.getKeyCode() == KeyEvent.VK_R && startSign == 1) {// register for a block
            int temp;
            if (changeFlag) {
                holdColor = blockColor;
                if (regNum == 0) {
                    regNum = blockNumber[0];
                    nextBlock=randomNum();
                    blockNumber = blockList(nextBlock);
                    temp = blockNumber[0];
                } else {
                    temp = blockNumber[0];
                    blockNumber[0] = regNum;
                    regNum = temp;
                }
                blockCreator(blockNumber[0]);
                nextBlock= blockNumber[1];
                posCheck();
            }
            repaint();
        }
        if (startSign == 0) {
            infoTex.setText("GAME OVER!");
        }
    }
    public void keyReleased(KeyEvent e) {
    }
    public void paint(Graphics g) {
        super.paintComponent(g);
        g.fill3DRect(0, 0, 300, 450, true);
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 10; j++) {
                g.setColor(Color.LIGHT_GRAY);
                g.fill3DRect(30 * j, 30 * (i - 4), 30, 30, true);//设置背景幕布
                if (game_body[i][j] == 1) {// falling part
                    g.setColor(blockColor);
                    g.fill3DRect(30 * j, 30 * (i - 4), 30, 30, true);
                }
                if (game_body[i][j] == 2) {// falled part
                    g.setColor(Color.GRAY);
                    g.fill3DRect(30 * j, 30 * (i - 4), 30, 30, true);
                }
            }
        }
        //paint register review
        for (int i = 0; i <= 3 ; i++) {
            for (int j = 0; j <= 3; j++) {
                g.setColor(Color.WHITE);
                if(blockFixed(regNum)[i][j] == 1){
                    g.setColor(holdColor);
                    g.fill3DRect(340 + (15 * j), 140 + (15 * i), 15, 15, true);
                }
            }
        }
        //paint NOW block review
        for (int i = 0; i <= 3 ; i++) {
            for (int j = 0; j <= 3; j++) {
                g.setColor(Color.WHITE);
                if(blockFixed(nextBlock)[i][j] == 1){
                    g.setColor(nextColor);
                    g.fill3DRect(430 + (15 * j), 140 + (15 * i), 15, 15, true);
                }
            }
        }
    }
    private int[][] blockFixed(int Num){
        int[][] shape = new int[4][4];
        switch (Num){
            case 1->{
                shape[0][2] = 1;
                shape[1][2] = 1;
                shape[2][2] = 1;
                shape[3][2] = 1;
                holdColor=Color.CYAN;
                nextColor=Color.CYAN;
            }
            case 2->{
                shape[1][1] = 1;
                shape[2][1] = 1;
                shape[1][2] = 1;
                shape[2][2] = 1;
                holdColor=Color.YELLOW;
                nextColor=Color.YELLOW;
            }
            case 3->{
                shape[0][1] = 1;
                shape[1][1] = 1;
                shape[2][1] = 1;
                shape[2][2] = 1;
                holdColor=Color.ORANGE;
                nextColor=Color.ORANGE;
            }
            case 4->{
                shape[2][1] = 1;
                shape[2][2] = 1;
                shape[2][3] = 1;
                shape[1][2] = 1;
                holdColor=Color.MAGENTA;
                nextColor=Color.MAGENTA;
            }
            case 5->{
                shape[2][1] = 1;
                shape[2][2] = 1;
                shape[0][2] = 1;
                shape[1][2] = 1;
                holdColor=Color.BLUE;
                nextColor=Color.BLUE;
            }
            case 6->{
                shape[1][0] = 1;
                shape[1][1] = 1;
                shape[2][1] = 1;
                shape[2][2] = 1;
                holdColor=Color.RED;
                nextColor=Color.RED;
            }
            case 7->{
                shape[1][2] = 1;
                shape[2][1] = 1;
                shape[2][2] = 1;
                shape[1][3] = 1;
                holdColor=Color.GREEN;
                nextColor=Color.GREEN;
            }
        }
        return shape;
    }

    public void left()//向左移动
    {
        int sign = 0;
        posCheck();
        for (int k = 0; k < 4; k++) {
            if (game_sign_x[k] == 0 || game_body[game_sign_y[k]][game_sign_x[k] - 1] == 2) {
                sign = 1;
                break;
            }
        }
        if (sign == 0 && !downSign) {
            num_csh_game();
            for (int k = 0; k < 4; k++) {
                game_body[game_sign_y[k]][game_sign_x[k] - 1] = 1;
            }
            infoTex.setText("LEFT");
            repaint();
        }
    }

    public void right()//向右移动
    {
        int sign = 0;
        posCheck();
        for (int k = 0; k < 4; k++) {
            if (game_sign_x[k] == 9 || game_body[game_sign_y[k]][game_sign_x[k] + 1] == 2) {
                sign = 1;
                break;
            }
        }
        if (sign == 0 && !downSign) {
            num_csh_game();
            for (int k = 0; k < 4; k++) {
                game_body[game_sign_y[k]][game_sign_x[k] + 1] = 1;
            }
            infoTex.setText("RIGHT");
            repaint();
        }
    }

    public void down()//下落
    {
        if (changeFlag) {
            int sign = 0;
            posCheck();
            for (int k = 0; k < 4; k++) {
                if (game_sign_y[k] == 18 || game_body[game_sign_y[k] + 1][game_sign_x[k]] == 2) {
                    sign = 1;
                    downSign = true;
                    changeColor();
                    initPOS();
                    getScore();
                    if (!game_over()) {
                        nextBlock=randomNum();
                        blockNumber = blockList(nextBlock);
                        blockCreator(blockNumber[0]);
                        nextBlock= blockNumber[1];
                        repaint();
                    }
                }
            }
            if (sign == 0) {
                num_csh_game();
                for (int k = 0; k < 4; k++) {
                    game_body[game_sign_y[k] + 1][game_sign_x[k]] = 1;
                }
                infoTex.setText("GAMING!");
                repaint();
            }
            repaint();
        }
    }

    public boolean game_over()//判断游戏是否结束
    {
        int sign = 0;
        for (int i = 0; i < 10; i++) {
            if (game_body[4][i] == 2) {
                sign = 1;
                break;
            }
        }
        if (sign == 1) {
            infoTex.setText("G A M E O V E R");
            changeColor();
            repaint();
            startSign = 0;
            timer.suspend();
            return true;
        } else {
            return false;
        }
    }

    public void getScore()//满行消除方法
    {
        for (int i = 0; i < 19; i++) {
            int sign = 0;
            for (int j = 0; j < 10; j++) {
                if (game_body[i][j] == 2) {
                    sign++;
                }
            }
            if (sign == 10) {
                gameScore += 100;
                scoreTex.setText(gameScore + "_");
                infoTex.setText("SCORE!");
                for (int j = i; j >= 1; j--) {
                    System.arraycopy(game_body[j - 1], 0, game_body[j], 0, 10);
                }
            }
        }
    }

    public void changeColor()//给已经落下的块换色
    {
        downSign = false;
        for (int k = 0; k < 4; k++) {
            game_body[game_sign_y[k]][game_sign_x[k]] = 2;
        }
    }

    public void posCheck()//确定其位置
    {
        int k = 0;
        initPOS();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 10; j++) {
                if (game_body[i][j] == 1) {
                    if (k < 4) {
                        game_sign_x[k] = j;
                        game_sign_y[k] = i;
                        k++;
                    }
                }
            }
        }
    }

    public void initPOS()//将定位数组初始化
    {
        for (int k = 0; k < 4; k++) {
            game_sign_x[k] = 0;
            game_sign_y[k] = 0;
        }
    }

    public void hero()//长条
    {
        game_body[0][4] = 1;
        game_body[1][4] = 1;
        game_body[2][4] = 1;
        game_body[3][4] = 1;
        blockColor = Color.CYAN;
    }

    public void smashBoy()//正方形
    {
        game_body[3][4] = 1;
        game_body[2][4] = 1;
        game_body[3][5] = 1;
        game_body[2][5] = 1;
        blockColor = Color.YELLOW;
    }

    public void orangeRicky()//正L
    {
        game_body[1][4] = 1;
        game_body[2][4] = 1;
        game_body[3][4] = 1;
        game_body[3][5] = 1;
        blockColor = Color.ORANGE;
    }

    public void teewee()//T形
    {
        game_body[1][4] = 1;
        game_body[2][4] = 1;
        game_body[3][4] = 1;
        game_body[2][5] = 1;
        blockColor = Color.MAGENTA;
    }

    public void blueRicky()//逆L
    {
        game_body[1][4] = 1;
        game_body[2][4] = 1;
        game_body[3][4] = 1;
        game_body[1][5] = 1;
        blockColor = Color.BLUE;
    }

    public void clevelandZ()//Z形
    {
        game_body[1][5] = 1;
        game_body[2][5] = 1;
        game_body[2][4] = 1;
        game_body[3][4] = 1;
        blockColor = Color.RED;
    }

    public void rhoodeLandZ()//S形
    {
        game_body[1][4] = 1;
        game_body[2][4] = 1;
        game_body[2][5] = 1;
        game_body[3][5] = 1;
        blockColor = Color.GREEN;
    }
}

//定时线程
class MyTimer extends Thread {
    Block myBlock;

    public MyTimer(Block myBlock) {
        this.myBlock = myBlock;
    }

    public void run() {
        while (Block.startSign == 1) {
            try {
                Thread.sleep((90 - Block.speedMark + 1) * 10L);//reverse speed
                myBlock.down();
            }
            catch (InterruptedException ignored) {
            }
        }
    }
}
