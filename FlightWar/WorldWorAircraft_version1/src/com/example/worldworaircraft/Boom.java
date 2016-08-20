package com.example.worldworaircraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Boom {
	private	Bitmap	mBoom;								//��ըЧ����Դͼ
	private int 	boomX, boomY;						//��ըЧ����λ������
	private int 	cureentFrameIndex;					//��ը�������ŵ�ǰ��֡�±�
	private int	 	totleFrame;							//��ըЧ������֡��
	private int 	frameW, frameH;						//ÿ֡�Ŀ��
	public 	boolean playEnd;							//�Ƿ񲥷���ϣ��Ż�����

	//��ըЧ���Ĺ��캯��
	public Boom(Bitmap mBoom, int x, int y, int totleFrame) {
		this.mBoom		=	mBoom;
		this.boomX		=	x;
		this.boomY 		=	y;
		this.totleFrame =	totleFrame;
		frameW 			=	mBoom.getWidth() / totleFrame;
		frameH 			= 	mBoom.getHeight();
	}

	//��ըЧ������
	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		canvas.clipRect(boomX, boomY, boomX + frameW, boomY + frameH);
		canvas.drawBitmap(mBoom, boomX - cureentFrameIndex * frameW, boomY, paint);
		canvas.restore();
	}

	//��ըЧ�����߼�
	public void logic() {
		if (cureentFrameIndex < totleFrame) {
			cureentFrameIndex++;
		} else {
			playEnd = true;
		}
	}
}
