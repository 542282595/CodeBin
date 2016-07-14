#include "Prepare.h"
void setPosition(int x, int y) {
	COORD pos;
	HANDLE hOutput;
	pos.X = x;
	pos.Y = y;
	hOutput = GetStdHandle(STD_OUTPUT_HANDLE);
	SetConsoleCursorPosition(hOutput, pos);
}

void hideCursor() {
	CONSOLE_CURSOR_INFO cursor_info = { 1, 0 };
	SetConsoleCursorInfo(GetStdHandle(STD_OUTPUT_HANDLE), &cursor_info);
}

void checkSnake() {
	int x = Snake.front().x;
	int y = Snake.front().y;
	list<SnakeNode>::iterator it = Snake.begin();
	for (it++; it != Snake.end(); it++) {
		if (x == (*it).x && y == (*it).y) {
			setPosition(2, 20);printf("check ERROR 1");
			getchar();
		}
		if ((*it).x > WIDTH || (*it).x < 2 || (*it).y > HEIGHT || (*it).y < 1) {
			setPosition(2, 20);printf("check ERROR 2");
			getchar();
		}
	}
}

void isHeadequalTail() {
	SnakeNode Head = Snake.front();
	SnakeNode Tail = Snake.back();
	if (Head.x == Tail.x && Head.y == Tail.y) {
		setPosition(10, 10);
		printf("isHeadequalTail ERROR");
		for (;true;) {
			getchar();
		}
	}
}

void createFood() {
	int x, y;
	bool flag = true;
	while (flag) {
		x = rand() % (WIDTH / 2);
		x = 2 * (x + 1);
		y = rand() % HEIGHT + 1;
		flag = false;
		for (list<SnakeNode>::iterator it = Snake.begin(); it != Snake.end(); it++) {
			if (x == (*it).x && y == (*it).y) {
				flag = true;
				break;
			}
		}
	}
	Food.x = x;
	Food.y = y;
	setPosition(Food.x, Food.y);
	printf("��");
}

void printSnake() {
	SnakeNode Head = Snake.front();

	if (Head.x == Food.x && Head.y == Food.y) {
		createFood();
		setPosition(WIDTH / 2 + 2, 0);
		printf("%3d", Snake.size());
	}
	else {
		setPosition(theTail.x, theTail.y);
		printf("  ");
	}
	setPosition(Head.x, Head.y);
	printf("��");
}

bool isOpposite(int a, int b) {
	if (0 == a && 1 == b) {
		return true;
	}
	else if (1 == a && 0 == b) {
		return true;
	}
	else if (2 == a && 3 == b) {
		return true;
	}
	else if (3 == a && 2 == b) {
		return true;
	}
	else {
		return false;
	}

}

void createInterface() {
	system("mode con cols=44 lines=24");									//���ÿ���̨
	hideCursor();															//
	for (int i = 0; i <= WIDTH; i += 2) {
		setPosition(i, 0);
		printf("��");
		setPosition(i, HEIGHT + ROWUNIT);
		printf("��");
	}
	for (int i = 0; i < HEIGHT + 2; i++) {
		setPosition(0, i);
		printf("��");
		setPosition(WIDTH + COLUMNUNIT, i);
		printf("��");//
	}
	setPosition(WIDTH / 2 - 2, 21);
	printf("AI Snake");
	SnakeNode temp;
	for (int i = 0; i < 4; i++) {
		temp.x = 2 + i * 2;
		temp.y = 1;
		setPosition(temp.x, temp.y);
		printf("��");
		Snake.push_front(temp);
	}
	SnakeDirection = 2;
	setPosition(WIDTH / 2 - 2, 0);
	printf("Score:");
	setPosition(WIDTH / 2 + 2, 0);
	printf("%3d", Snake.size());
	createFood();
}

