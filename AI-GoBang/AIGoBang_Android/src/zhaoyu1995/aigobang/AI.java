package zhaoyu1995.aigobang;

import android.graphics.Point;

public class AI {
	boolean wins[][][] = new boolean[15][15][600];//Ӯ�����飬ͳ�����п��ܵĻ�ʤ����
	private int winCount;//ͳ�ƻ�ʤ�������������Լ���õ���572��
	private int board[][] = new int[15][15];//��¼���̾��ƵĶ�ά���飬0�������ӣ�1�������壬 2������
	//AI����ͳ����Һ��Լ���ÿһ��Ӯ���ϵĻ��ۣ��ﵽ�����ʤ
	private int playerWin[] = new int[600];
	private int aiWin[] = new int[600];
	private int gameType;//0δ������1����ʤ��2����ʤ 
	
	public AI () {
		init();
	}
	
	public void init(){
		winCount = 0;
		gameType = 0;
		//ͳ�����еĻ�ʤ����
		//��
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 11; j++) {
				for (int k = 0; k < 5; k++) {
					wins[i][j+k][winCount] = true;
				}
				winCount++;
			}
		}
		//��
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 15; j++) {
				for (int k = 0; k < 5; k++) {
					wins[i+k][j][winCount] = true;
				}
				winCount++;
			}
		}
		//б
		for (int i = 0; i < 11; i++){
			for (int j = 0; j < 11; j++){
				for (int k = 0; k < 5; k++){
					wins[i+k][j+k][winCount] = true;
				}
				winCount++;
			}
		}
		for (int i = 0; i < 11; i++){
			for (int j = 14; j > 3; j--){
				for (int k = 0; k < 5; k++){
					wins[i+k][j-k][winCount] = true;
				}
				winCount++;
			}
		}
//		//��ʼʱ��˫����û�л���
//		Arrays.fill(aiWin, 0);
//		Arrays.fill(playerWin, 0);
//		//����û������
//		Arrays.fill(board, 0);	
		for (int k = 0; k < winCount; k++) {
			aiWin[k] = playerWin[k] = 0;
		}
		for (int i = 0; i < 15; i++){
			for (int j = 0; j < 15; j++){
				board[i][j] = 0;
			}
		}			
	}

	//CheckerBoardû��ʵ��������λ�ȡ�����ݣ������Ժ�ѧϰ������������⣬���ݵĴ���
		
	//�����ռ�����Ϣ����
	public Point getAnswer() {
		int x = 0;
		int y = 0;
		int max = -1;
		//ͳ����Һ�AI��ÿ��λ�õĵ÷֣���ʼΪ0
		int playerScore[][] = new int[15][15];
		int aiScore[][] = new int[15][15];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				playerScore[i][j] = 0;
				aiScore[i][j] = 0;
			}
		}
		
		//Ϊÿ��λ�ô��
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (0 == board[i][j]) {//����ǿ�λ
					for (int k = 0; k < winCount; k++) {
						//�鿴ÿ��Ӯ����ɵĳ̶�
						if (wins[i][j][k]) {
							//�����λ�öԵ�k��Ӯ���а���
							if (1 == playerWin[k]) {
								playerScore[i][j]+=200;
							} else if (2 == playerWin[k]) {
								playerScore[i][j]+=400;
							} else if (3 == playerWin[k]) {
								playerScore[i][j]+=2000;
							} else if (4 == playerWin[k]) {
								playerScore[i][j]+=10000;
							}
							
							if (1 == aiWin[k]) {
								aiScore[i][j]+=320;
							} else if (2 == aiWin[k]) {
								aiScore[i][j]+=420;
							} else if (3 == aiWin[k]) {
								aiScore[i][j]+=4200;
							} else if (4 == aiWin[k]) {
								aiScore[i][j]+=20000;
							}
						}
					}
				}//if
				//Ѱ����Ӧ�����ӵ�λ��
				if (playerScore[i][j] > max) {
					max = playerScore[i][j];
					x = j;
					y = i;//ע������ϵ
				} else if (playerScore[i][j] == max) {
					//������©
					if (aiScore[i][j] > aiScore[y][x]) {
						x = j;
						y = i;
					}
				}//if
				if (aiScore[i][j] > max) {
					max = aiScore[i][j];
					x = j;
					y = i;
				} else if (aiScore[i][j] == max) {
					if (playerScore[i][j] > playerScore[y][x]) {
						x = j;
						y = i;
					}
				}//if
				
			}
		}//for
		//��������������������ӵ�
		
		if (100 > max) {
			x = (int)(Math.random()*15);
			y = (int)(Math.random()*15);
		}
		return new Point(x, y);
	}
	//��ȡ����λ�ã����������ͣ�����Ӯ��ͳ������
	
	public void updateWinCount(Point point, int type) {
		//����board
		board[point.y][point.x] = type;
		
		if (1 == type) {
			//����
			for (int k = 0; k < winCount; k++) {
				if (wins[point.y][point.x][k]) {
					//��λ�öԵ�k��Ӯ���й���
					playerWin[k]++;
					aiWin[k] = -1;//���ֲ�����ͨ�����������ʤ����Ϊ�쳣
					if (5 == playerWin[k]) {
						gameType = 1;
						return;
					}
				}
			}
		} else if (2 == type) {
			//����
			for (int k = 0; k < winCount; k++) {
				if (wins[point.y][point.x][k]) {
					aiWin[k]++;
					playerWin[k] = -1;
					if (5 == aiWin[k]) {
						gameType = 2;
						return;
					}
				}
			}
		}
	}
	//����AI��board���ʤ��, 0:δ������1������ʤ����2������ʤ��3��ƽ��
	public int isGmaeOver() {
		//����Ƿ�ƽ��
		if (0 == gameType) {
			gameType = 3;
			ALL:
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					if (0 == board[i][j]) {
						gameType = 0;
						break ALL;
					}
				}
			}
		}
		return this.gameType;
	}
	public int[][] getBoard() {
		return board;
	}
}
