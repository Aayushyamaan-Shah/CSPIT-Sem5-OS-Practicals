package ProcessSchedular;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProcessSchedulerRunner {
    public static void main(String[] args) {
        MySystem system = new MySystem();
        System.out.println("First Come First Serve");
        try(Scanner in = new Scanner(new File("E:\\College (CSPIT)\\Sem 5\\OS\\Practicals\\IntelliJ\\src\\main\\java\\ProcessSchedular\\schedularInput.txt"))){
            int processes = Integer.parseInt(in.nextLine());
            for(int i = 0 ; i < processes; i++){
                String base = in.nextLine();
                String pid = base.split(",")[0];
                int bt = Integer.parseInt(base.split(",")[1]);
                int ct = Integer.parseInt(base.split(",")[2]);
                int pr = Integer.parseInt(base.split(",")[3]);
                system.addProcess(pid, bt, ct, pr);
            }
            system.scheduleAll(MySystem.SCHEDULER_FIRST_COME_FIRST_SERVE);
        }
        catch (FileNotFoundException e){
            System.out.println("The file in the path was not found...");
            e.printStackTrace();
        }
    }
}
