package com.zhaoyu1995;
import java.io.IOException;

public class Stick extends BallComponent {
	//���嵲���ƶ��ٶ�
	public static final int SPEED = 20;
	//�����ʼ����
	private int preWidth = 0;
	
	public Stick (int panelWidth, int panelHeight, String path) throws IOException {
		//���ø�������
		super(panelWidth, panelHeight, path);
		//����y����
		this.setY(panelHeight-super.getImage().getHeight(null));
		//����ԭ������
		this.preWidth = super.getImage().getWidth(null);
	}
	
	public int getPreWidth() {
		return this.preWidth;
	}
	public void setPreWidth(int preWidth) {
		this.preWidth = preWidth;
	}
}
