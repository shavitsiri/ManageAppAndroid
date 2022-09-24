package com.example.ManageYourSelf;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class CatalogFragment extends Fragment {

    GridView gridView;
    String[] parquetNames = {"עץ אלון","אלון טבעי","אלון מעושן","אפור","חום","אפור וינטז'","פישבון - FISHBONE","אלון למינציה"};
    String[] parquetDetails = {"דרגת שחיקה: AC5"+"\n"+"מוברש שמן ולקה" +"\n"+ "מחיר : ₪120 למטר",
            "דרגת שחיקה: AC4"+"\n"+"מוברש שמן " +"\n"+ "מחיר : 110₪ למטר",
            "דרגת שחיקה: AC5"+"\n"+"מוברש שמן ולקה" +"\n"+ "מחיר : 130₪ למטר",
            "דרגת שחיקה: AC3"+"\n"+"מוברש שמן " +"\n"+ "מחיר : 100₪ למטר",
            "דרגת שחיקה: AC4"+"\n"+"מוברש שמן " +"\n"+ "מחיר : 110₪ למטר",
            "דרגת שחיקה: AC4"+"\n"+"מוברש שמן " +"\n"+ "מחיר : 115₪ למטר",
            "דרגת שחיקה: AC5"+"\n"+"מוברש שמן ולקה " +"\n"+ "מחיר : ₪160 למטר",
            "דרגת שחיקה: AC4"+"\n"+"למינציה " +"\n"+ "מחיר : ₪125 למטר"};
    int[] parquetImages = {R.drawable.alon,R.drawable.alonnatural,R.drawable.alonsmoke,R.drawable.gray,R.drawable.brown,R.drawable.grayvintage,R.drawable.fishbone,R.drawable.alonwood};

    public CatalogFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_catalog, container, false);

        gridView = (GridView) view.findViewById(R.id.gridview);
        CatalogFragment.CustomAdapter customAdapter = new CatalogFragment.CustomAdapter();
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),GridItemActivity.class);
                intent.putExtra("name",parquetNames[position]);
                intent.putExtra("details", parquetDetails[position]);
                intent.putExtra("image", parquetImages[position]);
                startActivity(intent);
            }
        });

        return view;
    }

    public class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return parquetImages.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1 = getLayoutInflater().inflate(R.layout.row_data,null);
            TextView name = view1.findViewById(R.id.fruits);
//            TextView detailsText = view1.findViewById(R.id.detailsText);
            ImageView image = view1.findViewById(R.id.images);
            name.setText(parquetNames[position]);
//            detailsText.setText(parquetDetails[position]);
            image.setImageResource(parquetImages[position]);
            return view1;
        }
    }
}

