package com.brentsandstrom.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by Brent on 2/4/2019.
 */

// A fragment containing the RecyclerView, which contains all the crime list items.
public class CrimeListFragment extends Fragment {

    //RecyclerView allows us to scroll through data without rendering all of it at once.
    private RecyclerView mCrimeRecyclerView;
    //The custom adapter class to make the recyclerview work.
    private CrimeAdapter mAdapter;

    //Fragments must create UI components in onCreateView, not onCreate.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    //For example, when back button pushed in the CrimeFragment view.
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //Refresh the UI to match the current data
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        //If the adapter is null create one else reset the adapter to reflect new data.
        if (mAdapter == null) {
            //Create the Crime adapter
            mAdapter = new CrimeAdapter(crimes);
            //Pass the adapter to the recycler view
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    //Holds the list_item_crime view to be used by the recyclerView. IE each object in list.
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        //Constructor. Just create the view.
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        //Pass in a crime object and set the views to match the data in the crime.
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(DateFormat.getInstance().format(mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        //Open the CrimePagerActivity to edit the crime if it's clicked on
        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    //Creates CrimeHolders as needed
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        //Create a CrimeHolder by calling the constructor
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CrimeHolder(layoutInflater, parent);
        }

        //Bind the CrimeHolder by calling bind. How we recycle
        // Note that this needs to be very efficient to keep scrolling smooth
        // You need to know the position of the crime to use
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        // Return the number of crimes
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}
