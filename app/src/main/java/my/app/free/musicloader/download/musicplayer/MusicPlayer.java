package my.app.free.musicloader.download.musicplayer;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import my.app.free.musicloader.Bot4Shared;
import my.app.free.musicloader.ModelMusic;

import static my.app.free.musicloader.download.musicplayer.LoopOption.NoLoop;
import static my.app.free.musicloader.download.musicplayer.LoopOption.RepeatAll;
import static my.app.free.musicloader.download.musicplayer.LoopOption.RepeatOne;

/**
 * Created by loki on 2014. 6. 4..
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener {
    private String TAG = "MusicPlayer";
    private ArrayList<ModelMusic> _musics;
    private MediaPlayer _mediaPlayer;
    private int _currIndex = 0;
    private ModelMusic _currPlaying;

    private PlayOption _playOption = PlayOption.InOrder;
    private LoopOption _loopOption = NoLoop;
    private ArrayList<Integer> _playerOrder;

    public MusicPlayer() {
        _musics = new ArrayList<ModelMusic>();
        _mediaPlayer = new MediaPlayer();
    }

    public int GetNumberOfMusic() {
        return _musics.size();
    }

    public void AddMusic(ModelMusic music) {
        _musics.add(music);
        RefreshOrder();
    }

    public void Play(int index) {
        try {
            if (_mediaPlayer.isPlaying())
                _mediaPlayer.stop();

            int i = _playerOrder.indexOf(index);
            String path = Bot4Shared.GeneratePath(_musics.get(i)._title);
            Log.d(TAG, path);
            _currPlaying = _musics.get(i);

            _mediaPlayer.reset();
            _mediaPlayer.setDataSource(path);
            _mediaPlayer.prepare();
            _mediaPlayer.start();
            _mediaPlayer.setOnCompletionListener(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Play() {
        Play(_currIndex);
    }

    public void Play(ModelMusic music) {
        int i = _musics.indexOf(music);
        Play(i);
    }

    public void Pause() {
        if (_mediaPlayer.isPlaying())
            _mediaPlayer.pause();
        else
            _mediaPlayer.start();
    }

    public void Seek(double ratio) {
        if( ! _mediaPlayer.isPlaying() )
            return;

        int duration = _mediaPlayer.getDuration();
        duration *= ratio;
        _mediaPlayer.seekTo(duration);
    }

    public void RemoveMusic(ModelMusic music) {
        boolean isRemovedCurrentPlaying = false;
        ModelMusic curr = GetCurrentPlaying();
        if( curr._title == music._title ) {
            Pause();
            isRemovedCurrentPlaying = true;
        }


        int i = _musics.indexOf(music);
        _musics.remove(music);

        if (_currIndex == _playerOrder.indexOf(i)) {
            _playerOrder.remove(i);
            if (_currIndex == _playerOrder.size())
                RefreshOrder();
        }

        if( isRemovedCurrentPlaying && _loopOption == RepeatAll )
            Play();
    }

    public void SetPlayOption(PlayOption option) {
        _playOption = option;
    }

    public void SetLoopOption(LoopOption option) {
        _loopOption = option;
    }

    public void RefreshOrder() {
        ArrayList<Integer> newOrder = new ArrayList<Integer>();
        for (int i = 0; i < _musics.size(); i++) {
            newOrder.add(i);
        }

        if (_playOption == PlayOption.Random) {
            Collections.shuffle(newOrder);
        }
        _playerOrder = newOrder;
        _currIndex = 0;
    }

    public void Shuffle() {
        ArrayList<Integer> newOrder = new ArrayList<Integer>();
        for (int i = 0; i < _musics.size(); i++) {
            newOrder.add(i);
        }

        Collections.shuffle(newOrder);
        _playerOrder = newOrder;
    }

    public ModelMusic GetCurrentPlaying() {
        return _currPlaying;
    }

    public void Next() {
        _currIndex++;
        if (_currIndex == _playerOrder.size())
            RefreshOrder();

        Play(_currIndex);
    }

    public void Prev() {
        _currIndex--;
        if (_currIndex < 0)
            _currIndex = _playerOrder.size() - 1;

        Play(_currIndex);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        switch( _loopOption ) {
            case NoLoop:
                _mediaPlayer.stop();
                _currPlaying = null;
                break;
            case RepeatOne:
                _mediaPlayer.seekTo(0);
                _mediaPlayer.start();
                break;
            case RepeatAll:
                Next();
                break;
        }
    }
}
