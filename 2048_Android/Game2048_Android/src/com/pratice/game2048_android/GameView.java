package com.pratice.game2048_android;

//import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
//import android.R.bool;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {
	// ����һ����¼��Ƭ�Ķ�ά����
	private Card[][] cardMap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();
	public GameView(Context context) {
		super(context);
		
		initGameView();
	}
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}
	
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}

	public void initGameView() {
		setColumnCount(4);// ָ��������
		setBackgroundColor(0xffbbada0);// ���ñ���
		setOnTouchListener(new View.OnTouchListener() {
			// ֻ��Ҫ��ָ���º��뿪��λ��
			private double startX, startY, offsetX, offsetY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;
					// �ж��û���ͼ
					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						// ����ʵ��ݴ�ֵ
						if (offsetX < -5) {
							swipeLeft();
						} else if (offsetX > 5) {
							swipeRight();
						}
					} else {
						if (offsetY < -5) {
							swipeUp();
						} else if (offsetY > 5) {
							swipeDown();
							//System.out.println("down");// �������, �� LogCat ���ڼ���
						}
					}
					break;
				default:
					break;
				}
				return true;//������true
			}
		});
	}
	
	// ֻ�ڴ�����ʱ��ִ��һ�Σ���Ϊָ�����ֻ�ֻ��ֱ������
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		int cardWidth = (Math.min(w, h)-10)/4;// ע������϶
		
		addCard(cardWidth, cardWidth);
		startGame();
	}

	// ��ӿ�Ƭ
	public void addCard(int cardWidth, int cardHeight) {
		Card cdCard;
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				cdCard = new Card(getContext());
				cdCard.setNumber(0);
				addView(cdCard, cardWidth, cardHeight);
				cardMap[x][y] = cdCard;
			}
		}
	}
	private void addRandomNumber() {
		emptyPoints.clear();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				// �յ�
				if (0 >= cardMap[x][y].getNumber()) {
					emptyPoints.add(new Point(x, y));
				}
			}
		}
		// ���ѡһ����λ��
		Point point = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
		cardMap[point.x][point.y].setNumber(Math.random()>0.1?2:4);// ����������������
		
	}
	
	// ������Ϸ
	private void startGame() {
		MainActivity.getMainActivity().clearScore();
		// ����
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				cardMap[x][y].setNumber(0);
			}
		}
		addRandomNumber();
		addRandomNumber();

	}
	private void swipeLeft() {
		boolean isMoved = false;
		// ʵ����Ϸ����Ҫ�߼�
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				// ���м��
				for (int k = y+1; k < 4; k++) {
					if (cardMap[x][k].getNumber() > 0) {
						// ������ǿո�
						if (cardMap[x][y].getNumber() <= 0) {
							// ���ǰһλ���ǿո�,��Ѹ�λ�ÿ�Ƭ�ƶ���ȥ
							cardMap[x][y].setNumber(cardMap[x][k].getNumber());
							cardMap[x][k].setNumber(0);
							y--;
							isMoved = true;
							//break;//�˻�һ�����¼��
						} else if (cardMap[x][y].getNumber() == cardMap[x][k].getNumber()) {
							// ������ͬ��ϲ�
							cardMap[x][y].setNumber(2*cardMap[x][y].getNumber());
							cardMap[x][k].setNumber(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNumber());
							isMoved = true;
							//break;
						}
						break;
					}
				}
			}
		}
		if (isMoved) {
			addRandomNumber();
			checkGame();
		}
	}

	private void swipeRight() {
		boolean isMoved = false;
		// ʵ����Ϸ����Ҫ�߼�
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >= 0; y--) {
				// ���м��
				for (int k = y-1; k >= 0; k--) {
					if (cardMap[x][k].getNumber() > 0) {
						// ������ǿո�
						if (cardMap[x][y].getNumber() <= 0) {
							// ���ǰһλ���ǿո�,��Ѹ�λ�ÿ�Ƭ�ƶ���ȥ
							cardMap[x][y].setNumber(cardMap[x][k].getNumber());
							cardMap[x][k].setNumber(0);
							y++;
							isMoved = true;
							//break;//�˻�һ�����¼��
						} else if (cardMap[x][y].getNumber() == cardMap[x][k].getNumber()) {
							// ������ͬ��ϲ�
							cardMap[x][y].setNumber(2*cardMap[x][y].getNumber());
							cardMap[x][k].setNumber(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNumber());
							isMoved = true;
							//break;
						}
						// ע��break��λ�ã��������ע�͵���λ�û���� 2424-->48��bug
						break;
					}
				}
			}
		}
		if (isMoved) {
			addRandomNumber();
			checkGame();
		}
	}
	private void swipeUp() {
		boolean isMoved = false;
		// ʵ����Ϸ����Ҫ�߼�
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				// ���м��
				for (int k = x+1; k < 4; k++) {
					if (cardMap[k][y].getNumber() > 0) {
						// ������ǿո�
						if (cardMap[x][y].getNumber() <= 0) {
							// ���ǰһλ���ǿո�,��Ѹ�λ�ÿ�Ƭ�ƶ���ȥ
							cardMap[x][y].setNumber(cardMap[k][y].getNumber());
							cardMap[k][y].setNumber(0);
							x--;
							isMoved = true;
							//break;//�˻�һ�����¼��
						} else if (cardMap[x][y].getNumber() == cardMap[k][y].getNumber()) {
							// ������ͬ��ϲ�
							cardMap[x][y].setNumber(2*cardMap[x][y].getNumber());
							cardMap[k][y].setNumber(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNumber());
							isMoved = true;
							//break;
						}
						break;
					}
				}
			}
		}	
		if (isMoved) {
			addRandomNumber();
			checkGame();
		}
	}
	private void swipeDown() {
		boolean isMoved = false;
		// ʵ����Ϸ����Ҫ�߼�
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >= 0; x--) {
				// ���м��
				for (int k = x-1; k >= 0; k--) {
					if (cardMap[k][y].getNumber() > 0) {
						// ������ǿո�
						if (cardMap[x][y].getNumber() <= 0) {
							// ���ǰһλ���ǿո�,��Ѹ�λ�ÿ�Ƭ�ƶ���ȥ
							cardMap[x][y].setNumber(cardMap[k][y].getNumber());
							cardMap[k][y].setNumber(0);
							x++;
							isMoved = true;
						} else if (cardMap[x][y].getNumber() == cardMap[k][y].getNumber()) {
							// ������ͬ��ϲ�
							cardMap[x][y].setNumber(2*cardMap[x][y].getNumber());
							cardMap[k][y].setNumber(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNumber());
							isMoved = true;
						}
						break;
					}
				}
			}
		}
		if (isMoved) {
			addRandomNumber();
			checkGame();
		}
	}
	public void checkGame() {
		boolean isOver = true;
		ALL:
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				// ����
				if (0 == cardMap[x][y].getNumber() ||
						(x>0 && cardMap[x-1][y].getNumber()==cardMap[x][y].getNumber()) ||
						(x<3 && cardMap[x+1][y].getNumber()==cardMap[x][y].getNumber()) || 
						(y>0 && cardMap[x][y-1].getNumber()==cardMap[x][y].getNumber()) ||
						(y<3 && cardMap[x][y+1].getNumber()==cardMap[x][y].getNumber())) {
						isOver = false;
						break ALL;
				}
			}
		}
		if (isOver) {
			new AlertDialog.Builder(getContext()).setTitle("Attention").setMessage("Game is over").setPositiveButton("New Game", 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startGame();
						}
					}).show();
		}
	}
}
