function Init = FixedInit(L, random, FixedRandom)
% ���ܲ���flag��flagΪ1=0ʱΪ�����ʼ����flagΪ1ʱΪ��ʼ������
% ��ȡ������ָ���ı���
% ��ȡ�����С
Init = rand(L);
% ���ݱ�������������ֵ��1��ʾ������0��ʾ����
random = (random-1)/10;%�����ߵı���
Init(Init<random) = 1;
Init(Init ~= 1) = 0;
if FixedRandom== 11
    Init(round(L/2), :) = -1;
    Init(:, round(L/2)) = -1;
elseif FixedRandom==12

else
    for i=1:L
        for j=1:L
           if rand()<=((FixedRandom-1)/100)
              Init(i, j) = -1;% -1��ʾ�ᶨ�ر�����
           end
        end
    end    
end
    
