package ie.gmit.sw.ai.V3;

public class RunnerV5 {

	public static void main(String[] args) throws Throwable {
		Thread t1 = new Thread(new ClassRunnerV5());
		t1.start();
		t1.join();
	}

}
