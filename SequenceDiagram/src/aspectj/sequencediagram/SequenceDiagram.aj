package aspectj.sequencediagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

public aspect SequenceDiagram {
	private static List<TracingPojo> messages = new ArrayList<TracingPojo>();
	
	pointcut trace() : within(aspectj.code.*) && call(* aspectj.code.*.*(..));
	before(): trace() {
		printJoinPoint(thisJoinPoint);
		traceStart(getThis(thisJoinPoint), getTarget(thisJoinPoint), thisJoinPoint.getSignature(), thisJoinPoint.getSourceLocation(), thisJoinPoint.getArgs());
	}
	
	after() returning(Object o): trace() {
		traceEnd(getThis(thisJoinPoint), getTarget(thisJoinPoint), thisJoinPoint.getSignature(), thisJoinPoint.getSourceLocation(), o);
	}
	
	public void traceStart(final Class<? extends Object> joinPoint, final Class<? extends Object> target, 
			final Signature signature, final SourceLocation sourceLocation, final Object[] args) {
		if (joinPoint != null && target != null) {
			String message = joinPoint.getClass() + " -> " + target.getName() + ":" + signature.getName() + "(" + Arrays.deepToString(args) + ")";
			messages.add(new TracingPojo(TracingType.START, joinPoint, target, signature, sourceLocation, args));
			System.out.println(message);
		}
	}
	
	public void traceEnd(final Class<? extends Object> joinPoint, final Class<? extends Object> target, 
			final Signature signature, final SourceLocation sourceLocation, final Object... returnValue) {
		if (joinPoint != null && target != null) {
			String message = target.getClass() + " -> " + joinPoint.getName() + " : return" + "(" + Arrays.deepToString(returnValue) + ")";
			messages.add(new TracingPojo(TracingType.START, joinPoint, target, signature, sourceLocation, returnValue));
			System.out.println(message);
		}
	}
	
	public void printJoinPoint(JoinPoint joinPoint) {
		System.out.println("This     : "+ getThis(joinPoint));
        System.out.println("Target   : "+ getTarget(joinPoint));
        System.out.println("Signature: "+ joinPoint.getSignature());
        System.out.println("Args     : "+ Arrays.deepToString(joinPoint.getArgs()));
        System.out.println("Source   : "+ joinPoint.getSourceLocation().toString());
        System.out.println("\n");
	}
	
	private Class<? extends Object> getThis(final JoinPoint joinPoint) {
		if (joinPoint == null || joinPoint.getThis() == null) {
			return null;
		}
		
		return joinPoint.getThis().getClass();
	}
	
	private Class<? extends Object> getTarget(final JoinPoint joinPoint) {
		if (joinPoint == null || joinPoint.getTarget() == null) {
			return null;
		}
		
		return joinPoint.getTarget().getClass();
	}
	
	public static List<TracingPojo> getMessages() {
		return Collections.unmodifiableList(messages);
	}
	
	
	/*pointcut traceCall(): (execution(* *()) || initialization(*.new(..))) && within(aspectj.code.*);
	before(): traceCall() {
		System.out.println("Value " + thisJoinPointStaticPart);
		System.out.println("Call from " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getSimpleName()
				+ " line " + thisJoinPointStaticPart.getSourceLocation().getLine() 
				+ "\n to " + thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName()
				+ " with method " + thisJoinPointStaticPart.getSignature().getName());
		System.out.println(thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName());
	}*/
	//pointcut callMain(): within(aspectj.code.*) && cflow(call(* aspectj.code.*.*(..)));
	
	//pointcut initObj(): within(aspectj.code.*) && initialization(*.new(..));
	
	/*before(): initObj() {
		System.out.println("*****BEFORE INIT *****");
		System.out.println(
	               "Join Point at: " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName()
	                       + " --> " + thisJoinPointStaticPart.getSourceLocation().getLine());
		System.out.println(thisJoinPoint.getStaticPart().getClass().getSimpleName());
		System.out.println("This join point " + thisJoinPoint.getThis().getClass());
		System.out.println("Target join point " + thisJoinPoint.getTarget().getClass());
		System.out.println("Signature join point " + thisJoinPoint.getSignature().getName());
		System.out.println("Source location join point " + thisJoinPoint.getSourceLocation().getWithinType().getSimpleName());
		System.out.println("Args join point " + thisJoinPoint.getArgs());
		//System.out.println(
	      //         "Join Point at: " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName()
	        //               + " --> " + thisJoinPointStaticPart.getSourceLocation().getLine());
	}
	
	after(): initObj() {
		System.out.println("*****AFTER INIT *****");
		System.out.println(
	               "Join Point at: " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName()
	                       + " --> " + thisJoinPointStaticPart.getSourceLocation().getLine());
		System.out.println(thisJoinPoint.getStaticPart().getClass().getSimpleName());
		System.out.println("This join point " + thisJoinPoint.getThis().getClass());
		System.out.println("Target join point " + thisJoinPoint.getTarget().getClass());
		System.out.println("Signature join point " + thisJoinPoint.getSignature().getName());
		System.out.println("Source location join point " + thisJoinPoint.getSourceLocation().getWithinType().getSimpleName());
		System.out.println("Args join point " + thisJoinPoint.getArgs());
	}*/
	
	/*before() : callMain() {
		System.out.println(
	               "Join Point at: " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName()
	                       + " --> " + thisJoinPointStaticPart.getSourceLocation().getLine());
		if (thisJoinPoint.getThis() != null) {
			System.out.println("This join point " + thisJoinPoint.getThis().getClass());
		}
		if (thisJoinPoint.getTarget() != null) {
			System.out.println("Target join point " + thisJoinPoint.getTarget().getClass());
		}
		System.out.println("Signature join point " + thisJoinPoint.getSignature().getName());
		System.out.println("Source location join point " + thisJoinPoint.getSourceLocation().getWithinType().getSimpleName());
		System.out.println("Args join point " + thisJoinPoint.getArgs());

	}
	
	after() returning (): callMain() {
		System.out.println(
	               "Join Point at: " + thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName()
	                       + " --> " + thisJoinPointStaticPart.getSourceLocation().getLine());
		if (thisJoinPoint.getThis() != null) {
			System.out.println("This join point " + thisJoinPoint.getThis().getClass());
		}
		if (thisJoinPoint.getTarget() != null) {
			System.out.println("Target join point " + thisJoinPoint.getTarget().getClass());
		}
		System.out.println("Signature join point " + thisJoinPoint.getSignature().getName());
		System.out.println("Source location join point " + thisJoinPoint.getSourceLocation().getWithinType().getSimpleName());
		System.out.println("Args join point " + thisJoinPoint.getArgs());
		System.out.println("***********Returning **********");
	}*/
}