void getFoodDistance() {

	
	for (int x = 2; x <= WIDTH; x += COLUMNUNIT) {
		for (int y = 1; y <= HEIGHT; y += ROWUNIT) {
			bfsDistance[x][y] = INF;														//��ʼ��
		}
	}
	for (list<SnakeNode>::iterator it = Snake.begin(); it != Snake.end(); it++) {
		if ((*it).x < 2 || (*it).x > WIDTH || (*it).y < 1 || (*it).y >HEIGHT) {				//����ʹ�ã��쳣���
			setPosition(44, 20);printf("110:%d--d%", (*it).x, (*it).y);
			getchar();
		}
		bfsDistance[(*it).x][(*it).y] = SNAKEBODY;											//�����ó���SNAKEBODY���
	}
	for (int x = 0; x <= WIDTH + COLUMNUNIT; x += COLUMNUNIT) {
		bfsDistance[x][0] = bfsDistance[x][HEIGHT + ROWUNIT] = WALL;
	}
	for (int y = 0; y <= HEIGHT + ROWUNIT; y += ROWUNIT) {
		bfsDistance[0][y] = bfsDistance[WIDTH + COLUMNUNIT][y] = WALL;
	}																						//Χǽ�ó���WALL���
	bfsDistance[Food.x][Food.y] = 0;														//ʳ��λ�ü�Ϊ 0

	/************************************************************************/
	/*����bfsDistanceͨ��BFS����ÿ���ڵ㣬������ʳ��ľ��룬������ǽ��Ϊ�����	*/
	/************************************************************************/
	queue<SnakeNode> Point;
	Point.push(Food);
	SnakeNode temp, adj;
	while (!Point.empty()) {
		temp = Point.front();												
		Point.pop();												
		for (int i = 0; i < 4; i++) {
			adj.x = temp.x + Direction[i][0];
			adj.y = temp.y + Direction[i][1];
			if (INF == bfsDistance[adj.x][adj.y]) {											//INF˵������δ������λ��
				bfsDistance[adj.x][adj.y] = bfsDistance[temp.x][temp.y] + 1;
				Point.push(adj);
			}
		}
	}
	/************************************************************************/
	/* ע:�������н���Ӧ�����ÿ��λ����ʳ��ľ���		                        */
	/************************************************************************/
}

bool isReachable() {
	SnakeNode temp;
	SnakeNode Head = Snake.front();
	for (int i = 0; i < 4; i++) {
		if (isOpposite(i, SnakeDirection)) {									//�෴�����ų�
			continue;
		}
		temp.x = Head.x + Direction[i][0];
		temp.y = Head.y + Direction[i][1];
		if (bfsDistance[temp.x][temp.y] < INF) {								//����Ƿ��пɴﵽ�ķ���
			return true;
		}
	}
	return false;
}

