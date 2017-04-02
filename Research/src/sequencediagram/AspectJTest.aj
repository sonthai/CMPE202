package sequencediagram;

public aspect AspectJTest {
	/*pointcut traceCall(): (execution(* *()) || initialization(*.new(..))) && within(sample.test.*);
	before(): traceCall() {
		System.out.println("Call from " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getSimpleName()
				+ " line " + thisJoinPointStaticPart.getSourceLocation().getLine() 
				+ "\n to " + thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName()
				+ " with method " + thisJoinPointStaticPart.getSignature().getName());
		System.out.println(thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName());
	}*/
	pointcut call_cflow_callA() :  (cflow( call( * *.*() ))  || initialization(*.new(..)))   && within(sample.test.*);
	 
	   before() : call_cflow_callA()  {
		   if (thisJoinPoint.getThis() != null && thisJoinPoint.getTarget() != null) {
			   System.out.println("This join point " + thisJoinPoint.getThis().getClass());
			   System.out.println("Target join point " + thisJoinPoint.getTarget().getClass());
			   System.out.println("Signature join point " + thisJoinPoint.getSignature().getName());
			   System.out.println("Source location join point " + thisJoinPoint.getSourceLocation().getWithinType().getSimpleName());
		       System.out.println(
		               "Join Point at: " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName()
		                       + " --> " + thisJoinPointStaticPart.getSourceLocation().getLine());
		   } else {
			   System.out.println("*****Joint point NULL*****");
			   if (thisJoinPoint.getThis() != null) {
				   System.out.println("This join point " + thisJoinPoint.getThis().getClass());
	
			   }
			   
			   if (thisJoinPoint.getTarget() != null) {
				   System.out.println("Target join point " + thisJoinPoint.getTarget().getClass());
			   }
			   
			   System.out.println(
		               "Join Point at: " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName()
		                       + " --> " + thisJoinPointStaticPart.getSourceLocation().getLine());
			   
			   System.out.println("*****END Joint point NULL*****");
		   }
	   }
}