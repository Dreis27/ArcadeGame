package com.mygdx.game;

import com.badlogic.gdx.ScreenAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MenuScreen implements Screen {
    private MyGdxGame game;
    private Stage stage;
    private Skin skin;
    private int highScore = 0;
    
    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // high score lable
        String highScoreText = "High Score: " + highScore;
        Label highScoreLabel = new Label(highScoreText, skin);
        //buttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton enemyModeButton = new TextButton("Go to Enemy Mode", skin); // the button to test the enemy game mode
        TextButton exitButton = new TextButton("Exit", skin);

        // button listeners
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.spaceshipScreen);
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override                  
            public void changed(ChangeEvent event, Actor actor) {
                // settings
            }
        });

        enemyModeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EnemyGameScreen(game,5, 50000, 60, 50000)); // Switch to Enemy Game Screen
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // when pressed 'exit'
            }
        });

        // show high score
        table.add(highScoreLabel).colspan(2).padBottom(20).row();
        // add buttons to the table
        table.add(playButton).fillX().uniformX().padBottom(10);
        table.row();
        table.add(settingsButton).fillX().uniformX().padBottom(10);
        table.row();
        table.add(enemyModeButton).fillX().uniformX().padBottom(10);
        table.row();
        table.add(exitButton).fillX().uniformX();
    }
    

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public int getHighScore(){
        return highScore;
    }
    public void setHighScore(int score){
        this.highScore = score;
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }
}

