#include "Function.h"
int main(void) {
	srand((unsigned int)time(NULL));

	createInterface();						//������Ϸ����
	getchar();								//�س���ʼ
	while (Snake.size() < WIDTH*HEIGHT) {	//δռ��ȫ��
		snakeMove();						//�ƶ�һ��
		printSnake();						//��ӡ�µ���
		Sleep(20);							//�����ƶ��ٶ�
		//getchar();
		checkSnake();						//����ƶ��Ƿ�Ϸ�����Ҫ����ʹ��
	}
	getchar();								//�س�����
	return 0;
}