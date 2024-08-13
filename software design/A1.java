
import java.util.Scanner;
import java.util.Arrays;

public class A1 {
    int subjectNum;//学生人数
    int subjectID=0;//ID
    float[] studentScore = new float[1000];
    String[] studentName = new String[1000];
    int[] unsortID = new int[1000];//下标与ID映射
    Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        //这个UI太复杂了，三个框还带登录弹窗，我干脆不做了
        System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
        System.out.println("｜－－－－－－－－－－－－－－－－－－－－－－－－－－｜");
        System.out.println("｜｜　　　　　　　　　　　　　　　　　　　　　　　　｜｜");
        System.out.println("｜｜　　　　－－欢迎使用学生分数统计软件－－　　　　｜｜");
        System.out.println("｜｜　　　　　　　　　　　　　　　　　　　　　　　　｜｜");
        System.out.println("｜－－－－－－－－－－－－－－－－－－－－－－－－－－｜");
        System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
        A1 tablet = new A1();
        System.out.println("请输入人数：　　　　　　　　　　　　　　　　　　　　　｜");
        System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－｜");
        Scanner scan = new Scanner(System.in);
        int getNum = scan.nextInt();
        if (getNum>0) {
            tablet.subjectNum=getNum;
            System.out.println(" ");
        }
        else {
            System.out.println("人数错误：不可用");
        }
        for(int i=0;i< getNum;i++){
            tablet.inputData(i);
        }
        tablet.arraySort(tablet.subjectNum);
        System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
        for(int i=0;i< tablet.subjectNum;i++){
            System.out.println(tablet.getStudentName(tablet.unsortID[i])+" ｜ "+tablet.getStudentScore(tablet.unsortID[i]));
        }

    }

    public void setStudentScore(int subjectID, String input) {
        float subjectScore = Float.parseFloat(input);
        if (subjectScore >= 0) {
            this.studentScore[subjectID] = subjectScore;
        } else {
            System.out.println("分数错误：不可用");
        }
    }

    public void setSubjectName(int ID, String input) {
        this.studentName[ID] = input;
    }

    public float getStudentScore(int ID) {
        return studentScore[ID];
    }

    public String getStudentName(int ID) {
        return studentName[ID];
    }

    public void inputData(int ID) {
        if (ID < subjectNum) {
            System.out.println("请输入姓名：　　　　　　　　　　　　　　　　　　　　　｜");
            String name = scan.nextLine();
            setSubjectName(ID, name);
            System.out.println("请输入分数：　　　　　　　　　　　　　　　　　　　　　｜");
            String score = scan.nextLine();
            setStudentScore(ID, score);
            subjectID++;
        } else {
            System.out.println("状态不可用");
        }

    }

    public void arraySort(int length) {//unsort array
        float[] score_asc = new float[length];//升幂排序
        float[] score_desc = new float[length];//降幂排序
        for (int i = 0; i < length; i++) {
            score_asc[i]=studentScore[i];
        }
        Arrays.sort(score_asc);
        for (int i = 0; i < length; i++) {
            score_desc[length - 1 - i] = score_asc[i];
        }
        for(int i = 0; i < length; i++){
            for(int j = 0; j < length; j++){
                if(studentScore[i]==score_desc[j]){
                    unsortID[i]=j;
                }
            }
        }
    }
}
