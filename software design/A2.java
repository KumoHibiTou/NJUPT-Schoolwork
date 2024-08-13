import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class A2 extends JFrame implements ActionListener {
    JTextField inputField1, inputField2;
    JLabel label1;
    static Label label2 = new Label("");
    Button[] button = new Button[7];
    String[] butLab = new String[7];
    String input1 = null;
    String input2 = null;
    String randomString=null;
    int num = 0;
    boolean isNum=false;

    public void initial() {
        String titleNameHere = "打字练习";
        String inputString1 = "输入练习字符串长度";
        setTitle(titleNameHere);
        setSize(900, 600);  // Adjusted size for better component placement
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);  // Using null layout for manual component placement
        label1 = new JLabel(inputString1);
        label1.setBounds(50, 50, 800, 30);  // Set bounds for label1
        label2.setText("waiting");
        label2.setBounds(50, 4 * 50, 800, 30);  // Set bounds for label2

        inputField1 = new JTextField();
        inputField1.setBounds(150, 2 * 50, 600, 30);  // Set bounds for inputField1
        inputField2 = new JTextField();
        inputField2.setBounds(150, 5 * 50, 600, 30);  // Set bounds for inputField2

        butLab[0] = "ok";
        butLab[2] = "ok";
        butLab[3] = "no symbol";

        for (int i = 0; i < 7; i++) {
            button[i] = new Button(butLab[i]);
            button[i].addActionListener(this);
        }
        button[0].setBounds(450 - 60, 3 * 50, 120, 30);  // Set bounds for buttons
        button[2].setBounds(450 - 60, 6 * 50, 120, 30);//middle
        button[3].setBounds(630, 3 * 50, 90, 30);//switch
        button[3].setBackground(Color.gray);

        add(label1);
        add(inputField1);
        add(label2);
        add(inputField2);
        add(button[0]);
        add(button[2]);
        add(button[3]);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button[0]) {
            input1 = inputField1.getText();
            num = Integer.parseInt(String.valueOf(input1));
            randomString = stringCreator(num, isNum);//懒得做专门的切换按钮了，凑合一下
        }
        label2.setText(randomString);
        if (e.getSource() == button[2]) {
            input2 = inputField2.getText();
            showPopup("您的错误率是"+errorCalc(randomString,input2));
        }
        if (e.getSource() == button[3]) {
            if(!isNum){
                button[3].setBackground(Color.darkGray);
                isNum=true;
                button[3].setLabel("Symbol avail");
            }
            else{
                button[3].setBackground(Color.gray);
                isNum=false;
                button[3].setLabel("no Symbol");
            }
        }
    }
    public static void main(String[] args) {
        A2 win2 = new A2();
        win2.initial();
    }
    public String stringCreator(int stringLength,boolean isNumAvailable){
        //32-126,or(65-90)&(97-122)
        int[] charNum=new int[stringLength];
        char[] chars=new char[stringLength];
        for(int i=0;i<stringLength;i++){
            if(!isNumAvailable){
                charNum[i]=randomCreator(65,122);
                if((charNum[i]==91)||(charNum[i]==92)||(charNum[i]==93)){
                    charNum[i]=charNum[i]-3;
                }
                else if ((charNum[i]==94)||(charNum[i]==95)||(charNum[i]==96)){
                    charNum[i]=charNum[i]+3;
                }
            }
            else{
                charNum[i]=randomCreator(32,126);
            }
            chars[i]=(char)charNum[i];
        }
        return new String(chars);
    }
    public static int randomCreator(int lowerLimit,int upperLimit){
        Random random = new Random();
        //confirm random number
        return random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
    }
    public float errorCalc(String data1,String data2){
        //data1==源文件
        //data2==练习文件
        char[] aimText=data1.toCharArray();
        char[] inText= data2.toCharArray();
        int getNum=aimText.length;
        float errorRatio=0;
        int error=0;

            if(inText.length<aimText.length){
                error+=aimText.length-inText.length;
                for(int i=0;i<inText.length;i++){
                    if(aimText[i]!=inText[i]){
                        error++;
                    }
                }
            }
            else {
                for(int i=0;i<getNum;i++){
                    if(aimText[i]!=inText[i]){
                        error++;
                    }
                }
            }
            errorRatio= (float) error /getNum;
        return errorRatio;
    }
    public static void showPopup(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
