package com.pratice.game2048_android;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
	private int number = 0;
	private TextView label;
	
	public Card(Context context) {
		super(context);
		label = new TextView(getContext());
		label.setBackgroundColor(0x33ffffff);
		label.setTextSize(32);
		label.setGravity(Gravity.CENTER);// ��������λ��
		LayoutParams lp = new LayoutParams(-1, -1);// ��֪ʶ��
		lp.setMargins(10, 10, 0, 0);// ���ÿ�Ƭ���
		addView(label, lp);
		setNumber(0);
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
		if (number <= 0) {
			label.setText("");
		} else {
			label.setText(number+"");// ע��Ҫת��Ϊ�ַ���
		}
        try {
            Thread.sleep(10);//����һ�㶯��Ч��
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		setBgColor(number);
	}
	
	// �ж�����Ƭ�Ƿ����
	public boolean isCardEqual(Card cd) {
		return getNumber()==cd.getNumber();
	}
	// �������ֶ�Ӧ��ɫ
	public void setBgColor(int number) {
		switch (number) {
		case 2:
			label.setBackgroundColor(Color.argb(125, 255, 222, 173));
			break;
		case 4:
			label.setBackgroundColor(Color.argb(125, 244, 164, 96));
			break;
		case 8:
			label.setBackgroundColor(Color.argb(125, 255, 165, 0));
			break;
		case 16:
			label.setBackgroundColor(Color.argb(125, 255, 182, 193));
			break;
		case 32:
			label.setBackgroundColor(Color.argb(125, 255, 127, 80));
			break;
		case 64:
			label.setBackgroundColor(Color.argb(125, 50, 205, 50));
			break;
		case 128:
			label.setBackgroundColor(Color.argb(125, 0, 191, 255));
			break;
		case 256:
			label.setBackgroundColor(Color.argb(125, 100, 149, 237));
			break;
		case 512:
			label.setBackgroundColor(Color.argb(125, 255, 0, 255));
			break;
		case 1024:
			label.setBackgroundColor(Color.argb(125, 220, 20, 60));
			break;
		case 2048:
			label.setBackgroundColor(Color.argb(125, 255, 0, 0));
			break;
		case 4096:
			label.setBackgroundColor(Color.argb(125, 0, 128, 0));
			break;
		default:
			label.setBackgroundColor(0x33ffffff);
			break;
		}
		
	}

}
