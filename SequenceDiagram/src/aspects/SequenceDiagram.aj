package aspects; 
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

public aspect SequenceDiagram {
	private static List<TracingPojo> tracingPojos = new ArrayList<TracingPojo>();
	//pointcut exclusionClass(): !within(SequenceDiagram) && !within(SeqDiagramGen) && !within(TracingType) && !within(TracingPojo);
	//pointcut inclusion(): within(Main) && within(ConcreteObserver) && within(ConcreteSubject) && within(Observer) && within(Optimist) && within(Pessimist) && within(Subject) && within(TheEconomy);
	pointcut trace() : within (code.*) && call(* code.*.*(..)) && !cflow(initialization(*.new(..)));
	before(): trace() {
		printJoinPoint(thisJoinPoint);
		traceStart(getThis(thisJoinPoint), getTarget(thisJoinPoint), thisJoinPoint.getSignature(), thisJoinPoint.getSourceLocation(), thisJoinPoint.getArgs());
	}
	
	after() returning(Object o): trace() {
		traceEnd(getThis(thisJoinPoint), getTarget(thisJoinPoint), thisJoinPoint.getSignature(), thisJoinPoint.getSourceLocation(), o);
	}
	
	public void traceStart(final Class<? extends Object> joinPoint, final Class<? extends Object> target, 
			final Signature signature, final SourceLocation sourceLocation, final Object[] args) {
		if (target != null) {
			tracingPojos.add(new TracingPojo(TracingType.START, joinPoint, target, signature, sourceLocation, args));
		}
	}
	
	public void traceEnd(final Class<? extends Object> joinPoint, final Class<? extends Object> target, 
			final Signature signature, final SourceLocation sourceLocation, final Object... returnValue) {
		if (joinPoint != null && target != null) {
			if (returnValue[0] != null) {
				tracingPojos.add(new TracingPojo(TracingType.END, joinPoint, target, signature, sourceLocation, returnValue));
			}
		}
	}
	
	public void printJoinPoint(JoinPoint joinPoint) {
		System.out.println("This     : "+ getThis(joinPoint));
        System.out.println("Target   : "+ getTarget(joinPoint));
        System.out.println("Signature: "+ joinPoint.getSignature());
        System.out.println("Args     : "+ Arrays.deepToString(joinPoint.getArgs()));
        System.out.println("Source   : "+ joinPoint.getSourceLocation().getWithinType().getCanonicalName());//.getSourceLocation().toString());
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
	
	public static List<TracingPojo> getTracingPojos() {
		return Collections.unmodifiableList(tracingPojos);
	}

	pointcut doneTracing(): execution(* code.Main.main(..));
	
	after(): doneTracing() {
		System.out.println("After done tracing Number of message " + getTracingPojos().size());
		SeqDiagramGen seqGen = new SeqDiagramGen();
		String seqString = seqGen.buildSeqDiagram(getTracingPojos());
		try {
			seqGen.createFile(seqString);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Seq " + seqString);
	}
	
}
