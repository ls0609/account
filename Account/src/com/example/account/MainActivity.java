package com.example.account;


import java.util.Calendar;

import models.consumeClass;

import org.json.JSONArray;
import org.json.JSONObject;

import ai.olami.aiCloudService.sdk.engin.OlamiVoiceRecognizer;
import ai.olami.aiCloudService.sdk.interfaces.IOlamiVoiceRecognizerListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	private TabHost mTabHost;
	private Handler mHandler;
	private OlamiVoiceRecognizer mOlamiVoiceRecognizer;
	private OlamiVoiceRecognizerListener mOlamiVoiceRecognizerListener;
	private Button mBtnStart;
	private TextView mTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initHandler();
		initView();       
		initViaVoiceRecognizerListener();
		init();

	}

	private void initView()
	{
		mTabHost = getTabHost();
		Resources localResources = getResources();
		
		Intent localIntent2 = new Intent();
	    localIntent2.setClass(this, QueryByTodayActivity.class);
	    mTabHost.addTab(mTabHost.newTabSpec("账单查询")
                .setIndicator("账单查询",localResources.getDrawable(R.drawable.zhangdanchaxun))
                .setContent(localIntent2));  
		
		Intent localIntent1 = new Intent();
	    localIntent1.setClass(this, AddEvent.class);

		TabHost.TabSpec localTabSpec1 = mTabHost.newTabSpec("添加支出");
		localTabSpec1.setIndicator("添加支出",localResources.getDrawable(R.drawable.tianjiazhangdan));
        //localTabSpec1.setContent(R.id.RelativeLayout01);
		localTabSpec1.setContent(localIntent1);
		mTabHost.addTab(localTabSpec1);
        /*
		LayoutInflater.from(this).inflate(R.layout.baobiao, tabHost.getTabContentView(), true);*/
        Intent localIntent4 = new Intent();
	    localIntent4.setClass(this, shouru.class);
	    mTabHost.addTab(mTabHost.newTabSpec("添加收入")
                .setIndicator("添加收入",localResources.getDrawable(R.drawable.tongjibaobiao))
                .setContent(localIntent4));
		//LayoutInflater.from(this).inflate(R.layout.activity_main, tabHost.getTabContentView(), true);
        Intent localIntent3 = new Intent();
	    localIntent3.setClass(this, shezhi.class);
	    mTabHost.addTab(mTabHost.newTabSpec("软件设置")
                .setIndicator("软件设置",localResources.getDrawable(R.drawable.fenleichaxun))
                .setContent(localIntent3));
	    
	    mBtnStart = (Button) findViewById(R.id.btn_start);
	    
	    mBtnStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOlamiVoiceRecognizer != null)
					mOlamiVoiceRecognizer.start();	
			}			
		});
	    
	    mTextView = (TextView) findViewById(R.id.tv_result);
	}
	
	private void initHandler()
	{
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what){
				case MessageConst.CLIENT_ACTION_START_RECORED:
					mBtnStart.setText("录音中");
				    break;
				case MessageConst.CLIENT_ACTION_STOP_RECORED:
					mBtnStart.setText("识别中");
					break;
				case MessageConst.CLIENT_ACTION_CANCEL_RECORED:
					mBtnStart.setText("开始");
					//mTextView.setText("已取消");
					break;
				case MessageConst.CLIENT_ACTION_ON_ERROR:
					mTextView.setText("错误代码："+msg.arg1);
					mBtnStart.setText("开始");
					break;
				case MessageConst.CLIENT_ACTION_UPDATA_VOLUME:
					//mTextViewVolume.setText("音量: "+msg.arg1);
					break;
				case MessageConst.SERVER_ACTION_RETURN_RESULT:
//					if(msg.obj != null)
//						mTextView.setText("服务器返回: "+msg.obj.toString());
					mBtnStart.setText("开始");
					processServerMessage((String) msg.obj);					
					break;				
				}
			}
		};
	}
	
	public void init()
    {
    	initHandler();
    	mOlamiVoiceRecognizer = new OlamiVoiceRecognizer(MainActivity.this);
    	TelephonyManager telephonyManager=(TelephonyManager) this.getSystemService(this.getBaseContext().TELEPHONY_SERVICE);
    	String imei=telephonyManager.getDeviceId();
    	mOlamiVoiceRecognizer.init(imei);//set null if you do not want to notify olami server.
    	
    	mOlamiVoiceRecognizer.setListener(mOlamiVoiceRecognizerListener);
    	mOlamiVoiceRecognizer.setLocalization(OlamiVoiceRecognizer.LANGUAGE_SIMPLIFIED_CHINESE);
    	mOlamiVoiceRecognizer.setAuthorization("573031596fd746fca478e5cccf6ca9e2","asr","d5307ed38df64ab6a08e467c00c81d37","nli");    	
    	mOlamiVoiceRecognizer.setVADTailTimeout(2000);
    	mOlamiVoiceRecognizer.setLatitudeAndLongitude(31.155364678184498,121.34882432933009); 
    }
	
	
	private void initViaVoiceRecognizerListener()
	{
		mOlamiVoiceRecognizerListener = new OlamiVoiceRecognizerListener();
	}
	private class OlamiVoiceRecognizerListener implements IOlamiVoiceRecognizerListener{

		@Override
		public void onError(int errCode) {
			mHandler.sendMessage(mHandler.obtainMessage(MessageConst.CLIENT_ACTION_ON_ERROR,errCode,0));

		}

		@Override
		public void onEndOfSpeech() {
			mHandler.sendEmptyMessage(MessageConst.CLIENT_ACTION_STOP_RECORED);			
		}

		@Override
		public void onBeginningOfSpeech() {
			mHandler.sendEmptyMessage(MessageConst.CLIENT_ACTION_START_RECORED);			
		}

		@Override
		public void onResult(String result, int type) {
			mHandler.sendMessage(mHandler.obtainMessage(MessageConst.SERVER_ACTION_RETURN_RESULT, type, 0, result));
		}

		@Override
		public void onCancel() {
			mHandler.sendEmptyMessage(MessageConst.CLIENT_ACTION_CANCEL_RECORED);

		}

		@Override
		public void onUpdateVolume(int volume) {
			mHandler.sendMessage(mHandler.obtainMessage(MessageConst.CLIENT_ACTION_UPDATA_VOLUME, volume, 0, null));
		}
		
	}
	
	private void processServerMessage(String message)
	{
		try{
			String input = null;
			JSONObject jsonObject = new JSONObject(message);
			JSONArray jArrayNli = jsonObject.optJSONObject("data").optJSONArray("nli");
			JSONObject jObj = jArrayNli.optJSONObject(0);
			JSONArray jArraySemantic = null;
			if(message.contains("semantic"))
			{
			  jArraySemantic = jObj.getJSONArray("semantic");
			  input = jArraySemantic.optJSONObject(0).optString("input");
			}
			else{
				input = jsonObject.optJSONObject("data").optJSONObject("asr").optString("result");
			}
			JSONObject jObjSemantic;
			JSONArray jArraySlots;
			JSONArray jArrayModifier;
			String type = null;
			String pay_number = null;
			String pay_type = null;
			String day = null;
			if(jObj != null) {
				type = jObj.optString("type");
				if("account".equals(type))
				{
					jObjSemantic = jArraySemantic.optJSONObject(0);
					input = jObjSemantic.optString("input");
					jArraySlots = jObjSemantic.optJSONArray("slots");
					jArrayModifier = jObjSemantic.optJSONArray("modifier");
					String modifier = (String)jArrayModifier.opt(0);
					if((jArrayModifier != null) && ("pay".equals(modifier)))
					{
						if(jArraySlots != null)
						{
						   for(int i=0,k=jArraySlots.length(); i<k; i++)
						   {
							   JSONObject obj = jArraySlots.getJSONObject(i);
							   String name = obj.optString("name");
							   if("pay_type".equals(name))
								   pay_type = obj.optString("value");
							   else if("pay_number".equals(name))
							   {
								   pay_number = obj.getJSONObject("num_detail").getString("recommend_value");
							   }
							   else if("day".equals(name))
							   {
								   day = obj.getJSONObject("num_detail").getString("recommend_value");
							   }

						   }
						}
						String date = null;
						Calendar localCalendar = Calendar.getInstance();
					    int i_year = localCalendar.get(Calendar.YEAR);
					    int i_month = localCalendar.get(Calendar.MONTH)+1;
					    int i_day = localCalendar.get(Calendar.DAY_OF_MONTH);
						 if(day == null)
						 {
							 date = i_year + "-" + i_month + "-" + i_day;
						 }
						 else
						 {
							 date = i_year + "-" + i_month + "-" + day;
						 }
						 consumeClass trade = new consumeClass(0, Float.parseFloat("-"+pay_number), date, "123", pay_type, MainActivity.this);				       
					     trade.trade_add();
					}
					else if((jArrayModifier != null) && ("query_today".equals(modifier)))
					{
						QueryByTodayActivity.refreshListView(QueryByTodayActivity.QUERY_BY_DAY);
					}
					else if((jArrayModifier != null) && ("query_month".equals(modifier)))
					{
						QueryByTodayActivity.refreshListView(QueryByTodayActivity.QUERY_BY_MONTH);						
					}else if((jArrayModifier != null) && ("delete_today".equals(modifier)))
					{
						String index = null;
						if(jArraySlots != null)
						{						  
						   JSONObject obj = jArraySlots.getJSONObject(0);							 
						   index = obj.getJSONObject("num_detail").getString("recommend_value");							  						  
						}
						if(index != null && !"".equals(index))
						  QueryByTodayActivity.deleteTodayDataByIndex(Integer.parseInt(index));						
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}					
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