bool isVirtualTailReachable_Regular() {
	int tempDirection = -1, virtualDirection = SnakeDirection;
	/************************************************************************/
	/* Ϊ���жϳ���ʳ����ܷ��ҵ���β������һ��������ǰȥ��ʳ����ж�           */
	/************************************************************************/
	VirtualSnake.clear();															//��ʼ��
	for (list<SnakeNode>::iterator it = Snake.begin(); it != Snake.end(); it++) {
		VirtualSnake.push_back(*(it));
	}																				//��������
	SnakeNode MoveHead = Snake.front();												//��ȡ��ͷ
	SnakeNode adj;																	//������ʱʹ�õ��ھӱ���
	SnakeNode Result;																//��������ѡ����ھ�λ��
	while (MoveHead.x != Food.x || MoveHead.y != Food.y) {							//�����ͷ����ʳ��λ��
		/************************************************************************/
		/* Ѱ����ͷ��������ʳ�������λ��                                          */
		/************************************************************************/
		int MinDistance = INF;
		for (int i = 0; i < 4; i++) {
			if (isOpposite(i, virtualDirection)) {
				continue;
			}
			adj.x = MoveHead.x + Direction[i][0];
			adj.y = MoveHead.y + Direction[i][1];
			if (bfsDistance[adj.x][adj.y] < MinDistance) {
				MinDistance = bfsDistance[adj.x][adj.y];
				Result.x = adj.x;
				Result.y = adj.y;
				tempDirection = i;
			}
		}
		if (MinDistance >= INF) {												//�쳣���
			setPosition(46, 10);
			printf("isVirtualTailReachable_Regular ERROR");
			getchar();
			exit(-101);															//����һ���������
		}
		VirtualSnake.push_front(Result);
		if (Result.x != Food.x || Result.y != Food.y) {							//���û�Ե�ʳ��
			VirtualSnake.pop_back();												//�������ƶ�
		}
		virtualDirection = tempDirection;										//���������ߵķ���
		if (MoveHead.x == Snake.front().x && MoveHead.y == Snake.front().y) {	//��һ����¼�������Ա�ʹ��
			NextStep.x = Result.x;
			NextStep.y = Result.y;
			NextDirection = virtualDirection;
		}																		//�߼���ȷ�Ļ��������ִֻ��һ��
		MoveHead = VirtualSnake.front();										//������ͷλ��
	}
	//VirtualSnake.pop_back();ע����β���ü�ȥ�������߳�������߳���һ
	/************************************************************************/
	/* ���е��ˣ�������Ӧ�ó���ʳ��                                            */
	/************************************************************************/


	/************************************************************************/
	/* ������һ�������ߵ��ƶ��Ƿ���ȷ�������ȷ�����߳���+1                   */
	/************************************************************************/
	if (VirtualSnake.size() != Snake.size()+1) {
		setPosition(46, 11);printf("VirtualSnake != Snake, enter to exit");
		getchar();getchar();
		exit(-1);
	}

	/************************************************************************/
	/* ͨ�������߼���������ͷ�Ƿ��ܵ���������β                                 */
	/*���㷽��ͬ���� BFS����ͷ�ɴ�λ������β�ɴＴ�ܵ���							*/
	/*���ȳ�ʼ��������� bfsJudgeVirtualTail									*/
	/************************************************************************/
	for (int x = 2; x <= WIDTH; x += COLUMNUNIT) {
		for (int y = 1; y <= HEIGHT; y += ROWUNIT) {
			bfsJudgeVirtualTail[x][y] = INF;
		}
	}
	for (list<SnakeNode>::iterator it = VirtualSnake.begin(); it != VirtualSnake.end(); it++) {
		bfsJudgeVirtualTail[(*it).x][(*it).y] = SNAKEBODY;
	}
	for (int x = 0; x <= WIDTH + COLUMNUNIT; x += COLUMNUNIT) {
		bfsJudgeVirtualTail[x][0] = bfsJudgeVirtualTail[x][HEIGHT + ROWUNIT] = WALL;
	}
	for (int y = 0; y <= HEIGHT + ROWUNIT; y += ROWUNIT) {
		bfsJudgeVirtualTail[0][y] = bfsJudgeVirtualTail[WIDTH + COLUMNUNIT][y] = WALL;
	}
	SnakeNode VirtualSnakeTail = VirtualSnake.back();
	bfsJudgeVirtualTail[VirtualSnakeTail.x][VirtualSnakeTail.y] = 0;					//��β�ǳ����㣬���Ϊ0
	/************************************************************************/
	/* ���ö��� BFS �����������β�������پ���                                 */
	/************************************************************************/
	queue<SnakeNode> Point;
	Point.push(VirtualSnakeTail);
	SnakeNode temp;
	while (!Point.empty()) {
		temp = Point.front();												//ȡ����
		Point.pop();
		for (int i = 0; i < 4; i++) {
			adj.x = temp.x + Direction[i][0];
			adj.y = temp.y + Direction[i][1];
			if (INF == bfsJudgeVirtualTail[adj.x][adj.y]) {							//δ������
				bfsJudgeVirtualTail[adj.x][adj.y] = bfsJudgeVirtualTail[temp.x][temp.y] + 1;
				Point.push(adj);
			}
		}
	}
	/************************************************************************/
	/*���е��˴�Ӧ����ȷ���������β��ÿ��λ�õľ���	                            */
	/************************************************************************/
	bfsJudgeVirtualTail[VirtualSnakeTail.x][VirtualSnakeTail.y] = SNAKEBODY;//�����˾�������ͷ�Ե���β�Ŀ���

	/************************************************************************/
	/*�����ж�������ͷ�Ƿ�ɴ�                                                */
	/************************************************************************/
	int TailDistance = INF;
	SnakeNode VirtualSnakeHead = VirtualSnake.front();
	for (int i = 0; i < 4; i++) {
		adj.x = VirtualSnakeHead.x + Direction[i][0];
		adj.y = VirtualSnakeHead.y + Direction[i][1];
		if (bfsJudgeVirtualTail[adj.x][adj.y] < INF) {
			return true;
		}
	}
	return false;
}

