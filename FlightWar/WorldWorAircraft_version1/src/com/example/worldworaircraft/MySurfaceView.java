package com.example.worldworaircraft;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
public class MySurfaceView extends SurfaceView implements Callback,Runnable {
	private SurfaceHolder 	surfaceHolder;
	private Paint 			paint;
	private Canvas 			canvas;
	private Thread 			thread;
	public 	static 	int 	screenW, screenH;
	public 	static 	int 	gameState = Constant.GAME_MENU;
	private boolean	 		flag;
	private float 			scaleX, scaleY;

	private	Resources		res = this.getResources();	//����һ��Resourcesʵ�����ڼ���ͼƬ
	//������Ϸ���õ�����Դ
	private Bitmap 			mGameBg;							//��Ϸ����
	private Bitmap 			mBoom;								//��ըЧ��
	private Bitmap	 		mBossBoom;							//Boss��ըЧ��
	private Bitmap 			mButton;							//��Ϸ��ʼ��ť
	private Bitmap 			mButtonPress;						//��Ϸ��ʼ��ť�����
	private Bitmap 			mEnemyDuck;							//����Ѽ��
	private Bitmap 			mEnemyFly;							//�����Ӭ
	private Bitmap 			mEnemyBoss;							//������ͷBoss
	private Bitmap 			mGameWin;							//��Ϸʤ������
	private Bitmap 			mGameLost;							//��Ϸʧ�ܱ���
	private Bitmap 			mPlayer;							//��Ϸ���Ƿɻ�
	private Bitmap 			mPlayerBlood;						//���Ƿɻ�Ѫ��
	private Bitmap		 	mMenuBg;							//�˵�����
	public 	static 	Bitmap 	mBullet;							//�ӵ�
	public 	static 	Bitmap 	mEnemyBullet;						//�л��ӵ�
	public 	static	Bitmap 	mBossBullet;						//Boss�ӵ�
	
	
	private Menu 			gameMenu;							//������Ϸ����Ķ���
	private GameBg 			backGround;							//����һ��������Ϸ��������
	private Player 			player;								//�������Ƕ���
	private Vector<Bullet> 	enemyBullet;						//�л��ӵ�����
	private int 			countEnemyBullet;					//����ӵ��ļ�����
	private Vector<Bullet> 	playerBullet;						//�����ӵ�����
	private int 			countPlayerBullet;					//����ӵ��ļ�����
	private Vector<Enemy> 	enemy;								//����һ���л�����
	private int 			count;								//������
	//�������飺1��2��ʾ�л������࣬-1��ʾBoss
	private int 			enemyArray[][] = new int[Constant.ENEMY_NUM][Constant.ENEMY_NUM_KIND];
	private int 			enemyArrayIndex = 0;				//��ǰȡ��һά������±�
	private boolean 		isBoss;								//�Ƿ����Boss��ʶλ
	private Random 			random;								//�Ƿ����Boss��ʶλ
	private Vector<Boom> 	boom;								//��ըЧ������	
	private Boss 			boss;
	public static Vector<Bullet> bossBullet;					//Boss���ӵ�����
	private SoundPool		shotPool;							//��Ч
	private SoundPool		boomPool;
	private int 			shotID;
	private int 			boomID;
	
	
	/**
	 * @author ��ԣ
	 * ���ܣ����캯��
	 */
	public MySurfaceView(Context context) {
		super(context);
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
		//���ñ�������
		this.setKeepScreenOn(true);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		DisplayMetrics dm = getResources().getDisplayMetrics();  
        screenW = dm.widthPixels;  
        screenH = dm.heightPixels;  
		init();
		shotPool	= new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		boomPool	= new SoundPool(4, AudioManager.STREAM_MUSIC, 100);		
		shotID 		= shotPool.load(getContext(), R.raw.shot, 1);
		boomID		= boomPool.load(getContext(), R.raw.boom, 1);
		
		flag 		= true;
		thread 		= new Thread(this);
		thread.start();
		
	}

	
	/**
	 * @author ��ԣ
	 * ���ܣ���ʼ����Ϸ
	 */
	private void init() {
		if (gameState == Constant.GAME_MENU) {
			//1.������Ϸ��Դ
			mGameBg 	= 	BitmapFactory.decodeResource(res, R.drawable.background);
			mBoom 		= 	BitmapFactory.decodeResource(res, R.drawable.boom);
			mBossBoom 	= 	BitmapFactory.decodeResource(res, R.drawable.boos_boom);
			mButton 	= 	BitmapFactory.decodeResource(res, R.drawable.button);
			mButtonPress= 	BitmapFactory.decodeResource(res, R.drawable.button_press);
			mEnemyDuck 	= 	BitmapFactory.decodeResource(res, R.drawable.enemy_duck);
			mEnemyFly 	= 	BitmapFactory.decodeResource(res, R.drawable.enemy_fly);
			mEnemyBoss 	= 	BitmapFactory.decodeResource(res, R.drawable.enemy_pig);
			mGameWin 	= 	BitmapFactory.decodeResource(res, R.drawable.gamewin);
			mGameLost 	= 	BitmapFactory.decodeResource(res, R.drawable.gamelost);
			mPlayer 	= 	BitmapFactory.decodeResource(res, R.drawable.player);
			mPlayerBlood= 	BitmapFactory.decodeResource(res, R.drawable.hp);
			mMenuBg 	= 	BitmapFactory.decodeResource(res, R.drawable.menu);
			mBullet 	= 	BitmapFactory.decodeResource(res, R.drawable.bullet);
			mEnemyBullet= 	BitmapFactory.decodeResource(res, R.drawable.bullet_enemy);
			mBossBullet = 	BitmapFactory.decodeResource(res, R.drawable.boosbullet);
			
			
			scaleX 		= 	screenW*1.0f/mMenuBg.getWidth();
			scaleY 		= 	screenH*1.0f/mMenuBg.getHeight();
			
			//2.������Ļ
			mGameBg 	= 	Bitmap.createScaledBitmap(mGameBg, screenW,
					(int)(screenW*mGameBg.getHeight()*1.0f/mGameBg.getWidth()), true);//ע������Ƚ�����
			mMenuBg 	= 	Bitmap.createScaledBitmap(mMenuBg, screenW, screenH, true);
			mButton 	= 	Bitmap.createScaledBitmap(mButton, (int)scaleX*mButton.getWidth(),
					(int)scaleY*mButton.getHeight(), true);
			mGameWin 	=	Bitmap.createScaledBitmap(mGameWin, screenW, screenH, true);
			mGameLost 	=	Bitmap.createScaledBitmap(mGameLost, screenW, screenH, true);
			mPlayer 	= 	Bitmap.createScaledBitmap(mPlayer, (int)scaleX*mPlayer.getWidth(),
					(int)scaleY*mPlayer.getHeight(), true);
			mPlayerBlood= 	Bitmap.createScaledBitmap(mPlayerBlood, 
					(int)scaleX*mPlayerBlood.getWidth(), 
						(int)scaleY*mPlayerBlood.getHeight(), true);
			
			//ʵ�������
			gameMenu 	= 	new Menu(mMenuBg, mButton, mButtonPress);	//�˵���ʵ��
			backGround 	= 	new GameBg(mGameBg);
			player 		= 	new Player(mPlayer, mPlayerBlood);
			enemy		= 	new Vector<Enemy>();						//ʵ���л�����
			random		= 	new Random();								//ʵ�������
			boom 		= 	new Vector<Boom>();						//��ըЧ������ʵ��
			enemyBullet = 	new Vector<Bullet>();						//�л��ӵ�����ʵ��
			playerBullet=	new Vector<Bullet>();						//�����ӵ�����ʵ��
			
			
			
			//---Boss���
			boss = new Boss(mEnemyBoss);							//ʵ��boss����
			bossBullet = new Vector<Bullet>();						//ʵ��Boss�ӵ�����
			
			count = 0;
			enemyArray = CreateEnemy.create();
		}
	}

