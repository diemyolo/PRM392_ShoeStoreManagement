package com.shoestoreproject.Data_Layer.Helper;


import android.content.Context;
import android.widget.Toast;

import com.shoestoreproject.Data_Layer.Model.Shoe;

import java.util.ArrayList;

public class ManagementCart {

    private TinyDB tinyDB;
    private Context context;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(Shoe item) {
        ArrayList<Shoe> listFood = getListCart();
        boolean existAlready = false;
        int index = -1;

        for (int i = 0; i < listFood.size(); i++) {
            if (listFood.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                index = i;
                break;
            }
        }

        if (existAlready) {
            listFood.get(index).setNumberInCart(item.getNumberInCart());
        } else {
            listFood.add(item);
        }

        tinyDB.putListObject("CartList", listFood);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Shoe> getListCart() {
        ArrayList<Shoe> list = tinyDB.getListObject("CartList");
        return list != null ? list : new ArrayList<>();
    }

    public void minusItem(ArrayList<Shoe> listFood, int position, ChangeNumberItemListener listener) {
        if (listFood.get(position).getNumberInCart() == 1) {
            listFood.remove(position);
        } else {
            listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CartList", listFood);
        listener.onChanged();
    }

    public void plusItem(ArrayList<Shoe> listFood, int position, ChangeNumberItemListener listener) {
        listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listFood);
        listener.onChanged();
    }

    public double getTotalFee() {
        ArrayList<Shoe> listFood = getListCart();
        double fee = 0.0;
        for (Shoe item : listFood) {
            fee += item.getPrice() * item.getNumberInCart();
        }
        return fee;
    }
}
