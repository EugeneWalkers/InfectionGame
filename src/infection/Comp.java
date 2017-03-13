package infection;

import java.util.Comparator;
import java.util.Vector;

public class Comp implements Comparator<Vector<String>>{

	@Override
	public int compare(Vector<String> arg0, Vector<String> arg1) {
		return Integer.parseInt(arg1.get(1)) - Integer.parseInt(arg0.get(1));
	}

}
