package com.example.account;
/****************
 * Author:Zachary(F.SB)
 * 
 * 
 * ********************/
import java.util.ArrayList;
import java.util.Calendar;

import models.TradeClass;
import models.consumeClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddEvent extends Activity{
	public static final String PAY_TYPE_1 = "日常购物";
	public static final String PAY_TYPE_2 = "交际送礼";
	public static final String PAY_TYPE_3 = "餐饮开销";
	public static final String PAY_TYPE_4 = "购置衣物";
	public static final String PAY_TYPE_5 = "娱乐开销";
	public static final String PAY_TYPE_6 = "网费话费";
	public static final String PAY_TYPE_7 = "交通出行";
	public static final String PAY_TYPE_8 = "水电煤气";
	public static final String PAY_TYPE_9 = "其他花费";
	private TextView addDate = null;
	private Spinner typeSpinner;
	public String addType="";
	private Button addButton;
	private EditText money;
	DatePickerDialog.OnDateSetListener OnDateSetListener ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addevent);
		
	    this.typeSpinner = ((Spinner)findViewById(R.id.type));
	    ArrayList localArrayList = new ArrayList();
	    localArrayList.add(PAY_TYPE_1);
	    localArrayList.add(PAY_TYPE_2);
	    localArrayList.add(PAY_TYPE_3);
	    localArrayList.add(PAY_TYPE_4);
	    localArrayList.add(PAY_TYPE_5);
	    localArrayList.add(PAY_TYPE_6);
	    localArrayList.add(PAY_TYPE_7);
	    localArrayList.add(PAY_TYPE_8);
	    localArrayList.add(PAY_TYPE_9);
	    ArrayAdapter localArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, R.id.spinnerItem, localArrayList);
	    this.typeSpinner.setAdapter(localArrayAdapter);
	    this.typeSpinner.setPrompt("请选择消费类型");
	    this.typeSpinner.setOnItemSelectedListener(new SpinnerSelected());
	    this.addDate = ((TextView)findViewById(R.id.addDate));
	    this.addDate.setOnClickListener(new DateOnClick());
	    this.addButton = ((Button)findViewById(R.id.addButton));
	    this.addButton.setOnClickListener(new AddPocketClick());
	    this.money = ((EditText)findViewById(R.id.money));
	    
	    OnDateSetListener = new DatePickerDialog.OnDateSetListener()
		{
		    public void onDateSet(DatePicker paramDatePicker, int paramInt1, int paramInt2, int paramInt3)
		    {
		      AddEvent.this.addDate.setText(paramInt1 + "-" + (paramInt2 + 1) + "-" + paramInt3);
		    }
		};
		/*
	    this.addDate=(TextView)findViewById(R.id.addDate);
	    addDate.setOnTouchListener(new OnTouchListener(){
	
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(AddEvent.this, "This is a Test!", Toast.LENGTH_LONG).show();  
				
				return false;
			}
	    });*/
    	
    }
protected Dialog onCreateDialog(int paramInt)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    int i = localCalendar.get(Calendar.YEAR);
	    int j = localCalendar.get(Calendar.MONTH);
	    int k = localCalendar.get(Calendar.DAY_OF_MONTH);
	    switch (paramInt)
	    {
	    default:
	      return null;
	    case 1:
	    }
	    return new DatePickerDialog(this, this.OnDateSetListener, i, j, k);
	  }	
	
//弹出提示
private void dialog()
	  {
	    new AlertDialog.Builder(this).setTitle("添加一笔新消费?").setMessage("消费类型：" + this.addType + "\n消费金额：" + this.money.getText().toString() + "\n消费日期：" + this.addDate.getText().toString()).setPositiveButton("确定", new DialogInterface.OnClickListener()
	    {
	      public void onClick(DialogInterface paramDialogInterface, int paramInt)
	      {
	        AddEvent.this.setResult(-1);
	        //AddEvent.this.addPocket();
	        //确定添加
	        consumeClass trade=new consumeClass(0, Float.parseFloat("-"+AddEvent.this.money.getText().toString()), AddEvent.this.addDate.getText().toString(), "123", addType, AddEvent.this);
	        
	        trade.trade_add();
	        Toast.makeText(AddEvent.this, "添加完成", 0).show();
	      }
	    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
	    {
	      public void onClick(DialogInterface paramDialogInterface, int paramInt)
	      {
	      }
	    }).show();
	  }


class AddPocketClick implements View.OnClickListener
	  {
	    AddPocketClick()
	    {
	    }

	    public void onClick(View paramView)
	    {
	      if (AddEvent.this.addDate.getText().equals("点击选择日期"))
	      {
	        Toast.makeText(AddEvent.this, "请先选择消费日期", 0).show();
	        return;
	      }
	      if (AddEvent.this.money.getText().toString().trim().length() == 0)
	      {
	        Toast.makeText(AddEvent.this, "请先填写消费金额", 0).show();
	        return;
	      }
	      AddEvent.this.dialog();
	    }
	  }
	
class DateOnClick implements View.OnClickListener
  {
    DateOnClick()
    {

    }

    public void onClick(View paramView)
    {
    	AddEvent.this.showDialog(1);
    }
  }
	
	
class SpinnerSelected implements OnItemSelectedListener
	{
		SpinnerSelected()
		{
		}
		
		@Override
		public void onItemSelected(AdapterView<?> paramAdapterView, View arg1, int paramInt,
				long arg3) {
			// TODO Auto-generated method stub
			 String str = paramAdapterView.getItemAtPosition(paramInt).toString();
			 AddEvent.this.addType = str;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	}
}



