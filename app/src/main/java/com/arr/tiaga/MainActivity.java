package com.arr.tiaga;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<Mobile> mMobiles = new ArrayList<>(10);
    ArrayList<String>mBrands = new ArrayList<>();
    ArrayList<String>mModels = new ArrayList<>();
    double mTotalPrice;
    Mobile mMobile;

    Spinner mSpinner1,mSpinner2;
    RadioButton mRadioButton1,mRadioButton2,mRadioButton3;
    RadioGroup mRadioGroup;
    LinearLayout mLinearLayoutCheckbox;
    CheckBox mCheckBox1, mCheckBox2, mCheckBox3;
    ImageView mImageView;
    TextView mTextView1,mTextView2,mTextView3;
    SeekBar mSeekBar;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMobiles.add(new Mobile("Samsung","m12",12000.0,100));
        mMobiles.add(new Mobile("Redmi","Note10",12000.0,100));
        mMobiles.add(new Mobile("Realme","Narzo30",13000.0,100));
        mMobiles.add(new Mobile("Samsung","m21",14000,100));
        mMobiles.add(new Mobile("Samsung","m31",16000,100));
        mMobiles.add(new Mobile("Redmi","Note10Pro",14000.0,100));
        mMobiles.add(new Mobile("Redmi","Note10ProMAx",17000.0,100));
        mMobiles.add(new Mobile("Realme","realme8",14000.0,100));
        mMobiles.add(new Mobile("Realme","realme8Pro",17000.0,100));
        mMobiles.add(new Mobile("Poco","X3Pro",18000.0,100));

        mSpinner1 = findViewById(R.id.spinnerBrand);
        mSpinner2 = findViewById(R.id.spinnerModel);
        mRadioButton1 = findViewById(R.id.option1);
        mRadioButton2 = findViewById(R.id.option2);
        mRadioButton3 = findViewById(R.id.option3);
        mRadioGroup = findViewById(R.id.radioGroup);
        mLinearLayoutCheckbox = findViewById(R.id.checkLayout);
        mCheckBox1 = findViewById(R.id.screenGuard);
        mCheckBox2 = findViewById(R.id.backCover);
        mCheckBox3 = findViewById(R.id.earPhones);
        mImageView = findViewById(R.id.imgDisplay);
        mTextView1 = findViewById(R.id.txtFeatures);
        mTextView2 = findViewById(R.id.txtPrice);
        mTextView3 = findViewById(R.id.txtQuantity);
        mSeekBar = findViewById(R.id.seekQuantity);
        mButton = findViewById(R.id.btnPlaceOrder);

        for(Mobile mobile:mMobiles){
            mBrands.add(mobile.getBrand());
        }
        mBrands = new ArrayList<String>(new LinkedHashSet<String>(mBrands));

        ArrayAdapter brandAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,mBrands);
        mSpinner1.setAdapter(brandAdapter);

        mSpinner1.setOnItemSelectedListener(this);
        mSpinner2.setOnItemSelectedListener(this);

        mRadioButton1.setOnClickListener(new RadioButtonEvents());
        mRadioButton2.setOnClickListener(new RadioButtonEvents());
        mRadioButton3.setOnClickListener(new RadioButtonEvents());

        mCheckBox1.setOnCheckedChangeListener(new CheckBoxEvents());
        mCheckBox2.setOnCheckedChangeListener(new CheckBoxEvents());
        mCheckBox3.setOnCheckedChangeListener(new CheckBoxEvents());

        /*mCheckBox1.setOnClickListener(new CheckBoxEvent());
        mCheckBox2.setOnClickListener(new CheckBoxEvent());
        mCheckBox3.setOnClickListener(new CheckBoxEvent());*/

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                double price = 0.0;
                price = mTotalPrice*i;
                mTextView3.setText(String.valueOf(i));
                mTextView2.setText(String.format("%.2f",price));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = mTextView2.getText().toString();
                clear();
                Toast.makeText(getApplicationContext(), "Order of Value: " + price + " Placed Successfully",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void clear(){
        mSpinner1.setSelection(0);
        mSpinner2.setSelection(0);
        mRadioGroup.clearCheck();
        mCheckBox1.setChecked(false);
        mCheckBox2.setChecked(false);
        mCheckBox3.setChecked(false);
        mSeekBar.setProgress(0);
        mTextView1.setText("");
        mTextView2.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spinnerBrand:
                String brandSelected = mBrands.get(i);
                mModels.clear();
                for(Mobile mobile:mMobiles)
                    if (mobile.getBrand().equalsIgnoreCase(brandSelected)) mModels.add(mobile.getModel());
                ArrayAdapter modelAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,mModels);
                mSpinner2.setAdapter(modelAdapter);
            break;
            case R.id.spinnerModel:
                String modelSelected = mModels.get(i).toLowerCase();
                for(Mobile mobile:mMobiles)
                    if (mobile.getModel().equalsIgnoreCase(modelSelected)) {
                        mMobile = mobile;
                        break;
                    }
                mTextView1.setText("Brand: "+mMobile.getBrand()+"\n"+"Model: "+mMobile.getModel());
                mTotalPrice = mMobile.getPrice();
                mTextView2.setText(String.format("%.2f",mTotalPrice));
                int res = getResources().getIdentifier(modelSelected, "drawable", this.getPackageName());
                mImageView.setImageResource(res);
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    private class RadioButtonEvents implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            double price = 0;
            switch (view.getId()){
                case R.id.option1:
                    price += mMobile.getPrice();;
                break;
                case R.id.option2:
                    price = mMobile.getPrice()+1500;
                break;
                case R.id.option3:
                    price = mMobile.getPrice()+3000;
                break;
            }
            mTotalPrice = price;
            mTextView2.setText(String.format("%.2f",mTotalPrice));
        }
    }


    private class CheckBoxEvents implements CheckBox.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            double price = 0.0;
            if(buttonView.isChecked())
                price += 250.0;
            else
                price -= 250.0;
            mTotalPrice += price;
            mTextView2.setText(String.format("%.2f",mTotalPrice));
        }
    }


    /*private class CheckBoxEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            double price = Double.parseDouble(mTextView2.getText().toString());
            switch (view.getId()){
                case R.id.screenGuard:
                    if(mCheckBox1.isChecked())
                        price += 250.0;
                    else
                        price -= 250.0;
                break;   
                case R.id.backCover:
                    if(mCheckBox2.isChecked())
                        price += 250.0;
                    else
                        price -= 250.0;
                break;
                case R.id.earPhones:
                    if(mCheckBox3.isChecked())
                        price += 250.0;
                    else
                        price -= 250.0;
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + view.getId());
            }
            mTextView2.setText(String.format("%.2f",price));
        }*/
}