package topogenerator;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TGController {

	ArrayList<TGParticle> particles = new ArrayList<>();
	ReentrantReadWriteLock ptLock = new ReentrantReadWriteLock();

	public TGController() {
		// TODO Auto-generated constructor stub
	}

	public void addParticle(TGParticle pr) {
		ptLock.writeLock().lock();
		particles.add(pr);
		mergeParticles();
		ptLock.writeLock().unlock();
	}

	// 该方法只能由addParticle调用，否则线程不安全。
	private void mergeParticles() {
		for (int i = 0; i < particles.size(); ++i) {
		}
	}

	public void forwardParticle() {
		ptLock.writeLock().lock();
		if (particles.isEmpty() == false) {
			TGParticle tgp = particles.get(0);
			particles.remove(0);
		}
		ptLock.writeLock().unlock();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	Condition conInnerR1 = ptLock.readLock().newCondition();
	Condition conInnerW1 = ptLock.writeLock().newCondition();
	boolean innerThreadEndFlag = false;// 此变量应该加锁

	class InnerThread extends Thread {

		@Override
		public void run() {
			ptLock.writeLock().lock();
			while (innerThreadEndFlag == false) {

			}
			ptLock.writeLock().unlock();
		}
	}

	class SenderThread extends Thread {

		@Override
		public void run() {
		}
	}

	Condition conReceiverR1 = ptLock.readLock().newCondition();
	Condition conReceiverW1 = ptLock.writeLock().newCondition();
	boolean receiverThreadEndFlag = false;// 此变量应该加锁

	class ReceiverThread extends Thread {

		@Override
		public void run() {
			try {
				while (receiverThreadEndFlag == false) {
					if (particles.isEmpty() == true) {
						conReceiverR1.await();
					} else {
						forwardParticle();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