bool isVirtualTailReachable_Random() {
	int tempDirection = -1, virtualDirection = SnakeDirection;
	/************************************************************************/
	/* Ϊ���жϳ���ʳ����ܷ��ҵ���β������һ��������ǰȥ��ʳ����ж�           */
	/************************************************************************/
	VirtualSnake.clear();															//��ʼ��
	for (list<SnakeNode>::iterator it = Snake.begin(); it != Snake.end(); it++) {
		VirtualSnake.push_back(*(it));
	}																				//��������
	SnakeNode MoveHead = Snake.front();												//��ȡ��ͷ
	SnakeNode adj;																	//������ʱʹ�õ��ھӱ���
	SnakeNode Result;																//��������ѡ����ھ�λ��
	while (MoveHead.x != Food.x || MoveHead.y != Food.y) {							//�����ͷ����ʳ��λ��
		/************************************************************************/
		/* Ѱ����ͷ��������ʳ�������λ��                                          */
		/************************************************************************/
		int MinDistance = INF;
		int random_i = rand() % 4;
		for (int t = 0; t < 4; random_i = (random_i + 1) % 4,t++) {
			if (isOpposite(random_i, virtualDirection)) {
				continue;
			}
			adj.x = MoveHead.x + Direction[random_i][0];
			adj.y = MoveHead.y + Direction[random_i][1];
			if (bfsDistance[adj.x][adj.y] < MinDistance) {
				MinDistance = bfsDistance[adj.x][adj.y];
				Result.x = adj.x;
				Result.y = adj.y;
				tempDirection = random_i;
			}
		}
		if (MinDistance >= INF) {												//�쳣���
			setPosition(46, 10);
			printf("isVirtualTailReachable_Regular ERROR");
			getchar();
			exit(-1);
		}
		VirtualSnake.push_front(Result);
		if (Result.x != Food.x || Result.y != Food.y) {							//���û�Ե�ʳ��
			VirtualSnake.pop_back();												//�������ƶ�
		}
		virtualDirection = tempDirection;										//���������ߵķ���
		if (MoveHead.x == Snake.front().x && MoveHead.y == Snake.front().y) {	//��һ����¼�������Ա�ʹ��
			NextStep.x = Result.x;
			NextStep.y = Result.y;
			NextDirection = virtualDirection;
		}																		//�߼���ȷ�Ļ��������ִֻ��һ��
		MoveHead = VirtualSnake.front();										//������ͷλ��
	}
	/************************************************************************/
	/* ���е��ˣ�������Ӧ�ó���ʳ��                                            */
	/************************************************************************/

	/************************************************************************/
	/* ������һ�������ߵ��ƶ��Ƿ���ȷ�������ȷ�����߳���+1                    */
	/************************************************************************/
	if (VirtualSnake.size() != Snake.size()+1) {
		setPosition(46, 11);printf("VirtualSnake != Snake, enter to exit");
		getchar();getchar();
		exit(-1);
	}

	/************************************************************************/
	/* ͨ�������߼���������ͷ�Ƿ��ܵ���������β                                 */
	/*���㷽��ͬ���� BFS����ͷ�ɴ�λ������β�ɴＴ�ܵ���							*/
	/*���ȳ�ʼ��������� bfsJudgeVirtualTail									*/
	/************************************************************************/
	for (int x = 2; x <= WIDTH; x += COLUMNUNIT) {
		for (int y = 1; y <= HEIGHT; y += ROWUNIT) {
			bfsJudgeVirtualTail[x][y] = INF;
		}
	}
	for (list<SnakeNode>::iterator it = VirtualSnake.begin(); it != VirtualSnake.end(); it++) {
		bfsJudgeVirtualTail[(*it).x][(*it).y] = SNAKEBODY;
	}
	for (int x = 0; x <= WIDTH + COLUMNUNIT; x += COLUMNUNIT) {
		bfsJudgeVirtualTail[x][0] = bfsJudgeVirtualTail[x][HEIGHT + ROWUNIT] = WALL;
	}
	for (int y = 0; y <= HEIGHT + ROWUNIT; y += ROWUNIT) {
		bfsJudgeVirtualTail[0][y] = bfsJudgeVirtualTail[WIDTH + COLUMNUNIT][y] = WALL;
	}
	SnakeNode VirtualSnakeTail = VirtualSnake.back();
	bfsJudgeVirtualTail[VirtualSnakeTail.x][VirtualSnakeTail.y] = 0;					//��β�ǳ����㣬���Ϊ0
	/************************************************************************/
	/* ���ö��� BFS �����������β�������پ���                                 */
	/************************************************************************/
	queue<SnakeNode> Point;
	Point.push(VirtualSnakeTail);
	SnakeNode temp;
	while (!Point.empty()) {
		temp = Point.front();												//ȡ����
		Point.pop();
		for (int i = 0; i < 4; i++) {
			adj.x = temp.x + Direction[i][0];
			adj.y = temp.y + Direction[i][1];
			if (INF == bfsJudgeVirtualTail[adj.x][adj.y]) {							//δ������
				bfsJudgeVirtualTail[adj.x][adj.y] = bfsJudgeVirtualTail[temp.x][temp.y] + 1;
				Point.push(adj);
			}
		}
	}
	/************************************************************************/
	/*���е��˴�Ӧ����ȷ���������β��ÿ��λ�õľ���                             */
	/************************************************************************/
	bfsJudgeVirtualTail[VirtualSnakeTail.x][VirtualSnakeTail.y] = SNAKEBODY;			//�����˾�������ͷ�Ե���β�Ŀ���

	/************************************************************************/
	/*�����ж�������ͷ�Ƿ�ɴ�                                                */
	/************************************************************************/
	int TailDistance = INF;
	SnakeNode VirtualSnakeHead = VirtualSnake.front();
	for (int i = 0; i < 4; i++) {
		adj.x = VirtualSnakeHead.x + Direction[i][0];
		adj.y = VirtualSnakeHead.y + Direction[i][1];
		if (bfsJudgeVirtualTail[adj.x][adj.y] < INF) {
			return true;
		}
	}
	return false;

}

