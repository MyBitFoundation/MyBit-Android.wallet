package com.wallet.crypto.mybitapp.ui.widget.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.ui.widget.OnTokenClickListener;
import com.wallet.crypto.mybitapp.ui.widget.holder.TokenHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokensAdapter extends RecyclerView.Adapter<TokenHolder> {

    private final OnTokenClickListener onTokenClickListener;

    private final List<Token> items = new ArrayList<>();

    public TokensAdapter(OnTokenClickListener onTokenClickListener) {
        this.onTokenClickListener = onTokenClickListener;
    }

    @NonNull
    @Override
    public TokenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TokenHolder tokenHolder = new TokenHolder(R.layout.item_token, parent);
        tokenHolder.setOnTokenClickListener(onTokenClickListener);
        return tokenHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TokenHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void submitList(Token[] tokens) {
        items.clear();
        items.addAll(Arrays.asList(tokens));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
