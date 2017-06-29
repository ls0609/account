
package com.example.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

class Adapter_LS extends BaseAdapter
{
  public Map<Integer, Boolean> isSelected;
  public List<Integer> positions = null;
  private List<Map<String, Object>> data;
  private LayoutInflater mInflater;
  private Context paramContext0;
  public Adapter_LS(Context paramContext, List<Map<String, Object>> paramList, Map<Integer, Boolean> paramMap)
  {
	paramContext0=paramContext;
    this.mInflater = LayoutInflater.from(paramContext);
    this.data = paramList;
    isSelected = paramMap;
    //isSelected=new Map<Integer, Boolean>();
    positions = new ArrayList();
  }

  public int getIdByIndex(int index)
  {
	 int id = ((Integer) ((Map)this.data.get(index)).get("_id")).intValue();
	 return id;
  }
  
  public int getCount()
  {
	  //return 1;
    if (this.data == null)
      return 0;
    return this.data.size();
  }

  public Object getItem(int paramInt)
  {
	  return paramInt;
    //return this.data.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  @Override
  public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup)
  {
	
	paramView=mInflater.inflate(R.layout.pocket_item, null);
	ViewHolder localViewHolder=new ViewHolder();
	localViewHolder.img = ((ImageView)paramView.findViewById(R.id.imgTitle));
	localViewHolder.title = ((TextView)paramView.findViewById(R.id.typeName));
	localViewHolder.price = ((TextView)paramView.findViewById(R.id.moneyItem));
	localViewHolder.cBox = ((CheckBox)paramView.findViewById(R.id.check));
    (localViewHolder).img.setBackgroundResource(((Integer)((Map)this.data.get(paramInt)).get("icon")).intValue());
    localViewHolder.title.setText(((Map)this.data.get(paramInt)).get("type").toString());
    localViewHolder.price.setText("￥" + ((Map)this.data.get(paramInt)).get("money").toString());
    int i=((Integer) ((Map)this.data.get(paramInt)).get("_id")).intValue();
    positions.add(i);
    //Toast.makeText(paramContext0, "ddd"+i, 0).show();
    localViewHolder.cBox.setOnCheckedChangeListener(
  		  new CompoundButton.OnCheckedChangeListener(){
	      public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
	      {
	    	  if(paramBoolean){
	    		  //Toast.makeText(paramContext0, "test"+paramInt, 0).show();
	    		  isSelected.put(positions.get(paramInt), true);
	    	  }else{
	    		  isSelected.put(positions.get(paramInt), false);
	    	  }
    	  }
      });
	return paramView;
    }
	  public final class ViewHolder
	  {
	    public CheckBox cBox;
	    public ImageView img;
	    public TextView price;
	    public TextView title;
	
	    public ViewHolder()
	    {
	    }
	  }
  }

