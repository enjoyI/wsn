package topogenerator;

public class TGParticle {

	public static final int INS_NODE = 1;
	public static final int INS_GATEWAY = 2;
	public static final int VIEWPORT_REPAINT = 3;
	public static final int GATEWAYLIST_REPAINT = 4;
	public int type;
	public double nodex;
	public double nodey;
	public int gatewayAroundDensity;
	public TGParticle(int t) {
		type = t;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
