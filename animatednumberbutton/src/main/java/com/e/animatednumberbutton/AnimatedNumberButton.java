package com.e.animatednumberbutton;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AnimatedNumberButton extends RelativeLayout {

    private Context context;
    private AttributeSet attrs;
    private int attrStyle;
    private int resStyle;
    private OnClickListener monClickListener;
    private int initialNumber;
    private int lastNumber;
    private int currentNumber;
    private int finalNumber;
    private TextView textView;
    private ImageView addBtn, subtractBtn;
    private OnValueChangeListener onValueChangeListener;

    public AnimatedNumberButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AnimatedNumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }

    public AnimatedNumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.attrStyle = defStyleAttr;
        init();
    }

    public AnimatedNumberButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        this.attrs = attrs;
        this.attrStyle = defStyleAttr;
        this.resStyle = defStyleRes;
    }
    private void init(){
        inflate(context,R.layout.layout,this);
        final Resources resources = getResources();
        final int defaultColor = resources.getColor(R.color.colorPrimary);
        final int defaultTextColor = resources.getColor(R.color.colorText);
        final Drawable defaultDrawable = resources.getDrawable(R.drawable.rectangle);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AnimatedNumberButton,attrStyle,0);
        initialNumber = array.getInt(R.styleable.AnimatedNumberButton_initialNumber,0);
        finalNumber = array.getInt(R.styleable.AnimatedNumberButton_finalNumber,Integer.MAX_VALUE);
        float textSize = array.getDimension(R.styleable.AnimatedNumberButton_textSize,13);
        int color = array.getColor(R.styleable.AnimatedNumberButton_backGroundColor,defaultColor);
        int textColor = array.getColor(R.styleable.AnimatedNumberButton_textColor,defaultTextColor);
        Drawable drawable = array.getDrawable(R.styleable.AnimatedNumberButton_backgroundDrawable);

        subtractBtn = findViewById(R.id.subtract_btn);
        addBtn = findViewById(R.id.add_btn);
        textView = findViewById(R.id.number_counter);
        LinearLayout mLayout = findViewById(R.id.linear_layout);

        subtractBtn.setImageResource(R.drawable.minus);
        addBtn.setImageResource(R.drawable.plus);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);

        if (drawable == null){
            drawable = defaultDrawable;
        }
        assert drawable != null;
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
        textView.setBackground(drawable);

        textView.setText(String.valueOf(initialNumber));

        currentNumber = initialNumber;
        lastNumber = initialNumber;

        subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.valueOf(textView.getText().toString());
                setNumber(String.valueOf(num - 1), true);
                RotateAnimation animation = new RotateAnimation(0,360, mView.getWidth() >> 1, mView.getHeight() >> 1);
                subtractBtn.setAnimation(animation);
                animation.setDuration(500);
                subtractBtn.startAnimation(animation);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.valueOf(textView.getText().toString());
                setNumber(String.valueOf(num + 1), true);
                RotateAnimation animation = new RotateAnimation(0,360, mView.getWidth() >> 1, mView.getHeight() >> 1);
                addBtn.setAnimation(animation);
                animation.setDuration(500);
                addBtn.startAnimation(animation);
            }
        });
        array.recycle();
    }

    private void callListener(View view){
        if (monClickListener != null){
            monClickListener.onClick(view);
        }
        if (onValueChangeListener != null){
            if (lastNumber != currentNumber){
                onValueChangeListener.onValueChange(this,lastNumber,currentNumber);
            }
        }
    }

    public String getNumber(){
        return String.valueOf(currentNumber);
    }
    public void setNumber(String number){
        lastNumber = currentNumber;
        this.currentNumber = Integer.parseInt(number);
        if (this.currentNumber > finalNumber){
            this.currentNumber = finalNumber;
        }
        if (this.currentNumber < initialNumber){
            this.currentNumber = initialNumber;
        }
        textView.setText(String.valueOf(currentNumber));
    }
    public void setNumber(String number, boolean notifyListener){
        setNumber(number);
        if (notifyListener){
            callListener(this);
        }
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.monClickListener = onClickListener;
    }
    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener1){
        onValueChangeListener = onValueChangeListener1;
    }
    @FunctionalInterface
    public interface OnClickListener {
        void onClick(View view);
    }
    public interface OnValueChangeListener {
        void onValueChange(AnimatedNumberButton view, int oldValue, int newValue);
    }
    public void setRange(Integer startingNumber, Integer endingNumber) {
        this.initialNumber = startingNumber;
        this.finalNumber = endingNumber;
    }
    public void updatColors(int backgroundColor, int textColor){
        this.textView.setBackgroundColor(backgroundColor);
        this.textView.setTextColor(textColor);
    }
    public void updateTextSize(int unit, float newSize){
        this.textView.setTextSize(unit,newSize);
    }
}
