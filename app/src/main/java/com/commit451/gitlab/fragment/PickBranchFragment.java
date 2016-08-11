package com.commit451.gitlab.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commit451.easycallback.EasyCallback;
import com.commit451.gitlab.App;
import com.commit451.gitlab.R;
import com.commit451.gitlab.activity.PickBranchOrTagActivity;
import com.commit451.gitlab.adapter.BranchesAdapter;
import com.commit451.gitlab.model.api.Branch;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Pick a branch, any branch
 */
public class PickBranchFragment extends ButterKnifeFragment {

    private static final String EXTRA_PROJECT_ID = "project_id";

    public static PickBranchFragment newInstance(long projectId) {
        PickBranchFragment fragment = new PickBranchFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_PROJECT_ID, projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.list)
    RecyclerView mProjectsListView;
    @BindView(R.id.message_text)
    TextView mMessageView;

    BranchesAdapter mBranchesAdapter;

    long mProjectId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectId = getArguments().getLong(EXTRA_PROJECT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_branch, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBranchesAdapter = new BranchesAdapter(new BranchesAdapter.Listener() {
            @Override
            public void onBranchClicked(Branch entry) {
                Intent data = new Intent();
                data.putExtra(PickBranchOrTagActivity.EXTRA_REF, entry.getName());
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
        });
        mProjectsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProjectsListView.setAdapter(mBranchesAdapter);

        loadData();
    }

    @Override
    protected void loadData() {
        if (getView() == null) {
            return;
        }
        mMessageView.setVisibility(View.GONE);

        App.instance().getGitLab().getBranches(mProjectId).enqueue(new EasyCallback<List<Branch>>() {
            @Override
            public void success(@NonNull List<Branch> response) {
                if (getView() == null) {
                    return;
                }
                mBranchesAdapter.setEntries(response);
            }

            @Override
            public void failure(Throwable t) {
                Timber.e(t, null);
                if (getView() == null) {
                    return;
                }
                mMessageView.setVisibility(View.VISIBLE);
            }
        });
    }

}