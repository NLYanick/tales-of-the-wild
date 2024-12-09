package View;

public class PlayerView extends EntityView {
	
	private final static int IMAGESIZE = 96;
	
	public PlayerView(String imgURL)
	{
		super(imgURL);
		setMaxSize(IMAGESIZE, IMAGESIZE);
	}
	
}
