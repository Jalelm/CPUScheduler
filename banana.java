import java.util.*;
 
public class Algorithm {
	public static void fcfs(ArrayList<Process> processes)
	{
		Scanner sc = new Scanner(System.in);
		int time = 0;
		ArrayList<Integer> ready = new ArrayList<Integer>();
		Integer cpu = null;
		ArrayList<Integer> io = new ArrayList<Integer>();
		
 
		//sorting according to arrival times
		Collections.sort(processes, Comparator.comparing(Process:: getArrivalTime));
		
		//counter to increment through and add processes to ready queue
		int counter = 0;
		
		//while loop to execute within until processes complete
		while(time < processes.get(processes.size() - 1).arrivalTime || ready.size() > 0 || cpu != null || io.size() > 0) {
			//add all processes with start time at current time to ready queue
			while(counter < processes.size() && processes.get(counter).arrivalTime == time) {
				ready.add(counter);
				counter++;
			}
			
			//check if process done in cpu and move to proper location if it is
			if(cpu != null && processes.get(cpu).cpuBurstTime.get(processes.get(cpu).burstCount) == processes.get(cpu).burstTime) {
				if(processes.get(cpu).burstCount < processes.get(cpu).cpuBurstTime.size()) {
					io.add(cpu);
					processes.get(cpu).burstTime = 0;
					cpu = null;
				}
				else {
					processes.get(cpu).completionTime = time;
					cpu = null;
				}
			}
			ArrayList<Integer> temp = new ArrayList<Integer>();
			//move completed io bursts back to ready
			for(int i = 0; i < io.size(); i++) {
				//if io burst time is completed, move to ready, increment burst count, and reset burst timer
				if(processes.get(io.get(i)).burstTime == processes.get(io.get(i)).ioBurstTime.get(processes.get(io.get(i)).burstCount)) {
					processes.get(io.get(i)).burstCount++;
					processes.get(io.get(i)).burstTime = 0;
					ready.add(io.get(i));
					temp.add(i);
				}
			}
			
			for(int i = 0; i < temp.size(); i++) {
				io.remove(temp.get(i));
			}
			
			//if cpu is empty, move process from ready to cpu
			if(cpu == null && ready.size() > 0) {
				cpu = ready.get(0);
				ready.remove(0);
			}
			
			//increment everything
			for(int i = 0; i < io.size(); i++) {
				processes.get(io.get(i)).burstTime++;
			}
			if(cpu != null) {
				processes.get(cpu).burstTime++;
			}
			time++;
		}
	}
}
