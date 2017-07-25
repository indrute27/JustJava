/**
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 *
 * package com.example.android.justjava;
 *
 */

package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayQuantity();
    }

    //Below code removes the
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //get user name
        EditText nameText = (EditText) findViewById(R.id.name_view);
        String name = nameText.getText().toString();

        //check if user wants additional toppings
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whippedCream);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolateTopping);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int cost = 5;

        if (hasWhippedCream) {
            cost += 1;
        }

        if (hasChocolate) {
            cost += 2;
        }

        orderSummary(name, calculatePrice(cost), hasWhippedCream, hasChocolate);
    }

    /**
     * This method is called when the plus button is clicked.
     * It increases the amount of coffee and displays the new amount.
     */
    public void incrementCoffee(View view) {
        if (quantity < 100) {
            quantity += 1;
        }
        displayQuantity();
    }

    /**
     * This method is called when the minus button is clicked.
     * It decreases the amount of coffee and displays the new amount.
     */
    public void decrementCoffee(View view) {
        if (quantity > 0) {
          quantity -= 1;
        }
        displayQuantity();
    }

    /**
     * This method displays the given coffee amount on the screen.
     */
    private void displayQuantity() {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);
    }

    /**
     * This method calculates the total price.
     *
     * @return total cost
     */
    private int calculatePrice(int cost) {
        return quantity * cost;
    }

    /**
     * This method creates a text summary of the order
     *
     * @param name referes to the person who's ordering
     * @param price refers to the price of an individual coffee
     * @param hasWhippedCream refers to whether the user wants whipped cream or not
     * @param hasChocolate refers to whether the user wants chocolate topping or not
     *
     */
    private void orderSummary(String name, int price, boolean hasWhippedCream, boolean hasChocolate) {
        String body =  getString(R.string.order_summary_name, name);
        body += "\n" + getString(R.string.order_summary_whipped_cream, hasWhippedCream);
        body += "\n" + getString(R.string.order_summary_chocolate, hasChocolate);
        body += "\n" + getString(R.string.order_summary_quantity, quantity);
        body += "\n" + getString(R.string.order_summary_price, ("$" + price));
        body += "\n" + getString(R.string.thank_you);
        composeEmail(body, name);
    }

    /**
     * This method opens up email on the users phone and populates it with content
     */

    public void composeEmail(String body, String name) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "indrute27@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}