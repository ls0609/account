package com.example.account;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import models.MyPackage;
import models.TradeClass;
import models.consumeClass;
import models.incomeClass;

public class QueryByTodayActivity extends Activity
{
  private ListView listView;
  private TextView textView;
  private Adapter_TD mTDAdapter;
  private Adapter_LS mLSAdapter;
  private Map<typeClass, Boolean> mLocalmap1;
  private Map<Integer, Boolean> mLocalmap2;
  private static Handler mHandler;
  public static final int QUERY_BY_DAY = 0;
  public static final int QUERY_BY_MONTH = 1;	
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.querybytoday);
    //ivinfo=(ListView)findViewById(R.id.listView);
    //setAdapter();
    //GetTodayBill();
    initHandler();
  }
  

  private void initHandler()
	{
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what){
				case MessageConst.CLIENT_ACTION_QUERY_BY_DAY:
					GetTodayBill();
				    break;	
				case MessageConst.CLIENT_ACTION_QUERY_BY_MONTH:
					fillList();
				    break;	
				case MessageConst.CLIENT_ACTION_DELETE_BY_INDEX:
					deleteByIndex(msg.arg1);
					break;
				}
			}
		};
	}

  public static void refreshListView(int type)
  {
	  if(type == QUERY_BY_DAY)
		  mHandler.sendEmptyMessage(MessageConst.CLIENT_ACTION_QUERY_BY_DAY); 
	  else if(type == QUERY_BY_MONTH)
		  mHandler.sendEmptyMessage(MessageConst.CLIENT_ACTION_QUERY_BY_MONTH); 
  }
  
  public static void deleteTodayDataByIndex(int index)
  {
	  mHandler.sendMessage(mHandler.obtainMessage(MessageConst.CLIENT_ACTION_DELETE_BY_INDEX, index, 0)); 
  }
  
  private void GetTodayBill() {
	// TODO Auto-generated method stub
	//ArrayAdapter<String>  adapter=null;
	listView =(ListView)findViewById(R.id.listView);
	textView =(TextView)findViewById(R.id.textView);
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	float todaymenoy=0;
	MyPackage pack=new MyPackage(this);
	List<TradeClass> List=pack.getAlltrade();
	//bill_array=new String[List.size()];
	int i=0;
    Calendar localCalendar = Calendar.getInstance();
    int year = localCalendar.get(Calendar.YEAR);
    int month = localCalendar.get(Calendar.MONTH)+1;
    int day = localCalendar.get(Calendar.DAY_OF_MONTH);
	String str1=new String(year+"-"+month+"-"+day);
	String str;
	for(TradeClass con:List){
			str=con.gettime();
			if(str1.equals(str)){
				//bill_array[i]=con.getId()+"|*****|"+con.getMoney()+"\n"+con.getPocketType()+"|****|"+con.gettime();
				todaymenoy+=con.getMoney();
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("_id", con.getId());
				map.put("money", ""+con.getMoney());
				if(con.getPocketType().equals("日常购物")){
					map.put("icon",R.drawable.richanggouwu);
					map.put("classtype",0);
				}else if(con.getPocketType().equals("交际送礼")){
					map.put("icon",R.drawable.jiaojisongli);
					map.put("classtype",0);
				}else if(con.getPocketType().equals("餐饮开销")){
					map.put("icon",R.drawable.canyingkaixiao);
					map.put("classtype",0);
				}else if(con.getPocketType().equals("购置衣物")){
					map.put("icon",R.drawable.gouziyiwu);
					map.put("classtype",0);
				}else if(con.getPocketType().equals("娱乐开销")){
					map.put("icon",R.drawable.yulekaixiao);
					map.put("classtype",0);
				}else if(con.getPocketType().equals("水电煤气")){
					map.put("icon",R.drawable.shuidianmeiqi);
					map.put("classtype",0);
				}else if(con.getPocketType().equals("网费话费")){
					map.put("icon",R.drawable.wannluohuafei);
					map.put("classtype",0);
				}else if(con.getPocketType().equals("交通出行")){
					map.put("icon",R.drawable.jiaotongchuxing);
					map.put("classtype",0);
				}else if(con.getPocketType().equals("其他花费")){
					map.put("icon",R.drawable.qita);
					map.put("classtype",0);
				}else if(con.getPocketType().equals("工资收入")){
					map.put("icon",R.drawable.gongzi);
					map.put("classtype",1);
				}else if(con.getPocketType().equals("股票收入")){
					map.put("icon",R.drawable.gupiao);
					map.put("classtype",1);
				}else {
					map.put("icon",R.drawable.qita);
					map.put("classtype",1);
				}
				map.put("time", con.gettime());
				map.put("type", con.getPocketType());
				list.add(map);
				i++;
			}
	}
	mLocalmap1 = new HashMap<typeClass, Boolean>();
	mTDAdapter = new Adapter_TD(this, list, mLocalmap1);
	textView.setText("今日共花费："+(-todaymenoy)+"元");
	listView.setAdapter(mTDAdapter);
	if(i==0){
		Toast.makeText(getApplicationContext(), "今天您还没有消费哦！", Toast.LENGTH_SHORT);
	}
}

   private void fillList()
   {
		// TODO Auto-generated method stub
		//ArrayAdapter<String>  adapter=null;
		listView =(ListView)findViewById(R.id.listView);
		textView =(TextView)findViewById(R.id.textView);
		
	    Calendar localCalendar = Calendar.getInstance();
	    int year = localCalendar.get(Calendar.YEAR);
	    int month = localCalendar.get(Calendar.MONTH)+1;
		String str1=new String(year+"-"+month);
		String str;
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		float todaymenoy=0;
		MyPackage pack=new MyPackage(this);
		List<TradeClass> List=pack.getAlltrade();
		for(TradeClass con:List){
			str = con.gettime();
			str=str.substring(0, str.lastIndexOf('-'));
			if(str1.equals(str)){
					todaymenoy+=con.getMoney();
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("_id", con.getId());
					map.put("money", ""+con.getMoney());
					if(con.getPocketType().equals("日常购物")){
						map.put("icon",R.drawable.richanggouwu);
					}else if(con.getPocketType().equals("交际送礼")){
						map.put("icon",R.drawable.jiaojisongli);
					}else if(con.getPocketType().equals("餐饮开销")){
						map.put("icon",R.drawable.canyingkaixiao);
					}else if(con.getPocketType().equals("购置衣物")){
						map.put("icon",R.drawable.gouziyiwu);
					}else if(con.getPocketType().equals("娱乐开销")){
						map.put("icon",R.drawable.yulekaixiao);
					}else if(con.getPocketType().equals("水电煤气")){
						map.put("icon",R.drawable.shuidianmeiqi);
					}else if(con.getPocketType().equals("网费话费")){
						map.put("icon",R.drawable.wannluohuafei);
					}else if(con.getPocketType().equals("交通出行")){
						map.put("icon",R.drawable.jiaotongchuxing);
					}else if(con.getPocketType().equals("其他花费")){
						map.put("icon",R.drawable.qita);
					}else{
						map.put("icon",R.drawable.qita);
					}				
					map.put("time", con.gettime());
					map.put("type", con.getPocketType());
					list.add(map);
				}
			}
		mLocalmap2 = new HashMap<Integer, Boolean>();
		mLSAdapter=new Adapter_LS(this, list, mLocalmap2);
		textView.setText("本月共花费："+(-todaymenoy)+"元");
		listView.setAdapter(mLSAdapter);
  }

  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    paramMenu.add(0, 1, 1, "删除").setIcon(R.drawable.delete);
    return super.onCreateOptionsMenu(paramMenu);
  }

  private void deleteByIndex(int index)
  {
	  int position = index -1;
	  consumeClass tradeconsume=new consumeClass(0, 0, "", "123", "", QueryByTodayActivity.this);
	  int id = mTDAdapter.getIdByIndex(position);
	  int success = tradeconsume.trade_delect(id);
	  GetTodayBill(); 
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem){
	  	// = Adapter_TD.isSelected;
	    if (mLocalmap1.size() <= 0)
	    {
	      Toast.makeText(this, "请先选择要删除的消费记录!", 0).show();
	      return true;
	    }
        consumeClass tradeconsume=new consumeClass(0, 0, "", "123", "", QueryByTodayActivity.this);
        incomeClass tradeincome=new incomeClass(0, 0, "", "123", "", QueryByTodayActivity.this);
	    Iterator it = mLocalmap1.entrySet().iterator();
	    while (it.hasNext()) {
		     Map.Entry entry = (Map.Entry) it.next();
		     Object key = entry.getKey();
		     Object value = entry.getValue();
		     if((Boolean)value){
		    	 if(((typeClass)key).type==0){
		    		 int success=tradeconsume.trade_delect(((typeClass)key)._id);
		    		 if(success==1)Toast.makeText(this, "删除消费记录成功!", 0).show();
		    	 }else{
		    		 int success=tradeincome.trade_delect(((typeClass)key)._id);
		    		 if(success==1)Toast.makeText(this, "删除收入记录成功!", 0).show();
		    	 }
		    }
		     //Log.i("nihao","key=" + key + " value=" + value);
	    }
	    GetTodayBill(); 
	    return true;  
  }
  
  protected void onResume()
  {
	GetTodayBill();  
    super.onResume();
  }
}