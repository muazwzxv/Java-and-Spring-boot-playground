import java.util.List;
import java.util.ArrayList;

public class Journal {
	private final List<String> entries = new ArrayList<String>();
	private static int count = 0;

	public void add(String data) {
		entries.add("" + (++count) + ": " + data);
	}

	public void remove(int index) {
		entries.remove(index);
	}

	@Override
	public String toString() {
		return String.join(System.lineSeparator(), this.entries);
	}
}