	/**
	 * @author ��ԣ
	 * ���ܣ�������Ϸ״̬����
	 */
	private void draw() {
		try {
			canvas = surfaceHolder.lockCanvas();
			if (null != canvas) {
				canvas.drawColor(Color.WHITE);						//���ñ���ɫ
				switch (gameState) {								//�ж�״̬
				case Constant.GAME_MENU:
					gameMenu.draw(canvas, paint);
					break;
				case Constant.GAME_RUN:
					backGround.draw(canvas, paint);//���Ʊ���
					player.draw(canvas, paint);//�������
					drawEnemy();
					for (int i = 0; i < playerBullet.size(); i++) {	//���������ӵ�����
						playerBullet.elementAt(i).draw(canvas, paint);
					}
					for (int i = 0; i < boom.size(); i++) {			//��ըЧ������
						boom.elementAt(i).draw(canvas, paint);
					}
					
					break;
				case Constant.GAME_WIN:
					canvas.drawBitmap(mGameWin, 0, 0, paint);
					break;
				case Constant.GAME_LOST:
					canvas.drawBitmap(mGameLost, 0, 0, paint);
					break;
				case Constant.GAME_PAUSE:
					
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}
	
	private void drawEnemy() {
		if (!isBoss) {
			for (int i = 0; i < enemy.size(); i++) {			//�л�����
				enemy.elementAt(i).draw(canvas, paint);
			}

			for (int i = 0; i < enemyBullet.size(); i++) {		//�л��ӵ�����
				enemyBullet.elementAt(i).draw(canvas, paint);
			}
			
		} else {
			boss.draw(canvas, paint);							//Boos�Ļ���
			for (int i = 0; i < bossBullet.size(); i++) {		//Boss�ӵ��߼�
				bossBullet.elementAt(i).draw(canvas, paint);
			}
		}
		
	}
	
	/**
	 * @author ��ԣ
	 * ���ܣ���Ϸ�߼����
	 */
	private void logic() {
		switch (gameState) {
		case Constant.GAME_MENU:
			
			break;
		case Constant.GAME_RUN:
			backGround.logic();			//�����߼�
			player.logic();				//����߼�
			logicEnemy();				//�л��߼�
			logicPlayer();				//�����������йص��߼�
			break;
		default:
			break;
		}
	}
	private void logicEnemy() {
		if (!isBoss) {
			for (int i = 0; i < enemy.size(); i++) {	//�л��߼�
				Enemy en = enemy.elementAt(i);
				if (en.isDead) {						//�����������ô�ʹ�������ɾ��,�����������Ż����ã�
					enemy.removeElementAt(i);
				} else {
					en.logic();
				}
			}
			count++;//���ɵл�
			if (count % Constant.CREATE_ENEMY_TIME== 0) {
				for (int i = 0; i < enemyArray[enemyArrayIndex].length; i++) {
					if (enemyArray[enemyArrayIndex][i] == Constant.ENEMY_TYPE_FLY) {//��Ӭ
						int x = random.nextInt(screenW - 100) + 50;Log.d("Run", "���ɵл�");
						enemy.addElement(new Enemy(mEnemyFly, Constant.ENEMY_TYPE_FLY, x, -50));
					} else if (enemyArray[enemyArrayIndex][i] == Constant.ENEMY_TYPE_DUCKL) {//Ѽ����
						int y = random.nextInt(20);Log.d("Run", "���ɵл�1");
						enemy.addElement(new Enemy(mEnemyDuck, Constant.ENEMY_TYPE_DUCKL, -50, y));
					} else if (enemyArray[enemyArrayIndex][i] == Constant.ENEMY_TYPE_DUCKR) {//Ѽ����
						int y = random.nextInt(20);Log.d("Run", "���ɵл�2");
						enemy.addElement(new Enemy(mEnemyDuck, Constant.ENEMY_TYPE_DUCKR, screenW + 50, y));
					}
				}

				if (enemyArrayIndex == enemyArray.length - 1) {			
					isBoss = true;										//�����ж���һ���Ƿ�Ϊ���һ��(Boss)
				} else {
					enemyArrayIndex++;
				}
			}
			//����л������ǵ���ײ
			for (int i = 0; i < enemy.size(); i++) {
				if (player.isCollsionWith(enemy.elementAt(i))) {
					player.setPlayerBlood(player.getPlayerBlood() - 1);	//������ײ������Ѫ��-1
					if (player.getPlayerBlood() <= -1) {					//������Ѫ��С��0���ж���Ϸʧ��
						gameState = Constant.GAME_LOST;
					}
				}
			}
			//ÿ2�����һ���л��ӵ�
			countEnemyBullet++;
			if (countEnemyBullet % 40 == 0) {
				for (int i = 0; i < enemy.size(); i++) {
					Enemy en = enemy.elementAt(i);
					//��ͬ���͵л���ͬ���ӵ����й켣
					int bulletType = 0;
					switch (en.type) {
					//��Ӭ
					case Constant.ENEMY_TYPE_FLY:
						bulletType = Constant.BULLET_FLY;
						break;
					//Ѽ��
					case Constant.ENEMY_TYPE_DUCKL:
					case Constant.ENEMY_TYPE_DUCKR:
						bulletType = Constant.BULLET_DUCK;
						break;
					}
					enemyBullet.add(new Bullet(mEnemyBullet, en.x + 10, en.y + 20, bulletType));
				}
			}
			//����л��ӵ��߼�
			for (int i = 0; i < enemyBullet.size(); i++) {
				Bullet b = enemyBullet.elementAt(i);
				if (b.isDead) {
					enemyBullet.removeElement(b);
				} else {
					b.logic();
				}
			}
			//����л��ӵ���������ײ
			for (int i = 0; i < enemyBullet.size(); i++) {
				if (player.isCollsionWith(enemyBullet.elementAt(i))) {
					//������ײ������Ѫ��-1
					player.setPlayerBlood(player.getPlayerBlood() - 1);
					//������Ѫ��С��0���ж���Ϸʧ��
					if (player.getPlayerBlood() <= -1) {
						gameState = Constant.GAME_LOST;
					}
				}
			}
			//���������ӵ���л���ײ
			for (int i = 0; i < playerBullet.size(); i++) {
				//ȡ�������ӵ�������ÿ��Ԫ��
				Bullet blPlayer = playerBullet.elementAt(i);
				for (int j = 0; j < enemy.size(); j++) {
					//��ӱ�ըЧ��
					//ȡ���л�������ÿ��Ԫ�������ӵ������ж�
					if (enemy.elementAt(j).isCollsionWith(blPlayer)) {
						boomPool.play(boomID, 1f, 1f, 1, 0, 1.0f);
						boom.add(new Boom(mBoom, enemy.elementAt(j).x, enemy.elementAt(j).y, 7));
					}
				}
			}
			
		} else {
			//Boss����߼�
			//ÿ0.5�����һ�������ӵ�
			boss.logic();
			if (countPlayerBullet % 10 == 0) {
				//Boss��û����֮ǰ����ͨ�ӵ�
				bossBullet.add(new Bullet(mBossBullet, boss.x + 35, boss.y + 40, Constant.BULLET_FLY));
			}
			//Boss�ӵ��߼�
			for (int i = 0; i < bossBullet.size(); i++) {
				Bullet b = bossBullet.elementAt(i);
				if (b.isDead) {
					bossBullet.removeElement(b);
				} else {
					b.logic();
				}
			}
			//Boss�ӵ������ǵ���ײ
			for (int i = 0; i < bossBullet.size(); i++) {
				if (player.isCollsionWith(bossBullet.elementAt(i))) {
					//������ײ������Ѫ��-1
					player.setPlayerBlood(player.getPlayerBlood() - 1);
					//������Ѫ��С��0���ж���Ϸʧ��
					if (player.getPlayerBlood() <= -1) {
						gameState = Constant.GAME_LOST;
					}
				}
			}
			
			//Boss�������ӵ����У�������ըЧ��
			for (int i = 0; i <playerBullet.size(); i++) {
				Bullet b = playerBullet.elementAt(i);
				if (boss.isCollsionWith(b)) {
					boomPool.play(boomID, 1f, 1f, 1, 0, 1.0f);
					if (boss.blood <= 0) {
						//��Ϸʤ��
						gameState = Constant.GAME_WIN;
					} else {
						//��ʱɾ��������ײ���ӵ�����ֹ�ظ��ж����ӵ���Boss��ײ��
						b.isDead = true;
						//BossѪ����1
						boss.setBlood(boss.blood - 1);
						//��Boss���������Boss��ըЧ��
						boom.add(new Boom(mBossBoom, boss.x + 25, boss.y + 30, 5));
						boom.add(new Boom(mBossBoom, boss.x + 35, boss.y + 40, 5));
						boom.add(new Boom(mBossBoom, boss.x + 45, boss.y + 50, 5));
					}
				}
			}
			
		}
	}
	
	private void logicPlayer() {
		//ÿ1�����һ�������ӵ�
		countPlayerBullet++;
		if (countPlayerBullet % 20 == 0) {
			shotPool.play(shotID, 1f, 1f, 1, 0, 1.0f);
			playerBullet.add(new Bullet(mBullet, player.x + 15, player.y - 20,
					Constant.BULLET_PLAYER));
		}
		//���������ӵ��߼�
		for (int i = 0; i < playerBullet.size(); i++) {
			Bullet b = playerBullet.elementAt(i);
			if (b.isDead) {
				playerBullet.removeElement(b);
			} else {
				b.logic();
			}
		}
		//��ըЧ���߼�
		for (int i = 0; i < boom.size(); i++) {
			Boom b = boom.elementAt(i);
			if (b.playEnd) {
				//������ϵĴ�������ɾ��
				boom.removeElementAt(i);
			} else {
				boom.elementAt(i).logic();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (gameState) {
		case Constant.GAME_MENU:
			gameMenu.onTouchEvent(event);
			break;
		case Constant.GAME_RUN:
			player.onTouchEvent(event);
			break;
		default:
			break;
		}
		return true;//ע����һ�䣬������ture��������MotionEvent.ACTION_UP��֧δ��Ӧ
	}
	
	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			draw();
			//Log.d("RUN", "RUN");
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < Constant.SLEEP_TIME) {
					Thread.sleep(Constant.SLEEP_TIME - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		flag = false;
		boomPool.release();
		shotPool.release();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int arg1, int arg2, int arg3) {

	}
}
