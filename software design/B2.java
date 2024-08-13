import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.text.DecimalFormat;

public class B2 extends JFrame implements ActionListener{
    //基于通用仪表盘
    JTextField phoneNumberField;
    JLabel label1,label2,fileLocationField;
    Button[] button = new Button[4];
    String[] butLab = new String[4];
    String fileAddress = "C:/Telecom/hd.dat";
    String[][] phoneLog = new String[21][5];//记录+{0主叫区号  1主叫电话号码  2被叫区号 3被叫电话号码  4通话时长（秒）}注意从第1号开始，0号设置为null
    int temp=0;
    float fee = 0;
    String[][] num =new String[5][2];//行号+{区号+费用}
    JTextField filenameField;

    public void initial(){

        String titleNameHere = "话费计数查询系统";
        String fileString = "请存放文件在地址:";
        String numberString = "输入电话号码";
        setTitle(titleNameHere);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);  // Using null layout for manual component placement
        label1 = new JLabel(fileString);
        label1.setBounds(50, 50, 800, 30);
        filenameField = new JTextField("C:/Telecom/hd.dat");
        filenameField.setBounds(150, 2 * 50, 600, 30);
        label2 = new JLabel(numberString);
        label2.setBounds(50, 4*50, 800, 30);
        phoneNumberField = new JTextField();
        phoneNumberField.setBounds(150, 5 * 50, 600, 30);// Set bounds for inputField1

        butLab[0] = "ok";
        butLab[1] = "CB query";//话费查询
        butLab[2] = "CDR query";//话单查询

        for (int i = 0; i < 4; i++) {
            button[i] = new Button(butLab[i]);
            button[i].addActionListener(this);
        }

        button[0].setBounds(450-60, 3*50, 120, 30);  // Set bounds for buttons
        button[1].setBounds(150+60, 6*50, 120, 30);//left
        button[2].setBounds(450+80, 6*50, 120, 30);//middle

