package topogenerator;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ViewportPanel extends JPanel {

	TGData tgData;
	int pixPerReal=1;
	double xPos;
	double yPos;
	private static int gatewayRadius=5;
	private static int nodeRadius=3;
	
	public ViewportPanel(TGData tgd){
		super();
		this.setBackground(Color.white);
		tgData = tgd;
		xPos = 0;
		yPos = 0;
	}
	
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        // Flood the drawing area with a "background" color
        Color temp = g.getColor();
        
        g.setColor(Color.green);
        for(int i=0;i<tgData.nodes.size();++i){
        	if(pntInViewport(tgData.nodes.get(i))==1){
        		g.fillOval((int)(tgData.nodes.get(i).xPos-nodeRadius)*pixPerReal,
        				(int)(tgData.nodes.get(i).yPos-nodeRadius)*pixPerReal,
        				nodeRadius, nodeRadius);
        	}
        }
        
        g.setColor(Color.red);
        for(int i=0;i<tgData.gateways.size();++i){
        	if(pntInViewport(tgData.gateways.get(i))==1){
        		g.fillOval((int)(tgData.gateways.get(i).xPos-gatewayRadius)*pixPerReal,
        				(int)(tgData.gateways.get(i).yPos-gatewayRadius)*pixPerReal,
        				gatewayRadius, gatewayRadius);
        	}
        }
        
        g.setColor(temp);
    }
	
    public int pntInViewport(Pnt pnt){
    	if(((pnt.xPos-xPos)*pixPerReal>=0)
    		&&((pnt.xPos-xPos)*pixPerReal<=this.getWidth())
    		&&((pnt.yPos-yPos)*pixPerReal<=this.getHeight())
    		&&((pnt.yPos-yPos)*pixPerReal>=0)   		
    			){
    		return 1;
    	}
    	else{
    		return 0;
    	}
    }
    
    public void setPos(double x,double y){
    	xPos = x;
    	yPos = y;
    }
    
    public void setPixPerReal(int x){
    	if(x<=0) return;
    	pixPerReal = x;
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