void eatFoodMove() {
	Snake.push_front(NextStep);													//ɾ��β�͵Ĳ����ڴ�ӡʱ����
	SnakeDirection = NextDirection;												//ע��Ҫ���·���
	theTail = Snake.back();
	if (NextStep.x != Food.x || NextStep.y != Food.y) {
		Snake.pop_back();														//û�Ե�ʳ����β��ɾ��
	}
}

bool eatFood() {
	getFoodDistance();
	if (isReachable()) {
		if (isVirtualTailReachable_Regular() || isVirtualTailReachable_Random()) {//ע���������˶�·����
			eatFoodMove();
			return true;
		} else {
			return false;
		}
	}
	return false;																   //������ɴ�
}

bool isSafe(SnakeNode NewHead) {
	VirtualSnake.clear();
	for (list<SnakeNode>::iterator it = Snake.begin(); it != Snake.end(); it++) {
		VirtualSnake.push_back(*(it));
	}																			//������ʵ��
	VirtualSnake.push_front(NewHead);
	VirtualSnake.pop_back();

	for (int x = 2; x <= WIDTH; x += COLUMNUNIT) {								//����������ÿ��λ����������β�ľ���
		for (int y = 1; y <= HEIGHT; y += ROWUNIT) {
			bfsJudgeVirtualTail[x][y] = INF;
		}
	}
	for (list<SnakeNode>::iterator it = VirtualSnake.begin(); it != VirtualSnake.end(); it++) {
		bfsJudgeVirtualTail[(*it).x][(*it).y] = SNAKEBODY;
	}
	for (int x = 0; x <= WIDTH + COLUMNUNIT; x += COLUMNUNIT) {
		bfsJudgeVirtualTail[x][0] = bfsJudgeVirtualTail[x][HEIGHT + ROWUNIT] = WALL;
	}
	for (int y = 0; y <= HEIGHT + ROWUNIT; y += ROWUNIT) {
		bfsJudgeVirtualTail[0][y] = bfsJudgeVirtualTail[WIDTH + COLUMNUNIT][y] = WALL;
	}
	SnakeNode VirtualSnakeTail = VirtualSnake.back();
	bfsJudgeVirtualTail[VirtualSnakeTail.x][VirtualSnakeTail.y] = 0;
	queue<SnakeNode> Point;
	Point.push(VirtualSnakeTail);												//���ö��� BFS �����������β�������پ���
	SnakeNode temp, adj;
	while (!Point.empty()) {
		temp = Point.front();													//ȡ����
		Point.pop();
		for (int i = 0; i < 4; i++) {
			adj.x = temp.x + Direction[i][0];
			adj.y = temp.y + Direction[i][1];
			if (INF == bfsJudgeVirtualTail[adj.x][adj.y]) {						//δ������
				bfsJudgeVirtualTail[adj.x][adj.y] = bfsJudgeVirtualTail[temp.x][temp.y] + 1;
				Point.push(adj);
			}
		}
	}
	bfsJudgeVirtualTail[VirtualSnakeTail.x][VirtualSnakeTail.y] = SNAKEBODY;	//����

	SnakeNode VirtualSnakeHead = VirtualSnake.front();
	if (VirtualSnakeHead.x != NewHead.x || VirtualSnakeHead.y != NewHead.y) {
		setPosition(46, 16);printf("VirtualSnakeHead != NewHead");
		getchar();
		exit(-105);																//�쳣���������ʹ��
	}
	for (int i = 0; i < 4; i++) {
		adj.x = VirtualSnakeHead.x + Direction[i][0];
		adj.y = VirtualSnakeHead.y + Direction[i][1];
		if (bfsJudgeVirtualTail[adj.x][adj.y] < INF) {
			return true;
		}
	}
	return false;
}

