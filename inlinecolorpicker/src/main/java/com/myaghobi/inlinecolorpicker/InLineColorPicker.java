package com.myaghobi.inlinecolorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class InLineColorPicker extends View {

	private int alignmentLeft = 0;
	private int alignmentCenter = 1;
	private int alignmentRight = 2;
	private int alignment = alignmentCenter;

	private float border = 0;
	private int borderColor = 0;
	private float space = 0;
	private float radius = 0;
	private int[] colors;
	private int defaultSelectedIndex = -1;
	private boolean isSelected = false;

	private boolean actionDown = false;
	private int layoutWidth;

	private Paint paint;
	private OnColorChangeListener onColorChange=null;


	public InLineColorPicker(Context context){
		this(context, null);
	}

	public InLineColorPicker(Context context, AttributeSet attrs) {
		super(context, attrs);

		paint = new Paint();
		paint.setStyle(Style.FILL);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.InLineColorPicker, 0, 0);

		try {
			alignment = a.getInteger(R.styleable.InLineColorPicker_alignment, alignmentCenter);
			radius = a.getDimension(R.styleable.InLineColorPicker_radius, context.getResources().getDimension(R.dimen.defaultRadius));
			space = a.getDimension(R.styleable.InLineColorPicker_space, context.getResources().getDimension(R.dimen.defaultSpaces));
			border = a.getDimension(R.styleable.InLineColorPicker_borderWidth, 0);
			borderColor=a.getColor(R.styleable.InLineColorPicker_borderColor, -1);

			int colorRef = 0;
			if ((colorRef = a.getResourceId(R.styleable.InLineColorPicker_colors, -1))>0) {
				colors = context.getResources().getIntArray(colorRef);
			} else {
				colors = context.getResources().getIntArray(R.array.defaultPaletteColor);
			}


			int sColor_ = -1;
			if ((sColor_=a.getInteger(R.styleable.InLineColorPicker_defaultSelectedColor, -1)) != -1)
				setDefaultColorByIndex(colors[sColor_]);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			a.recycle();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float offset = 0;
		float temp = 0;
		float x = 0;

		if (alignment == alignmentLeft) {
			x = radius;
			offset = border;
		} else if (alignment == alignmentCenter)  {
			temp = colors.length*(radius*2+space+border)-space-border;
			x = (canvas.getWidth()-temp)/2+radius+border;
			offset = border*-1;
		} else if (alignment == alignmentRight) {
			offset = border*-1;
			temp = colors.length*(radius*2+space+border)-space-border;
			x = (canvas.getWidth()-temp)+radius;
		}

		for (int i = 0; i < colors.length; i++) {
			if (border!=0 && isSelected && i == defaultSelectedIndex) {
				if (borderColor==-1)
					paint.setColor(colors[i]);
				else
					paint.setColor(borderColor);
				canvas.drawCircle(x+offset, radius+border, radius+border, paint);
			}

			paint.setColor(colors[i]);
			canvas.drawCircle(x+offset, radius+border, radius, paint);

			if (border==0 && isSelected && i == defaultSelectedIndex) {
				paint.setColor(Color.parseColor("#77ffffff"));
				canvas.drawCircle(x+offset, radius+border, radius, paint);
				paint.setColor(colors[i]);
				canvas.drawCircle(x+offset, radius+border, radius/2, paint);
			}
			x+=space+radius*2+border;
		}

	}

	public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener_) {
		this.onColorChange = onColorChangeListener_;
	}

	private void onColorChange(int color) {
		if (onColorChange != null)
			onColorChange.onColorChange(color, Integer.toHexString(color));
	}


	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				actionDown = true;
				break;
			case MotionEvent.ACTION_MOVE:
				setDefaultColorByIndex(findColorByX(motionEvent.getX()));
				break;
			case MotionEvent.ACTION_UP:
				setDefaultColorByIndex(findColorByX(motionEvent.getX()));
				if (actionDown) {
					super.performClick();
					actionDown=false;
				}
				break;
			default:
				actionDown=false;
				break;
		}

		return true;
	}

	private int findColorByX(float x) {
		float offset = 0;
		float temp = 0;
		float x_ = 0;
		float x2_ = 0;


		if (alignment == alignmentLeft) {
			offset = 0;
		} else if (alignment == alignmentCenter) {
			temp = colors.length*(radius*2+space+border)-space+border;
			offset = (layoutWidth-temp)/2;
		} else if (alignment == alignmentRight) {
			temp = colors.length*(radius*2+space+border)-space+border;
			offset = (layoutWidth-temp);
		}

		x2_ = x_+2*radius+border+space/2;

		for (int i = 0; i < colors.length; i++) {
			if (i==0) {
				x_ = offset+border;
				x2_=x_+radius*2+space/2;
			} else {
				x_=x2_;
				x2_=x_+radius*2+space+border;
			}


			if (x2_ >= x) {
				return i;
			}

			x2_+=border;
		}

		return -1;
	}

	@Override
	protected void onMeasure(int widthMeasure, int heightMeasure) {
		float desiredWidth = colors.length*(2*radius+border+space)+border-space;
		float desiredHeight = 2*(radius+border);

		int widthMode = MeasureSpec.getMode(widthMeasure);
		int widthSize = MeasureSpec.getSize(widthMeasure);

		int heightMode = MeasureSpec.getMode(heightMeasure);
		int heightSize = MeasureSpec.getSize(heightMeasure);

		float width;
		float height;

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(desiredWidth, widthSize);
		} else {
			width = desiredWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(desiredHeight, heightSize);
		} else {
			height = desiredHeight;
		}

		setMeasuredDimension((int)width, (int)height);
	}


	@Override
	public Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable("superState", super.onSaveInstanceState());
		bundle.putBoolean("isSelected", this.isSelected);
		bundle.putInt("defaultSelectedIndex", this.defaultSelectedIndex);
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable parcelable) {
		if (parcelable instanceof Bundle) {
			Bundle bundle = (Bundle) parcelable;
			this.isSelected = bundle.getBoolean("isSelected");
			this.defaultSelectedIndex = bundle.getInt("defaultSelectedIndex");
			parcelable = bundle.getParcelable("superState");
		}
		super.onRestoreInstanceState(parcelable);
	}


	@Override
	protected void onSizeChanged(int width, int height, int prevWith, int prevHeight) {
		layoutWidth = width;
		super.onSizeChanged(width, height, prevWith, prevHeight);
	}


	public void setDefaultColorByIndex(int index) {
		if (index>=0 && index<colors.length) {
			defaultSelectedIndex=index;
			isSelected = true;
			invalidate();
			onColorChange(colors[index]);
		}
	}

	public void setDefaultColor(int color) {
		for (int i=0; i<colors.length; i++) {
			if (colors[i]==color) {
				defaultSelectedIndex=i;
				isSelected = true;
				invalidate();
				break;
			}
		}
	}

	public void setRadius(float radius) {
		this.radius = radius;

		invalidate();
	}

	public void setSpace(float space) {
		this.space = space;

		invalidate();
	}

	public void setBorderWidth(float border) {
		this.border = border;

		invalidate();
	}

	public void setBorderColor(int color) {
		this.borderColor = color;

		invalidate();
	}


	public int getSelectedColorInt() {
		if (defaultSelectedIndex!=-1)
			return colors[defaultSelectedIndex];
		else
			return defaultSelectedIndex;
	}
	public String getSelectedColorHex() {
		if (defaultSelectedIndex!=-1)
			return Integer.toHexString(colors[defaultSelectedIndex]);
		else
			return "";
	}

	public int[] getColors() {
		return colors;
	}

	public void setColors(int[] colorsArray) {
		this.colors = colorsArray;
		defaultSelectedIndex = 0;

		invalidate();
	}


	public interface OnColorChangeListener {
		void onColorChange(int color, String hex);
	}


}
