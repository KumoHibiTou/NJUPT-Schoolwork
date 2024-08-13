import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

class Student {
    String id;
    double dailyGrade;
    double midtermGrade;
    double finalGrade;
    double totalGrade;

    public Student(String id, double dailyGrade, double midtermGrade, double finalGrade) {
        this.id = id;
        this.dailyGrade = dailyGrade;
        this.midtermGrade = midtermGrade;
        this.finalGrade = finalGrade;
        this.totalGrade = calculateTotalGrade();
    }

    private double calculateTotalGrade() {
        return dailyGrade * 0.3 + midtermGrade * 0.3 + finalGrade * 0.4;
    }

    public double getTotalGrade() {
        return totalGrade;
    }

    public String getId() {
        return id;
    }
}

public class B1 extends JFrame {
    JTextField filenameField;
    Button calculateButton;
    JLabel label1;
    JTextArea outputTextArea;
    String fileLoc;

    public void initial() {
        String titleNameHere = "分数统计器";
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setTitle(titleNameHere);
        label1 = new JLabel("输入文档地址");
        label1.setBounds(50, 50, 800, 30);
        filenameField = new JTextField();
        filenameField.setBounds(150, 3 * 50, 600, 30);
        calculateButton = new Button("ok");
        calculateButton.addActionListener(this::actionPerformed);
        calculateButton.setBounds(450 - 60, 5 * 50, 120, 30);
        outputTextArea = new JTextArea(20, 40);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        add(scrollPane, BorderLayout.CENTER);
        add(label1);
        add(filenameField);
        add(calculateButton);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        fileLoc = filenameField.getText();
        if (e.getSource() == calculateButton) {//检查文件地址
            fileLoc = filenameField.getText();
            List<Student> students = readGradesFromFile(fileLoc);
            if (students.isEmpty()) {
                outputTextArea.setText("文件中没有数据。");
                return;
            }

            // 计算分布
            StringBuilder output = new StringBuilder();
            outputTextArea.setText(output.toString());
            String notice ="班级本课程总平均成绩:\n"+calculateClassAverage(students)+"\n"+"成绩分布:\n"+(calculateGradeDistribution(students));
            showPopup(notice);
            // 写入到地址
            writeGradeToFile(students,"C:/Telecom/output.txt");
            copyToClipboard("C:"+"\\"+"Telecom"+"\\"+"output.txt");


        }
    }
    private List<Student> readGradesFromFile(String filename) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 4) {
                    String id = parts[0];
                    double daily = Double.parseDouble(parts[1]);
                    double midterm = Double.parseDouble(parts[2]);
                    double finalGrade = Double.parseDouble(parts[3]);
                    students.add(new Student(id, daily, midterm, finalGrade));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }
    private String calculateClassAverage(List<Student> students) {
        double sum = 0.0;
        int count = students.size();

        for (Student student : students) {
            sum += student.getTotalGrade();
        }
        double average = sum / count;
        return String.format("%.2f", average);
    }
    private String calculateGradeDistribution(List<Student> students) {
        Map<String, Integer> gradeCount = new HashMap<>();
        gradeCount.put("优", 0);
        gradeCount.put("良", 0);
        gradeCount.put("中", 0);
        gradeCount.put("及格", 0);
        gradeCount.put("不及格", 0);
        int totalStudents = students.size();

        for (Student student : students) {
            double totalGrade = student.getTotalGrade();
            if (totalGrade >= 90) {
                gradeCount.put("优", gradeCount.get("优") + 1);
            } else if (totalGrade >= 80) {
                gradeCount.put("良", gradeCount.get("良") + 1);
            } else if (totalGrade >= 70) {
                gradeCount.put("中", gradeCount.get("中") + 1);
            } else if (totalGrade >= 60) {
                gradeCount.put("及格", gradeCount.get("及格") + 1);
            } else {
                gradeCount.put("不及格", gradeCount.get("不及格") + 1);
            }
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : gradeCount.entrySet()) {
            String grade = entry.getKey();
            int count = entry.getValue();
            double percentage = (double) count / totalStudents * 100.0;
            result.append(String.format("%s: %d 人, %.2f%%\n", grade, count, percentage));
        }
        return result.toString();
    }
    private void writeGradeToFile(List<Student> students,String writeLocation) {
        try (PrintWriter writer = new PrintWriter(writeLocation)) {
            writer.println("成绩分布:");
            writer.println("优:");
            for (Student student : students) {
                if (student.getTotalGrade() >= 90) {
                    writer.println(student.getId() + " " + String.format("%.2f",student.getTotalGrade()));
                }
            }
            writer.println("良:");
            for (Student student : students) {
                if (student.getTotalGrade() >= 80 && student.getTotalGrade() < 90) {
                    writer.println(student.getId() + " " + String.format("%.2f",student.getTotalGrade()));
                }
            }
            writer.println("中:");
            for (Student student : students) {
                if (student.getTotalGrade() >= 70 && student.getTotalGrade() < 80) {
                    writer.println(student.getId() + " " + String.format("%.2f",student.getTotalGrade()));
                }
            }
            writer.println("及格:");
            for (Student student : students) {
                if (student.getTotalGrade() >= 60 && student.getTotalGrade() < 70) {
                    writer.println(student.getId() + " " + String.format("%.2f",student.getTotalGrade()));
                }
            }
            writer.println("不及格:");
            for (Student student : students) {
                if (student.getTotalGrade() < 60) {
                    writer.println(student.getId() + " " + String.format("%.2f",student.getTotalGrade()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        B1 win0 = new B1();
        win0.initial();
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
        showPopup("文件输出到: \n" + content);
    }
}



