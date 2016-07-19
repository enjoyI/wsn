package topogenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RandomGenerator {

	ArrayList<Atom> gateways;
	ArrayList<Double> gatewayProb;
	ArrayList<Atom> nodes;
	double xsize;
	double ysize;
	double radio;
	
	public static double[][] gatewaySetting={
//			{0.25,0.75,0.50,0.25,0.75},
//			{0.25,0.25,0.52,0.75,0.75},
//			{10,5,5,5,10}
//			{0.16,0.48,0.81,0.17,0.50,0.79},
//			{0.23,0.26,0.27,0.74,0.77,0.74},
//			{5,10,5,5,10,5}
//			{0.33,0.67,0.24,0.51,0.76,0.30,0.63},
//			{0.25,0.25,0.51,0.49,0.48,0.75,0.75},
//			{10,5,5,5,5,5,10}
//			{0.16,0.49,0.79,0.33,0.67,0.14,0.51,0.78},
//			{0.12,0.16,0.13,0.49,0.52,0.84,0.86,0.88},
//			{5,10,5,5,5,5,10,5}
//			{0.24,0.49,0.76,0.26,0.51,0.74,0.26,0.49,0.77},
//			{0.22,0.21,0.19,0.48,0.49,0.52,0.80,0.79,0.78},
//			{5,5,5,10,5,10,5,5,5}
//			{0.12,0.38,0.63,0.88,0.24,0.74,0.13,0.37,0.61,0.87},
//			{0.16,0.17,0.19,0.18,0.48,0.52,0.80,0.79,0.84,0.85},
//			{5,5,5,5,10,10,5,5,5,5}
//			{0.12,0.38,0.63,0.88,0.24,0.51,0.74,0.13,0.37,0.61,0.87},
//			{0.16,0.17,0.19,0.18,0.48,0.49,0.52,0.80,0.79,0.84,0.85},
//			{5,5,5,5,10,10,10,5,5,5,5}
			{0.16,0.49,0.82,0.17,0.49,0.83,0.14,0.52,0.82,0.18,0.48,0.81},
			{0.12,0.13,0.14,0.37,0.36,0.35,0.62,0.61,0.63,0.87,0.88,0.86},
			{5,5,5,5,5,10,10,5,5,5,5,5}
//			{0.51,0.12,0.38,0.63,0.88,0.24,0.51,0.74,0.13,0.37,0.61,0.87,0.48},
//			{0.11,0.24,0.32,0.31,0.24,0.49,0.52,0.51,0.79,0.71,0.69,0.74,0.91},
//			{5,5,10,5,5,5,5,10,5,10,5,5,5}
	};
	
	
	public RandomGenerator(double x,double y,double r){
		this.init(x, y, r);
	}
	
 	public void initGateway(){
		gateways = new ArrayList<>();
		gatewayProb = new ArrayList<>();
		Atom tatom=null;
		tatom=new Atom(xsize*0.15,ysize*0.2,0);
		gateways.add(tatom);
		gatewayProb.add(10.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.63,ysize*0.2,0);
		gateways.add(tatom);
		gatewayProb.add(5.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.5,ysize*0.4,0);
		gateways.add(tatom);
		gatewayProb.add(5.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.12,ysize*0.5,0);
		gateways.add(tatom);
		gatewayProb.add(5.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.85,ysize*0.5,0);
		gateways.add(tatom);
		gatewayProb.add(5.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.3,ysize*0.6,0);
		gateways.add(tatom);
		gatewayProb.add(10.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.6,ysize*0.6,0);
		gateways.add(tatom);
		gatewayProb.add(5.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.4,ysize*0.9,0);
		gateways.add(tatom);
		gatewayProb.add(5.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.7,ysize*0.9,0);
		gateways.add(tatom);
		gatewayProb.add(10.0/(Math.PI*radio*radio));
	}
 	
 	public void initGateway1(){
		gateways = new ArrayList<>();
		gatewayProb = new ArrayList<>();
		Atom tatom=null;
		tatom=new Atom(xsize*0.25,ysize*0.2,0);
		gateways.add(tatom);
		gatewayProb.add(5.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.75,ysize*0.2,0);
		gateways.add(tatom);
		gatewayProb.add(5.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.25,ysize*0.75,0);
		gateways.add(tatom);
		gatewayProb.add(10.0/(Math.PI*radio*radio));
		tatom=new Atom(xsize*0.75,ysize*0.8,0);
		gateways.add(tatom);
		gatewayProb.add(5.0/(Math.PI*radio*radio));
	}

	public void initGatewayByArray() {
		gateways = new ArrayList<>();
		gatewayProb = new ArrayList<>();
		Atom tatom = null;
		for (int i = 0; i < gatewaySetting[0].length; ++i) {
			tatom = new Atom(xsize * gatewaySetting[0][i], ysize * gatewaySetting[1][i], 0);
			gateways.add(tatom);
			gatewayProb.add(gatewaySetting[2][i] / (Math.PI * radio * radio));
		}
	}
	
	public void init(double xsize, double ysize, double radio){
		this.xsize=xsize;
		this.ysize=ysize;
		this.radio=radio;
		initGatewayByArray();
		initNodes2();
	}
	
	public void initNodes(){
		nodes = new ArrayList<>();
		double mind=xsize,d=0;
		double minp=1;
		Atom tatom=null;
		for(int i=0;i<(int)xsize*10;++i){
			for(int j=0;j<(int)ysize*10;++j){
				mind=xsize;
				minp=1;
				for(int k=0;k<gateways.size();++k){
					d=Math.sqrt(Math.pow((i+0.0)/10-gateways.get(k).x, 2)
							+Math.pow((j+0.0)/10-gateways.get(k).y, 2));
					if(mind>d){
						mind=d;
						minp=gatewayProb.get(k);
					}
				}
				if(Math.random()<minp/100){
					tatom=new Atom(i/10.0,j/10.0,0);
					nodes.add(tatom);
				}
			}
		}
	}
	
	
	public void initNodes2(){
		nodes = new ArrayList<>();
		double mind=xsize,d=0;
		double minp=1;
		double keyStep;
		Atom tatom=null;
		for(int i=0;i<(int)xsize*10;++i){
			for(int j=0;j<(int)ysize*10;++j){
				mind=xsize;
				minp=1;
				keyStep=0;
				for(int k=0;k<gateways.size();++k){
					d=Math.sqrt(Math.pow((i+0.0)/10-gateways.get(k).x, 2)
							+Math.pow((j+0.0)/10-gateways.get(k).y, 2));
					if(mind>d){
						mind=d;
						minp=gatewayProb.get(k);
					}
				}
				keyStep = Math.sqrt(1/minp);
				if(((i/10.0/keyStep-(int)(i/10.0/keyStep))<(0.1/keyStep))
						&&((j/10.0/keyStep-(int)(j/10.0/keyStep))<(0.1/keyStep))){
					tatom=new Atom(i/10.0,j/10.0,0);
					nodes.add(tatom);
				}
			}
		}
	}
	
	public String serilizeAllInfo(){
		String info="";
		for(int i=0;i<gateways.size();++i){
			info+="node ";
			info+=gateways.get(i).serilizeRandomInfo();
			info+="\n";
		}
		for(int i=0;i<nodes.size();++i){
			info+="node ";
			info+=nodes.get(i).serilizeRandomInfo();
			info+="\n";
		}
		return info;
	}
	
	public static void main(String args[]){
		RandomGenerator rg = new RandomGenerator(81,108,10.0/175*125);
		System.out.println(rg.serilizeAllInfo());
		System.out.println(rg.nodes.size());
		System.out.println();

		try {
            FileWriter writer = new FileWriter("topo.txt",true);
            writer.write(rg.serilizeAllInfo());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
