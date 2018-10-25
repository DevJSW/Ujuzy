package Tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ujuzy.ujuzy.ujuzy.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment
{

   // private ReviewsAdapter reviewsAdapter;
    private RecyclerView reviewsListRv;
    private static String BASE_URL = "https://api.ujuzy.com/";
   // ArrayList<Datum> results;
    String reviews;
    String service_id = "";
    private ProgressBar progressBar;
    private TextView noService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_reviews, container, false);

        reviewsListRv = (RecyclerView) v.findViewById(R.id.reviews_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        noService = (TextView) v.findViewById(R.id.noService);

        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {
            service_id = bundle2.getString("serviceId", null);
        }

        initProgessBar();

        return v;
    }

    private void initProgessBar()
    {

        noService.setVisibility(View.VISIBLE);
        noService.setText("No reviews posted yet!");
    }

   /* private void viewData()
    {
        reviewsAdapter = new ReviewsAdapter(getActivity(), results);

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        final LinearLayoutManager serviceLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewsListRv.setLayoutManager(serviceLayoutManager);
        reviewsListRv.setAdapter(reviewsAdapter);
    }*/
}
