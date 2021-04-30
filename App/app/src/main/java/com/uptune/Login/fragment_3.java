package com.uptune.Login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uptune.Account.Account;
import com.uptune.R;


public class fragment_3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_3, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btn = (ImageButton) view.findViewById(R.id.go_account);
        TextView bubble_1 = (TextView) view.findViewById(R.id.bubble_1);
        TextView bubble_2 = (TextView) view.findViewById(R.id.bubble_2);
        TextView bubble_3 = (TextView) view.findViewById(R.id.bubble_3);
        TextView bubble_4 = (TextView) view.findViewById(R.id.bubble_4);
        btn.setVisibility(View.GONE);
        BubbleAnim(bubble_1, 1000);
        BubbleAnim(bubble_2, 700);
        BubbleAnim(bubble_3, 1200);
        BubbleAnim(bubble_4, 900);


        Handler handler = new Handler();
        handler.postDelayed(() -> ButtonAnim(btn, 1000), 4000);

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Account.class);
            /*
            intent.putExtra("name", name);
            intent.putExtra("username", username);
            intent.putExtra("email", mail);
            intent.putExtra("phone", phone);
            */
            startActivity(intent);
        });
    }



    private void ButtonAnim(ImageButton btn, int duration) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(btn, "alpha", 0, 1f);
        btn.setVisibility(View.VISIBLE);
        fadeOut.setDuration(duration);
        fadeOut.start();
    }

    private void BubbleAnim(TextView bubble, int duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(bubble, "scaleX", 0.9f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(bubble, "scaleY", 0.9f, 1.1f);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
        AnimatorSet scaleAnim = new AnimatorSet();
        scaleAnim.setDuration(duration);
        scaleAnim.play(scaleX).with(scaleY);
        scaleAnim.start();
    }
}