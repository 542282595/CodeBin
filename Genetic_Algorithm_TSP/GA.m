clc,clear                        % ������������
sj0=load('data.txt');       %����100��Ŀ�������
x=sj0(:,1:2:8); x=x(:);
y=sj0(:,2:2:8); y=y(:);
sj=[x y]; d1=[70,40]; 
sj=[d1;sj;d1]; sj=sj*pi/180;  %��λ���ɻ���
d=zeros(102); %�������d�ĳ�ʼֵ

% ����ֱ�߾���
for i=1:101
    for j=i+1:102
        d(i,j)=6370*acos(cos(sj(i,1)-sj(j,1))*cos(sj(i,2))*cos(sj(j,2))+sin(sj(i,2))*sin(sj(j,2)));
    end
end

d=d+d';

w=50; g=100; %wΪ��Ⱥ�ĸ�����gΪ�����Ĵ���
rand('state',sum(clock)); %��ʼ�������������

for k=1:w  %ͨ������Ȧ�㷨ѡȡ��ʼ��Ⱥ
    c=randperm(100); %����1��...��100��һ��ȫ����  
    c1=[1,c+1,102]; %���ɳ�ʼ��
    for t=1:102 %�ò�ѭ�����޸�Ȧ 
        flag=0; %�޸�Ȧ�˳���־
        for m=1:100
          for n=m+2:101
            if d(c1(m),c1(n))+d(c1(m+1),c1(n+1))<d(c1(m),c1(m+1))+d(c1(n),c1(n+1))
               c1(m+1:n)=c1(n:-1:m+1);  flag=1; %�޸�Ȧ
            end
          end
        end
        if flag==0
          J(k,c1)=1:102; break %��¼�½ϺõĽⲢ�˳���ǰ��ѭ��
        end
   end
end

J(:,1)=0; J=J/102; %����������ת����[0,1]�����ϵ�ʵ������ת����Ⱦɫ�����
for k=1:g  %�ò�ѭ�������Ŵ��㷨�Ĳ��� 
    A=J; %��������Ӵ�B�ĳ�ʼȾɫ��
    c=randperm(w); %�������潻�������Ⱦɫ��� 
    for i=1:2:w  
        F=2+floor(100*rand(1)); %������������ĵ�ַ
        temp=A(c(i),[F:102]); %�м�����ı���ֵ
        A(c(i),[F:102])=A(c(i+1),[F:102]); %�������
        A(c(i+1),F:102)=temp;  
    end
    by=[];  %Ϊ�˷�ֹ��������յ�ַ�������ȳ�ʼ��
    while ~length(by)
        by=find(rand(1,w)<0.1); %������������ĵ�ַ
    end
    B=A(by,:); %������������ĳ�ʼȾɫ��
    for j=1:length(by)
       bw=sort(2+floor(100*rand(1,3)));  %�������������3����ַ
       B(j,:)=B(j,[1:bw(1)-1,bw(2)+1:bw(3),bw(1):bw(2),bw(3)+1:102]); %����λ��
    end
   G=[J;A;B]; %�������Ӵ���Ⱥ����һ��
   [SG,ind1]=sort(G,2); %��Ⱦɫ�巭���1��...,102������ind1
   num=size(G,1); long=zeros(1,num); %·�����ȵĳ�ʼֵ
   for j=1:num
       for i=1:101
           long(j)=long(j)+d(ind1(j,i),ind1(j,i+1)); %����ÿ��·������
       end
   end
     [slong,ind2]=sort(long); %��·�����Ȱ��մ�С��������
     J=G(ind2(1:w),:); %��ѡǰw���϶̵�·����Ӧ��Ⱦɫ��
end

path=ind1(ind2(1),:);
flong=slong(1) ;%���·����·������
xx=sj(path,1);yy=sj(path,2);
plot(xx,yy,'-o') %����·��