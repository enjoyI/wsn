package topogenerator;

import java.io.FileWriter;
import java.io.IOException;

public class TopoGenerator {

	public Crystal crystal;
	public double floatRadio;
	public double communicateRadio;
	public int xlen;
	public int ylen;
	
	public TopoGenerator(int x, int y) {
		this.init(x,y);
		this.changeAtomPos();
	}

	public void init(int x,int y){
		this.xlen=x;
		this.ylen=y;
		crystal=new Crystal(x,y);
		communicateRadio = CrystalCell.sideLength*1.365;
		floatRadio = 0.0*communicateRadio;
	}
	
	public void changeAtomPos(){
		crystal.changeAtomPos(this.floatRadio);
	}
	
	public String serilizeAllInfo(){
		String info="";
		info +=String.valueOf(floatRadio);
		info +="\n";
		info+=crystal.serilizeAllInfo();
		return info;
	}
	
	public String atomPixInfo(){
		String info="";
		info = crystal.atomPixInfo();
		return info;		
	}
	
	public static void main(String args[]){
		TopoGenerator t = new TopoGenerator(6,10);
		System.out.println(t.serilizeAllInfo());
		System.out.println();

		try {
            FileWriter writer = new FileWriter("topo.txt",true);
            writer.write(t.atomPixInfo());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
