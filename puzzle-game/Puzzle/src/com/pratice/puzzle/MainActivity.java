package com.pratice.puzzle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;


public class MainActivity extends Activity {
	//�ж���Ϸ�Ƿ�ʼ
	private boolean isBegin = false;
	//���ö�ά���鴴�����ɸ���ϷС����
	private ImageView[][] gameArr = new ImageView[3][5];
	//��Ϸ������
	private GridLayout game;
	//����շ���
	private ImageView nullView;
	//��ǰ����
	private GestureDetector	curDetector;
	//ע��Ҫ�����Ƽ�����д,����Ҫ��
	
	//��ǰ�����Ƿ�����ִ���ƶ�
	boolean isAnimationRun = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return curDetector.onTouchEvent(event);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		curDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        curDetector = new GestureDetector(this, new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				int type = getDirectionByGesture(e1.getX(), e1.getY(), e2.getX(), e2.getY());
				//Toast.makeText(MainActivity.this, ""+type, Toast.LENGTH_SHORT).show();
				exchangeByDirection(type);
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        /*��ʼ����Ϸ�����ɸ�С����*/
        //��ȡһ�Ŵ�ͼ??ע����һ�����﷨
        /*ע�⣡���������Ǹı���ͼƬ��С����Ӧ��Ļ������ò�ͬ��ͼƬƥ����Ļ�ǽ����Ҫѧϰ�ģ����ﲻ�ǹ�ע���ص�*/
        //������PNGͼƬ������
        Bitmap bigPic;
        switch ((int)(Math.random()*5)) {
		case 0:
			bigPic = ((BitmapDrawable)getResources().getDrawable(R.drawable.game_pic0)).getBitmap();
			break;
		case 1:
			bigPic = ((BitmapDrawable)getResources().getDrawable(R.drawable.game_pic1)).getBitmap();
			break;
		case 2:
			bigPic = ((BitmapDrawable)getResources().getDrawable(R.drawable.game_pic2)).getBitmap();
			break;
		case 3:
			bigPic = ((BitmapDrawable)getResources().getDrawable(R.drawable.game_pic3)).getBitmap();
			break;
		case 4:
			bigPic = ((BitmapDrawable)getResources().getDrawable(R.drawable.game_pic4)).getBitmap();
			break;
		default:
			bigPic = ((BitmapDrawable)getResources().getDrawable(R.drawable.game_pic0)).getBitmap();
			break;
		}
        int picWidth = bigPic.getWidth() / 5;
        //��ȡ��Ļ�ߴ�
        int blockWidth = getWindowManager().getDefaultDisplay().getWidth() / 5;
        for (int i = 0; i < gameArr.length; i++) {
        	for (int j = 0; j < gameArr[0].length; j++) {
        		//�����к����г�������ϷСͼƬ
        		Bitmap unit = Bitmap.createBitmap(bigPic, j*picWidth, i*picWidth, picWidth, picWidth);
        		gameArr[i][j] = new ImageView(this);
        		gameArr[i][j].setImageBitmap(unit);
        		//����ͼƬ���
        		gameArr[i][j].setLayoutParams(new LayoutParams(blockWidth, blockWidth));
        		gameArr[i][j].setPadding(2, 2, 2, 2);
        		//���Զ�������
        		gameArr[i][j].setTag(new GameData(i, j, unit));
        		gameArr[i][j].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						boolean flag = isNeighbourofNull((ImageView)v);						
						//Toast.makeText(MainActivity.this, "�Ƿ����"+flag, Toast.LENGTH_LONG).show();
						if (flag) {
							exchangeData((ImageView)v);
						}
					}
				});
        	}
        }
        
        /*��ʼ����Ϸ�����棬��������ɸ�С����*/
        game = (GridLayout)findViewById(R.id.game);
        for (int i = 0; i < gameArr.length; i++) {
        	for (int j = 0; j < gameArr[0].length; j++) {
        		game.addView(gameArr[i][j]);
        	}
        }        
        //�������һ������Ϊ��
        setNullImageView(gameArr[2][4]);
        
        //�������˳��
        randomExchange();
        isBegin = true;
    }
    /**
     * ����ĳ������Ϊ�շ���
     * @param mImageView ��ǰҪ����Ϊ�յķ���ʵ��
     */
    public void setNullImageView(ImageView mImageView) {
		mImageView.setImageBitmap(null);
		nullView = mImageView;
	}
    /**
     * �жϵ�ǰ����ķ����Ƿ���շ��������ڹ�ϵ
     * @param mImageView ��ǰ����ķ���
     * @return true:���ڣ�false:������
     */
    public boolean isNeighbourofNull(ImageView mImageView) {
    	//��ȡ�շ���͵�ǰ������������
    	GameData nullData = (GameData) nullView.getTag();
    	GameData curData  = (GameData) mImageView.getTag();
    	if (nullData.x==curData.x && Math.abs(nullData.y-curData.y)==1) {
			return true;
		} else if (nullData.y==curData.y && Math.abs(nullData.x-curData.x)==1) {
			return true;
		}
    	return false;
	}
    
    //���أ������;���Ĵ�������
    public void exchangeByDirection(int type) {
    	exchangeByDirection(type, true);
    }
    
    //���ݼ����������Ʒ��򣬻�ȡ�շ����Ӧ����λ�ã�������ڷ��飬����н���
    public void exchangeByDirection(int type, boolean isAnimation) {
		//��ȡ��ǰ�շ����λ��
    	GameData nullData = (GameData)nullView.getTag();
    	//��ȡ���ڷ���λ��
    	int new_x = nullData.x;
    	int new_y = nullData.y;
    	switch (type) {
		case 1:
			new_x++;
			break;
		case 2:
			new_x--;
			break;
		case 3:
			new_y++;
			break;			
		case 4:
			new_y--;
			break;
		default:
			break;
		}  	
    	//�ж��Ƿ�Ϸ�
    	if (new_x>=0 && new_y>=0 && new_x<gameArr.length && new_y<gameArr[0].length) {
    		//�Ϸ�������ƶ�
    		if (isAnimation) {//�ж��Ƿ��ж���
    			exchangeData(gameArr[new_x][new_y]);
    		} else {
    			exchangeData(gameArr[new_x][new_y], isAnimation);//false
    		}
    	}
    	
	}
    //���ƻ��������ж�
    //1:�� 	2:��		3����		4����
    public int getDirectionByGesture(float start_x, float start_y, float end_x, float end_y) {
    	boolean isLeftOrRight = Math.abs(start_x-end_x) > Math.abs(start_y-end_y)?true:false;
    	if (isLeftOrRight) {
    		boolean isLeft = start_x-end_x>0?true:false;
    		if (isLeft) {
    			return 3;
    		} else {
    			return 4;
    		}
    	} else {
    		boolean isUp = start_y-end_y>0?true:false;
    		if (isUp) {
    			return 1;
    		} else {
    			return 2;
    		}
    	}
    }
    //�������˳��
    public void randomExchange() {
		//������Ҵ���
    	for (int i = 0; i < 100; i++) {
    		//�������ƽ��н����������޶���
    		int type = (int)(Math.random()*4)+1;
    		exchangeByDirection(type, false);
    	}
	}
    
    //�������ؽ����;���ĺ�������������
    public void exchangeData(final ImageView mImageView) {
    	exchangeData(mImageView, true);
    }
    
    public void checkGame() {
    	boolean isGameOver = true;
		//����ÿ����ϷС����
    	for (int i = 0; i < gameArr.length; i++) {
    		for (int j = 0; j < gameArr[0].length; j++) {
    			//Ϊ������
    			if (gameArr[i][j] == nullView) {
    				continue;
    			}
    			GameData curData = (GameData)gameArr[i][j].getTag();
    			if (!curData.isTrue()) {
    				isGameOver = false;
    				break;
    			}
    		}
    	}
    	if (isGameOver) {
    		Toast.makeText(this, "��Ϸ����", Toast.LENGTH_SHORT).show();
    	}
	}
    //���ö�������֮�󽻻��������������
    public void exchangeData(final ImageView mImageView, boolean isAnimation) {
		if (isAnimationRun) {
			return;
		}
    	//���û�ж���
    	if (!isAnimation) {
			//��������Ҫ�߼�֮һ��ע��������
			//���˸о����ﻹ����setter\getter�ȽϺ�
			mImageView.clearAnimation();
			GameData mData = (GameData)mImageView.getTag();
			nullView.setImageBitmap(mData.bm);//��ֱ�ӷ����ڲ����˽�ж��󣬵�������ʵ������û�úÿ��鰡 	
			GameData nullData = (GameData)nullView.getTag();
			nullData.bm = mData.bm;
			nullData.px = mData.px;
			nullData.py = mData.py;
			setNullImageView(mImageView);
	    	if (isBegin) {
	    		checkGame();
	    	}
			return;
    	}
    	//����һ�����������ú÷����ƶ��ľ���
    	TranslateAnimation exchangeAnimation = null;
    	if (mImageView.getX() > nullView.getX()) {
    		//���·��������ƶ�
    		exchangeAnimation = new TranslateAnimation(0.1f, -mImageView.getWidth(), 0.1f, 0.1f);
    	} else if (mImageView.getX() < nullView.getX()) {
    		//���Ϸ���������
    		exchangeAnimation = new TranslateAnimation(0.1f, mImageView.getWidth(), 0.1f, 0.1f);
    	} else if (mImageView.getY() > nullView.getY()) {
    		//���ұߣ������ƶ�
    		exchangeAnimation = new TranslateAnimation(0.1f, 0.1f, 0.1f, -mImageView.getWidth());
    	} else if (mImageView.getY() < nullView.getY()) {
    		//����ߣ������ƶ�
    		exchangeAnimation = new TranslateAnimation(0.1f, 0.1f, 0.1f, mImageView.getWidth());
    	}
    	//���ö���ʱ��
    	exchangeAnimation.setDuration(50);
    	//���ö�������֮���Ƿ�ͣ��
    	exchangeAnimation.setFillAfter(true);
    	//���������ݽ���
    	exchangeAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				isAnimationRun = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				isAnimationRun = false;
				//��������Ҫ�߼�֮һ��ע��������
				//���˸о����ﻹ����setter\getter�ȽϺ�
				mImageView.clearAnimation();
				GameData mData = (GameData)mImageView.getTag();
				nullView.setImageBitmap(mData.bm);//��ֱ�ӷ����ڲ����˽�ж��󣬵�������ʵ������û�úÿ��鰡 	
				GameData nullData = (GameData)nullView.getTag();
				nullData.bm = mData.bm;
				nullData.px = mData.px;
				nullData.py = mData.py;
				setNullImageView(mImageView);//ԭ��λ�õ�px,py��û��������
		    	if (isBegin) {
		    		checkGame();
		    	}
			}
		});
    	//ִ�ж���
    	mImageView.startAnimation(exchangeAnimation);
	}
    
    //ÿ����ϷС������Ҫ�󶨵�����
    class	GameData {
    	//ʵ��λ��
    	private int x;
    	private int y;
    	//С�����Ӧ��ͼƬ
    	private Bitmap bm;
    	//ͼƬλ��
    	private int px;
    	private int py;
    	public GameData(int x, int y, Bitmap bm) {
			super();
			this.x = x;
			this.y = y;
			this.bm = bm;
			this.px = x;
			this.py = y;
		}
    	//�ж�ÿ��С����λ���Ƿ���ȷ
		public boolean isTrue() {
			if (this.x==this.px && this.y==this.py) {
				return true;
			}
			return false;
		}
    }
}


//ϸ�����⣬�����ƶ������в��ɲ���
//��������2D��ת��DemoҲ���������


//���Ʋ�����ͼƬ�������Ч�����漰ʱ��ķַ����ƣ���ʱ������̫���


//�Լ���δ���ͼƬ��ʵ����Ļ