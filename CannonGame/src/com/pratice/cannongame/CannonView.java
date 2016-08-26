package com.pratice.cannongame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CannonView extends SurfaceView implements SurfaceHolder.Callback{
	private static final String TAG = "CannonView"; // for logging errors

	private CannonThread cannonThread; // controls the game loop
	private Activity activity; // to display Game Over dialog in GUI thread
	private boolean dialogIsDisplayed = false;   
               
	// variables for the game loop and tracking statistics
	private boolean gameOver; // is the game over?
	private double timeLeft; // time remaining in seconds
	private int shotsFired; // shots the user has fired
	private double totalElapsedTime; // elapsed seconds 

	// variables for the blocker and target
	private Line blocker; // start and end points of the blocker
	private int blockerDistance; // blocker distance from left
	private int blockerBeginning; // blocker top-edge distance from top
	private int blockerEnd; // blocker bottom-edge distance from top
	private int initialBlockerVelocity; // initial blocker speed multiplier
	private float blockerVelocity; // blocker speed multiplier during game

	private Line target; // start and end points of the target
	private int targetDistance; // target distance from left
	private int targetBeginning; // target distance from top
	private double pieceLength; // length of a target piece
	private int targetEnd; // target bottom's distance from top
	private int initialTargetVelocity; // initial target speed multiplier
	private float targetVelocity; // target speed multiplier 

	private int lineWidth; // width of the target and blocker
	private boolean[] hitStates; // is each target piece hit?
	private int targetPiecesHit; // number of target pieces hit (out of 7)

	// variables for the cannon and cannonball
	private Point cannonball; // cannonball image's upper-left corner
	private int cannonballVelocityX; // cannonball's x velocity
	private int cannonballVelocityY; // cannonball's y velocity
	private boolean cannonballOnScreen; // whether cannonball on the screen
	private int cannonballRadius; // cannonball's radius
	private int cannonballSpeed; // cannonball's speed
	private int cannonBaseRadius; // cannon base's radius
	private int cannonLength; // cannon barrel's length
   	private Point barrelEnd; // the endpoint of the cannon's barrel
   	private int screenWidth; 
   	private int screenHeight; 
   	private SoundPool soundPool; // plays sound effects
   	private SparseIntArray soundMap; // maps IDs to SoundPool

   	// Paint variables used when drawing each item on the screen
   	private Paint textPaint; // Paint used to draw text
   	private Paint cannonballPaint; // Paint used to draw the cannonball
   	private Paint cannonPaint; // Paint used to draw the cannon
   	private Paint blockerPaint; // Paint used to draw the blocker
   	private Paint targetPaint; // Paint used to draw the target
   	private Paint backgroundPaint; // Paint used to clear the drawing area
	public CannonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (Activity) context;//��������ã��Ա�����Ϸ�����Ǵ�Activity��GUI�߳���ʾһ��Dialog
		//��CannonViewע���ʵ��SurfaceHolder.Callback�Ķ�����֪ʶ��
		getHolder().addCallback(this);
		
		//��ʼ��������Ϸ����������͵�
		blocker = new Line();
		target	= new Line();
		cannonball = new Point();
		
		//��ʼ���ò���ֵ�����ײ��״̬
		hitStates = new boolean[Constant.TARGET_PIECES];
		
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		
		//����Ϸ��Ч���龰��Ӧ��������Դ
		soundMap = new SparseIntArray(3);//��hashMap���ƣ���С��ģ����Ч��
		soundMap.put(Constant.TARGET_SOUND_ID,
				soundPool.load(context, R.raw.target_hit, 1));
		soundMap.put(Constant.CANNON_SOUND_ID,
				soundPool.load(context, R.raw.cannon_fire, 1));
		soundMap.put(Constant.BLOCKER_SOUND_ID,
				soundPool.load(context, R.raw.blocker_hit, 1));
		
		//��ʼ�����ƻ���Ļ���
		textPaint = new Paint();
		cannonPaint = new Paint();
		cannonballPaint = new Paint();
		blockerPaint = new Paint();
		targetPaint = new Paint();
		backgroundPaint = new Paint();
	}
	//�����Դ�С�Լ�λ�õ�����
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		screenWidth = w;
		screenHeight = h;
		cannonBaseRadius = h/18;
		cannonLength = w/8;
		
		cannonballRadius = w / 36;
		cannonballSpeed = w * 3 / 2;
		
		lineWidth = w / 24;
		
		//������blocker�йز���
		blockerDistance = w * 5 / 8;
		blockerBeginning = h / 8;
		blockerEnd = h * 3 / 8;
		initialBlockerVelocity = h / 2;
		blocker.start = new Point(blockerDistance, blockerBeginning);
		blocker.end = new Point(blockerDistance, blockerEnd);
		
		//������Ŀ�����йصĲ���
		targetDistance = w * 7 / 8;
		targetBeginning = h / 8;
		targetEnd = h * 7 / 8;
		pieceLength = (targetEnd - targetBeginning) / Constant.TARGET_PIECES;
		initialTargetVelocity = - h / 4;
		target.start = new Point(targetDistance, targetBeginning);
		target.end = new Point(targetDistance, targetEnd);
		
		//��Ͳ
		barrelEnd = new Point(cannonLength, h / 2);
		
		//���û���
		textPaint.setTextSize(w/20);
		textPaint.setAntiAlias(true);
		cannonPaint.setStrokeWidth(lineWidth * 1.5f);
		blockerPaint.setStrokeWidth(lineWidth);
		targetPaint.setStrokeWidth(lineWidth);
		backgroundPaint.setColor(Color.WHITE);		
		Log.d(TAG, "above newgame");
		newGame();
	}
	
	public void newGame() {
		//����ÿ��Ԫ�ص�ײ��״̬
		for (int i = 0; i < Constant.TARGET_PIECES; i++) {
			hitStates[i] = false;
		}
		//Ŀ�걻������������ʼʱ��Ϊ0
		targetPiecesHit = 0;
		
		blockerVelocity = initialBlockerVelocity;
		targetVelocity = initialTargetVelocity;
		timeLeft = 10.0;
		cannonballOnScreen = false;
		
		shotsFired = 0;
		totalElapsedTime = 0.0;
		
		blocker.start.set(blockerDistance, blockerBeginning);
		blocker.end.set(blockerDistance, blockerEnd);
		target.start.set(targetDistance, targetBeginning);
		target.end.set(targetDistance, targetEnd);
		
		if (gameOver) {
			gameOver = false;
			cannonThread = new CannonThread(getHolder());
			Log.d(TAG, "thread start");
			cannonThread.start();
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
			fireCannonball(event);
		}
		return true;
	}
	
	private class CannonThread extends Thread {
		private SurfaceHolder surfaceHolder;
		public boolean threadIsRunning = true;
		public CannonThread(SurfaceHolder holder) {
			surfaceHolder = holder;
			setName("CannonThread");
		}
		

		public void setRunning(boolean b) {
			threadIsRunning = b;
		}
		@Override
		public void run() {
			Canvas canvas = null;
			long previousFrameTime = System.currentTimeMillis();
			while (threadIsRunning) {
				try {
					canvas = surfaceHolder.lockCanvas(null);
					
					synchronized (surfaceHolder) {
						long currentTime = System.currentTimeMillis();
						double elapsedTimeMS = currentTime - previousFrameTime;
						totalElapsedTime += elapsedTimeMS/1000.0;
						updatePositions(elapsedTimeMS);

						drawGameElements(canvas);
						previousFrameTime = currentTime;
					}
				} finally{
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
	}
	private void updatePositions(double elapsedTimeMS) {
		double interval = elapsedTimeMS / 1000.0;
		if (cannonballOnScreen) {
			cannonball.x += interval * cannonballVelocityX;
			cannonball.y += interval * cannonballVelocityY;
			Log.d(TAG,"run here");			
			if (cannonball.x+cannonballRadius > blockerDistance && 
					cannonball.x-cannonballRadius < blockerDistance &&
					cannonball.y+cannonballRadius > blocker.start.y &&
					cannonball.y-cannonballRadius < blocker.end.y) {//���ϰ������ײ���
				cannonballVelocityX *= -1;
				cannonball.x = blockerDistance-cannonballRadius-1;
				timeLeft -= Constant.MISS_PENALTY;
				soundPool.play(soundMap.get(Constant.BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
			} else if (cannonball.x + cannonballRadius > screenWidth || 
		            cannonball.x - cannonballRadius < 0)
	         {
	            cannonballOnScreen = false; // remove cannonball from screen
	         } else if (cannonball.y + cannonballRadius > screenHeight || 
	            cannonball.y - cannonballRadius < 0)
	         {
	            cannonballOnScreen = false; // remove cannonball from screen
	         } else if (cannonball.x+cannonballRadius > targetDistance && 
					cannonball.x-cannonballRadius < targetDistance &&
					cannonball.y+cannonballRadius > target.start.y &&
					cannonball.y-cannonballRadius < target.end.y) {//��Ŀ�����ײ���
				//�жϱ����е�Ŀ������һ��
				int section = (int) ((cannonball.y-target.start.y)/pieceLength);
				//�ж��Ƿ����
				if (section >= 0 && section < Constant.TARGET_PIECES && 
						!hitStates[section]) {
					//��������Ϸ�߼�������
					hitStates[section] = true;
					cannonballOnScreen = false;
					timeLeft += Constant.HIT_REWARD;
					soundPool.play(soundMap.get(Constant.TARGET_SOUND_ID), 1, 1, 1, 0, 1f);
					++ targetPiecesHit;
					if (Constant.TARGET_PIECES == targetPiecesHit) {//�ж���Ϸ�Ƿ����
						cannonThread.setRunning(false);
						showGameOverDialog(R.string.win);
						gameOver= true;
					}
				}
			}
		}
		
		//�����ϰ����Ŀ���λ��
		double blockerUpdate = interval * blockerVelocity;
		blocker.start.y += blockerUpdate;
		blocker.end.y += blockerUpdate;
		
		double targetUpdate = interval * targetVelocity;
		target.start.y += targetUpdate;
		target.end.y += targetUpdate;
		
		//λ�ü��
		if(blocker.start.y < 0 || blocker.end.y > screenHeight) {
			blockerVelocity *= -1;
		}
		if (target.start.y < 0 || target.end.y > screenHeight) {
			targetVelocity *= -1;
		}
		timeLeft -= interval;
		
		//�ж���Ϸ�Ƿ����
		if (timeLeft <= 0.0) {
			timeLeft = 0.0;//�Ͻ���
			gameOver = true;
			cannonThread.setRunning(false);
			showGameOverDialog(R.string.lose);
		}
	}
	
	public void fireCannonball(MotionEvent event) {
		if (cannonballOnScreen) {
			return;
		}
		
		//��ȡ��̨�Ƕ�
		double angle = alignCannon(event);
		
		cannonball.x = cannonballRadius;
		cannonball.y = screenHeight / 2;
		
		cannonballVelocityX = (int) (cannonballSpeed * Math.sin(angle));
		cannonballVelocityY = (int) (-cannonballSpeed * Math.cos(angle));
		
		cannonballOnScreen = true;
		
		++shotsFired;
		
		soundPool.play(soundMap.get(Constant.CANNON_SOUND_ID), 1, 1, 1, 0, 1f);		
	}
	public double alignCannon(MotionEvent event) {
		//��ȡ������
		Point touchPoint = new Point((int)event.getX(), (int)event.getY());
		
		double centerMinusY = (screenHeight/2 - touchPoint.y);
		double angle = 0;
		if (0 != centerMinusY) {
			angle = Math.atan((double)touchPoint.x/centerMinusY);
		}
		if (touchPoint.y > screenHeight/2) {
			angle +=  Math.PI;
		}
		barrelEnd.x = (int) (cannonLength*Math.sin(angle));
		barrelEnd.y = (int) (-cannonLength*Math.cos(angle)+screenHeight/2);
		return angle;		
	}
	public void drawGameElements(Canvas canvas) {
		
		//������
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
		
		//��ʾʣ��ʱ��
		canvas.drawText(getResources().getString(R.string.time_remaining_format, timeLeft),
				30, 50, textPaint);
		
		//������ڵ������
		if (cannonballOnScreen) {
			canvas.drawCircle(cannonball.x, cannonball.y, cannonballRadius, cannonballPaint);
		}
		
		//������̨
		canvas.drawLine(0, screenHeight/2, barrelEnd.x, barrelEnd.y, cannonPaint);
		//����̨��
		canvas.drawCircle(0,  screenHeight/2, cannonBaseRadius, cannonPaint);
		
		//�����ϰ�
		canvas.drawLine(blocker.start.x, blocker.start.y,
				blocker.end.x, blocker.end.y, blockerPaint);
		Log.d(TAG, "draw finish");
		Point currentPoint = new Point();
		//��ʼ��Ŀ��ĵ�ǰ��ʼλ��
		currentPoint.x = target.start.x;
		currentPoint.y = target.start.y;
		for (int i = 0; i < Constant.TARGET_PIECES; i++) {
			if (!hitStates[i]) {
				if (i%2 == 0) {
					targetPaint.setColor(Color.BLUE);
				} else {
					targetPaint.setColor(Color.YELLOW);
				}
				canvas.drawLine(currentPoint.x, currentPoint.y, 
						target.end.x, (int)(currentPoint.y+pieceLength), targetPaint);
				
			}
			currentPoint.y += pieceLength;
		}

	}
	
	private void showGameOverDialog(final int messageId) {
		final DialogFragment gameResult = new DialogFragment(){
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(getResources().getString(messageId));
				builder.setMessage(getResources().getString(R.string.result_format, 
						shotsFired, totalElapsedTime));
				builder.setPositiveButton(R.string.reset_game, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialogIsDisplayed = false;
								newGame();
							}
						});
		
				return builder.create();
			}
		};
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialogIsDisplayed = true;
				gameResult.setCancelable(false);
				gameResult.show(activity.getFragmentManager(), "results");
			}
		});
	}
	public void stopGame() {
		if (cannonThread != null) {
			cannonThread.setRunning(false);
		}
	}
	public void releaseResources() {
		soundPool.release();
		soundPool = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!dialogIsDisplayed) {
			cannonThread = new CannonThread(holder);
			cannonThread.setRunning(true);
			cannonThread.start();
		}
	}
	

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//ȷ����Դ����ȷ����
		boolean retry = true;
		cannonThread.setRunning(false);
		
		while (retry) {
			try {
				cannonThread.join();//�ȴ��߳̽���
				retry = false;
			} catch (InterruptedException e) {
				Log.e(TAG, "Thread interrupted", e);
			}
		}
		
	}
}
