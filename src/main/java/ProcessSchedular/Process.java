package ProcessSchedular;

import java.util.ArrayList;

public class Process {
    private String processID;
    private int burstTime;
    private int arrivalTime;
    private int priority;

    private int startTime;
    private int completionTime;
    private int remainingTime;

    public Process(String processID, int burstTime, int arrivalTime){
        setProcessID(processID);
        setBurstTime(burstTime);
        setArrivalTime(arrivalTime);
        setPriority(0);
        setRemainingTime(burstTime);
        setCompletionTime(-1);
        setStartTime(-1);
    }

    public Process(String processID, int burstTime, int arrivalTime, int priority){
        setProcessID(processID);
        setBurstTime(burstTime);
        setArrivalTime(arrivalTime);
        setPriority(priority);
        setRemainingTime(burstTime);
        setCompletionTime(-1);
        setStartTime(-1);
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String toString(){
        return processID + "\t" + burstTime + "\t" + arrivalTime + "\t" + priority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getWaitingTime() {
        return getTurnAroundTime() - getBurstTime();
    }


    public int getTurnAroundTime() {
        return getCompletionTime() - getArrivalTime();
    }

    public int getResponseTime() {
        return getStartTime() - getArrivalTime();
    }

    public static float getAvgWaitingTime(ArrayList<Process> processes){
        float totalWaitingTime = 0;
        for(Process p : processes){
            totalWaitingTime += p.getWaitingTime();
        }
        return totalWaitingTime / processes.size();
    }

    public static float getAvgTurnAroundTime(ArrayList<Process> processes){
        float totalTurnAroundTime = 0;
        for(Process p : processes){
            totalTurnAroundTime += p.getTurnAroundTime();
        }
        return totalTurnAroundTime / processes.size();
    }

    public static float getAvgResponseTime(ArrayList<Process> processes){
        float totalResponseTime = 0;
        for(Process p : processes){
            totalResponseTime += p.getResponseTime();
        }
        return totalResponseTime / processes.size();
    }

    public static String printDetailed(ArrayList<Process> processes){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
            "\nPID\tBT\tAT\tP\tST\tCT\tTAT\tWT\tRT"
        );
        stringBuilder.append("\n");
        for(Process p: processes){
            stringBuilder.append(p.getProcessID()).append("\t");
            stringBuilder.append(
                            p.getBurstTime()).append("\t" );
            stringBuilder.append(
                            p.getArrivalTime() ).append("\t" );
            stringBuilder.append(
                            p.getPriority()).append("\t" );
            stringBuilder.append(
                            p.getStartTime()).append("\t" );
            stringBuilder.append(
                            p.getCompletionTime()).append("\t" );
            stringBuilder.append(
                            p.getTurnAroundTime()).append("\t" );
            stringBuilder.append(
                            p.getWaitingTime()).append("\t" );
            stringBuilder.append(
                            p.getResponseTime()).append("\t" );
            stringBuilder.append("\n");
        }
        stringBuilder.append("Average Turn Around Time: ").append(getAvgTurnAroundTime(processes));
        stringBuilder.append("\n");
        stringBuilder.append("Average Waiting Time: ").append(getAvgWaitingTime(processes));
        stringBuilder.append("\n");
        stringBuilder.append("Average Response Time: ").append(getAvgResponseTime(processes));
        return stringBuilder.toString();
    }

}
