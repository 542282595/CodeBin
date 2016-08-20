package com.example.worldworaircraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {
	public 	int 	type;						//�л��������ʶ
	public 	Bitmap 	mEnemy;						//�л�ͼƬ��Դ
	public 	int 	x, y;						//�л�����
	public 	int 	frameW, frameH;				//�л�ÿ֡�Ŀ��
	private int 	frameIndex;					//�л���ǰ֡�±�
	private int 	speed;						//�л����ƶ��ٶ�
	public 	boolean isDead;						//�жϵл��Ƿ��Ѿ�����
	//�л��Ĺ��캯��
	public Enemy(Bitmap mEnemy, int enemyType, int x, int y) {
		this.mEnemy	= 	mEnemy;
		frameW 		= 	mEnemy.getWidth() / 10;
		frameH 		=	mEnemy.getHeight();
		this.type 	= 	enemyType;
		this.x 		= 	x;
		this.y 		= 	y;
		switch (type) {
		case Constant.ENEMY_TYPE_FLY:				//��Ӭ
			speed = Constant.ENEMY_FLY_SPEED;
			break;
		case Constant.ENEMY_TYPE_DUCKL:				//Ѽ��
			speed = Constant.ENEMY_DUCK_SPEED;
			break;
		case Constant.ENEMY_TYPE_DUCKR:									
			speed = Constant.ENEMY_DUCK_SPEED;
			break;
		}
		isDead =false;
	}

	//�л���ͼ����
	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		canvas.clipRect(x, y, x + frameW, y + frameH);
		canvas.drawBitmap(mEnemy, x - frameIndex * frameW, y, paint);
		canvas.restore();
	}

	//�л��߼�AI
	public void logic() {
		frameIndex++;								//����ѭ������֡�γɶ���
		if (frameIndex >= 10) {
			frameIndex = 0;
		}
		//��ͬ����ĵл�ӵ�в�ͬ��AI�߼�
		switch (type) {
		case Constant.ENEMY_TYPE_FLY:
			if (isDead == false) {
				//���ٳ��֣����ٷ���
				speed -= 1;
				y += speed;
				if (y <= -200) {
					isDead = true;
				}
			}
			break;
		case Constant.ENEMY_TYPE_DUCKL:
			if (isDead == false) {
				//б���½��˶�
				x += speed / 2;
				y += speed;
				if (x > MySurfaceView.screenW) {
					isDead = true;
				}
			}
			break;
		case Constant.ENEMY_TYPE_DUCKR:
			if (isDead == false) {
				//б���½��˶�
				x -= speed / 2;
				y += speed;
				if (x < -50) {
					isDead = true;
				}
			}
			break;
		}
	}

	//�ж���ײ(�л��������ӵ���ײ)
	public boolean isCollsionWith(Bullet bullet) {
		int x2 = bullet.bulletX;
		int y2 = bullet.bulletY;
		int w2 = bullet.mBullet.getWidth();
		int h2 = bullet.mBullet.getHeight();
		if (x >= x2 && x >= x2 + w2) {
			return false;
		} else if (x <= x2 && x + frameW <= x2) {
			return false;
		} else if (y >= y2 && y >= y2 + h2) {
			return false;
		} else if (y <= y2 && y + frameH <= y2) {
			return false;
		}
		//������ײ����������
		isDead = true;
		return true;
	}
}
