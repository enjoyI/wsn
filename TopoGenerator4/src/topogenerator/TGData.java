package topogenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TGData {

	public static final int defaultGatewayAroundAmt = 5;
	ArrayList<TGParticle> innerEvents;
	ArrayList<TGParticle> senderEvents;
	ArrayList<TGParticle> receiverEvents;

	ArrayList<Pnt> nodes;
	ArrayList<Pnt> gateways;
	double xsize;
	double ysize;
	double radiu;

//	TGPanel panel;

	private static double[] dn = { 1, 1.0 / 1, 1.0 / 2, 1.0 / 3, 1.0 / 4, 1.0 / 5, 1.0 / 6, 1.0 / 7, 1.0 / 8, 1.0 / 9,
			1.0 / 10 };
	public static Double[][] gatewaySetting;

	public TGData() {
		init();
		initThread();
	}

	public void generateGatewayArray() {
		ArrayList<Double> xList = new ArrayList<>();
		ArrayList<Double> yList = new ArrayList<>();
		ArrayList<Double> amt = new ArrayList<>();
		int[] dnList = {
//				8, 7, 8, 7, 8, 7, 8, 7
//				8,9,9,9,9,9,9,8
//				9,9,9,9,8,9,9,9,9
//				10,10,10,10,10,10,10,10,10
				10,10,10,10,10,10,10,10,10,10
				};
		for (int i = 0; i < dnList.length; ++i) {
			for (int j = 0; j < dnList[i]; ++j) {
				xList.add(dn[dnList[i]] * (j + 0.5));
				yList.add(dn[dnList.length] * (i + 0.5));
				amt.add(5.0);
			}
		}
		
		int[] mL = {
//				0,4,9,13,17,21, 26, 30,34,40,44,46,50,54,58
//				3,7,8,11,14,19, 23,26,31,36,40, 44,48,52,56,59, 62,66
				0,4,8,12,16, 20,24,28,32,36, 40,44,48,55,59, 63,67,71,75,79, 83,87,91,95,99
				};
		for (int i = 0; i < mL.length;++i) {
			amt.set(mL[i], 10.0);
		}
		System.out.println("TGData: amt.size"+amt.size());
		gatewaySetting = new Double[3][];
		Double[] t = new Double[xList.size()];
		xList.toArray(t);
		gatewaySetting[0] = t;
		t = new Double[yList.size()];
		yList.toArray(t);
		gatewaySetting[1] = t;
		t = new Double[amt.size()];
		amt.toArray(t);
		gatewaySetting[2] = t;
	}

	public void setGatewayByArray() {
		gateways = new ArrayList<>();
		Pnt pnt = null;
		for (int i = 0; i < gatewaySetting[0].length; ++i) {
			pnt = new Pnt(xsize * gatewaySetting[0][i], ysize * gatewaySetting[1][i],
					Math.round(gatewaySetting[2][i].floatValue()));
			gateways.add(pnt);
		}
	}

	public void init() {
		nodes = new ArrayList<>();
		gateways = new ArrayList<>();
		innerEvents = new ArrayList<>();
		senderEvents = new ArrayList<>();
		receiverEvents = new ArrayList<>();

		xsize = 0;
		ysize = 0;
		radiu = 10;
	}

	public void initThread() {
		InnerThread it = new InnerThread();
		SenderThread st = new SenderThread();
		ReceiverThread rt = new ReceiverThread();
		it.start();
		st.start();
		rt.start();
	}

	public void setSize(double x, double y) {
		xsize = x;
		ysize = y;
		gateways.clear();
		nodes.clear();
	}

	public void setxSize(double x) {
		xsize = x;
		gateways.clear();
		nodes.clear();
	}

	public void setySize(double y) {
		ysize = y;
		gateways.clear();
		nodes.clear();
	}

	public int checkVisualable() {
		if (xsize > 0 && ysize > 0 && radiu > 0 && gateways.isEmpty() == false) {
			return 1;
		} else {
			return 0;
		}
	}

	public void addGateway(double x, double y, int p) {
		if (x > xsize || x < 0 || y > ysize || y < 0)
			return;
		gateways.add(new Pnt(x, y, p));
		changeNodes();
		System.out.println("TGData: insert gateway " + gateways.size());
		panelRepaint();
	}

	public void addNode(double x, double y) {
		if (x > xsize || x < 0 || y > ysize || y < 0)
			return;
		double mind = Math.sqrt(xsize * xsize + ysize * ysize), d = 0;
		int minpp = -1;
		for (int k = 0; k < gateways.size(); ++k) {
			d = Math.sqrt(Math.pow(x - gateways.get(k).xPos, 2) + Math.pow(y - gateways.get(k).yPos, 2));
			if (mind > d) {
				mind = d;
				minpp = k;
			}
		}
		Pnt tPnt = new Pnt(x, y);
		tPnt.parentGateway = gateways.get(minpp).id;
		gateways.get(minpp).nodesAmt++;
		gateways.sort(new PntCmp2());
		nodes.add(tPnt);
		System.out.println("TGData: insert node " + nodes.size() + "parent gateway:" + tPnt.parentGateway);
		panelRepaint();
	}

	public void panelRepaint() {
		TGParticle tgp1 = new TGParticle(TGParticle.VIEWPORT_REPAINT);
		TGParticle tgp2 = new TGParticle(TGParticle.GATEWAYLIST_REPAINT);
//		synchronized (panel.receiverEvents) {
//			panel.receiverEvents.add(tgp1);
//			panel.receiverEvents.add(tgp2);
//			panel.receiverEvents.notify();
//		}
	}

	public void changeNodes() {
		synchronized (nodes) {
			nodes = new ArrayList<>();
			resetGatewayAmt();
			double mind = Math.sqrt(xsize * xsize + ysize * ysize), d = 0;
			double minp = 100;
			int minpp = -1;
			double keyStep;
			Pnt tatom = null;
			if (gateways.isEmpty() != true) {
				for (int i = 0; i < (int) xsize * 10; ++i) {
					for (int j = 0; j < (int) ysize * 10; ++j) {
						mind = Math.sqrt(xsize * xsize + ysize * ysize);
						minp = 1;
						minpp = -1;
						keyStep = 0.0;
						int k;
						for (k = 0; k < gateways.size(); ++k) {
							d = Math.sqrt(Math.pow((i + 0.0) / 10 - gateways.get(k).xPos, 2)
									+ Math.pow((j + 0.0) / 10 - gateways.get(k).yPos, 2));
							// System.out.println("TGData>> d: "+d);
							if (mind > d) {
								minpp = k;
								mind = d;
								minp = gateways.get(k).gatewayAroundDensity / (Math.PI * radiu * radiu);
							}
						}
						keyStep = Math.sqrt(1 / minp);
						if (((i / 10.0 / keyStep - (int) (i / 10.0 / keyStep)) < (0.1 / keyStep))
								&& ((j / 10.0 / keyStep - (int) (j / 10.0 / keyStep)) < (0.1 / keyStep))) {
							tatom = new Pnt(i / 10.0, j / 10.0);
							if (minpp == -1) {
								System.out.println("minpp==-1 gateways size=" + gateways.size() + " d=" + d + " mind="
										+ mind + " k=" + k);
							}
							tatom.parentGateway = gateways.get(minpp).id;
							gateways.get(minpp).nodesAmt++;
							nodes.add(tatom);
							// }
						}
					}
				}
				gateways.sort(new PntCmp2());
				System.out.println("nodes amount: " + nodes.size());
			}
		}
	}

	public void resetGatewayAmt() {
		for (int i = 0; i < gateways.size(); ++i) {
			gateways.get(i).nodesAmt = 0;
		}
	}

	public void deleteNode(double x, double y, double deltax, double deltay) {
		Pnt pnt;
		Pnt gpnt;
		for (int i = 0; i < nodes.size(); ++i) {
			pnt = nodes.get(i);
			if (pnt.xPos >= x && pnt.xPos <= (x + deltax) && pnt.yPos >= y && pnt.yPos <= (y + deltay)) {
				gpnt = selectById(pnt.parentGateway);
				gpnt.nodesAmt--;
				nodes.remove(pnt);
				gateways.sort(new PntCmp2());
				panelRepaint();
				break;
			}
		}
	}

	public void deleteGateway(double x, double y, double deltax, double deltay) {
		Pnt pnt;
		for (int i = 0; i < gateways.size(); ++i) {
			pnt = gateways.get(i);
			if (pnt.xPos >= x && pnt.xPos <= (x + deltax) && pnt.yPos >= y && pnt.yPos <= (y + deltay)) {
				gateways.remove(pnt);
				changeNodes();
				panelRepaint();
				break;
			}
		}
	}

	public Pnt selectById(long id) {
		Pnt pnt;
		for (int i = 0; i < gateways.size(); ++i) {
			if (gateways.get(i).id == id) {
				pnt = gateways.get(i);
				return pnt;
			}
		}
		for (int i = 0; i < nodes.size(); ++i) {
			if (nodes.get(i).id == id) {
				pnt = nodes.get(i);
				return pnt;
			}
		}
		return null;
	}

	public static void main(String[] args) {

	}

	@SuppressWarnings("unused")
	private class PntCmp implements Comparator<Pnt> {

		@Override
		public int compare(Pnt o1, Pnt o2) {
			if (o1.nodesAmt > o2.nodesAmt) {
				return 1;
			} else if (o1.nodesAmt < o2.nodesAmt) {
				return -1;
			} else {
				return 0;
			}
		}

	}

	private class PntCmp2 implements Comparator<Pnt> {

		@Override
		public int compare(Pnt o1, Pnt o2) {
			if (o1.nodesAmt > o2.nodesAmt) {
				return -1;
			} else if (o1.nodesAmt < o2.nodesAmt) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	
	class InnerThread extends Thread {

		@Override
		public void run() {
			try {
				synchronized (innerEvents) {
					while (true) {
						if (innerEvents.isEmpty() == true) {
							innerEvents.wait();
						} else {

						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class SenderThread extends Thread {

		@Override
		public void run() {
			try {
				synchronized (senderEvents) {
					while (true) {
						if (senderEvents.isEmpty() == true) {
							senderEvents.wait();
						} else {

						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static final int receiverThreadEnd=0;
	private int receiverThreadEndFlag=receiverThreadEnd;
	private ReentrantReadWriteLock receiverEventsLock = new ReentrantReadWriteLock();
	class ReceiverThread extends Thread {

		@Override
		public void run() {
			try {
				while (receiverThreadEndFlag==receiverThreadEnd) {
					TGParticle tgp = null;
					receiverEventsLock.writeLock().lock();
						if (receiverEvents.isEmpty() == true) {
							receiverEvents.wait();
						} else {
							tgp = receiverEvents.get(0);
							receiverEvents.remove(0);
						}
					receiverEventsLock.writeLock().unlock();
					if (tgp != null) {
						if (tgp.type == TGParticle.INS_GATEWAY) {
							addGateway(tgp.nodex, tgp.nodey, tgp.gatewayAroundDensity);
						} else if (tgp.type == TGParticle.INS_NODE) {
							addNode(tgp.nodex, tgp.nodey);
							System.out.println("TGData->ReceiverThread: insert node");
						}
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	String serilize2Pix() {
		String str = "";
		for (int i = 0; i < gateways.size(); ++i) {
			str += "node ";
			str += String.valueOf((int) (gateways.get(i).xPos / radiu * 140));
			str += " ";
			str += String.valueOf((int) (gateways.get(i).yPos / radiu * 140));
			str += " 0 \n";
		}
		for (int i = 0; i < nodes.size(); ++i) {
			str += "node ";
			str += String.valueOf((int) (nodes.get(i).xPos / radiu * 140));
			str += " ";
			str += String.valueOf((int) (nodes.get(i).yPos / radiu * 140));
			str += " 0 \n";
		}
		return str;
	}

	public void save2Text() {
		try {
			FileWriter writer = new FileWriter("topo.txt", true);
			writer.write(serilize2Pix());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}