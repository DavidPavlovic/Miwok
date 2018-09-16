package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorFragment extends Fragment {

    private MediaPlayer md = new MediaPlayer();
    private AudioManager am;

    private AudioManager.OnAudioFocusChangeListener mAudioFCListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if(i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                md.pause();
                md.seekTo(0);
            }else if(i == AudioManager.AUDIOFOCUS_GAIN) {
                md.start();
            }else if(i == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };


    public ColorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);
        am = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("red","weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("green","chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown","ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray","ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black","kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white","kelelli", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("dusty yellow","ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow","chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_colors);

        ListView listView = (ListView)rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                releaseMediaPlayer();
                Word word = words.get(i);
                int result = am.requestAudioFocus(mAudioFCListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    md = MediaPlayer.create(getActivity(), word.getAudioId());
                    md.start();

                    md.setOnCompletionListener(mCompletionListener);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    public void releaseMediaPlayer() {
        if(md != null) {
            md.release();
            md = null;
            am.abandonAudioFocus(mAudioFCListener);
        }
    }
}
