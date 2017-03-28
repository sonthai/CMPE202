package sample.test;

public class Test {
	public void callA() {
		callB();
		callC();
	}
	
	public void callC() {
		callD();
	}
	
	public void callD() {
		Test2 test2 = new Test2();
		test2.callE();
	}
	
	public void callB() {
		callC();
	}
}
