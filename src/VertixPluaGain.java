import java.util.List;
import java.util.ArrayList;
import java.util.List;

public class VertixPluaGain {
	 private List<String> vertices;
	    private double gain;

	    public VertixPluaGain(List<String> vertices, double gain) {
	        this.vertices = vertices;
	        this.gain = gain;
	    }

	    public List<String> getVertices() {
	        return vertices;
	    }

	    public double getGain() {
	        return gain;
	    }

	    @Override
	    public String toString() {
	        return "vertices: " + vertices.toString() + " gain: " + gain;
	    }
}
