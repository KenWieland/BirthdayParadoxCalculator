package com.kenwieland.birthdayparadoxcalculator;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


public class MainActivity extends Activity {

    private static final int AMOUNTOFDAYS = 365;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView)findViewById(R.id.textView_result);
        final EditText editText = (EditText)findViewById(R.id.editText_input);
        Button button = (Button)findViewById(R.id.button_calculateProbability);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sAmountOfPeople = editText.getText().toString();
                int iAmountOfPeople = Integer.parseInt(sAmountOfPeople);

                double resultPercent = calculate(iAmountOfPeople);
                if (resultPercent == 100 || resultPercent == 0)
                    textView.setText((int)resultPercent + "%");
                else
                    textView.setText("≈" + resultPercent + "%");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //calculate a binomial n over k
    private static BigDecimal binomial(int n, int k)
    {
        if (k>n-k)
            k=n-k;

        BigDecimal b = new BigDecimal("1");
        for (int i=1, m=n; i<=k; i++, m--)
            b = b.multiply(new BigDecimal("" + m)).divide(new BigDecimal("" + i));
        return b;
    }

    //Calculates n!
    public static BigDecimal factorial(int n) {
        BigDecimal fact = new BigDecimal("1"); // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact = fact.multiply(new BigDecimal("" + i));
        }
        return fact;
    }

    /* Calculates the Birthday problem
     * n! * (365 over n)
     * divided by
     * 365 ^n
     */
    private static double calculate(int amountOfPeople) {
        //n! * (365 over n)
        BigDecimal calculation_top = binomial(AMOUNTOFDAYS, amountOfPeople).multiply(new BigDecimal("" + factorial(amountOfPeople)));
        //365^n
        BigDecimal calculation_bottom = new BigDecimal("" + AMOUNTOFDAYS);
        calculation_bottom = calculation_bottom.pow(amountOfPeople);


        BigDecimal result = calculation_top.divide(calculation_bottom, 5, RoundingMode.HALF_UP);

        double newResult = result.doubleValue();
        newResult *= 100000;
        newResult = Math.round(newResult);
        newResult /= 100000;

        newResult = (1 - newResult) * 100;

        return newResult;
    }
}
