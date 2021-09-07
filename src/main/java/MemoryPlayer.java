import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class MemoryPlayer {
    public static void main(String[] args) {
        try(Scanner in = new Scanner(new File("E:\\College (CSPIT)\\Sem 5\\OS\\Practicals\\IntelliJ\\src\\main\\java\\Input.txt"))
        ){
            MyMemory memories = new MyMemory(MyMemory.BEST_FIT);
            int total = in.nextInt();
            for(int i = 0; i < total; i++){
                memories.addMemoryUnit(in.nextInt());
            }
            int totalProcess = in.nextInt();

            for(int i = 0; i < totalProcess; i++){
                memories.addProcess(in.nextInt(), in.nextLine().replace(" ",""));
            }

            System.out.println(memories);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }
}
