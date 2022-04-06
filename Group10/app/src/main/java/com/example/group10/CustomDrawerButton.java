package com.example.group10;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;


public class CustomDrawerButton extends androidx.appcompat.widget.AppCompatImageButton implements DrawerLayout.DrawerListener {

    private DrawerLayout mDrawerLayout;
    private int side = Gravity.LEFT;

    public CustomDrawerButton(Context context) {
        super(context);
    }
    public CustomDrawerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomDrawerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void changeState(){
        if ( mDrawerLayout.isDrawerOpen( side ) ){
            mDrawerLayout.closeDrawer( side );
        }else{
            mDrawerLayout.openDrawer( side );
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        Log.e("BUTTOM DRAWER: ", "onDrawerSlide");
    }
    @Override
    public void onDrawerOpened(View drawerView) {
        Log.e("BUTTOM DRAWER: ", "onDrawerOpened");
    }
    @Override
    public void onDrawerClosed(View drawerView) {
        Log.e("BUTTOM DRAWER: ", "onDrawerClosed");
    }
    @Override
    public void onDrawerStateChanged(int newState) {
        Log.e("BUTTOM DRAWER: ", "onDrawerStateChanged");
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public CustomDrawerButton setDrawerLayout(DrawerLayout mDrawerLayout) {
        this.mDrawerLayout = mDrawerLayout;
        return this;
    }

}