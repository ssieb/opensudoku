package cz.romario.opensudoku.gui.inputmethod;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cz.romario.opensudoku.R;
import cz.romario.opensudoku.game.SudokuCell;
import cz.romario.opensudoku.game.SudokuGame;
import cz.romario.opensudoku.gui.HintsManager;
import cz.romario.opensudoku.gui.SudokuBoardView;

/**
 * This class represents following type of number input workflow: Number buttons are displayed
 * in the sidebar, user selects one number and then fill values by tapping the cells.
 * 
 * @author romario
 *
 */
public abstract class IMSingleNumber extends InputMethod {

	private int mSelectedNumber = 0;
	
	private Context mContext;
	private Handler mGuiHandler;
//	private Drawable mSelectedBackground;
	private Map<Integer,Button> mNumberButtons;
//	private Map<Integer,Drawable> mNumberButtonsBackgrounds;
	
	
	
	public IMSingleNumber(Context context, SudokuGame game,
			SudokuBoardView board, HintsManager hintsManager) {
		super(context, game, board, hintsManager);
		
		mContext = context;
		mGuiHandler = new Handler();
	}

	@Override
	protected void onControlPanelCreated(View controlPanel) {
		mNumberButtons = new HashMap<Integer, Button>(); 
		mNumberButtons.put(1, (Button)controlPanel.findViewById(R.id.button_1));
		mNumberButtons.put(2, (Button)controlPanel.findViewById(R.id.button_2));
		mNumberButtons.put(3, (Button)controlPanel.findViewById(R.id.button_3));
		mNumberButtons.put(4, (Button)controlPanel.findViewById(R.id.button_4));
		mNumberButtons.put(5, (Button)controlPanel.findViewById(R.id.button_5));
		mNumberButtons.put(6, (Button)controlPanel.findViewById(R.id.button_6));
		mNumberButtons.put(7, (Button)controlPanel.findViewById(R.id.button_7));
		mNumberButtons.put(8, (Button)controlPanel.findViewById(R.id.button_8));
		mNumberButtons.put(9, (Button)controlPanel.findViewById(R.id.button_9));
		
//		mSelectedBackground = mContext.getResources().getDrawable(R.drawable.group_button_selected);
//		mNumberButtonsBackgrounds = new HashMap<Integer, Drawable>();
		for (Integer num : mNumberButtons.keySet()) {
			Button b = mNumberButtons.get(num);
//			mNumberButtonsBackgrounds.put(num, b.getBackground());
			b.setTag(num);
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Integer num = (Integer)v.getTag();
					mSelectedNumber = mSelectedNumber == num ? 0 : num;
					
					update();
					
					if (!mHintsManager.wasDisplayed("single_number_pressed")) {
						hint("single_number_pressed", mContext.getString(R.string.hint_single_number_pressed, num),
								Toast.LENGTH_LONG);
					}
				}
			});
		}
	}
	
	private void update() {
		// TODO: sometimes I change background too early and button stays in pressed state
		// this is just ugly workaround
		mGuiHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				for (Button b : mNumberButtons.values()) {
					if (b.getTag().equals(mSelectedNumber)) {
						b.setTextAppearance(mContext, android.R.style.TextAppearance_Large_Inverse);
						// TODO: add color to resources
						b.getBackground().setColorFilter(new LightingColorFilter(Color.rgb(240, 179, 42), 0));
					} else {
						b.setTextAppearance(mContext, android.R.style.TextAppearance_Widget_Button);
						b.getBackground().setColorFilter(null);
					}
				}
			}
		}, 100);
		
	}

	protected int getSelectedNumber() {
		return mSelectedNumber;
	}
	
	@Override
	protected void onActivated() {
		update();
		
		if (!mHintsManager.wasDisplayed("single_number_activated")) {
			hint("single_number_activated", mContext.getString(R.string.hint_single_number_activated), 
					Toast.LENGTH_LONG);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt(getInputMethodName() + ".sel_number", mSelectedNumber);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		
		mSelectedNumber = savedInstanceState.getInt(getInputMethodName() + ".sel_number");
		if (isControlPanelCreated()) {
			update();
		}
	}

}