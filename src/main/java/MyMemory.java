import java.util.ArrayList;

public class MyMemory {

    private final ArrayList<MyMemoryUnit> memoryUnits;
    public int selectedType;
    public static final int BEST_FIT = 0;

    public int getType() {
        return selectedType;
    }

    public void setType(int selectedType) {
        this.selectedType = selectedType;
    }

    public static final int FIRST_FIT = 1;
    public static final int WORST_FIT = 2;
    int maxCapacity;

    public MyMemory(){
        memoryUnits = new ArrayList<>();
        selectedType = BEST_FIT;
        maxCapacity = -1;
    }

    public MyMemory(int memoryType){
        if(memoryType > 3 || memoryType < 0){
            System.out.println("Selected memory type is not allowed");
            throw new IllegalArgumentException("The memory type selected is not allowed.");
        }else{
            memoryUnits = new ArrayList<>();
            selectedType = memoryType;
            maxCapacity = -1;
        }
    }

    public MyMemory(int size, int count){
        memoryUnits = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            memoryUnits.add(new MyMemoryUnit(size));
        }
        selectedType = BEST_FIT;
        maxCapacity = -1;
    }

    public MyMemory(int size, int count, int memoryType){
        if(memoryType > 3 || memoryType < 0){
            System.out.println("Selected memory type is not allowed");
            throw new IllegalArgumentException("The memory type selected is not allowed.");
        }else{

            selectedType = memoryType;
            memoryUnits = new ArrayList<>();
            for(int i = 0; i < count; i++) {
                memoryUnits.add(new MyMemoryUnit(size));
            }
            maxCapacity = -1;
        }
    }

    public void addMemoryUnit(int capacity){
        memoryUnits.add(new MyMemoryUnit(capacity));
        updateMaxCapacity(capacity);
    }

    public void addMemoryUnit(int capacity, int allocatedSize){
        memoryUnits.add(new MyMemoryUnit(capacity, allocatedSize));
        updateMaxCapacity(capacity);
    }

    public void addMemoryUnit(int capacity, String tag){
        memoryUnits.add(new MyMemoryUnit(capacity, tag));
        updateMaxCapacity(capacity);
    }

    public void addMemoryUnit(int capacity, int allocatedSize, String tag){
        memoryUnits.add(new MyMemoryUnit(capacity, allocatedSize, tag));
        updateMaxCapacity(capacity);
    }

    public void addProcess(int size, String processID){
        if(size > maxCapacity){
            System.out.println("Process \""+processID+"\" cannot be allocated in this memory.");
        }else{
            System.out.println((addProcessInternal(size, processID) == 1)?"Process added successfully":"Process was not added");
        }
    }

    private int addProcessInternal(int size, String processID){
        if(selectedType == FIRST_FIT){
            for(MyMemoryUnit memoryUnit : memoryUnits){
                if(memoryUnit.isEmpty() && memoryUnit.getCapacity() >= size){
                    memoryUnit.setAllottedSize(size);
                    memoryUnit.setTag(processID);
                    return 1;
                }
            }
        }
        else if(selectedType == BEST_FIT){
            int counter = -1;
            int min = -1;
            int selectedCounter = -1;
            for(MyMemoryUnit memoryUnit : memoryUnits){
                counter++;
                if(memoryUnit.isEmpty() && memoryUnit.getCapacity() == size) {
                    memoryUnit.setAllottedSize(size);
                    memoryUnit.setTag(processID);
                    return 1;
                }else if(memoryUnit.isEmpty() && memoryUnit.getCapacity() > size){
                    if(min < 0){
                        min = memoryUnit.getCapacity();
                        selectedCounter = counter;
                    }
                    if(memoryUnit.getCapacity() < min){
                        min = memoryUnit.getCapacity();
                        selectedCounter = counter;
                    }
                }
            }
            if(selectedCounter > -1){
                memoryUnits.get(selectedCounter).setAllottedSize(size);
                memoryUnits.get(selectedCounter).setTag(processID);
                return 1;
            }
        }
        else if(selectedType == WORST_FIT){
            int counter = -1;
            int maxDiff = -1;
            int selectedCounter = -1;
            for(MyMemoryUnit memoryUnit : memoryUnits){
                counter++;
                if(memoryUnit.isEmpty() && memoryUnit.getCapacity() >= size){
                    if(maxDiff < 0){
                        maxDiff = memoryUnit.getCapacity() - size;
                        selectedCounter = counter;
                    }
                    if(memoryUnit.getCapacity() - size >= maxDiff){
                        maxDiff = memoryUnit.getCapacity() - size;
                        selectedCounter = counter;
                    }
                }
            }
            if(selectedCounter > -1){
                memoryUnits.get(selectedCounter).setAllottedSize(size);
                memoryUnits.get(selectedCounter).setTag(processID);
                return 1;
            }
        }
        return -1;
    }

    private void updateMaxCapacity(int newCapacity){
        maxCapacity = Math.max(maxCapacity, newCapacity);
    }

    public void clearAllProcesses(){
        for(int i = 0; i < memoryUnits.size(); i++){
            memoryUnits.set(i, new MyMemoryUnit(memoryUnits.get(i).getCapacity()));
        }
    }

    @Override
    public String toString(){
        String header = "PID\tMC\tAC\tIF\n";
        StringBuilder stringBuilder = new StringBuilder(header);
        for(MyMemoryUnit m : memoryUnits){
            stringBuilder.append(m.getSummary());
            stringBuilder.append("\n");
        }
        switch (selectedType){
            case BEST_FIT: stringBuilder.append("Type: BEST_FIT"); break;
            case FIRST_FIT: stringBuilder.append("Type: FIRST_FIT"); break;
            case WORST_FIT: stringBuilder.append("Type: WORST_FIT"); break;
        }
        return stringBuilder.toString();
    }

}

class MyMemoryUnit{
    private String tag;
    private int allottedSize;
    private int capacity;

    public MyMemoryUnit(int capacity) {
        setTag("N/A");
        setAllottedSize(0);
        setCapacity(capacity);
    }

    public MyMemoryUnit(int capacity, String tag){
        setTag(tag);
        setAllottedSize(0);
        setCapacity(capacity);
    }

    public MyMemoryUnit(int capacity, int allottedSize){
        setTag("N/A");
        setAllottedSize(allottedSize);
        setCapacity(capacity);
    }

    public MyMemoryUnit(int capacity, int allottedSize, String tag){
        setTag(tag);
        setAllottedSize(allottedSize);
        setCapacity(capacity);
    }

    public String getSummary(){
        return getTag() + "\t"
                + getCapacity() +"\t"
                + getAllottedSize() + "\t"
                + ((isFragmented())?getFragmentation():"N/A");
    }

    public int getFragmentation(){
        return (allottedSize > 0)? capacity - allottedSize : 0;
    }

    public boolean isFragmented(){ return allottedSize > 0 && allottedSize < capacity; }

    public String getTag() { return tag; }

    public void setTag(String tag) { this.tag = tag; }

    public int getAllottedSize() { return allottedSize; }

    public void setAllottedSize(int allottedSize) { this.allottedSize = allottedSize; }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isEmpty() {
        return allottedSize <= 0;
    }
}
