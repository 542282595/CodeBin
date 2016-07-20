package com.zhaoyu1995;
//import java.awt.Image;
import java.io.IOException;
//ש����
public class Brick extends BallComponent {
	// �������
	private Magic magic = null;
	// ����һ��boolean�������ñ����Ƿ���Ч
	private boolean disable = false;
	public static final int MAGIC_LONG_TYPE = 1;
	public static final int MAGIC_SHORT_TYPE = 2;
	
	//������
	public Brick(String path, int type, int x, int y) throws IOException{
		super(path);
		if (Brick.MAGIC_LONG_TYPE == type) {
			this.magic = new ShortMagic("images/long.gif", x, y);
		} else if (Brick.MAGIC_SHORT_TYPE == type) {
			this.magic = new ShortMagic("images/short.gif", x, y);
		} 
		if (null != this.magic) {
			this.magic.setX(x);
			this.magic.setY(y);
		}
	}
	
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public boolean isDisable() {
		return this.disable;
	}
	public Magic getMagic() {
		return this.magic;
	}
	public void setMagic(Magic magic) {
		this.magic = magic;
	}	
}
