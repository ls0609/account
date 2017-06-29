package models;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import db.DBHelper;

public class MyPackage {
	float income_sum;
	float consume_sum;
	private DBHelper dbhelper;
	private SQLiteDatabase db;
	private Context context;
	//public 
	public MyPackage(Context context){
		this.context=context;
		dbhelper=new DBHelper(context);
	}
	public float getincome(){
		return income_sum;
	} 
	
	public float getconsume(){
		return consume_sum;
	}
	public List<TradeClass> getAlltrade(){
		List<TradeClass> TradeList=new ArrayList<TradeClass>();
		db=dbhelper.getReadableDatabase();
		Cursor cu=db.rawQuery("select * from tb_outaccount", null);
		while(cu.moveToNext()){
			TradeList.add(new consumeClass(cu.getInt(cu.getColumnIndex("_id")),cu.getFloat(cu.getColumnIndex("money")),
					cu.getString(cu.getColumnIndex("addTime")),cu.getString(cu.getColumnIndex("mark")),cu.getString(cu.getColumnIndex("pocketType")),
					context));	
		}
		cu=db.rawQuery("select * from tb_inaccount", null);
		while(cu.moveToNext()){
			TradeList.add(new incomeClass(cu.getInt(cu.getColumnIndex("_id")),cu.getFloat(cu.getColumnIndex("money")),
					cu.getString(cu.getColumnIndex("addTime")),cu.getString(cu.getColumnIndex("mark")),cu.getString(cu.getColumnIndex("pocketType")),
					context));
		}
		return TradeList;
	}
}
