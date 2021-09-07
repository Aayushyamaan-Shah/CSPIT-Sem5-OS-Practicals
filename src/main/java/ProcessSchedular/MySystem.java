package ProcessSchedular;

import java.util.ArrayList;
import java.util.Comparator;

public class MySystem {
    String systemName;
    ArrayList<Process> processPool;
    private int schedulerMode;

    // Constants
    public static final int SCHEDULER_FIRST_COME_FIRST_SERVE = 0;
    public static final int SCHEDULER_SHORTEST_JOB_FIRST = 1;
    public static final int SCHEDULER_SHORTEST_REMAINING_JOB_FIRST = 2;
    public static final int SCHEDULER_ROUND_ROBIN = 3;
    public static final int SCHEDULER_PRIORITY_SCHEDULING = 4;

    public static final int PROCESS_ARRIVAL_TIME = 0;
    public static final int PROCESS_REMAINING_TIME_FIRST = 1;
    public static final int PROCESS_ROUND_ROBIN = 3;
    public static final int PROCESS_PRIORITY = 4;


    // Constructors
    public MySystem(){
        setSystemName("System");
        initProcessPool();
        setSchedulerMode(0);
    }

    public MySystem(String systemName){
        setSystemName(systemName);
        initProcessPool();
        setSchedulerMode(0);
    }

    public MySystem(ArrayList<Process> processPool){
        setSystemName("System");
        initProcessPool(processPool);
        setSchedulerMode(0);
    }

    public MySystem(String systemName, ArrayList<Process> processPool){
        setSystemName(systemName);
        initProcessPool(processPool);
        setSchedulerMode(0);
    }

    // Getters, Setters and Initializers
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public int getSchedulerMode() {
        return schedulerMode;
    }

    public void setSchedulerMode(int schedulerMode) {
        this.schedulerMode = schedulerMode;
    }

    private void initProcessPool(){
        processPool = new ArrayList<>();
    }

    private void initProcessPool(ArrayList<Process> processPool){
        this.processPool = processPool;
    }

