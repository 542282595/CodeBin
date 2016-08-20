package com.example.worldworaircraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Boss {
	public	int		blood 	=	Constant.BOSS_BLOOD;		//Boss��Ѫ��
	private	Bitmap 	mBoss;									//Boss��ͼƬ��Դ
	public	int 	x, y;									//Boss����
	public	int 	frameW, frameH;							//Bossÿ֡�Ŀ��
	private int 	frameIndex;								//Boss��ǰ֡�±�
	private int 	speed	= Constant.ENEMY_BOSS_SPEED;	//Boss�˶����ٶ�
	//Boss���˶��켣
	//һ��ʱ���������Ļ�·��˶������ҷ����Χ�ӵ������Ƿ��̬��
	//����״̬�� ���ӵ���ֱ�����˶�
	private boolean isCrazy;
	private int		crazyTime = 200;						//������״̬��״̬ʱ����
	private int		count;									//������

	//Boss�Ĺ��캯��
	public Boss(Bitmap mBoss) {
		this.mBoss	=	mBoss;
		frameW 		=	mBoss.getWidth() / 10;
		frameH 		=	mBoss.getHeight();
		x			=	MySurfaceView.screenW / 2 - frameW / 2;
		y			= 	0;
	}

	//Boss�Ļ���
	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		canvas.clipRect(x, y, x + frameW, y + frameH);
		canvas.drawBitmap(mBoss, x - frameIndex * frameW, y, paint);
		canvas.restore();
	}

	//Boss���߼�
	public void logic() {
		//����ѭ������֡�γɶ���
		frameIndex++;
		if (frameIndex >= 10) {
			frameIndex = 0;
		}
		//û�з���״̬
		if (isCrazy == false) {
			x += speed;
			if (x + frameW >= MySurfaceView.screenW) {
				speed = -speed;
			} else if (x <= 0) {
				speed = -speed;
			}
			count++;
			if (count % crazyTime == 0) {
				isCrazy = true;
				speed = 24;
			}
		} else {							//����״̬
			speed -= 1;
			if (speed == 0) {				//��Boss����ʱ���������ӵ�
				//���8�����ӵ�
				MySurfaceView.bossBullet.add(new Bullet(MySurfaceView.mBossBullet,
								x+40, y+10, Constant.BULLET_BOSS, Constant.DIR_UP));
				MySurfaceView.bossBullet.add(new Bullet(MySurfaceView.mBossBullet, 
								x+40, y+10, Constant.BULLET_BOSS, Constant.DIR_DOWN));
				MySurfaceView.bossBullet.add(new Bullet(MySurfaceView.mBossBullet, 
								x+40, y+10, Constant.BULLET_BOSS, Constant.DIR_LEFT));
				MySurfaceView.bossBullet.add(new Bullet(MySurfaceView.mBossBullet, 
								x+40, y+10, Constant.BULLET_BOSS, Constant.DIR_RIGHT));
				MySurfaceView.bossBullet.add(new Bullet(MySurfaceView.mBossBullet, 
								x+40, y+10, Constant.BULLET_BOSS, Constant.DIR_UP_LEFT));
				MySurfaceView.bossBullet.add(new Bullet(MySurfaceView.mBossBullet, 
								x+40, y+10, Constant.BULLET_BOSS, Constant.DIR_UP_RIGHT));
				MySurfaceView.bossBullet.add(new Bullet(MySurfaceView.mBossBullet, 
								x+40, y+10, Constant.BULLET_BOSS, Constant.DIR_DOWN_LEFT));
				MySurfaceView.bossBullet.add(new Bullet(MySurfaceView.mBossBullet, 
								x+40, y+10, Constant.BULLET_BOSS, Constant.DIR_DOWN_RIGHT));
			}
			y += speed;
			if (y <= 0) {
				isCrazy = false;					//�ָ�����״̬
				speed = Constant.ENEMY_BOSS_SPEED;
			}
		}
	}

	//�ж���ײ(Boss�������ӵ�����)
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
		return true;
	}

	//����BossѪ��
	public void setBlood(int blood) {
		this.blood = blood;
	}
}
