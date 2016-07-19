package topogenerator;

import java.util.ArrayList;

public class CrystalCell {

	public static double sideLength=1;
	public static int perfectAmount=4;
	private double length;//x axes
	private double width;//y axes
	ArrayList<Atom> atoms;
	
	CrystalCell(){
		initToGraphite();
	}
	
	public void initToGraphite(){
		//innercellid 从左往右，从上至下编号为0-7
		CrystalCell.perfectAmount=4;
		width = CrystalCell.sideLength*Math.sqrt(3);
		length = CrystalCell.sideLength*3;
		atoms = new ArrayList<>();
		Atom tAtom;
		tAtom = new Atom(0,0,0,1,5);
		atoms.add(tAtom);
		tAtom = new Atom(0,width,0,1,0);
		atoms.add(tAtom);
		tAtom = new Atom(length,0,0,1,7);
		atoms.add(tAtom);
		tAtom = new Atom(length,width,0,1,2);
		atoms.add(tAtom);
		tAtom = new Atom(2*sideLength,0,0,1,6);
		atoms.add(tAtom);
		tAtom = new Atom(2*sideLength,width,0,1,1);
		atoms.add(tAtom);
		tAtom = new Atom(0.5*sideLength,0.5*sideLength*Math.sqrt(3),0,1,3);
		atoms.add(tAtom);
		tAtom = new Atom(1.5*sideLength,0.5*sideLength*Math.sqrt(3),0,1,4);
		atoms.add(tAtom);
	}
	//本节点在cc的左边
	public void rightAdd(CrystalCell cc){
		int[] src = {2,7};
		int[] dst = {0,5};
		combine(cc,src,dst);
	}
	//本节点在cc的右边
	public void leftAdd(CrystalCell cc){
		int[] src = {0,5};
		int[] dst = {2,7};
		combine(cc,src,dst);
	}
	//本节点在cc的上边
	public void downAdd(CrystalCell cc){
		int[] src = {5,6,7};
		int[] dst = {0,1,2};
		combine(cc,src,dst);
	}
	//本节点在cc的下边
	public void upAdd(CrystalCell cc){
		int[] src = {0,1,2};
		int[] dst = {5,6,7};
		combine(cc,src,dst);
	}

	public void quadrant1Add(CrystalCell cc){
		int[] src = {2};
		int[] dst = {5};
		combine(cc,src,dst);
	}
	public void quadrant2Add(CrystalCell cc){
		int[] src = {0};
		int[] dst = {7};
		combine(cc,src,dst);
	}
	public void quadrant3Add(CrystalCell cc){
		int[] src = {5};
		int[] dst = {2};
		combine(cc,src,dst);
	}
	public void quadrant4Add(CrystalCell cc){
		int[] src = {7};
		int[] dst = {0};
		combine(cc,src,dst);
	}
	
	public void combine(CrystalCell cc,int[] src, int[] dst){//src是自己的
		Atom tsA;
		Atom tdA;
		for(int i=0;i<this.atoms.size();++i){
			tsA = this.atoms.get(i);
			for(int j=0;j<src.length;++j){
				if(tsA.innerCellNum==src[j]){
					for(int k=0;k<cc.atoms.size();++k){
						tdA = cc.atoms.get(k);
						if(tdA.innerCellNum==dst[j]){
							tsA.n+=tdA.n;
							tdA.recycleID();
							cc.atoms.remove(tdA);
						}
						else{}
					}
				}
				else{}
			}
		}
	}
	
	public String serilizeAllInfo(){
		String info="";
		info+=String.valueOf(length);
		info+=" ";
		info+=String.valueOf(width);
		info+="\n";
		for(int i=0;i<atoms.size();++i){
			info+=atoms.get(i).serilizeAllInfo();
			info+="\n";
		}
		return info;
	}
	
	public void changeAtomPos(double floatRatio){
		double angleSeed;
		double radioSeed;
		double deltaX;
		double deltaY;
		for(int i=0;i<atoms.size();++i){
			angleSeed = Math.random()*2*Math.PI;
			radioSeed = Math.random()*floatRatio;
			deltaX=Math.sin(angleSeed)*radioSeed;
			deltaY=Math.cos(angleSeed)*radioSeed;
			atoms.get(i).changePosByDelta(deltaX,deltaY);
		}
	}
	public static ArrayList<Integer> neighborInnerCellNum(int n){
		ArrayList<Integer> nei = new ArrayList<>();
		switch(n){
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		default:
				break;
		}
		return nei;
	}
	public String atomPixInfo(){
		String info="";
		for(int i=0;i<atoms.size();++i){
			info+=atoms.get(i).atomPixInfo();
			info+="\n";
		}
		return info;		
	}
}