    // Adding new processes to the system
    public boolean addProcess(String processID, int burstTime, int arrivalTime){
        try{
            processPool.add(new Process(processID, burstTime, arrivalTime));
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean addProcess(String processID, int burstTime, int arrivalTime, int priority){
        try{
            processPool.add(new Process(processID, burstTime, arrivalTime, priority));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean schedule(int numberOfProcesses, int schedulerMode, int timeQuantum){
        setSchedulerMode(schedulerMode);
        int timer = 0;
        switch (this.schedulerMode){
            case SCHEDULER_FIRST_COME_FIRST_SERVE:{
                sortProcesses(processPool, PROCESS_ARRIVAL_TIME);
                timer = processPool.get(0).getArrivalTime();
                for(Process p: processPool){
                    if(timer < p.getArrivalTime())
                        timer = p.getArrivalTime();
                    p.setStartTime(timer);
                    timer += p.getBurstTime();
                    p.setRemainingTime(0);
                    p.setCompletionTime(timer);
                }
                System.out.println(Process.printDetailed(processPool));
            }break;
            case SCHEDULER_SHORTEST_JOB_FIRST:{
                SchedulerQueue queue = new SchedulerQueue(PROCESS_REMAINING_TIME_FIRST);
                sortProcesses(processPool, PROCESS_ARRIVAL_TIME);
                int scheduledProcessCounter = 0;
                boolean toEnterFlag = true;
                while(scheduledProcessCounter < processPool.size()-1 || queue.getSize() > 0){
                    while(processPool.get(scheduledProcessCounter).getArrivalTime() <= timer && toEnterFlag){
                        queue.addProcess(processPool.get(scheduledProcessCounter));
                        if(scheduledProcessCounter < processPool.size()-1){
                            scheduledProcessCounter++;
                        }else{
                            toEnterFlag = false;
                            break;
                        }
                    }
                    if(queue.getSize() > 0){
                        processPool.get(processPool.indexOf(queue.processesPool.get(0))).setStartTime(timer);
                        timer += queue.processesPool.get(0).getBurstTime();
                        processPool.get(processPool.indexOf(queue.processesPool.get(0))).setRemainingTime(0);
                        processPool.get(processPool.indexOf(queue.processesPool.get(0))).setCompletionTime(timer);
                        queue.removeProcess(0);
                    }else{
                        timer+= 1;
                    }
                }
                System.out.println(Process.printDetailed(processPool));
            }break;
            case SCHEDULER_SHORTEST_REMAINING_JOB_FIRST:{
                SchedulerQueue queue = new SchedulerQueue(PROCESS_REMAINING_TIME_FIRST);
                sortProcesses(processPool, PROCESS_ARRIVAL_TIME);
                int scheduledProcessCounter = 0;
                boolean toEnterFlag = true;
                while(scheduledProcessCounter < processPool.size()-1 || queue.getSize() > 0){
                    while(processPool.get(scheduledProcessCounter).getArrivalTime() <= timer && toEnterFlag){
                        queue.addProcess(processPool.get(scheduledProcessCounter));
                        if(scheduledProcessCounter < processPool.size()-1){
                            scheduledProcessCounter++;
                        }else{
                            toEnterFlag = false;
                            break;
                        }
                    }
                    if(queue.getSize() > 0){
                        if(processPool.get(processPool.indexOf(queue.processesPool.get(0))).getStartTime() < 0){
                            processPool.get(processPool.indexOf(queue.processesPool.get(0))).setStartTime(timer);
                        }

                        processPool.get(processPool.indexOf(queue.processesPool.get(0))).setRemainingTime(
                                processPool.get(processPool.indexOf(queue.processesPool.get(0))).getRemainingTime() - 1
                                );

                        processPool.get(processPool.indexOf(queue.processesPool.get(0))).setCompletionTime(timer+1);

                        if(processPool.get(processPool.indexOf(queue.processesPool.get(0))).getRemainingTime() == 0){
                            queue.removeProcess(0);
                        }
                        queue.reSort();
                    }
                    timer++;
                }
                System.out.println(Process.printDetailed(processPool));

            }break;
            case SCHEDULER_ROUND_ROBIN:{
                if(timeQuantum < 1)
                    throw new IllegalArgumentException("The value of time quantum cannot be less than 1");
                SchedulerQueue queue = new SchedulerQueue(PROCESS_ROUND_ROBIN);
                sortProcesses(processPool, PROCESS_ARRIVAL_TIME);
                int scheduledProcessCounter = 0;
                boolean toEnterFlag = true;
                while(scheduledProcessCounter < processPool.size()-1 || queue.getSize() > 0){
                    while(processPool.get(scheduledProcessCounter).getArrivalTime() <= timer && toEnterFlag){
                        queue.addProcess(processPool.get(scheduledProcessCounter));
                        if(scheduledProcessCounter < processPool.size()-1){
                            scheduledProcessCounter++;
                        }else{
                            toEnterFlag = false;
                            break;
                        }
                    }
                    queue.sendBack();
                    if(queue.getSize() > 0){
                        if(processPool.get(processPool.indexOf(queue.processesPool.get(0))).getStartTime() < 0){
                            processPool.get(processPool.indexOf(queue.processesPool.get(0))).setStartTime(timer);
                        }

                        int remainingTime = queue.processesPool.get(0).getRemainingTime();
                        if(remainingTime < timeQuantum){
                            timer += remainingTime;
                            processPool.get(processPool.indexOf(queue.processesPool.get(0))).setRemainingTime(0);
                        }else{
                            timer += timeQuantum;
                            processPool.get(processPool.indexOf(queue.processesPool.get(0))).setRemainingTime(
                                    processPool.get(processPool.indexOf(queue.processesPool.get(0))).getRemainingTime() - timeQuantum
                            );
                        }
                        processPool.get(processPool.indexOf(queue.processesPool.get(0))).setCompletionTime(timer);

                        if(processPool.get(processPool.indexOf(queue.processesPool.get(0))).getRemainingTime() == 0){
                            queue.removeProcess(0);
                        }
                    }else{
                        timer += 1;
                    }

                }
                System.out.println(Process.printDetailed(processPool));
            }break;
            case SCHEDULER_PRIORITY_SCHEDULING:{
                SchedulerQueue queue = new SchedulerQueue(PROCESS_PRIORITY);
                sortProcesses(processPool, PROCESS_ARRIVAL_TIME);
                int scheduledProcessCounter = 0;
                boolean toEnterFlag = true;
                while(scheduledProcessCounter < processPool.size()-1 || queue.getSize() > 0){
                    while(processPool.get(scheduledProcessCounter).getArrivalTime() <= timer && toEnterFlag){
                        queue.addProcess(processPool.get(scheduledProcessCounter));
                        if(scheduledProcessCounter < processPool.size()-1){
                            scheduledProcessCounter++;
                        }else{
                            toEnterFlag = false;
                            break;
                        }
                    }
                    queue.reSort();
                    if(queue.getSize() > 0){
                        if(processPool.get(processPool.indexOf(queue.processesPool.get(0))).getStartTime() < 0){
                            processPool.get(processPool.indexOf(queue.processesPool.get(0))).setStartTime(timer);
                        }
                        processPool.get(processPool.indexOf(queue.processesPool.get(0))).setRemainingTime(
                                processPool.get(processPool.indexOf(queue.processesPool.get(0))).getRemainingTime() - 1
                        );
                        processPool.get(processPool.indexOf(queue.processesPool.get(0))).setCompletionTime(timer+1);

                        if(processPool.get(processPool.indexOf(queue.processesPool.get(0))).getRemainingTime() == 0){
                            queue.removeProcess(0);
                        }
                    }
                    timer += 1;

                }
                System.out.println(Process.printDetailed(processPool));

            }break;
            default:
                throw new IllegalArgumentException("The selected option does not match any scheduling scheme");
        }
        return false;
    }

    public boolean schedule(int numberOfProcesses){
        return schedule(numberOfProcesses, SCHEDULER_FIRST_COME_FIRST_SERVE, -1);
    }

    public boolean schedule(int numberOfProcesses, int timeQuantum){
        return schedule(numberOfProcesses, SCHEDULER_FIRST_COME_FIRST_SERVE, timeQuantum);
    }

    public boolean scheduleAll(){
        return schedule(processPool.size());
    }

    public boolean scheduleAll(int schedulerMode){
        return schedule(processPool.size(), schedulerMode, -1);
    }
    public boolean scheduleAll(int schedulerMode, int timeQuantum){
        return schedule(processPool.size(), schedulerMode, timeQuantum);
    }

    private static void sortProcesses(ArrayList<Process> processPool, int schedulerMode){
        switch (schedulerMode){
            case MySystem.PROCESS_ARRIVAL_TIME:
                processPool.sort(new ArrivalTimeSorter());
                break;
            case MySystem.PROCESS_REMAINING_TIME_FIRST:
                processPool.sort(new RemainingTimeSorter());
                break;
            case MySystem.PROCESS_ROUND_ROBIN:
                break;
            case MySystem.PROCESS_PRIORITY:
                processPool.sort(new PrioritySorter());
        }
    }
}

// Comparator class to sort the process based on its arrival time
class ArrivalTimeSorter implements Comparator<Process>{
    public int compare(Process p1, Process p2){
        return p1.getArrivalTime() - p2.getArrivalTime();
    }
}

// Comparator class to sort the process based on its remaining burst time
class RemainingTimeSorter implements Comparator<Process>{
    public int compare(Process p1, Process p2){
        return p1.getRemainingTime() - p2.getRemainingTime();
    }
}

// Comparator class to sort the process based on its remaining burst time
class PrioritySorter implements Comparator<Process>{
    public int compare(Process p1, Process p2){
        return p2.getPriority() - p1.getPriority();
    }
}

class SchedulerQueue{
    ArrayList<Process> processesPool;
    private final int schedulingStyle;
    public SchedulerQueue(int schedulingStyle){
        processesPool = new ArrayList<>();
        this.schedulingStyle = schedulingStyle;
    }
    public void addProcess(Process process){
        processesPool.add(process);
        reSort();
    }
    public void removeProcess(Process process){
        processesPool.remove(process);
    }
    public void removeProcess(int index){
        processesPool.remove(index);
    }
    public int getSize(){
        return processesPool.size();
    }
    public void reSort(){
        switch (schedulingStyle){
            case MySystem.PROCESS_ARRIVAL_TIME:
                processesPool.sort(new ArrivalTimeSorter());
                break;
            case MySystem.PROCESS_REMAINING_TIME_FIRST:
                processesPool.sort(new RemainingTimeSorter());
                break;
            case MySystem.PROCESS_ROUND_ROBIN:
                break;
            case MySystem.PROCESS_PRIORITY:
                processesPool.sort(new PrioritySorter());
        }
    }
    public void sendBack(){
        processesPool.add(processesPool.size()==0?0:processesPool.size(), processesPool.get(0));
        processesPool.remove(0);
    }
}