void followTailMove() {

	Snake.push_front(NextStep);//ɾ��β�͵Ĳ����ڴ�ӡʱ����
	SnakeDirection = NextDirection;//ע��Ҫ���·���
	theTail = Snake.back();
	if (NextStep.x != Food.x || NextStep.y != Food.y) {
		Snake.pop_back();
	}
}

int FoodDistance(SnakeNode adj) {
	if (bfsDistance[adj.x][adj.y] < INF) {										//��οɴ�����������
		return bfsDistance[adj.x][adj.y];								
	} else {
		return abs(adj.x - Food.x) + abs(adj.y - Food.y);						//���ɴ���������پ���
	}
}

bool followTail() {
	/************************************************************************/
	/* �������ȥ��ʳ���ִ�з�������Զ��ʳ����Ǳ��뱣֤�ƶ������ҵ���      */
	/************************************************************************/
	for (int x = 2; x <= WIDTH; x += COLUMNUNIT) {
		for (int y = 1; y <= HEIGHT; y += ROWUNIT) {
			bfsJudgeRealTail[x][y] = INF;
		}
	}
	for (list<SnakeNode>::iterator it = Snake.begin(); it != Snake.end(); it++) {
		bfsJudgeRealTail[(*it).x][(*it).y] = SNAKEBODY;
	}
	for (int x = 0; x <= WIDTH + COLUMNUNIT; x += COLUMNUNIT) {
		bfsJudgeRealTail[x][0] = bfsJudgeRealTail[x][HEIGHT + ROWUNIT] = WALL;
	}
	for (int y = 0; y <= HEIGHT + ROWUNIT; y += ROWUNIT) {
		bfsJudgeRealTail[0][y] = bfsJudgeRealTail[WIDTH + COLUMNUNIT][y] = WALL;
	}

	SnakeNode RealSnakeTail = Snake.back();
	bfsJudgeRealTail[RealSnakeTail.x][RealSnakeTail.y] = 0;
	//���ö��� BFS �������β�������پ���
	queue<SnakeNode> Point;
	Point.push(RealSnakeTail);
	SnakeNode temp, adj;
	while (!Point.empty()) {
		temp = Point.front();												//ȡ����
		Point.pop();
		for (int i = 0; i < 4; i++) {
			adj.x = temp.x + Direction[i][0];
			adj.y = temp.y + Direction[i][1];
			if (INF == bfsJudgeRealTail[adj.x][adj.y]) {					//δ������
				bfsJudgeRealTail[adj.x][adj.y] = bfsJudgeRealTail[temp.x][temp.y] + 1;
				Point.push(adj);
			}
		}
	}
	//���е�����Ӧ�ü��������β������λ�õľ��룬�����ж���ͷ�Ƿ���Ե�����β
	//������Ҫע����ǣ���βλ��Ҫ����Ϊ SNAKEBODY��������ͷ������β�غ�
	bfsJudgeRealTail[RealSnakeTail.x][RealSnakeTail.y] = SNAKEBODY;
	/************************************************************************/
	/* ���ж���β�Ƿ�ɴ�			                                            */
	/************************************************************************/
	SnakeNode tempAdj;
	SnakeNode tempHead = Snake.front();
	bool tempReachable = false;
	for (int i = 0; i < 4; i++) {
		tempAdj.x = tempHead.x + Direction[i][0];
		tempAdj.y = tempHead.y + Direction[i][1];
		if (bfsJudgeRealTail[tempAdj.x][tempAdj.y] < INF) {
			tempReachable = true;
			break;
		}
	}
	/************************************************************************/
	/*����������ƶ�һ����Ӧ�����ƶ�֮�������ҵ���β                            */
	/*�ƶ���ʳ�ﾡ����Զ�룬���ڳ��ռ�ȴ�ʳ����Ե���                          */
	/************************************************************************/
	int SafeDirection[4];
	int count = 0;
	SnakeNode RealHead = Snake.front();
	for (int i = 0; i < 4; i++) {											//�ҳ������ƶ��ķ���
		if (isOpposite(i, SnakeDirection)) {
			continue;
		}
		adj.x = RealHead.x + Direction[i][0];
		adj.y = RealHead.y + Direction[i][1];
		if (bfsJudgeRealTail[adj.x][adj.y] < INF) {
			if (isSafe(adj)) {
				SafeDirection[count] = i;
				count++;
			}
		}
	}

	int MaxDistance = -1;
	SnakeNode Result;
	if (0 == count) {														//���û�а�ȫ�ķ���
		return false;
	}
	else {
		for (int k = 0; k < count; k++) {									//�ҳ���Զ�ķ���
			adj.x = RealHead.x + Direction[SafeDirection[k]][0];
			adj.y = RealHead.y + Direction[SafeDirection[k]][1];
			int tempDistance = FoodDistance(adj);
			if (tempDistance > MaxDistance) {
				MaxDistance = tempDistance;
				Result.x = adj.x;
				Result.y = adj.y;
				NextDirection = SafeDirection[k];
			}
		}
		NextStep.x = Result.x;
		NextStep.y = Result.y;
		followTailMove();
		checkSnake();														//���
		isHeadequalTail();
		return true;
	}
	return false;
}

