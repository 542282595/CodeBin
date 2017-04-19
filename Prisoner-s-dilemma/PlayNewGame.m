% ��ȡ���༭���Լ���ť����ֵ
T = get(handles.edit1, 'String');
R = get(handles.edit2, 'String');
P = get(handles.edit3, 'String');
S = get(handles.edit4, 'String');
L = get(handles.edit5, 'String');
TimeStep = get(handles.edit6, 'String');
T = str2double(T);
R = str2double(R);
P = str2double(P);
S = str2double(S);
L = str2num(L);
TimeStep = str2num(TimeStep);

var = get(handles.popupmenu2, 'Value');
fixedrandom = get(handles.Fixed, 'Value');
CurrentPlayers = FixedInit(L, var, fixedrandom);

%TempPlayers�ǲ���ʱ��ʱ�洢��ǰ��ֵľ���
TempPlayers = CurrentPlayers;

%�洢���Ըı���Ϣ�ľ���
Changes = zeros(L);

% Payoff��¼��ǰ�����ÿ���㲩������õ������棨��8���ھӼ��Լ���
%ÿ������������ʼ��Ϊ0
SumFixed = sum(sum(CurrentPlayers==-1));
Payoff = zeros(L);
for t = 1:TimeStep
    % ������ôͳ����һ������
    %
    Frequency = sum(sum(CurrentPlayers==1))/(L*L - SumFixed);
    Frequency = roundn(Frequency, -3);
    set(handles.Output, 'String', Frequency);
    axes(handles.axes2);
    axis([0, TimeStep, 0, 1]);
    plot(t, Frequency, '.');
    hold on
    % ͬʱ����
    %ԭ����ʹ�÷����ڱ߽�
    
    % û��ʼһ���µĲ��ģ���һ�ֵ�������Ҫ����ġ�
    % �������ǣ�������������
    Payoff = zeros(L);
    for i = 1:L
        for j = 1:L
           for m = -1:1
              for n = -1:1%ע��T\R\P\S��Ӧ�����
                  % �Լ������Լ�����
                  I = i + m;
                  J = j + n;
                  %�����ڱ߽�ı��ʵ�֣����������������
                  
                  % 0���ѣ� 1������ -1�ᶨ����
                  if I>0 && I<=L && J>0 && J<=L                                        
                      if CurrentPlayers(i, j)==1 && CurrentPlayers(I, J)==1
                          Payoff(i, j) = Payoff(i, j) + R;
                      elseif CurrentPlayers(i, j)==1 && (CurrentPlayers(I, J)==0 || CurrentPlayers(I, J)==-1)
                          Payoff(i, j) = Payoff(i, j) + S;
                      elseif (CurrentPlayers(i, j)==0 || CurrentPlayers(i, j)==-1) && CurrentPlayers(I, J)==1
                          Payoff(i, j) = Payoff(i, j) + T;
                      elseif (CurrentPlayers(i, j)==0 || CurrentPlayers(i, j)==-1) && (CurrentPlayers(I, J)==0 || CurrentPlayers(I, J)==-1)
                          Payoff(i, j) = Payoff(i, j) + P;
                      end
                  end
              end
           end
        end
    end
    %��ô��ѭ���������Ծ�������
    for i = 1:L
        for j = 1:L
            % ����ǹ̶��������򲻽���ѡ��ֱ������
            if CurrentPlayers(i, j)== -1
                continue;
            end
            TempPayoff = Payoff(i, j);
           for m = -1:1
              for n = -1:1
                  % �Լ�û��Ҫ���Լ�����ѡ�����������
                  if m~=0 || n~=0
                      I = i + m;
                      J = j + n;

                      %�����ڱ߽�ı��ʵ�֣����������������


                      % ���´�����һ������
                      % ʵ�ʲ������˲���һ��һ������
                      % �������һ��ì����
                      % ���Բ���������
                      % �������һ�� ���Լ� ����� ���Լ��裺
                      % �������������ͬʱ���˸�������ѡ�������
                      % ��Ȼ�ˣ�Ҳ���Գ��Ը������ڱ��ѣ���Ϊ���ѿ��Ա�֤�Լ���������

                     if I>0 && I<=L && J>0 && J<=L
                          if Payoff(I, J) > TempPayoff
                              if CurrentPlayers(I, J)==-1
                                TempPlayers(i, j) = 0;
                              else
                                TempPlayers(i, j) = CurrentPlayers(I, J);
                              end
                              % ��һ��Ҳ�ǳ�������©
                              %����ע�⣬����ֱ�ӶԸ�λ�õ����渳ֵ����Ϊ�仹Ҫ�������Ƚ�
                              TempPayoff = Payoff(I, J);
                              % �������������ͬʱ����ѡ�����ı��ѻ������ĺ�������
                          elseif Payoff(I, J) == TempPayoff && (CurrentPlayers(I, J)==0 || CurrentPlayers(I, J)==-1)
                              TempPlayers(i, j) = 0;
                          end
                      end
                  end
              end
           end
        end
    end
    Map = [0 0 1;1 1 0;0 1 0;1 0 0];
    %�����Ըı�ͼ
    for i = 1:L
        for j = 1:L
            if CurrentPlayers(i, j) == 1 && TempPlayers(i, j) == 1
                Changes(i, j) = 1;
            elseif CurrentPlayers(i, j) == 1 && TempPlayers(i, j) == 0
                Changes(i, j) = 2;
            elseif CurrentPlayers(i, j) == 0 && TempPlayers(i, j) == 1
                Changes(i, j) = 3;
            elseif CurrentPlayers(i, j) == 0 && TempPlayers(i, j) == 0
                Changes(i, j) = 4;
            elseif CurrentPlayers(i, j) == -1 && TempPlayers(i, j) == -1
                Changes(i, j) = 5;
                Map = [0 0 1;1 1 0;0 1 0;1 0 0;0 0 0];
            end
        end
    end
    %ָ����ɫ
    axes(handles.axes1);
    colormap(Map);
    imagesc(Changes);
    drawnow
    
    
    %ͬ��
    CurrentPlayers = TempPlayers;
  
    

    
    % ��������ٶ�
    % �������Ҳ���Լӵ�GUI�ϣ����ǵ��ռ䲻���ã�
    % �ٶ�Ҳû������Ҫ�󣬿�ֱ����Դ�������趨
    pause(0);
end



