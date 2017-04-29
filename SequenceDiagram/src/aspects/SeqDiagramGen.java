package aspects; 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;

public class SeqDiagramGen {
	static String activator = "";
	static boolean isStart = false;
	static Stack<String> startStack = new Stack<String>();
	static String currentTarget ="";
	static String currentSource = "";
	public String buildSeqDiagram(List<TracingPojo> pojos) {
		StringBuilder sb = new StringBuilder();
		sb.append("@startuml\n").append("autonumber\n");
		for (TracingPojo pojo: pojos) {
			//printStack();
			switch(pojo.getTracingType()) {
				case START:
					sb.append(buildCallMessage(pojo));
					break;
				case END:
					sb.append(buildReturnMessage(pojo));
					break;
			}
		}
		//printStack();
		sb.append("@enduml\n");
		return sb.toString();
	}
	
	public void createFile(String seqStr, String outputDir) throws FileNotFoundException {
		 try {
			 StringBuilder sb = new StringBuilder(outputDir);
			 sb.append("/sequence.png");
			 SourceStringReader reader = new SourceStringReader(seqStr);
	         FileOutputStream output = new FileOutputStream(new File(sb.toString()));
	         reader.generateImage(output, new FileFormatOption(FileFormat.PNG, false));
	     } catch (Exception e) {
	        System.out.println(e.getMessage());
	     }
	}
	
	private static String buildCallMessage(TracingPojo pojo) {
		StringBuilder sb = new StringBuilder();
		String source = (pojo.getSource() != null ? pojo.getSource().getSimpleName() : pojo.getSourceLocation().getWithinType().getSimpleName());
		
		while (!startStack.isEmpty() && !startStack.peek().equals(source)) {
			//System.out.println("Pop " + startStack.peek());
			sb.append("deactivate ").append(startStack.pop()).append("\n");
		}
		
		sb.append(source).append("->").append(pojo.getTarget().getSimpleName()).append(":")
				.append(pojo.getSignature().getName()).append("(");
		if (pojo.getArgs().length > 0) {
			sb.append(Arrays.deepToString(pojo.getArgs()));
		}
		sb.append(")\n");
		sb.append("activate ").append(pojo.getTarget().getSimpleName()).append("\n");

		startStack.push(pojo.getTarget().getSimpleName());
		//System.out.println("Push " + startStack.peek());
		
		return sb.toString();
	}
	
	private static String buildReturnMessage(TracingPojo pojo) {
		if (startStack.peek().equals(pojo.getTarget().getSimpleName())) {
			//System.out.println("Pop After return " + startStack.peek());
			startStack.pop();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(pojo.getTarget().getSimpleName()).append("-->").append(pojo.getSource().getSimpleName())
				.append(": return").append("(").append(Arrays.deepToString(pojo.getArgs())).append(")\n");
		sb.append("deactivate ").append(pojo.getTarget().getSimpleName()).append("\n");
		return sb.toString();
	}
	
	private void printStack() {
		System.out.println("***********************");
		for (int i=0; i < startStack.size(); i++) {
			System.out.println(startStack.get(i));
		}
		System.out.println("***********************");
	}

}
