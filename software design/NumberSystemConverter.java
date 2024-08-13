import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class NumberSystemConverter {
    //BIN-->1
    //OCT-->3
    //HEX-->4
    //DEC-->0
    public static void main(String[] args) {
        NumberSystemConverter converter = new NumberSystemConverter();
        System.out.println("1-->任意转十");
        System.out.println("2-->十转任意");
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
        Scanner scan = new Scanner(System.in);
        int button = scan.nextInt();
        if (button == 1) {
            System.out.println("请输入转换内容，以'|'为结束符，后接目来源进制(阿拉伯数字)");
            try {//all 2 dec
                String input = inputStream.readLine();
                int index = input.lastIndexOf("|");
                String location = input.substring(index + 1);
                String content = input.substring(0, index);
                System.out.println(content+".."+location);
                System.out.println(converter.all2Decimal(content,Integer.parseInt(location)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (button == 2) {
            System.out.println("请输入转换内容，以'|'为结束符，后接目标进制(阿拉伯数字)");
            try {//dec 2 all
                String input = inputStream.readLine();
                int index = input.lastIndexOf("|");
                String location = input.substring(index + 1);
                String content = input.substring(0, index);

                System.out.println(converter.decimal2All(Long.parseLong(content),Integer.parseInt(location)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public long all2Decimal(String input_num,int NSCode){
        Long num=(long)0;
        if(NSCode<10){
            for(int test=NSCode;test<10;test++){
                if((input_num.indexOf(test)==-1)){
                    num=Long.valueOf(input_num,NSCode);
                }
                else {
                    System.out.println("非法输入，请检查输入");
                }
            }
        }
        else{
            num=Long.valueOf(input_num,NSCode);
        }

        return (long)num;
    }
    public String decimal2All(long input_num,int NSCode){
        return Long.toString(input_num,NSCode);
    }
}
