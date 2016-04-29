package com.inetcar.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.inetcar.startup.R;

import java.io.InputStream;
import java.lang.reflect.Field;


public class PowerImageView extends ImageView {

	private Movie movie;
	private long start;
	private int width;
	private int height;
	private boolean autoplay;
	
	public PowerImageView(Context context) {
		super(context);
	}
	public PowerImageView(Context context, AttributeSet attrs){ 
		this(context,attrs,0);
	}
	public PowerImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle); 
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PowerImageView);
		int resourceId = getResourceId(a,context,attrs);
		if(resourceId!=0)
		{
			InputStream in = getResources().openRawResource(resourceId);
			movie = Movie.decodeStream(in);
			if(movie!=null)
			{
				Log.i("movie","not null" );
				autoplay = a.getBoolean(R.styleable.PowerImageView_auto_play, true);
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				width = bitmap.getWidth();
				height = bitmap.getHeight();
				bitmap.recycle();
				
			}
			
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(movie==null)
		{
			super.onDraw(canvas);
		}
		else
		{
			if(autoplay)
			{
				playMovie(canvas);
				invalidate();
			}
		}
		
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(movie!=null)
		{
			this.setMeasuredDimension(width, height);
		}
	}
	private boolean playMovie(Canvas canvas )
	{
		long now = SystemClock.uptimeMillis();
		if(start==0)
		{
			start = now;
		}
		int duration = movie.duration();
		if(duration==0)
		{
			duration = 1000;
		}
		int realtime = (int)((now-start)%duration);
		movie.setTime(realtime);
		movie.draw(canvas, 0, 0);
		if((now-start)>=duration)
		{
			start = 0;
			return true;
		}
		return false;
	}
	private int getResourceId(TypedArray a, Context context, AttributeSet attrs)
	{
		 try {
			Field field = TypedArray.class.getDeclaredField("mValue");
			field.setAccessible(true);
			TypedValue typedValueObject = (TypedValue) field.get(a);
			 return typedValueObject.resourceId;  
		 
		 } catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}  
		 finally{
			 if(a!=null)
			 {
				 a.recycle();
			 }
		 }
		 return 0;
	}

}
