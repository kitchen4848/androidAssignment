package com.example.stocktradingsystem;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.example.stocktradingsystem.controller.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MenuItem;
import android.view.View;

public class PieChart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_pie_chart);
		
		SQLiteDatabase db = DatabaseCommunicate.getOpeningDatabaseObject(this);
		PortfolioItemCollection pic = DatabaseCommunicate.getAllPortfolioItems(db);
		db.close();
		

		TreeMap<String, Integer> data = new TreeMap<String, Integer>();
		int dataTotal = 0;
		for (PortfolioItem i : pic) {
			data.put(i.getStockName(), i.getLotSize() * i.getQuantityOnHand());
			dataTotal += (i.getLotSize() * i.getQuantityOnHand());
		}
		
		setContentView(new Panel(this, data, dataTotal));

		// add back button?
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	// https://developer.android.com/training/implementing-navigation/ancestral.html
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class Panel extends View {
		String title;
		TreeMap<String, Integer> data;
		ArrayList<String> keys = new ArrayList<String>(data.keySet());
		int dataTotal;
		float panelHeight = 0f;
		float panelWidth = 0f;
		Paint paint = new Paint();
		
        int rColor[] = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED,
        		Color.YELLOW};
		
		public Panel(Context context, TreeMap<String, Integer> data, int dataTotal) {
			super(context);
			this.data = data;
			this.dataTotal = dataTotal;
		}

		float cDegree = 0;
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			panelHeight = h;
			panelWidth = w;
		}

		@Override
		public void onDraw(Canvas c) {
			super.onDraw(c);
			float diameter = (panelHeight < panelWidth ? panelHeight : panelWidth) - 100;
			RectF rec = getDrawingRegion(diameter);
			paint.setStyle(Paint.Style.FILL);

			// Make the entire canvas in white
			paint.setColor(Color.WHITE);
			c.drawPaint(paint);

			// Draw the pie chart
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.FILL);
			
			float startAngle = 0;
			
			int total = 0;
			
			int drawing = 0;
			for (Entry<String, Integer> item : data.entrySet()) {
				// set color
				paint.setColor(rColor[drawing++ % rColor.length]);
				paint.setStrokeWidth(10);
				paint.setStyle(Paint.Style.STROKE);
				
				// draw the line of the sector
				Point endPoint = getDeltaPoint(rec.centerX(), rec.centerY(), diameter, startAngle);
				c.drawLine(rec.centerX(), rec.centerY(), endPoint.x, endPoint.y, paint);
				
				// Draw the arc (Radius of the pie = 200px)
				float sweepAngle = item.getValue() * 360 / 100;
				c.drawArc(rec, startAngle, sweepAngle, true, paint);
				
				total += item.getValue();
				
				startAngle += sweepAngle;
			}

			// Draw the title
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.FILL);
			paint.setTextSize(40);
			paint.setTypeface(Typeface.SERIF);
			c.drawText(title, 20, 50, paint);

			int cBottom = getHeight() - 50;
			paint.setTextSize(20);
			
			for (int i = data.entrySet().size(); i >= 0; i--) {
				String key = keys.get(i);
				int value = data.get(key);
				
				// Draw the legend rect (20px SQ)
				paint.setColor(rColor[i % rColor.length]);
	        	c.drawRect(rec.right, cBottom - 20, rec.right+20, cBottom, paint);

				// Draw the label
//				paint.setColor(rColor[i % rColor.length]);
	        	c.drawText((key.length() > 15 ? key.substring(0, 12) + "..." : key) + " (" + value + " stocks)", rec.right+25, cBottom - 20, paint);
	        	
	        	cBottom -= 24;
			}
		}
		
		private Point getDeltaPoint(float x, float y, float length, float angle) {
			double angleRad = angle * Math.PI / 180;
			double deltaX = length * Math.cos(angleRad);
			double deltaY = length * Math.sin(angleRad);
			
			int x2 = (int)Math.round(x + deltaX);
			int y2 = (int)Math.round(y + deltaY);
			
			return new Point(x2, y2);
		}
		
		private RectF getDrawingRegion(float diameter) {
			boolean landscape = panelWidth > panelHeight;
			float innerSpacing = Math.abs(panelWidth - panelHeight);
			float left = 50 + (landscape ? innerSpacing : 0);
			float top = 50 + (landscape ? 0 : innerSpacing);
			float right = left + diameter;
			float bottom = top + diameter;
			return new RectF(left, top, right, bottom);
		}
	}
}
