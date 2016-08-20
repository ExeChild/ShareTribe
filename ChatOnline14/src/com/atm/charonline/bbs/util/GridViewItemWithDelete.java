package com.atm.charonline.bbs.util;



import com.example.studentsystem01.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GridViewItemWithDelete extends RelativeLayout {
	private Context context;
	private ImageView iv_photo;
	public ImageView iv_delete;

	
	public GridViewItemWithDelete(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.gridview_item1, this);
		iv_photo = (ImageView) view.findViewById(R.id.photo_img_view);
		iv_delete = (ImageView) view.findViewById(R.id.photo_delete);
	}

	public GridViewItemWithDelete(Context context, AttributeSet attrs) {
		this(context, attrs ,0);
		// TODO Auto-generated constructor stub
	}

	public GridViewItemWithDelete(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	public void mySetBitmap(Bitmap bit){
		if(iv_photo != null){
			iv_photo.setImageBitmap(bit);
		}
	}


}