        add(label1);
        //add(fileLocationField);
        add(label2);
        add(phoneNumberField);
        add(button[0]);
        add(button[1]);
        add(button[2]);
        add(filenameField);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button[0]) {
            //文件地址读取
            fileAddress=filenameField.getText();
            bufferReaderTest(fileAddress);
            calculateBilling(phoneLog);


        }
        if(e.getSource() == button[1]) {
            //话费查询
            bufferReaderTest(fileAddress);
            temp= Integer.parseInt(String.valueOf(phoneNumberField.getText()));
            CBclac(phoneLog,temp);
        }
        if(e.getSource() == button[2]) {
            //话单查询
            bufferReaderTest(fileAddress);
            temp= Integer.parseInt(String.valueOf(phoneNumberField.getText()));
            CDRcalc(phoneLog,temp);
        }

    }

    public void bufferReaderTest(String phoneOrigin) {

        String line;
        for(int i=0;i<21;i++){
            if ((lineReader(phoneOrigin,i))  != null) {
                // 处理每一行数据
                line = lineReader(phoneOrigin,i);
                phoneLog[i] = line.split(" ");
                //System.out.println(Arrays.toString(phoneLog[i]));
            }
        }

    }

    private void calculateBilling(String[][] log) {
        // 实现计费和显示结果的逻辑
        // 根据用户输入的电话号码进行计费和结果显示
        // 0主叫区号  1主叫电话号码  2被叫区号 3被叫电话号码  4通话时长（秒）
        String result ="";
        for(int i=1;i<21;i++){
            String master= log[i][0];
            int masterNum= Integer.parseInt(log[i][1]);
            String slave= log[i][2];
            int duration= Integer.parseInt(log[i][4]);

            String username = getUsernameFromUserFile(String.valueOf(masterNum)); // 从用户文件中获取用户名
            double localFee = 0;
            double longDistanceFee = 0;
            int time =(int)(duration/60)+1;
            if(Objects.equals(master, slave)){// 计算本地话费
                if(time<=3) {
                    localFee = 0.5;
                }
                else{
                    localFee = 0.5 + 0.2*((int)(time-3)/3);
                }
            }
            if(!Objects.equals(master, slave)){// 计算长途话费
                longDistanceFee = (double) getLongDistanceFee(slave)*(double)time;
                System.out.println(slave+"!"+getLongDistanceFee(slave));
            }
            double totalFee = localFee + longDistanceFee; // 总话费
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            String formattedloc = decimalFormat.format(localFee);
            String formattedlong = decimalFormat.format(longDistanceFee);
            String formattedfee = decimalFormat.format(totalFee);

            result = result + ("用户: " + username + " "
                    + "电话号码: " + masterNum + "\n"
                    + "本地费用: " + formattedloc + " "
                    + "长途费用: " + formattedlong + " "
                    + "总费用: " + formattedfee+"             "+"\n");
        }
        showPopup(result);
    }
    private void CBclac(String[][] log,int phoneNumber){
        //内容大体一致
        String result ="";
        for(int i=1;i<21;i++){
            String master= log[i][0];
            int masterNum= Integer.parseInt(log[i][1]);
            String slave= log[i][2];
            int duration= Integer.parseInt(log[i][4]);

            String username = getUsernameFromUserFile(String.valueOf(masterNum)); // 从用户文件中获取用户名
            double localFee = 0;
            double longDistanceFee = 0;
            int time =(int)(duration/60)+1;
            if(Objects.equals(master, slave)){// 计算本地话费
                if(time<=3) {
                    localFee = 0.5;
                }
                else{
                    localFee = 0.5 + 0.2*((int)(time-3)/3);
                }
            }
            if(!Objects.equals(master, slave)){// 计算长途话费
                longDistanceFee = getLongDistanceFee(slave)*time;
            }
            double totalFee = localFee + longDistanceFee; // 总话费
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            String formattedloc = decimalFormat.format(localFee);
            String formattedlong = decimalFormat.format(longDistanceFee);
            String formattedfee = decimalFormat.format(totalFee);

            if(masterNum==phoneNumber){
                result = result + ("用户: " + username + " "
                        + "电话号码: " + masterNum + " "
                        + "本地费用: " + formattedloc + " "
                        + "长途费用: " + formattedlong + " "
                        + "总费用: " + formattedfee+"             "+"\n");
            }
        }
        showPopup(result);
    }
    private void CDRcalc(String[][] log,int phoneNumber){
        //内容大体一致
        String result ="";
        for(int i=1;i<21;i++){
            int masterNum= Integer.parseInt(log[i][1]);
            int slaveNum= Integer.parseInt(log[i][3]);
            int duration= Integer.parseInt(log[i][4]);


            String username = getUsernameFromUserFile(String.valueOf(masterNum)); // 从用户文件中获取用户名

            if(masterNum==phoneNumber){
                result = result + ("用户: " + username + " "
                        + "主叫电话号码: " + masterNum + " "
                        + "被叫电话号码: " + slaveNum + " "
                        + "通话时长(秒): " + duration +"             "+"\n");
            }
        }
        showPopup(result);
    }
    public static String lineReader(String filePath, int lineNumber) {
        String line = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int currentLine = 1;
            while ((line = reader.readLine()) != null) {
                if (currentLine == lineNumber) {
                    break;
                }
                currentLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
    private float getLongDistanceFee(String region){
        String line=null;

        for(int i=1;i<5;i++) {
            if (lineReader("C:/Telecom/fl.dat", i) != null) {
                line = lineReader("C:/Telecom/fl.dat", i);
                num[i] = line.split(" ");//0regionNum+1fee
            }
        }
        for(int i=1;i<5;i++){
            if(region.equals(num[i][0])){
                fee=Float.parseFloat(num[i][1]);
            }
        }
        return fee;
    }
    private String getUsernameFromUserFile(String phoneNumber) {
        String username = "Unknown"; // 默认用户名，如果未找到匹配的电话号码则返回Unknown
        String line;
        for(int i=0;i<6;i++){
            if ((line = lineReader("C:/Telecom/yh.dat",i)) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 2) {
                    String phone = parts[0].trim();
                    String name = parts[1].trim();
                    if (phone.equals(phoneNumber)) {
                        username = name; // 找到匹配的电话号码，获取对应的用户名
                    }
                }
            }
        }

        return username;
    }
    public static void showPopup(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    public static void main(String[] args) {
        B2 win01 = new B2();
        win01.initial();
    }

}

