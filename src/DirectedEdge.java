
public class DirectedEdge {
	private String from;
    private String to;
    private int weight;

    public DirectedEdge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "From: " + from + " To: " + to + " Weight: " + weight;
    }
}
