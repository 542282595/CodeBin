function Init = InitMatrix(flag, L, random)
% ���ܲ���flag��flagΪ1=0ʱΪ�����ʼ����flagΪ1ʱΪ��ʼ������
if flag==0
    % ��ȡ������ָ���ı���
    % ��ȡ�����С
    Init = rand(L);
    % ���ݱ�������������ֵ��1��ʾ������0��ʾ����
    random = (random-1)/10;%�����ߵı���
    Init(Init<random) = 1;
    Init(Init ~= 1) = 0;
elseif flag==1
    % ��ȡ�����С
    Init = ones(L);
    if mod(L, 2)==1
        Init((L+1)/2, (L+1)/2) = 0;
    elseif mod(L, 2)==0
        Init(L/2, L/2) = 0;
        Init(L/2 + 1, L/2 + 1) = 0;
        Init(L/2 + 1, L/2) = 0;
        Init(L/2, L/2 + 1) = 0;
    end
end