package com.zhaoyu1995;
//import java.awt.Image;
//import java.io.File;
//import javax.imageio.ImageIO;
import java.io.IOException;

public class Ball extends BallComponent {
	// ������������ٶ�
	private int speedY = 10;
	// ���嵯��ĺ����ٶ�
	private int speedX = 8;
	// �����Ƿ����˶�
	private boolean started = false;
	// �����Ƿ�����˶�
	private boolean stop = false;
	
	
	public Ball(int panelWidth, int panelHeight, int offset, String path) throws IOException {
		// ���ø�������
		super(panelWidth, panelHeight, path);
		// ����y����
		this.setY(panelHeight - super.getImage().getHeight(null) - offset);
	}
	//���ú����ٶ�
	public void setSpeedX(int speed) {
		this.speedX = speed;
	}
	//���������ٶ�
	public void setSpeedY(int speed) {
		this.speedY = speed;
	}
	//�����Ƿ��˶�
	public void setStarted(boolean b) {
		this.started = b;
	}
	//�Ƿ�����˶�
	public void setStop(boolean b) {
		this.stop = b;
	}
	//���غ����ٶ�
	public int getSpeedX() {
		return this.speedX;
	}
	//���������ٶ�
	public int getSpeedY() {
		return this.speedY;
	}	
	//�Ƿ����˶�
	public boolean isStarted() {
		return this.started;
	}
	//�Ƿ��Ѿ������˶�
	public boolean isStop() {
		return this.stop;
	}	
}