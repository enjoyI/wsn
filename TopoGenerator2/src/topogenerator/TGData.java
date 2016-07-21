package topogenerator;

import java.util.ArrayList;
import java.util.Comparator;

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

	TGPanel panel;

	public TGData() {
		init();
		initThread();
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
	}

	public void addGateway(double x, double y, int p) {
		gateways.add(new Pnt(x, y, p));
		changeNodes();
		System.out.println("TGData: insert gateway " + gateways.size());
		panelRepaint();
	}

	public void addNode(double x, double y) {
		double mind = xsize, d = 0;
		int minpp = -1;
		for (int k = 0; k < gateways.size(); ++k) {
			d = Math.sqrt(Math.pow((x + 0.0) / 10 - gateways.get(k).xPos, 2)
					+ Math.pow((y + 0.0) / 10 - gateways.get(k).yPos, 2));
			if (mind > d) {
				mind = d;
				minpp = k;
			}
		}
		Pnt tPnt = new Pnt(x, y);
		nodes.add(tPnt);
		gateways.get(minpp).nodesAmt++;
		gateways.sort(new PntCmp());
	}

	public void panelRepaint() {
		TGParticle tgp1 = new TGParticle(TGParticle.VIEWPORT_REPAINT);
		TGParticle tgp2 = new TGParticle(TGParticle.GATEWAYLIST_REPAINT);
		synchronized (panel.receiverEvents) {
			panel.receiverEvents.add(tgp1);
			panel.receiverEvents.add(tgp2);
			panel.receiverEvents.notify();
		}
	}

	public void changeNodes() {
		nodes = new ArrayList<>();
		double mind = xsize, d = 0;
		double minp = 1;
		int minpp = -1;
		double keyStep;
		Pnt tatom = null;
		for (int i = 0; i < (int) xsize * 10; ++i) {
			for (int j = 0; j < (int) ysize * 10; ++j) {
				mind = xsize;
				minp = 1;
				minpp = -1;
				keyStep = 0;
				for (int k = 0; k < gateways.size(); ++k) {
					d = Math.sqrt(Math.pow((i + 0.0) / 10 - gateways.get(k).xPos, 2)
							+ Math.pow((j + 0.0) / 10 - gateways.get(k).yPos, 2));
					if (mind > d) {
						mind = d;
						minp = gateways.get(k).gatewayAroundDensity / (Math.PI * radiu * radiu);
						minpp = k;
					}
				}
				keyStep = Math.sqrt(1 / minp);
				if (((i / 10.0 / keyStep - (int) (i / 10.0 / keyStep)) < (0.1 / keyStep))
						&& ((j / 10.0 / keyStep - (int) (j / 10.0 / keyStep)) < (0.1 / keyStep))) {
					tatom = new Pnt(i / 10.0, j / 10.0);
					if (minpp != -1) {
						tatom.parentGateway = gateways.get(minpp).id;
						gateways.get(minpp).nodesAmt++;
						nodes.add(tatom);
					}
				}
			}
		}
		gateways.sort(new PntCmp());
	}

	public Pnt selectById(int id) {
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

	class ReceiverThread extends Thread {

		@Override
		public void run() {
			try {
				synchronized (receiverEvents) {
					while (true) {
						if (receiverEvents.isEmpty() == true) {
							receiverEvents.wait();
						} else {
							TGParticle tgp = receiverEvents.get(0);
							receiverEvents.remove(0);
							if (tgp.type == TGParticle.INS_GATEWAY) {
								addGateway(tgp.nodex, tgp.nodey, tgp.gatewayAroundDensity);
							}
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
