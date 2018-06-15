package memcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessCache {

    private static ProcessCache instance = new ProcessCache();
    public static ProcessCache getInstance(){
        return instance;
    }

    private static Map<Long, Process> list;

    private ProcessCache(){
        list = new ConcurrentHashMap<>();
    }

    public void addProcess(Process process){
        list.put(process.pid(), process);
    }
    public void deleteProcess(long pid){
        list.remove(pid);
    }
    public Process getProcess(long pid){
        return list.get(pid);
    }
}
