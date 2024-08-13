import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class A3 extends JFrame implements ActionListener {
    JTextField inputField1, inputField2, inputField3;
    JLabel label1;
    static Label label2 = new Label("");
    Button[] button = new Button[7];
    String[] butLab = new String[7];
    String fileAddress = new String();
    /*
      C:/Users/KumoHibi.Tou/Desktop/info/sample.txt
      The_quick_brown_fox_jumps_over_a_lazy_dog.
      The_quick_brown_fox_jumps_over a lazy_dog.
     */
    public void initial() {
        String titleNameHere = "文档编辑器";
        String inputString1 = "输入文档地址";
        setTitle(titleNameHere);
        setSize(900, 600);  // Adjusted size for better component placement
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);  // Using null layout for manual component placement
        label1 = new JLabel(inputString1);
        label1.setBounds(50, 50, 800, 30);  // Set bounds for label1
        label2.setText("Generic input bar");
        label2.setBounds(50, 4 * 50, 800, 30);  // Set bounds for label2

        inputField1 = new JTextField();
        inputField1.setBounds(150, 2 * 50, 600, 30);  // Set bounds for inputField1
        inputField2 = new JTextField();
        inputField2.setBounds(150, 5 * 50, 600, 30);  // Set bounds for inputField2

        butLab[0] = "ok";
        butLab[1] = "create";
        butLab[2] = "copy";
        butLab[3] = "edit";
        butLab[5] = "ok";

        for (int i = 0; i < 7; i++) {
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
        add(button[0]);
        add(button[1]);
        add(button[2]);
        add(button[3]);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button[0]) {//检查文件地址
            fileAddress = inputField1.getText();
            //System.out.println("!");
            try{
                filePrinter(fileAddress);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == button[1]) {
            label2.setText("Please input the text you want to write, ending with a ENTER");
            String input = inputField2.getText();
            try {
                fileWriter(fileAddress,input);
                filePrinter(fileAddress);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == button[2]) {
            label2.setText("Please enter the address to which the file will be copied");
            String input = inputField2.getText();
            try {
                fileCopier(fileAddress,input);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == button[3]) {
            label2.setText("The file is edited in the form of (starting text character position $ character content).");
            String rawedit = inputField2.getText();
            try {
                fileEditor(fileAddress,rawedit);
                filePrinter(fileAddress);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public static void showPopup(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    public static void filePrinter(String fileLocation) throws IOException {
        String temp="";
        File targetFile = new File(fileLocation);
        if(!targetFile.exists()){
            targetFile.createNewFile();
        }
        FileInputStream inFile = new FileInputStream(fileLocation);
        int fileLength =(int)targetFile.length();
        byte[] Bytes =new byte[fileLength];
        temp += "该文件大小: " + inFile.read(Bytes);//print file's size

        String file1 = new String(Bytes);

        if(inFile.read(Bytes)==0){
            temp += "　　　　　　　　　　N／A　　　　　　　　　　　";

        }
        else{
            temp += "\n"+"当前文件内容如下：\n " + file1;
        }
        showPopup(temp);

        //close file
        inFile.close();
    }
    public static void fileWriter(String fileLocation,String data) throws IOException{
        try(OutputStream file = new FileOutputStream(fileLocation,false)) {
            byte [] buffer = data.getBytes("utf8"); // 指定编码集
            file.write(buffer);
            System.out.println("写入完成");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void fileCopier(String initLoc,String finLoc) throws IOException {
        try {//input file
            FileInputStream sourceFile = new FileInputStream(initLoc);//file to be read
            FileOutputStream targetFile = new FileOutputStream(finLoc); // Copy each byte from the input to output
            int byteValue;
            //read byte from first file and write it into second line
            while ((byteValue = sourceFile.read()) != -1) {
                targetFile.write(byteValue);
            }
            //Close the files
            sourceFile.close();
            targetFile.close();
            System.out.println("复制完毕");
        }
        // If something went wrong, report it!
        catch (IOException e) {
            System.out.println("Exception: " + e.toString());
        }
    }
    public static void fileEditor (String fileLocation, String rawText) throws IOException {
        FileInputStream oldFile = new FileInputStream(fileLocation);
        Scanner sc = new Scanner(oldFile);
        String olddata=null;
        while (sc.hasNext()) {
            olddata = sc.nextLine();
        }
        int index = rawText.indexOf("$");//depart loc&data
        String editText = rawText.substring(index + 1);//content
        String substring = rawText.substring(0, index);
        int editLoc = Integer.parseInt(substring);//location
        if(olddata!=null){
            String suber = olddata.substring(0,editLoc-1);
            String latter = olddata.substring(editLoc,olddata.length());
            String newText = suber+editText+latter;
            fileWriter(fileLocation,newText);
        }
    }
    public static void main(String[] args) {
        A3 win3 = new A3();
        win3.initial();
    }
}
