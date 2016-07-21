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
	JPanel statuPanel3;
	JTextField xsizeText;
	JTextField ysizeText;
	JTextField scaleText;
	JPanel gatewaysInfoPanel;
	JButton save2Text;
	JButton save2Img;
	TGData tgd;

	ArrayList<TGParticle> innerEvents;
	ArrayList<TGParticle> senderEvents;
	ArrayList<TGParticle> receiverEvents;

	public TGPanel() {
		initial();
		initialEventQueue();
		initThread();
	}

	public void initial() {
		this.setLayout(null);
		statuPanel1 = new JPanel();
		statuPanel1.setBounds(551, 0, 100, 550);
		statuPanel1.add(new JLabel("x:"));
		xsizeText = new JTextField(5);
		statuPanel1.add(xsizeText);
		statuPanel1.add(new JLabel("y:"));
		ysizeText = new JTextField(5);
		statuPanel1.add(ysizeText);
		statuPanel1.add(new JLabel("scale:"));
		scaleText = new JTextField(5);
		statuPanel1.add(scaleText);

		statuPane2 = new JScrollPane();
		statuPane2.setBounds(651, 0, 100, 550);
		gatewaysInfoPanel = new JPanel();
		gatewaysInfoPanel.setLayout(new GridLayout(0,1));
		statuPane2.setViewportView(gatewaysInfoPanel);

		statuPanel3 = new JPanel();
		statuPanel3.setBounds(751, 0, 100, 550);
		save2Text = new JButton("to text");
		save2Img = new JButton("to img");
		statuPanel3.add(save2Text);
		statuPanel3.add(save2Img);

		this.add(statuPanel1);
		this.add(statuPane2);
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
	
	public void repaintViewport(){
		viewportPanel.repaint();
	}
	
	public void repaintGateway(){
		JLabel label;
		gatewaysInfoPanel.removeAll();
		for(int i=0;i<tgd.gateways.size();++i){
			label = new JLabel(String.valueOf(tgd.gateways.get(i).id));	
			gatewaysInfoPanel.add(label);
//			System.out.println("TGPanel: gateway id "+tgd.gateways.get(i).id);
		}
		statuPane2.validate();
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		TGPanel p = new TGPanel();
//		TGData d = new TGData();
//		p.intiTGData(d);
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
			// TODO Auto-generated method stub
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
							TGParticle tgp=receiverEvents.get(0);
							receiverEvents.remove(0);
							if(tgp.type==TGParticle.GATEWAYLIST_REPAINT){
								repaintGateway();
//								System.out.println("TGPanel: repaint gateway");
							}else if(tgp.type==TGParticle.VIEWPORT_REPAINT){
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
			System.out.println("viewport get focus");
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == viewportPanel) {

		}

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
		senderEvents.add(tgp);
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
				double x = (p.getX()-viewportPanel.getLocationOnScreen().getX()+0.0)
						/viewportPanel.pixPerReal + viewportPanel.xPos;
				double y = (p.getY()-viewportPanel.getLocationOnScreen().getY()+0.0)
						/viewportPanel.pixPerReal + viewportPanel.yPos;
				System.out.println("TGPanel: insert node at"+x+","+y);
				insertNode(x, y);
			} else if (e.getKeyChar() == DELETE_NODE) {

			} else if (e.getKeyChar() == INSERT_GATEWAY) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				double x = (p.getX()-viewportPanel.getLocationOnScreen().getX()+0.0)/viewportPanel.pixPerReal + viewportPanel.xPos;
				double y = (p.getY()-viewportPanel.getLocationOnScreen().getY()+0.0)/viewportPanel.pixPerReal + viewportPanel.yPos;
				System.out.println("TGPanel: insert gateway at"+x+","+y);
				insertGateway(x, y, TGData.defaultGatewayAroundAmt);

			} else if (e.getKeyChar() == DELETE_GATEWAY) {

			} else if (e.getKeyChar() == INC_SCALE) {
				viewportPanel.pixPerReal++;

			} else if (e.getKeyChar() == DEC_SCALE) {
				if (viewportPanel.pixPerReal > 1)
					viewportPanel.pixPerReal--;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == save2Img) {

		} else if (e.getSource() == save2Text) {

		} else if (e.getSource() == scaleText) {
			int i=Integer.parseInt(scaleText.getText());
			viewportPanel.pixPerReal=i;
			System.out.println("TGPanel: set scale "+i);

		}else if (e.getSource()==xsizeText){
			double d=Double.parseDouble(xsizeText.getText());
			tgd.xsize=d;
			System.out.println("TGPanel: set xsize "+d);
		}else if (e.getSource()==ysizeText){
			double d=Double.parseDouble(ysizeText.getText());
			tgd.ysize=d;
			System.out.println("TGPanel: set ysize "+d);
		}
	}
}
