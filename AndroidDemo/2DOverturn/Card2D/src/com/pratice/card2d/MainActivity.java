package com.pratice.card2d;
/*
 * 
 * 
 * 
 * 
 * 
 * */
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;


public class MainActivity extends Activity {
	private ImageView imageView1;
	private ImageView imageView2;
	//
	private ScaleAnimation sato1 = new ScaleAnimation(1, 0, 1, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
	private ScaleAnimation sato2 = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //�ҵ������ؼ�
        findViewById(R.id.root).setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (imageView1.getVisibility() == View.VISIBLE){
					imageView1.startAnimation(sato1);
				} else {
					imageView2.startAnimation(sato1);//ע����ص�����һ������					
				}
			}
		});
    }
    private void showImage1() {
		imageView1.setVisibility(View.VISIBLE);
		imageView2.setVisibility(View.INVISIBLE);
	}
    private void showImage2() {
		imageView1.setVisibility(View.INVISIBLE);//���ÿɼ���
		imageView2.setVisibility(View.VISIBLE);
	}
    private void init() {
		imageView1 = (ImageView)findViewById(R.id.pic_1);
		imageView2 = (ImageView)findViewById(R.id.pic_2);
		showImage1();
		sato1.setDuration(500);//���ö���ִ�е�ʱ��
		sato2.setDuration(500);
		//��������¼���������һ�������Ľ���
		sato1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub	
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if (imageView1.getVisibility() == View.VISIBLE){
					imageView1.setAnimation(null);
					showImage2();
					imageView2.startAnimation(sato2);
				} else {
					imageView2.setAnimation(null);
					showImage1();		
					imageView1.startAnimation(sato2);//ע���ǵڶ�������Ч��
				}
				
			}
		});
	}
}

/*
 * С�ǣ�
 * ��֪��Ϊʲô
 * 800*1280��ͼƬ��û�����Ч��
 * ò�ƿ�̫������Բ���Ч�����ǲ��ǳ�����Ļ��ԭ��
 * markһ�£�ѧһ��ʱ�����������������
 * ��һֱ��Ϊ��bug�����˴����
 * */
