package aspectj.sequencediagram;

public aspect AspectJSequenceDiagram {
	pointcut traceCall(): (execution(* *()) || initialization(*.new(..))) && within(aspectj.code.*);
	before(): traceCall() {
		System.out.println("Call from " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getSimpleName()
				+ " line " + thisJoinPointStaticPart.getSourceLocation().getLine() 
				+ "\n to " + thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName()
				+ " with method " + thisJoinPointStaticPart.getSignature().getName());
		System.out.println(thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName());
	}

}
