package topogenerator;

public class Atom {

	public static final int idrAmount=10000;
	public double x;
	public double y;
	public double z;
	public double absx;
	public double absy;
	public double absz;
	public int pixx;
	public int pixy;
	public int pixz;
	public int n;   //占 perfectAmount的个数
	public int innerCellNum;
	public int id;
	public static int[] idr;
	
	public Atom(double x,double y,double z, int n,int innerCellId){
		this.x=x;
		this.y=y;
		this.z=z;
		this.n=n;
		this.innerCellNum=innerCellId;
		this.id = this.requestId();
	}
	
	public Atom(double x,double y,double z){
		this.x=x;
		this.y=y;
		this.z=z;
		this.id = this.requestId();
	}
	
	static{
		initIDR();
	}
	
	private static void initIDR(){
		idr= new int[idrAmount];
		for(int i=0;i<idr.length;++i){
			idr[i]=0;
		}
	}
	
	private int requestId(){
		for(int i=0;i<idr.length;++i){
			if(idr[i]==0){
				idr[i] = 1;
				return i;
			}
		}
		return -1;
	}
	
	public void recycleID(){
		if(id>=0&&id<idrAmount){
			idr[id] = 0;
		}
		else{
			
		}
	}
	
	public String serilizeAllInfo(){
		String info="";
		info += String.valueOf(x);
		info += " ";
		info += String.valueOf(y);
		info += " ";
		info += String.valueOf(z);
		info += " ";
		info += String.valueOf(n);
		info += " ";
		info += String.valueOf(innerCellNum);
		info += " ";
		info += String.valueOf(id);
		return info;
	}
	

	public String serilizeRandomInfo(){
		String info="";
		info += String.valueOf((int)(x*175.0/10+50));
		info += " ";
		info += String.valueOf((int)(y*175.0/10+50));
		info += " ";
		info += String.valueOf((int)(z*175.0/10+0));
		return info;
	}

	public void changePosByDelta(double deltaX, double deltaY) {
		this.x+=deltaX;
		this.y+=deltaY;		
	}
	
	public String atomPixInfo(){
		String info="";
		info+="node";
		info+=" ";
		info +=String.valueOf(id);
		info+=" ";
		info+=String.valueOf((int)(absx*100.0/(CrystalCell.sideLength*1.365)));
		info+=" ";
		info+=String.valueOf((int)(absy*100.0/(CrystalCell.sideLength*1.365)));
		info+=" ";
		info+=String.valueOf((int)(absz*100.0/(CrystalCell.sideLength*1.365)));
		return info;
	}

	public void changeAbsPos(double x, double y,double z) {
		this.absx=x;
		this.absy=y;
		this.absz=z;
		
	}
}
