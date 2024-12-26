package View;

import Model.Direction;

public class NPCView extends EntityView {

	public NPCView(String imageURL, int startX, int startY, Direction walkDirection) {
		super(imageURL);
		
		move(startX, startY);
		
		setUpThread(walkDirection);
	}
	

	public NPCView(String imageURL, int startX, int startY) {
		super(imageURL);
		move(startX, startY);
	}
	
	private void setUpThread(Direction dir) {
		Thread walkingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				boolean running = true;
				while(running) {
					
				}
			}
			
		});
		walkingThread.start();
	}

}
