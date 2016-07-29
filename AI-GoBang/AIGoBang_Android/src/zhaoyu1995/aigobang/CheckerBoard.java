package zhaoyu1995.aigobang;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;



public class CheckerBoard extends View {
	private int checkerBoardWidth;//���̿��
	private float unitWidth;//ÿһС��Ŀ��
	private static final int CHECKERBOARD_SIZE = 15;//���̴�С��15*15
	private Bitmap whitePiece;//��������
	private Bitmap blackPiece;
	private int pieceRadio;
	private float proportion = (float) (9.0 / 10.0);//��������ڵ�Ԫ������Ŵ�С
	//���Ӽ�¼
	boolean isBlack = true;
	private ArrayList<Point> whiteList = new ArrayList<Point>();
	private ArrayList<Point> blackList = new ArrayList<Point>();
	private Paint paint = new Paint();	
	private AI aiPlayer = new AI();//ʵ����һ��AI
	private int gameState = 0;
	private int whiteWins = 0;
	private int blackWins = 0;
	private boolean isDialogOver = true;
	private Point lastPoint;
	private MediaPlayer player = MediaPlayer.create(getContext(), R.raw.voice);//��ȻҪMP3
	private boolean AIvsAI = false;
	public void setAIvsAI(boolean b) {
		AIvsAI = b;
	}
	public CheckerBoard(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CheckerBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//setBackgroundColor(0x44ff0000);//����ʹ�ã�ʹ���ֿɼ�
		
		//��ʼ��
		init();
	}
	public void init() {
		lastPoint = null;
		//���û�����ɫ
		paint.setColor(0x99000000);//��ɫ��͸��
		paint.setAntiAlias(true);//�����
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		//���
		
		//��һ����������
		//��ʼ������
		blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.piece1);
		whitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.piece2);
	}

	public CheckerBoard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	//�Զ���View�Ĳ������⣬��̫��
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int width = Math.min(widthSize, heightSize);
		//���Ӵ��뽡׳�̶�
		if (MeasureSpec.UNSPECIFIED == widthMode) {
			width = heightSize;
		} else if (MeasureSpec.UNSPECIFIED == heightMode) {
			width = widthSize;
		}		
		setMeasuredDimension(width, width);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		checkerBoardWidth = w;
		unitWidth = checkerBoardWidth*1.0f/CHECKERBOARD_SIZE;
		pieceRadio = (int)(unitWidth*proportion);
		//�������Ӵ�С��
		blackPiece = Bitmap.createScaledBitmap(blackPiece, pieceRadio, pieceRadio, false);
		whitePiece = Bitmap.createScaledBitmap(whitePiece, pieceRadio, pieceRadio, false);
	}
	//��������
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBoard(canvas);
		
		//�������أ������˽̳��еĺ��������ڲ����Լ�����ʱ�����Ĵ���
		drawPiece(canvas, true);
		
		player.start();
		
		//AI����Ӧ����������Ӳ�������ӻ��ƺ����
		aiPlay();
		gameState = aiPlayer.isGmaeOver();
		checkGameState();
	}
	public void checkGameState() {
		if (0 == gameState) {
			return;
		}
		if (!isDialogOver) {//������Ϊʲô���ǵ������Σ������������һ��������ʱ����
			return;
		}
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
		dialog.setTitle("��Ϸ����");
		if (1 == gameState) {
			blackWins++;
			dialog.setMessage("����ʤ!�ܱȷ֣���"+blackWins+":"+whiteWins+"��");
		} else if (2 == gameState){
			whiteWins++;
			dialog.setMessage("����ʤ!�ܱȷ֣���"+blackWins+":"+whiteWins+"��");
		} else if (3 == gameState){
			dialog.setMessage("ƽ��!�ܱȷ֣���"+blackWins+":"+whiteWins+"��");
		}
		dialog.setCancelable(false);
		dialog.setPositiveButton("����һ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				setAIvsAI(false);
				reStart();
				isDialogOver = true;
			}
		});
		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				setAIvsAI(false);
				isDialogOver = true;
				
			}
		});
		isDialogOver = false;
		dialog.show();
	}
	public void aiRePlay() {
		if (AIvsAI){
			if (isBlack){//��ʵһ���Ǻ��ӣ�Ϊ�˰�ȫ������Ǽ��һ��
				Point point = aiPlayer.getAnswer();
				lastPoint = point;
				blackList.add(point);
				aiPlayer.updateWinCount(point, 1);
				isBlack = !isBlack;	
				invalidate();//ˢ�£���û������ǲ��е�
			} else {
				Toast.makeText(getContext(), "�д���", Toast.LENGTH_LONG).show();//����ʹ��,���Բ�ɾ��
			}
			
		}
	}
	public void aiPlay() {
		if (isBlack) {
			return;
		}
		Point point = aiPlayer.getAnswer();
		lastPoint = point;
		whiteList.add(point);
		aiPlayer.updateWinCount(point, 2);
		isBlack = !isBlack;	
		invalidate();//ˢ�£���û������ǲ��е�
	}
	public void drawPiece(Canvas canvas) {
		for (int i = 0; i < whiteList.size(); i++) {
			Point point = whiteList.get(i);
			canvas.drawBitmap(whitePiece, 
					(point.x+(1-proportion)/2)*unitWidth, 
					(point.y+(1-proportion)/2)*unitWidth, null);
		}
		for (int i = 0; i < blackList.size(); i++) {
			Point point = blackList.get(i);
			canvas.drawBitmap(blackPiece, 
					(point.x+(1-proportion)/2)*unitWidth, 
					(point.y+(1-proportion)/2)*unitWidth, null);
		}
	}
	public void drawPiece(Canvas canvas, boolean b) {
		int[][] board = aiPlayer.getBoard();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (1 ==  board[i][j]) {
					canvas.drawBitmap(blackPiece, 
							(float)(j+(1-proportion)/2.0)*unitWidth, 
							(float)(i+(1-proportion)/2.0)*unitWidth, null);
				} else if (2 == board[i][j]) {
					canvas.drawBitmap(whitePiece, 
							(float)(j+(1-proportion)/2.0)*unitWidth, 
							(float)(i+(1-proportion)/2.0)*unitWidth, null);
				}

			}
		}
		
		//Ϊ���ʵ���ԣ�������ӵĵط������
		Paint tempPaint = new Paint();
		tempPaint.setARGB(255, 255 ,0, 0);
		tempPaint.setAntiAlias(true);
		tempPaint.setStrokeWidth(2);
		if (lastPoint != null && isBlack){
			canvas.drawLine((lastPoint.x+(1-proportion)/2)*unitWidth, (lastPoint.y+(1-proportion)/2)*unitWidth,
					(lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio/3, (lastPoint.y+(1-proportion)/2)*unitWidth, tempPaint);
			canvas.drawLine((lastPoint.x+(1-proportion)/2)*unitWidth, (lastPoint.y+(1-proportion)/2)*unitWidth,
					(lastPoint.x+(1-proportion)/2)*unitWidth, (lastPoint.y+(1-proportion)/2)*unitWidth+pieceRadio/3, tempPaint);
			
			canvas.drawLine((lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio, (lastPoint.y+(1-proportion)/2)*unitWidth,
					(lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio-pieceRadio/3, (lastPoint.y+(1-proportion)/2)*unitWidth, tempPaint);
			canvas.drawLine((lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio, (lastPoint.y+(1-proportion)/2)*unitWidth,
					(lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio, (lastPoint.y+(1-proportion)/2)*unitWidth+pieceRadio/3, tempPaint);
			
			canvas.drawLine((lastPoint.x+(1-proportion)/2)*unitWidth, (lastPoint.y+(1-proportion)/2)*unitWidth+pieceRadio,
					(lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio/3, (lastPoint.y+(1-proportion)/2)*unitWidth+pieceRadio, tempPaint);
			canvas.drawLine((lastPoint.x+(1-proportion)/2)*unitWidth, (lastPoint.y+(1-proportion)/2)*unitWidth+pieceRadio,
					(lastPoint.x+(1-proportion)/2)*unitWidth, (lastPoint.y+(1-proportion)/2)*unitWidth-pieceRadio/3+pieceRadio, tempPaint);
			
			canvas.drawLine((lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio, (lastPoint.y+(1-proportion)/2)*unitWidth+pieceRadio,
					(lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio-pieceRadio/3, (lastPoint.y+(1-proportion)/2)*unitWidth+pieceRadio, tempPaint);
			canvas.drawLine((lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio, (lastPoint.y+(1-proportion)/2)*unitWidth+pieceRadio,
					(lastPoint.x+(1-proportion)/2)*unitWidth+pieceRadio, (lastPoint.y+(1-proportion)/2)*unitWidth+pieceRadio-pieceRadio/3, tempPaint);
		}
	    
	}
	//ʹ��һ�Σ���ʼ��ʱ��������
	public void drawBoard(Canvas canvas) {
		//��ȡ����
		int boardW = checkerBoardWidth;
		float unitH = unitWidth;
		for (int i = 0; i < CHECKERBOARD_SIZE; i++) {
			int startX = (int)(unitH/2);
			int endX = (int)(boardW - unitH/2);
			int y = (int)((0.5+i)*unitH);
			canvas.drawLine(startX, y, endX, y, paint);
			canvas.drawLine(y, startX, y, endX, paint);
		}
	}
	private Point getValidPoint(int x, int y) {
		//ע���õ����ĸ�����ϵ
		return new Point((int)(x/unitWidth), (int)(y/unitWidth));
	}
	//�������¼�
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (AIvsAI){
			aiRePlay();//��������ʱ��������������¼�
			return false;
		}
		
		//��Ϸ��������������
		if (0 != gameState) {
			return false;
		}
		int action = event.getAction();
		if (MotionEvent.ACTION_UP == action) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			Point point = getValidPoint(x, y);
			//�жϸ�λ���Ƿ�������
			if (whiteList.contains(point) || blackList.contains(point)) {
				return false;
			}
			if (isBlack) {
				blackList.add(point);
				lastPoint = point;
				aiPlayer.updateWinCount(point, 1);
				isBlack = !isBlack;
				invalidate();
			}
			
			//Ϊ��ΪAI��ע�͵������
//			else {
//				whiteList.add(point);
//				aiPlayer.updateWinCount(point, 2);
//			}
			
			
			
		}
		//���뷵��true�������ܴ�����¼�
		return true;
	}
	
	/*
	 * �����ID��Ȼ���ܴ洢����Ŀǰ��δ�������ѧϰһ�����֪ʶ���ٻ������/
	 * ��ʱ������������Ϊ����
	 */
	//���̴洢
	private static final String INSTANCE = "instance";
	private static final String INSTANCE_GAME_STATE = "instance_game_state";
	private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
	private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		//ϵͳĬ�ϵĲ�Ҫ���Ǵ洢
		bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
		bundle.putInt(INSTANCE_GAME_STATE, gameState);
		bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, whiteList);
		bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, blackList);
		return bundle;
	}
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle)state;
			gameState = bundle.getInt(INSTANCE_GAME_STATE);
			whiteList = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
			blackList = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
			//��һ�䲻������
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
			return;
		}
		super.onRestoreInstanceState(state);
	}
	public void reStart() {
		whiteList.clear();
		blackList.clear();
		gameState = 0;
		lastPoint = null;
		aiPlayer.init();
		invalidate();
	}
}
