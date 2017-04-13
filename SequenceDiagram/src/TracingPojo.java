package aspectj.sequencediagram;

import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

public class TracingPojo {
	private TracingType tracingType;
	private Class<? extends Object> source;
	private Class<? extends Object> target;
	private Signature signature;
	private SourceLocation sourceLocation;
	private Object[] args;
	
	public TracingPojo(TracingType tracingType, Class<? extends Object> source, Class<? extends Object> target, Signature signature,
						SourceLocation sourceLocation, Object[] args) {
		this.tracingType = tracingType;
		this.source = source;
		this.target = target;
		this.sourceLocation = sourceLocation;
		this.signature = signature;
		this.args = args;
	}
	
	public TracingType getTracingType() {
		return tracingType;
	}
	public void setTracingType(TracingType tracingType) {
		this.tracingType = tracingType;
	}
	public Class<? extends Object> getSource() {
		return source;
	}
	public void setSource(Class<? extends Object> source) {
		this.source = source;
	}
	public Class<? extends Object> getTarget() {
		return target;
	}
	public void setTarget(Class<? extends Object> target) {
		this.target = target;
	}
	public Signature getSignature() {
		return signature;
	}
	public void setSignature(Signature signature) {
		this.signature = signature;
	}
	public SourceLocation getSourceLocation() {
		return sourceLocation;
	}
	public void setSourceLocation(SourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}

}
