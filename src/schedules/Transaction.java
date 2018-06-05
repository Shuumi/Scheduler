package schedules;
import java.util.InputMismatchException;
import java.util.LinkedList;

/**
 * 
 * @author Denis
 *
 */
public class Transaction {
	private LinkedList<Operation> operations;
	private boolean aborted = false;
	private boolean ended = false;
	
	public Transaction(){
		operations = new LinkedList<>();
	}
	
	public void addOperation(Operation op){
		if(ended){
			throw new InputMismatchException("No further operations after commit/abort allowed.");
		}
		
		operations.add(op);
		if(op.getType().equals("a")){
			aborted = true;
		}
		if(op.getType().equals("a") || op.getType().equals("c")){
			ended = true;
		}
	}
	
	public String stringify(){
		String returned = "";
		for(Operation op: operations){
			returned+= op.stringify();
		}
		
		return returned;
	}
	
	//-----------------------Getters------------------------
	
	public LinkedList<Operation> getOperations(){
		return operations;
	}
	
	public boolean isAborted(){
		return aborted;
	}
}
