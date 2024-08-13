import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;

public class A5 extends JFrame implements ActionListener {
    JTextField inputField1, inputField2;
    JLabel label1;
    static Label label2 = new Label("");
    Button[] button = new Button[7];
    String[] butLab = new String[7];
    boolean isDEC=false;
    String output= "";
    String temp="";
    public void initial() {
        String titleNameHere = "进制转换器";
        String inputString1 = "当前模式:十进制转其他";
        butLab[0] = "switch";
        butLab[1] = "BIN";
        butLab[2] = "OCT";
        butLab[3] = "HEX";
        setTitle(titleNameHere);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        label1 = new JLabel(inputString1);
        label1.setBounds(50, 50, 800, 30);  // Set bounds for label1
        label2.setText("input your content");
        label2.setBounds(50, 4*50, 800, 30);  // Set bounds for label2

        inputField1 = new JTextField();
        inputField1.setBounds(150, 2*50, 600, 30);  // Set bounds for inputField1
        inputField2 = new JTextField();
        inputField2.setBounds(150, 5*50, 600, 30);  // Set bounds for inputField2

        for(int i=0;i<7;i++){
            button[i] = new Button(butLab[i]);
            button[i].addActionListener(this);
        }
        button[0].setBounds(350, 2*50, 200, 30);  // Set bounds for buttons
        button[1].setBounds(150, 6*50, 120, 30);//left
        button[2].setBounds(450-60, 6*50, 120, 30);//middle
        button[3].setBounds(629, 6*50, 120, 30);//right
        button[4].setBounds(150, 8*50, 120, 30);//left
        button[5].setBounds(450-60, 8*50, 120, 30);//middle
        button[6].setBounds(630, 8*50, 120, 30);//right
        button[0].setBackground(Color.gray);

        add(label1);
        add(label2);
        add(inputField2);
        add(button[0]);
        add(button[1]);
        add(button[2]);
        add(button[3]);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button[0]) {
            if(!isDEC){
                button[0].setBackground(Color.darkGray);
                isDEC=true;
                setTitle("进制转换器：其他转十进制");
                label1.setText("当前模式:其他转十进制");

            }
            else{
                button[0].setBackground(Color.gray);
                isDEC=false;
                setTitle("进制转换器：十进制转其他");
                label1.setText("当前模式:十进制转其他");
            }
        }
        if (e.getSource() == button[1]) {
            //bin
            temp=String.valueOf(inputField2.getText());
            if(isDEC){
                output = String.valueOf(all2Decimal(temp,2));
            }
            else{
                output = decimal2All(Long.parseLong(temp),2);
            }
            copyToClipboard(output);
        }
        if (e.getSource() == button[2]) {
            //oct
            temp=String.valueOf(inputField2.getText());
            if(isDEC){
                output = String.valueOf(all2Decimal(temp,8));
            }
            else{
                output = decimal2All(Long.parseLong(temp),8);
            }
            copyToClipboard(output);
        }
        if (e.getSource() == button[3]) {
            //hex
            temp=String.valueOf(inputField2.getText());
            if(isDEC){
                output = String.valueOf(all2Decimal(temp,16));
            }
            else{
                output = decimal2All(Long.parseLong(temp),16);
            }
            copyToClipboard(output);
        }
    }
    public long all2Decimal(String input_num,int NSCode){
        Long num=(long)0;
        boolean flag=false;
        if(NSCode<10){
            for(int test=NSCode;test<10;test++){
                System.out.println(test);
                if((input_num.indexOf(test) == -1)){
                    num=Long.valueOf(input_num,NSCode);
                    flag=false;
                }
                else {
                    num= 0L;
                    flag=true;
                }
            }
        }
        else{
            num=Long.valueOf(input_num,NSCode);
        }
        if(flag){
            showPopup("非法输入，请检查输入");
        }
        return (long)num;
    }
    public String decimal2All(long input_num, int NSCode){
        return Long.toString(input_num,NSCode);
    }
    public static void showPopup(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    public static void copyToClipboard(String content) {
        StringSelection stringSelection = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, new ClipboardOwner() {
            @Override
            public void lostOwnership(Clipboard clipboard, java.awt.datatransfer.Transferable contents) {
                // Do nothing
            }
        });
        showPopup("转换完成，已复制到剪贴板: \n" + content);
    }

    public static void main(String[] args) {
        A5 win5 = new A5();
        win5.initial();
    }
}
