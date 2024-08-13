import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;

public class A4 extends JFrame implements ActionListener {
    JTextField inputField1, inputField2;
    JLabel label1;
    static Label label2 = new Label("");
    Button[] button = new Button[7];
    String[] butLab = new String[7];
    int key=0;
    String output= "";

    public void initial() {
        String titleNameHere = "加密/解密";
        String inputString1 = "输入密钥";
        butLab[1] = "encrypt";
        butLab[3] = "decrypt";
        setTitle(titleNameHere);
        setSize(900, 600);  // Adjusted size for better component placement
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);  // Using null layout for manual component placement
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
        button[0].setBounds(450-60, 3*50, 120, 30);  // Set bounds for buttons
        button[1].setBounds(150, 6*50, 120, 30);//left
        button[2].setBounds(450-60, 6*50, 120, 30);//middle
        button[3].setBounds(629, 6*50, 120, 30);//right
        button[4].setBounds(150, 8*50, 120, 30);//left
        button[5].setBounds(450-60, 8*50, 120, 30);//middle
        button[6].setBounds(630, 8*50, 120, 30);//right

        add(label1);
        add(inputField1);
        add(label2);
        add(inputField2);
        add(button[1]);
        add(button[3]);

        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button[1]) {
            key = Integer.parseInt(inputField1.getText());
            if((key<-126)||(key>126)){
                showPopup("该密钥不可用，请重新输入");
            }
            else {
                output=caesar(inputField2.getText(),key);
                copyToClipboard(output);
            }
        }
        if (e.getSource() == button[3]) {
            key = Integer.parseInt(inputField1.getText());
            if((key<-126)||(key>126)){
                showPopup("该密钥不可用，请重新输入");
            }
            else {//对称加密意味着加密和解密是同样的方法的两个方向
                output=caesar(inputField2.getText(),-1*key);
                copyToClipboard(output);
            }
        }
    }
    public String caesar(String inString,int codeKey){
        char[] inText= inString.toCharArray();
        int[] encryptedNum =new int[inString.length()];
        char[] encryptedCode =new char[inString.length()];
        for(int i=0;i<inString.length();i++){
            if(inText[i]==32){//skip space
                encryptedNum[i]=32;
            }
            else{
                encryptedNum[i]=(int)inText[i]+codeKey;
            }
            encryptedCode[i]=(char)encryptedNum[i];
        }
        return new String(encryptedCode);
    }
    public static void showPopup(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    public static void main(String[] args) {
        A4 win4 = new A4();
        win4.initial();
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
        showPopup("处理完毕，内容已复制到剪贴板: \n" + content);
    }
}
