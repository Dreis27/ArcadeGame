package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.SpaceBlastGame;

public class SoundManager {
    private Sound buttonClick, shot, hit, jump, explosion;
    private Music background1, background2;
    private SpaceBlastGame game;

    public SoundManager (SpaceBlastGame game){
        this.game = game;
        buttonClick = Gdx.audio.newSound(Gdx.files.internal("blipSelect.wav"));
        shot = Gdx.audio.newSound(Gdx.files.internal("laserShoot.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("hitHurt.wav"));
        jump  = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
        background1 = Gdx.audio.newMusic(Gdx.files.internal("Liu.mp3"));
        background1.setLooping(true);
        background2 = Gdx.audio.newMusic(Gdx.files.internal("Liu2.mp3"));
        background2.setLooping(true);
        updateMusicVol();
    }

    public Music getBackground1(){
        return background1;
    }

    public Music getBackground2(){
        return background2;
    }

    public void playButton() {
        buttonClick.play(game.getGameVol()/3);
    }

    public void playShotSound() {
        shot.play(game.getGameVol()/6);
    }

    public void playEnenyShotSound() {
        shot.play(game.getGameVol()/10);
    }

    public void playHit(){
        hit.play(game.getGameVol()/2);
    }
    public void playEnemyHit(){
        hit.play(game.getGameVol()/4);
    }

    public void playJump(){
        jump.play(game.getGameVol()/4);
    }
    public void playEnemyJump(){
        jump.play(game.getGameVol()/7);
    }

    public void playExplosion(){
        explosion.play(game.getGameVol());
    }

    public void updateMusicVol(){
        background1.setVolume(game.getVol()*3);
        background2.setVolume(game.getVol());
    }

    public void restartMusic1() {
        if (background1.isPlaying()) {
            background1.stop();
        }
        background1.play();
    }

    public void playIfNotPlaying(){
        if(!background1.isPlaying()) background1.play();
    }


    public void dispose(){
        buttonClick.dispose();
        shot.dispose();
        jump.dispose();
        explosion.dispose();
        hit.dispose();
    }
}
