% ��ȡ���༭���Լ���ť����ֵ
var = get(handles.popupmenu2, 'Value');
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

% ��ȡ��ʼʱ�̵�ģʽ��������Ǽ��ζԳ�
if get(handles.Regular, 'Value');
    flag = 1;
elseif get(handles.Random, 'Value')
    flag = 0;
end

%��ȡ��ʼ���
%CurrrentPayers�ǵ�ǰ���
CurrentPlayers = InitMatrix(flag, L, var);

%TempPlayers�ǲ���ʱ��ʱ�洢��ǰ��ֵľ���
TempPlayers = CurrentPlayers;

%�洢���Ըı���Ϣ�ľ���
Changes = zeros(L);

% Payoff��¼��ǰ�����ÿ���㲩������õ������棨��8���ھӼ��Լ���
%ÿ������������ʼ��Ϊ0

Payoff = zeros(L);
for t = 1:TimeStep
    Frequency = sum(sum(CurrentPlayers))/(L*L);
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
                  if I>0 && I<=L && J>0 && J<=L                                        
                      if CurrentPlayers(i, j)==1 && CurrentPlayers(I, J)==1
                          Payoff(i, j) = Payoff(i, j) + R;
                      elseif CurrentPlayers(i, j)==1 && CurrentPlayers(I, J)==0
                          Payoff(i, j) = Payoff(i, j) + S;
                      elseif CurrentPlayers(i, j)==0 && CurrentPlayers(I, J)==1
                          Payoff(i, j) = Payoff(i, j) + T;
                      elseif CurrentPlayers(i, j)==0 && CurrentPlayers(I, J)==0
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
                              TempPlayers(i, j) = CurrentPlayers(I, J);
                              % ��һ��Ҳ�ǳ�������©
                              %����ע�⣬����ֱ�ӶԸ�λ�õ����渳ֵ����Ϊ�仹Ҫ�������Ƚ�
                              TempPayoff = Payoff(I, J);
                              % �������������ͬʱ����ѡ�����ı��ѻ������ĺ�������
                          elseif Payoff(I, J) == TempPayoff && CurrentPlayers(I, J)==0
                              TempPlayers(i, j) = CurrentPlayers(I, J);
                          end
                      end
                  end
              end
           end
        end
    end
    
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
            end
        end
    end
    %ָ����ɫ
    Map = [0 0 1;1 1 0;0 1 0;1 0 0];
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



