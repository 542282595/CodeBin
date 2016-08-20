package com.example.worldworaircraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bullet {
	public	Bitmap 	mBullet;					//�ӵ�ͼƬ��Դ
	public	int		bulletX, bulletY;			//�ӵ�������
	public	int 	speed;						//�ӵ����ٶ�
	public	int 	bulletType;					//�ӵ���������
	public	boolean isDead;						//�ӵ��Ƿ����� �Ż�����
	private int 	dirBoss;					//��ǰBoss�ӵ�����
	public Bullet(Bitmap mBullet, int bulletX, int bulletY, int bulletType) {
		this.mBullet 	= 	mBullet;
		this.bulletX 	= 	bulletX;
		this.bulletY 	= 	bulletY;
		this.bulletType =	bulletType;
		switch (bulletType) {
		case Constant.BULLET_PLAYER:
			speed = Constant.BULLET_PLAYER_SPEED;
			break;
		case Constant.BULLET_DUCK:
			speed = Constant.BULLET_DUCK_SPEED;
			break;
		case Constant.BULLET_FLY:
			speed = Constant.BULLET_FLY_SPEED;
			break;
		case Constant.BULLET_BOSS:
			speed = Constant.BULLET_BOSS_SPEED;
			break;
		}
	}
	public Bullet(Bitmap mBullet, int bulletX, int bulletY, int bulletType, int dir) {
		this.mBullet 	= 	mBullet;
		this.bulletX 	= 	bulletX;
		this.bulletY 	= 	bulletY;
		this.bulletType = 	bulletType;
		speed 			=	5;
		this.dirBoss 	= 	dir;
	}
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(mBullet, bulletX, bulletY, paint);
	}
	
	//�ӵ����߼�
	public void logic() {									//��ͬ���ӵ������߼���һ
		switch (bulletType) {								//���ǵ��ӵ���ֱ�����˶�
		case Constant.BULLET_PLAYER:
			bulletY -= speed;
			if (bulletY < -50) {
				isDead = true;
			}
			break;
		case Constant.BULLET_DUCK:							//Ѽ�ӺͲ�Ӭ���ӵ����Ǵ�ֱ�����˶�
		case Constant.BULLET_FLY:
			bulletY += speed;
			if (bulletY > MySurfaceView.screenH) {
				isDead = true;
			}
			break;
		case Constant.BULLET_BOSS:							//Boss���״̬�µ�8�����ӵ��߼�
			switch (dirBoss) {
			case Constant.DIR_UP:							//�����ϵ��ӵ�
				bulletY -= speed;
				break;
			case Constant.DIR_DOWN:							//�����µ��ӵ�
				bulletY += speed;
				break;
			case Constant.DIR_LEFT:							//��������ӵ�
				bulletX -= speed;
				break;
			case Constant.DIR_RIGHT:						//�����ҵ��ӵ�
				bulletX += speed;
				break;
			case Constant.DIR_UP_LEFT:						//�������ϵ��ӵ�
				bulletY -= speed;
				bulletX -= speed;
				break;
			case Constant.DIR_UP_RIGHT:						//�������ϵ��ӵ�
				bulletX += speed;
				bulletY -= speed;
				break;
			case Constant.DIR_DOWN_LEFT:					//�������µ��ӵ�
				bulletX -= speed;
				bulletY += speed;
				break;
			case Constant.DIR_DOWN_RIGHT:					//�������µ��ӵ�
				bulletY += speed;
				bulletX += speed;
				break;
			}
			if (bulletY > MySurfaceView.screenH || bulletY <= -40 ||
					bulletX > MySurfaceView.screenW || bulletX <= -40) {
				isDead = true;								//�߽紦��
			}
			break;
		}
	}
}
