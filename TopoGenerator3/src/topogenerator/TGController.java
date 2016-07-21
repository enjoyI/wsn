package topogenerator;

import java.util.ArrayList;

public class TGController {

	TGPanel tgPanel;
	TGData tgData;

	ArrayList<TGParticle> toDataEvents;
	ArrayList<TGParticle> toViewEvents;

	public TGController() {
		// TODO Auto-generated constructor stub
	}
	
	public void TGCInit(){
		tgPanel = new TGPanel();
		tgData = new TGData();
		toDataEvents = new ArrayList<>();
		toViewEvents = new ArrayList<>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	class DataThread extends Thread {

		@Override
		public void run() {
			try {
				synchronized (toDataEvents) {
					while (true) {
						if (toDataEvents.isEmpty() == true) {
							toDataEvents.wait();
						}
						else{
							
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class ViewThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				synchronized (toViewEvents) {
					while (true) {
						if (toViewEvents.isEmpty() == true) {
							toViewEvents.wait();
						}
						else{
							
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