void DFS(int x, int y)
{
	if (WALL == dfsDistance[x][y]) {
		return;
	}
	dfsDistance[x][y] = WALL;											//��һ��Ҫ�������
	if (currentDepth > deepest) {
		deepest = currentDepth;
	}
	currentDepth++;
	SnakeNode Temp;
	for (int i = 0; i < 4; i++) {
		Temp.x = x + Direction[i][0];
		Temp.y = y + Direction[i][1];
		if (Temp.x < 2 || Temp.x > WIDTH || Temp.y < 1 || Temp.y > HEIGHT) {
			continue;
		}
		if (WALL == dfsDistance[Temp.x][Temp.y]) {
			continue;
		}
		DFS(Temp.x, Temp.y);
	}
	currentDepth--;
}

int getDepth(SnakeNode temp) {
	for (int x = 2; x <= WIDTH; x += COLUMNUNIT) {						//��ʼ��;Ϊ�˲����ڵݹ鵼��ʼ�պ�磬���Ա��ز�����ݹ�
		for (int y = 1; y <= HEIGHT; y += ROWUNIT) {
			dfsDistance[x][y] = 0;
		}
	}
	//���������߽�
	for (int x = 0; x <= WIDTH + COLUMNUNIT; x += COLUMNUNIT) {
		dfsDistance[x][0] = dfsDistance[x][HEIGHT + ROWUNIT] = WALL;
	}
	for (int y = 1; y <= HEIGHT + ROWUNIT; y += ROWUNIT) {
		dfsDistance[0][y] = dfsDistance[WIDTH + COLUMNUNIT][y] = WALL;
	}

	for (list<SnakeNode>::iterator it = Snake.begin(); it != Snake.end(); it++) {
		if ((*it).x < 2 || (*it).x> WIDTH || (*it).y > HEIGHT || (*it).y < 1) {		//����ʹ��
			setPosition(44, 4);printf("iterator Error\t%d\t%d", (*it).x, (*it).y);
			getchar();
			exit(-1);
		}
		dfsDistance[(*it).x][(*it).y] = WALL;
	}

	currentDepth = 0, deepest = -1;											//��ʼ��
	DFS(temp.x, temp.y);													//�ݹ�������ȱ���
	return deepest;
}

