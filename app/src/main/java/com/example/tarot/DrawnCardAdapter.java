package com.example.tarot;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarot.model.TarotCard;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DrawnCardAdapter extends RecyclerView.Adapter<DrawnCardAdapter.ViewHolder> {

    private final List<String> positionMeanings = new ArrayList<>();
    private final List<TarotCard> drawnCards = new ArrayList<>();

    public DrawnCardAdapter(List<String> positionMeanings, List<TarotCard> drawnCards) {
        if (positionMeanings != null) {
            this.positionMeanings.addAll(positionMeanings);
        }
        if (drawnCards != null) {
            this.drawnCards.addAll(drawnCards);
        }
    }

    public void updateData(List<String> positionMeanings, List<TarotCard> drawnCards) {
        this.positionMeanings.clear();
        if (positionMeanings != null) {
            this.positionMeanings.addAll(positionMeanings);
        }
        this.drawnCards.clear();
        if (drawnCards != null) {
            this.drawnCards.addAll(drawnCards);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawn_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String posMeaning = (position < positionMeanings.size()) ? positionMeanings.get(position) : ("位置 " + (position + 1));
        holder.tvCardPosition.setText("第 " + (position + 1) + " 張 • 【" + posMeaning + "】");

        Context context = holder.itemView.getContext();

        if (position < drawnCards.size() && drawnCards.get(position) != null) {
            TarotCard card = drawnCards.get(position);

            holder.tvLockBadge.setText("🔒 已鎖定 (點擊放大)");
            holder.tvLockBadge.setBackgroundColor(ContextCompat.getColor(context, R.color.tarot_surface_variant));

            holder.tvCardName.setText(card.getName() + " (" + card.getEnglishName() + ")");
            holder.tvCardName.setVisibility(View.VISIBLE);

            // 動態載入左側 assets/cards/ 中的卡牌圖片或預設框
            loadCardImage(holder.ivCardImage, context, card);

            if (card.isReversed()) {
                holder.tvOrientation.setText("逆位 (Reversed)");
                holder.tvOrientation.setBackgroundColor(ContextCompat.getColor(context, R.color.tarot_reversed_crimson));
            } else {
                holder.tvOrientation.setText("正位 (Upright)");
                holder.tvOrientation.setBackgroundColor(ContextCompat.getColor(context, R.color.tarot_upright_green));
            }
            holder.tvOrientation.setVisibility(View.VISIBLE);

            holder.tvArcanaSuit.setText(card.getArcana() + " • " + card.getSuit());
            holder.tvArcanaSuit.setVisibility(View.VISIBLE);

            holder.tvKeywords.setText("關鍵字：" + card.getCurrentKeywords());
            holder.tvKeywords.setVisibility(View.VISIBLE);

            holder.tvDescription.setText(card.getDescription());
            holder.tvDescription.setVisibility(View.VISIBLE);

            // 點擊卡牌項目，開啟大圖與完整牌義細節彈窗
            holder.itemView.setOnClickListener(v -> showCardDetailDialog(context, card, posMeaning));

        } else {
            // Pending draw position
            holder.tvLockBadge.setText("⏳ 待抽取");
            holder.tvLockBadge.setBackgroundColor(ContextCompat.getColor(context, R.color.tarot_card_border));

            holder.ivCardImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.card_back_placeholder));
            holder.ivCardImage.setVisibility(View.VISIBLE);
            holder.ivCardImage.setRotation(0f);

            holder.tvCardName.setText("卡牌尚在牌庫中，等待抽取...");
            holder.tvOrientation.setVisibility(View.GONE);
            holder.tvArcanaSuit.setVisibility(View.GONE);
            holder.tvKeywords.setVisibility(View.GONE);
            holder.tvDescription.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(null);
        }
    }

    private void showCardDetailDialog(Context context, TarotCard card, String posMeaning) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_card_detail, null);

        TextView tvName = dialogView.findViewById(R.id.dialog_tv_card_name);
        TextView tvOrientation = dialogView.findViewById(R.id.dialog_tv_orientation);
        TextView tvArcanaSuit = dialogView.findViewById(R.id.dialog_tv_arcana_suit);
        ImageView ivImage = dialogView.findViewById(R.id.dialog_iv_card_image);
        TextView tvKeywords = dialogView.findViewById(R.id.dialog_tv_keywords);
        TextView tvDescription = dialogView.findViewById(R.id.dialog_tv_description);
        MaterialButton btnClose = dialogView.findViewById(R.id.dialog_btn_close);

        tvName.setText(card.getName() + " (" + card.getEnglishName() + ")\n【" + posMeaning + "】");

        if (card.isReversed()) {
            tvOrientation.setText("逆位 (Reversed)");
            tvOrientation.setBackgroundColor(ContextCompat.getColor(context, R.color.tarot_reversed_crimson));
        } else {
            tvOrientation.setText("正位 (Upright)");
            tvOrientation.setBackgroundColor(ContextCompat.getColor(context, R.color.tarot_upright_green));
        }

        tvArcanaSuit.setText(card.getArcana() + " • " + card.getSuit());

        // 載入卡牌大圖 (若無對應圖片則顯示預設牌背框)
        boolean hasImage = loadCardImage(ivImage, context, card);
        if (!hasImage) {
            ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.card_back_placeholder));
            ivImage.setVisibility(View.VISIBLE);
        }

        tvKeywords.setText("當前狀態關鍵字：" + card.getCurrentKeywords());
        tvDescription.setText("【牌義詳細解讀】：\n" + card.getDescription() + "\n\n正位含意：" + card.getUprightKeywords() + "\n逆位含意：" + card.getReversedKeywords());

        AlertDialog dialog = new AlertDialog.Builder(context, R.style.Theme_Tarot_Dialog)
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.tarot_surface)));
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private boolean loadCardImage(ImageView ivCardImage, Context context, TarotCard card) {
        if (ivCardImage == null || card == null) return false;
        try {
            AssetManager assetManager = context.getAssets();
            String pngPath = "cards/card_" + card.getId() + ".png";
            String jpgPath = "cards/card_" + card.getId() + ".jpg";

            InputStream is = null;
            try {
                is = assetManager.open(pngPath);
            } catch (IOException ignored) {
                try {
                    is = assetManager.open(jpgPath);
                } catch (IOException ignored2) {}
            }

            if (is != null) {
                Drawable drawable = Drawable.createFromStream(is, null);
                ivCardImage.setImageDrawable(drawable);
                ivCardImage.setVisibility(View.VISIBLE);
                ivCardImage.setRotation(card.isReversed() ? 180f : 0f); // 逆位時自動旋轉 180 度
                is.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 當尚無上傳圖片時，顯示預設卡牌框
        ivCardImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.card_back_placeholder));
        ivCardImage.setVisibility(View.VISIBLE);
        ivCardImage.setRotation(0f);
        return false;
    }

    @Override
    public int getItemCount() {
        return positionMeanings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardPosition;
        TextView tvLockBadge;
        ImageView ivCardImage;
        TextView tvCardName;
        TextView tvOrientation;
        TextView tvArcanaSuit;
        TextView tvKeywords;
        TextView tvDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardPosition = itemView.findViewById(R.id.tv_card_position);
            tvLockBadge = itemView.findViewById(R.id.tv_lock_badge);
            ivCardImage = itemView.findViewById(R.id.iv_card_image);
            tvCardName = itemView.findViewById(R.id.tv_card_name);
            tvOrientation = itemView.findViewById(R.id.tv_orientation);
            tvArcanaSuit = itemView.findViewById(R.id.tv_arcana_suit);
            tvKeywords = itemView.findViewById(R.id.tv_keywords);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
