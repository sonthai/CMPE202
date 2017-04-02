package sample.test;

public class Test {
	public void callA() {
		 
	       callB();
	 
	       callC();
	   }
	 
	   public void callB() {
	 
	       callC();
	   }
	 
	   public void callC() {
		   Test2 t2 = new Test2();
		   t2.callE();
		   t2.callF();
	   }
}
