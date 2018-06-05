package schedules;
import java.security.KeyStore.Entry.Attribute;

/**
 * 
 * @author Denis
 *
 */
public class Operation {
	private String type, attribute, transaction;

	public Operation(String type, String attribute, String transaction) {
		this.type = type;
		this.attribute = attribute;
		this.transaction = transaction;
	}
	public String getType() {
		return type;
	}

	public String getAttribute() {
		return attribute;
	}

	public String getTransaction() {
		return transaction;
	}

	public String stringify() {
		if (type.equals("r") || type.equals("w") || type.equals("rl") || type.equals("wl") || type.equals("u"))
			return type + transaction + "(" + attribute + ")";
		else
			return type + transaction;
	}
}