void snakeWander() {
	SnakeNode NextHead;														//Ϊ�������������о����ܼ�֣����� DFS 
	SnakeNode temp;
	SnakeNode CurrentHead = Snake.front();

	for (int x = 4; x <= WIDTH - COLUMNUNIT; x += COLUMNUNIT) {				//�������󣬱�ǲ����ƶ����ĵ�
		for (int y = 2; y <= HEIGHT - ROWUNIT; y += ROWUNIT) {
			Mark[x][y] = INF;
		}
	}																		//���Լ�ǽ��λ����������
	for (list<SnakeNode>::iterator it = VirtualSnake.begin(); it != VirtualSnake.end(); it++) {
		Mark[(*it).x][(*it).y] = SNAKEBODY;
	}
	for (int x = 0; x <= WIDTH + COLUMNUNIT; x += COLUMNUNIT) {
		Mark[x][0] = Mark[x][HEIGHT + ROWUNIT] = WALL;
		Mark[x][1] = Mark[x][HEIGHT] = WALL;
	}
	for (int y = 0; y <= HEIGHT + ROWUNIT; y += ROWUNIT) {
		Mark[0][y] = Mark[WIDTH + COLUMNUNIT][y] = WALL;
		Mark[2][y] = Mark[WIDTH][y] = WALL;
	}

	int MaxDepth = -100;
	int tempDirection = -1;
	for (int i = 0; i < 4; i++) {
		if (isOpposite(i, SnakeDirection)){
			continue;
		}
		temp.x = CurrentHead.x + Direction[i][0];
		temp.y = CurrentHead.y + Direction[i][1];
		if (INF == Mark[temp.x][temp.y]) {									//�ж��Ƿ����Ҫ��
			int depth = getDepth(temp);
			if (depth > MaxDepth) {
				MaxDepth = depth;
				tempDirection = i;
				NextHead.x = temp.x;
				NextHead.y = temp.y;
			}
		}
	}
	if (-1 == tempDirection) {
		getchar();
		exit(-3);
	} else {
		SnakeDirection = tempDirection;
		Snake.push_front(NextHead);
		theTail = Snake.back();
		if (NextStep.x != Food.x || NextStep.y != Food.y) {
			Snake.pop_back();
		}
	}
}

void snakeMove() {
	if (!eatFood()) {														//����״̬1��ȥ��ʳ��
		if (!followTail()) {												//����״̬2��׷β��
			snakeWander();													//���ѡ��
		}
	}
}
