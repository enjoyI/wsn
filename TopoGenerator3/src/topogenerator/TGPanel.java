package topogenerator;

import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TGPanel extends JPanel implements ActionListener, KeyListener, MouseListener {

	private static final char INSERT_NODE = 'q';
	private static final char DELETE_NODE = 'w';
	private static final char INSERT_GATEWAY = 'e';
	private static final char DELETE_GATEWAY = 'r';
	private static final char INC_SCALE = 'a';
	private static final char DEC_SCALE = 's';

	ViewportPanel viewportPanel;
	JPanel statuPanel1;
	JScrollPane statuPane2;
	JScrollPane statuPane2_1;
	JPanel statuPanel3;
	JTextField xsizeText;
	JTextField ysizeText;
	JTextField scaleText;
	JTextField ardAmtText;
	JPanel gatewaysInfoPanel;
	JPanel gatewaysInfoPanel2;
	JButton save2Text;
	JButton save2Img;
	TGData tgd;

	ArrayList<TGParticle> innerEvents;
	ArrayList<TGParticle> senderEvents;
	ArrayList<TGParticle> receiverEvents;

	int ardAmt = TGData.defaultGatewayAroundAmt;

	public TGPanel() {
		initial();
		initialEventQueue();
		initThread();
		tgd.setSize(379, 379);
		tgd.generateGatewayArray();
		tgd.setGatewayByArray();
		tgd.changeNodes();
	}

	public void initial() {
		this.setLayout(null);
		statuPanel1 = new JPanel();
		statuPanel1.setBounds(551, 0, 100, 550);
		statuPanel1.setLayout(new GridLayout(0, 1));
		statuPanel1.add(new JLabel("x:"));
		xsizeText = new JTextField(5);
		statuPanel1.add(xsizeText);
		statuPanel1.add(new JLabel("y:"));
		ysizeText = new JTextField(5);
		statuPanel1.add(ysizeText);
		statuPanel1.add(new JLabel("scale:"));
		scaleText = new JTextField(5);
		statuPanel1.add(scaleText);
		statuPanel1.add(new JLabel("ard_amt:"));
		ardAmtText = new JTextField(5);
		statuPanel1.add(ardAmtText);

		statuPane2 = new JScrollPane();
		statuPane2.setBounds(651, 0, 90, 550);
		gatewaysInfoPanel = new JPanel();
		gatewaysInfoPanel.setLayout(new GridLayout(0, 1));
		statuPane2.setViewportView(gatewaysInfoPanel);

		statuPane2_1 = new JScrollPane();
		statuPane2_1.setBounds(751, 0, 90, 550);
		gatewaysInfoPanel2 = new JPanel();
		gatewaysInfoPanel2.setLayout(new GridLayout(0, 1));
		statuPane2_1.setViewportView(gatewaysInfoPanel2);

		statuPanel3 = new JPanel();
		statuPanel3.setBounds(851, 0, 90, 550);
		save2Text = new JButton("to text");
		save2Img = new JButton("to img");
		statuPanel3.add(save2Text);
		statuPanel3.add(save2Img);

		this.add(statuPanel1);
		this.add(statuPane2);
		this.add(statuPane2_1);
		this.add(statuPanel3);

		tgd = new TGData();
		tgd.panel = this;
		viewportPanel = new ViewportPanel(tgd);
		viewportPanel.setBounds(0, 0, 550, 550);
		this.add(viewportPanel);

		viewportPanel.addMouseListener(this);
		viewportPanel.addKeyListener(this);
		xsizeText.addActionListener(this);
		ysizeText.addActionListener(this);
		scaleText.addActionListener(this);
		ardAmtText.addActionListener(this);
		save2Text.addActionListener(this);
	}

	public void initialEventQueue() {
		innerEvents = new ArrayList<>();
		senderEvents = new ArrayList<>();
		receiverEvents = new ArrayList<>();
	}

	public void intiTGData(TGData tgd) {
	}

	public void initThread() {
		InnerThread it = new InnerThread();
		SenderThread st = new SenderThread();
		ReceiverThread rt = new ReceiverThread();
		it.start();
		st.start();
		rt.start();
	}

	public void repaintViewport() {
		viewportPanel.repaint();
	}

	public void repaintGateway() {
		JLabel label;
		gatewaysInfoPanel.removeAll();
		for (int i = 0; i < tgd.gateways.size(); ++i) {
			label = new JLabel(String.valueOf(tgd.gateways.get(i).id));
			gatewaysInfoPanel.add(label);
			// System.out.println("TGPanel: gateway id
			// "+tgd.gateways.get(i).id);
		}
		statuPane2.validate();
		gatewaysInfoPanel2.removeAll();
		for (int i = 0; i < tgd.gateways.size(); ++i) {
			label = new JLabel(String.valueOf(tgd.gateways.get(i).nodesAmt));
			gatewaysInfoPanel2.add(label);
			// System.out.println("TGPanel: gateway id
			// "+tgd.gateways.get(i).id);
		}
		statuPane2_1.validate();
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		TGPanel p = new TGPanel();
		// TGData d = new TGData();
		// p.intiTGData(d);
		f.add(p);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(0, 0, 860, 600);
		f.setVisible(true);
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
			// TODO Auto-generated method stub
			try {
				synchronized (receiverEvents) {
					while (true) {
						if (receiverEvents.isEmpty() == true) {
							receiverEvents.wait();
						} else {
							TGParticle tgp = receiverEvents.get(0);
							receiverEvents.remove(0);
							if (tgp.type == TGParticle.GATEWAYLIST_REPAINT) {
								repaintGateway();
								// System.out.println("TGPanel: repaint
								// gateway");
							} else if (tgp.type == TGParticle.VIEWPORT_REPAINT) {
								repaintViewport();
								System.out.println("TGPanel: repaint viewport");
							}
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == viewportPanel) {
			viewportPanel.requestFocus();
			beginDrag();
			System.out.println("viewport get focus");
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == viewportPanel) {
			endDrag();
		}

	}

	double dragBeginX;
	double dragBeginY;
	double dragEndX;
	double dragEndY;

	public void beginDrag() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		dragBeginX = p.getX();
		dragBeginY = p.getY();
	}

	public void endDrag() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		dragEndX = p.getX();
		dragEndY = p.getY();
		viewportPanel.xPos = viewportPanel.xPos + (dragBeginX - dragEndX) / viewportPanel.pixPerReal;
		viewportPanel.yPos = viewportPanel.yPos + (dragBeginY - dragEndY) / viewportPanel.pixPerReal;
		viewportPanel.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void insertNode(double x, double y) {
		TGParticle tgp = new TGParticle(TGParticle.INS_NODE);
		tgp.nodex = x;
		tgp.nodey = y;
		synchronized (tgd.receiverEvents) {
			tgd.receiverEvents.add(tgp);
			tgd.receiverEvents.notify();
		}
	}

	public void deleteNode(double x, double y) {
		tgd.deleteNode(x, y, 1, 1);
	}

	public void deleteGateway(double x, double y) {
		tgd.deleteGateway(x, y, 1, 1);
	}

	public void insertGateway(double x, double y, int amt) {
		TGParticle tgp = new TGParticle(TGParticle.INS_GATEWAY);
		tgp.nodex = x;
		tgp.nodey = y;
		tgp.gatewayAroundDensity = amt;
		synchronized (tgd.receiverEvents) {
			tgd.receiverEvents.add(tgp);
			tgd.receiverEvents.notify();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == viewportPanel) {
			// System.out.println(e.getKeyChar());
			if (e.getKeyChar() == INSERT_NODE) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				double x = (p.getX() - viewportPanel.getLocationOnScreen().getX() + 0.0) / viewportPanel.pixPerReal
						+ viewportPanel.xPos;
				double y = (p.getY() - viewportPanel.getLocationOnScreen().getY() + 0.0) / viewportPanel.pixPerReal
						+ viewportPanel.yPos;
				System.out.println("TGPanel: insert node at" + x + "," + y);
				insertNode(x, y);
			} else if (e.getKeyChar() == DELETE_NODE) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				Pnt pnt = changeToReal(p.getX(), p.getY());
				System.out.println("TGPanel: delete node at" + pnt.xPos + "," + pnt.yPos);
				deleteNode(pnt.xPos, pnt.yPos);
			} else if (e.getKeyChar() == INSERT_GATEWAY) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				double x = (p.getX() - viewportPanel.getLocationOnScreen().getX() + 0.0) / viewportPanel.pixPerReal
						+ viewportPanel.xPos;
				double y = (p.getY() - viewportPanel.getLocationOnScreen().getY() + 0.0) / viewportPanel.pixPerReal
						+ viewportPanel.yPos;
				System.out.println("TGPanel: insert gateway at" + x + "," + y);
				insertGateway(x, y, ardAmt);

			} else if (e.getKeyChar() == DELETE_GATEWAY) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				Pnt pnt = changeToReal(p.getX(), p.getY());
				System.out.println("TGPanel: delete gateway at" + pnt.xPos + "," + pnt.yPos);
				deleteGateway(pnt.xPos, pnt.yPos);
			} else if (e.getKeyChar() == INC_SCALE) {
				viewportPanel.pixPerReal++;
				viewportPanel.repaint();
			} else if (e.getKeyChar() == DEC_SCALE) {
				if (viewportPanel.pixPerReal > 1) {
					viewportPanel.pixPerReal--;
					viewportPanel.repaint();
				}
			}
		}
	}

	Pnt changeToReal(double x, double y) {
		Pnt pnt = new Pnt();
		pnt.xPos = (x - viewportPanel.getLocationOnScreen().getX() + 0.0) / viewportPanel.pixPerReal
				+ viewportPanel.xPos;
		pnt.yPos = (y - viewportPanel.getLocationOnScreen().getY() + 0.0) / viewportPanel.pixPerReal
				+ viewportPanel.yPos;
		return pnt;
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == save2Img) {

		} else if (e.getSource() == scaleText) {
			int i = Integer.parseInt(scaleText.getText());
			viewportPanel.pixPerReal = i;
			System.out.println("TGPanel: set scale " + i);

		} else if (e.getSource() == xsizeText) {
			double d = Double.parseDouble(xsizeText.getText());
			tgd.setxSize(d);
			System.out.println("TGPanel: set xsize " + d);
		} else if (e.getSource() == ysizeText) {
			double d = Double.parseDouble(ysizeText.getText());
			tgd.setySize(d);
			System.out.println("TGPanel: set ysize " + d);
		} else if (e.getSource() == ardAmtText) {
			int d = Integer.parseInt(ardAmtText.getText());
			ardAmt = d;
			System.out.println("TGPanel: set ardAmt " + d);
		} else if (e.getSource() == save2Text) {
			tgd.save2Text();
			System.out.println("TGPanel: save to text");
		}
	}
}
