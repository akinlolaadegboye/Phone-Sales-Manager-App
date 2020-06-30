package com.salesrepmanager.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesrepmanager.R;
import com.salesrepmanager.helperClass.AppUtilityClass;

import java.util.ArrayList;
public class ViewSalesCustomAdapter extends BaseAdapter {
	Context context;
	ArrayList<ViewSalesItems> arraylist;
	public ViewSalesCustomAdapter(Context context, ArrayList<ViewSalesItems> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arraylist.get(arg0);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (view == null) {
			view = inflater.inflate(R.layout.view_sales_items, parent, false);
			holder = new ViewHolder();
			holder.modelTextView = (TextView) view.findViewById(R.id.model);
			holder.priceTextView=(TextView) view.findViewById(R.id.price);
			holder.timeTextView=(TextView) view.findViewById(R.id.time);
			holder.dateTextView=(TextView) view.findViewById(R.id.date);
			holder.image = (ImageView) view.findViewById(R.id.image);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		String model = arraylist.get(position).getModel();
		String price = arraylist.get(position).getprice();
		String time = arraylist.get(position).getTime();
		String date = arraylist.get(position).getDate();
		byte[] image_ = arraylist.get(position).getImage();
		holder.modelTextView.setText(model);
        holder.priceTextView.setText(price+"NGN");
		holder.timeTextView.setText(time);
		holder.dateTextView.setText(date);
		holder.image.setImageBitmap(AppUtilityClass.getImage(image_));

		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});
		return view;
	}
	public class ViewHolder {
		TextView modelTextView;
		TextView priceTextView;
		TextView timeTextView;
		TextView dateTextView;
		ImageView image;
	}
}
