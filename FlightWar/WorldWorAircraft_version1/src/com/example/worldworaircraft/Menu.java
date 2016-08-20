package com.example.worldworaircraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Menu {
	//�˵�����ͼ
	private Bitmap 	mMenuBg;
	//��ťͼƬ��Դ(���º�δ����ͼ)
	private Bitmap 	mButton, mButtonPress;
	//��ť������
	private int 	btnX, btnY;
	//��ť�Ƿ��±�ʶλ
	private Boolean isPress;
	Menu(Bitmap mMenuBg, Bitmap mButton, Bitmap mButtonPress) {
		this.mMenuBg 		= 	mMenuBg;
		this.mButton 		= 	mButton;
		this.mButtonPress 	=	mButtonPress;
		btnX 				= 	MySurfaceView.screenW/2 - mButton.getWidth()/2;
		btnY 				=	MySurfaceView.screenH   - mButton.getHeight();
		isPress 			= 	false;
	}
	public void draw(Canvas canvas, Paint paint) {
		//���Ʋ˵�����ͼ
		canvas.drawBitmap(mMenuBg, 0, 0, null);
		//����δ���°�ťͼ
		if (isPress) {//�����Ƿ��»��Ʋ�ͬ״̬�İ�ťͼ
			canvas.drawBitmap(mButtonPress, btnX, btnY, paint);
		} else {
			canvas.drawBitmap(mButton, btnX, btnY, paint);
		}
	}
	public void onTouchEvent(MotionEvent event) {
		//��ȡ�û���ǰ����λ��
		int pointX = (int) event.getX();
		int pointY = (int) event.getY();
//		Log.d("TAG", "btnX"+btnX+"btnY"+btnY);
//		Log.d("TAG", "pointX"+pointX+"pointY"+pointY);
//		Log.d("TAG", "W"+mButton.getWidth()+"H"+mButton.getHeight());
		//���û��ǰ��¶������ƶ�����
		if (event.getAction() == MotionEvent.ACTION_DOWN ||
				event.getAction() == MotionEvent.ACTION_MOVE) {		//�ж��û��Ƿ����˰�ť
			//Log.d("DOWN", "DOWN");
			if (pointX > btnX && pointX < btnX + mButton.getWidth()) {
				if (pointY > btnY && pointY < btnY + mButton.getHeight()) {
					isPress = true;
				} else {
					isPress = false;
				}
			} else {
				isPress = false;
			}
			
		} else if (event.getAction() == MotionEvent.ACTION_UP) {	//̧���ж��Ƿ�����ť����ֹ�û��ƶ�����
			//Log.d("UP", "UP");//���û���̧����
			if (pointX > btnX && pointX < btnX + mButton.getWidth()) {
				if (pointY > btnY && pointY < btnY + mButton.getHeight()) {
					isPress = false;								//��ԭButton״̬Ϊδ����״̬
					Log.d("RUN", "RUN");
					MySurfaceView.gameState = Constant.GAME_RUN;	//�ı䵱ǰ��Ϸ״̬Ϊ��ʼ��Ϸ����Ϸ���
				}
			}
		}
	}
}
