package com.example.worldworaircraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Player {
	//���ǵ�Ѫ����Ѫ��λͼ
	private int 	playerBlood = 3;							//Ĭ��3Ѫ
	private Bitmap 	mPlayerBlood;
	public int 		x, y;										//���ǵ������Լ�λͼ
	private Bitmap 	mPlayer;

	//��ײ�����޵�ʱ��
	private int 	noCollisionCount = 0;						//��ʱ��
	private int 	noCollisionTime = Constant.NO_DIE_TIME;
	//�Ƿ���ײ�ı�ʶλ
	private boolean isCollision;

	//���ǵĹ��캯��
	public Player(Bitmap mPlayer, Bitmap mPlayerBlood) {
		this.mPlayer 		= 	mPlayer;
		this.mPlayerBlood 	= 	mPlayerBlood;
		x 					= 	MySurfaceView.screenW / 2 - mPlayer.getWidth() / 2;
		y 					= 	MySurfaceView.screenH - mPlayer.getHeight();	//�ײ�
	}

	//���ǵĻ�ͼ����
	public void draw(Canvas canvas, Paint paint) {
		//��������
		//�������޵�ʱ��ʱ����������˸
		if (isCollision) {
			if (noCollisionCount % 2 == 0) {				//ÿ2����Ϸѭ��������һ������
				canvas.drawBitmap(mPlayer, x, y, paint);	//��˸Ч��
			}
		} else {
			canvas.drawBitmap(mPlayer, x, y, paint);
		}
		//��������Ѫ��
		for (int i = 0; i < playerBlood; i++) {
			canvas.drawBitmap(mPlayerBlood, i * mPlayerBlood.getWidth(), 
					MySurfaceView.screenH - mPlayerBlood.getHeight(), paint);
		}
	}



	//���ǵ��߼�
	public void logic() {
		//�ж���ĻX�߽�
		if (x + mPlayer.getWidth() >= MySurfaceView.screenW) {
			x = MySurfaceView.screenW - mPlayer.getWidth();
		} else if (x <= 0) {
			x = 0;
		}
		//�ж���ĻY�߽�
		if (y + mPlayer.getHeight() >= MySurfaceView.screenH) {
			y = MySurfaceView.screenH - mPlayer.getHeight();
		} else if (y <= 0) {
			y = 0;
		}
		//�����޵�״̬
		if (isCollision) {
			noCollisionCount++;					//��ʱ����ʼ��ʱ
			if (noCollisionCount >= noCollisionTime) {
				isCollision = false;			//�޵�ʱ����󣬽Ӵ��޵�״̬����ʼ��������
				noCollisionCount = 0;
			}
		}
	}
	
	public void onTouchEvent(MotionEvent event) {
		int touchX	= (int) event.getX();
		int touchY	= (int) event.getY();
		//����̫Զ�ƶ���Ч
		if (Math.abs(touchX-x) > MySurfaceView.screenW/8 ||
				Math.abs(touchY-y) > MySurfaceView.screenH/8) {
			return;
		}
			
		if(event.getAction() == MotionEvent.ACTION_MOVE) {
			x = (int) event.getX();
			y = (int) event.getY();
		}

	}
	
	//��������Ѫ��
	public void setPlayerBlood(int Blood) {
		this.playerBlood = Blood;
	}

	//��ȡ����Ѫ��
	public int getPlayerBlood() {
		return playerBlood;
	}

	//�ж���ײ(������л�)
	public boolean isCollsionWith(Enemy en) {
		if (isCollision == false) {			//�Ƿ����޵�ʱ��
			int x2 = en.x;
			int y2 = en.y;
			int w2 = en.frameW;
			int h2 = en.frameH;
			if (x >= x2 && x >= x2 + w2) {
				return false;
			} else if (x <= x2 && x + mPlayer.getWidth() <= x2) {
				return false;
			} else if (y >= y2 && y >= y2 + h2) {
				return false;
			} else if (y <= y2 && y + mPlayer.getHeight() <= y2) {
				return false;
			}
			isCollision = true;				//��ײ�������޵�״̬
			return true;
		} else {							//�����޵�״̬��������ײ
			return false;
		}
	}
	//�ж���ײ(������л��ӵ�)
	public boolean isCollsionWith(Bullet bullet) {
		if (isCollision == false) {							//�Ƿ����޵�ʱ��
			int x2 = bullet.bulletX;
			int y2 = bullet.bulletY;
			int w2 = bullet.mBullet.getWidth();
			int h2 = bullet.mBullet.getHeight();
			if (x >= x2 && x >= x2 + w2) {
				return false;
			} else if (x <= x2 && x + mPlayer.getWidth() <= x2) {
				return false;
			} else if (y >= y2 && y >= y2 + h2) {
				return false;
			} else if (y <= y2 && y + mPlayer.getHeight() <= y2) {
				return false;
			}
			isCollision = true;								//��ײ�������޵�״̬
			return true;
		} else {											//�����޵�״̬��������ײ
			return false;
		}
	}
}
