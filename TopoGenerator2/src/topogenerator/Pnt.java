package topogenerator;

public class Pnt {

	double xPos;
	double yPos;
	int gatewayAroundDensity;
	int nodesAmt;
	long parentGateway=-1;
	long id;
	private static long _id=0;
	
	public Pnt(double x,double y) {
		xPos=x;
		yPos=y;
		nodesAmt=0;
		id=_id;
		_id++;
	}

	public Pnt(double x,double y,int p) {
		xPos=x;
		yPos=y;
		gatewayAroundDensity=p;
		nodesAmt=0;
		id=_id;
		_id++;
	}

	public void addPnt(){
		nodesAmt++;
	}
	
	public void minerPnt(){
		nodesAmt--;
	}
}
