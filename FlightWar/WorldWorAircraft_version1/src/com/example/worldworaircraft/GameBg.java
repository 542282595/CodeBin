package com.example.worldworaircraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameBg {
	//��Ϸ������ͼƬ��Դ
	//Ϊ��ѭ�����ţ����ﶨ������λͼ����
	//����Դ���õ���ͬһ��ͼƬ
	private Bitmap 	mBackground_1;
	private Bitmap 	mBackground_2;
	//��Ϸ��������
	private int 	bg1x, bg1y, bg2x, bg2y;
	//���������ٶ�
	private int speed = Constant.BG_SPEED;

	//��Ϸ�������캯��
	public GameBg(Bitmap mBackground) {
		this.mBackground_1 = mBackground;
		this.mBackground_2 = mBackground;
		//�����õ�һ�ű����ײ���������������Ļ
		bg1y = -Math.abs(mBackground_1.getHeight() - MySurfaceView.screenH);
		//�ڶ��ű���ͼ�����ڵ�һ�ű������Ϸ�
		//+101��ԭ����Ȼ���ű���ͼ�޷�϶���ӵ�����ΪͼƬ��Դͷβ
		//ֱ�����Ӳ���г��Ϊ�����Ӿ�������������ͼ���Ӷ�������λ��
		bg2y = bg1y - mBackground_1.getHeight() + 111;
	}
	
	//��Ϸ�����Ļ�ͼ����
	public void draw(Canvas canvas, Paint paint) {
		//�������ű���
		canvas.drawBitmap(mBackground_1, bg1x, bg1y, paint);
		canvas.drawBitmap(mBackground_2, bg2x, bg2y, paint);
	}
	
	//��Ϸ�������߼�����
	public void logic() {
		bg1y += speed;
		bg2y += speed;
		//����һ��ͼƬ��Y���곬����Ļ��
		//���������������õ��ڶ���ͼ���Ϸ�
		if (bg1y > MySurfaceView.screenH) {
			bg1y = bg2y - mBackground_1.getHeight() + Constant.MATCH_DIS;
		}
		//���ڶ���ͼƬ��Y���곬����Ļ��
		//���������������õ���һ��ͼ���Ϸ�
		if (bg2y > MySurfaceView.screenH) {
			bg2y = bg1y - mBackground_1.getHeight() + Constant.MATCH_DIS;
		}
	}
}
