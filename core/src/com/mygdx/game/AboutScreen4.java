package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.helpers.SoundManager;


public class AboutScreen4 extends AboutScreen {
    private TextButton nextButton;
    private TextButton prevButton;

    public AboutScreen4(SpaceBlastGame game, SoundManager sounds) {
        super(game, sounds, "aboutAsteroid1.png");
    }

    public void createButtons() {
        nextButton = new TextButton(">", game.getMenu().getButtonStyle());
        nextButton.addListener(new ChangeListener() {                
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.getAboutScreen5());
                sounds.playButton();
            }
        });
        prevButton = new TextButton("<", game.getMenu().getButtonStyle());
        prevButton.addListener(new ChangeListener() {                
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.getAboutScreen3());
                sounds.playButton();
            }
        });
    }
    @Override
    public void setupButtons() {
        nextButton.setPosition(Gdx.graphics.getWidth()-70, Gdx.graphics.getHeight()-60);
        nextButton.setHeight(40);
        nextButton.setWidth(40);

        prevButton.setPosition(Gdx.graphics.getWidth()-120, Gdx.graphics.getHeight()-60);
        prevButton.setHeight(40);
        prevButton.setWidth(40);

        stage.addActor(nextButton);
        stage.addActor(prevButton);
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
}
