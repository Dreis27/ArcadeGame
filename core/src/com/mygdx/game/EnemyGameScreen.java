package com.mygdx.game;

import com.mygdx.helpers.Constants;
import com.mygdx.helpers.WorldContactListener;
import com.mygdx.helpers.WorldCreator;
import com.mygdx.sprites.Enemy;
import com.mygdx.sprites.EnemyBullet;
import com.mygdx.sprites.Player;
import com.mygdx.sprites.Bullet;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class EnemyGameScreen implements Screen{
    private MyGdxGame game;
    private OrthographicCamera camera;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;

    private TextureAtlas atlas;

    private Array<Bullet> bullets;
    private Array<EnemyBullet> enemyBullets;
    private Array<Enemy> enemies;

    private float viewportWidth = 18;
    private float viewportHeight = 10;

    private float tileSize, mapWidth, mapHeight;
    private int mapWidthTiles, mapHeightTiles;
    private BitmapFont font;

    private Texture backgroundTexture, blackTexture;
    private float backgroundScaleX = 0;
    private float backgroundScaleY = 0;

    private float fade = 0f;
    public boolean startFade = false;
    private float fadeDuration = 3f;
    private float fadeInOpacity = 1.0f; 
    private float fadeInSpeed = 0.5f; 
    private ShapeRenderer shapeRenderer;

    private Random random;
    public int enemyCount;


    public EnemyGameScreen(MyGdxGame game, int roundNumber, float playerHealth, float playerWeaponStrength){

        this.game = game;
        this.camera = new OrthographicCamera();
        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("map4.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, 1/Constants.PPM);
        this.bullets = new Array<Bullet>();
        this.enemyBullets = new Array<EnemyBullet>();
        this.world = new World(new Vector2(0,-10), true);
        this.debugRenderer = new Box2DDebugRenderer();
        this.atlas = new TextureAtlas("Mario_and_Enemies.pack");

        new WorldCreator(world, map);
        this.player = new Player(world, this, playerHealth, playerWeaponStrength);
        this.blackTexture = new Texture("BlackScreen.jpg");
        this.random = new Random();
        this.enemies = new Array<Enemy>();
        this.enemyCount = 1+roundNumber;

        // create enemies for the given round (while keeping some aspects of their properties random)
        for(int i = 0; i<enemyCount; i++ ){
            int enemyHealth = 60 + random.nextInt(61) + 60*(roundNumber/3);
            float enemySpeed = 1 + random.nextInt(3+(roundNumber/4)); 
            int x = 450 + random.nextInt(300);
            int y = 300;

            enemies.add(new Enemy(world, x,y, enemySpeed, enemyHealth, player, this));
        }

        this.camera.setToOrtho(false, 18, 10);
        this.world.setContactListener(new WorldContactListener());

        this.tileSize = 16;
        this.mapWidthTiles = map.getProperties().get("width", Integer.class);
        this.mapHeightTiles = map.getProperties().get("height", Integer.class);
        this.mapWidth = mapWidthTiles * tileSize / Constants.PPM;
        this.mapHeight = mapHeightTiles * tileSize / Constants.PPM;

    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1);
         
        // set background image properties
        backgroundTexture = new Texture("9.png");
        float mapPixelWidth = mapWidthTiles * tileSize;
        float mapPixelHeight = mapHeightTiles * tileSize;
        backgroundScaleX = mapPixelWidth / backgroundTexture.getWidth()/Constants.PPM;
        backgroundScaleY = mapPixelHeight / backgroundTexture.getHeight()/Constants.PPM;
    }

    @Override
    public void render(float delta) {
        if (fadeInOpacity > 0) {
            fadeInOpacity -= fadeInSpeed * delta;
            fadeInOpacity = Math.max(fadeInOpacity, 0.0f);
        }

        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
        
        // background
        game.getBatch().draw(backgroundTexture, 0, 0, backgroundTexture.getWidth() * backgroundScaleX, backgroundTexture.getHeight() * backgroundScaleY);
        game.getBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        // palyer's health bar (top right)
        player.drawHealthBar(game.getBatch(), font);

        game.getBatch().end();

        renderer.render();
        debugRenderer.render(world, camera.combined);
        game.getBatch().setProjectionMatrix(camera.combined);  

        game.getBatch().begin();
        player.draw(game.getBatch());

        for (Bullet bullet : bullets) {
           bullet.draw(game.getBatch());
        }
        for (EnemyBullet bullet : enemyBullets) {
            bullet.draw(game.getBatch());
        }
        for (Enemy enemy : enemies) {
            enemy.drawHealthBar(game.getBatch());
            enemy.draw(game.getBatch());
        }
        game.getBatch().end();

        // add fade out effect after player crashed
        if (startFade) {
            fade += delta / fadeDuration;
            if (fade > 1) {
                fade = 1;
                //this.dispose();
                this.reset();
                game.setScreen(new GameOverScreen(game));
            }
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, fade);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.getBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        game.getBatch().begin();
        player.drawHealthBar(game.getBatch(), font);
        game.getBatch().end();

        // fade in effect
        if(fadeInOpacity>0){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.getBatch().begin();
            game.getBatch().setColor(1, 1, 1, fadeInOpacity);
            game.getBatch().draw(blackTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.getBatch().setColor(1, 1, 1, 1); // reset colour
            game.getBatch().end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void update(float dt){
        handleInput(dt);

        world.step(1/60f, 6, 2);
        cameraUpdate();
        game.getBatch().setProjectionMatrix(camera.combined);
        player.update(dt);

        for (Enemy enemy : enemies) {
            enemy.update(dt);
        }

        for (int i = 0; i < bullets.size; i++) {
            Bullet bullet = bullets.get(i);
            bullet.update(dt);
                
                if (bullet.getToRemove()) {
                    bullets.removeIndex(i);
                    world.destroyBody(bullet.getBody()); 
                    i--;
                }
        }
        for (int j = 0; j < enemyBullets.size; j++) {
            EnemyBullet bullet = enemyBullets.get(j);
            bullet.update(dt);
                
                if (bullet.getToRemove()) {
                    enemyBullets.removeIndex(j);
                    world.destroyBody(bullet.getBody()); 
                    j--;
                }
        }

        // move back to spaceship game mode if player has defeated all enemies
        if(enemyCount==0){
            fade += dt / fadeDuration;
            if (fade > 1) {
                fade = 1;
                game.spaceshipScreen.setPlayerHealth(player.getCurrentHealth());
                game.spaceshipScreen.restart(true);
                game.spaceshipScreen.setDisposeEnemyScreen(this);
                game.setScreen(game.spaceshipScreen);
            }
        }
        
        renderer.setView(camera);
    }

    private void cameraUpdate(){
        if(!player.getIsDefeated()){
            Vector3 position = camera.position;

            float camMinX = viewportWidth / 2;
            float camMaxX = mapWidth - viewportWidth / 2;
            float camMinY = viewportHeight / 2;
            float camMaxY = mapHeight - viewportHeight / 2;

            //making sure we can't see the area outside of the map when player comes closer to the edge of the map
            position.x = MathUtils.clamp(player.getBody().getPosition().x, camMinX, camMaxX);
            position.y = MathUtils.clamp(player.getBody().getPosition().y, camMinY, camMaxY);
            camera.position.set(position);
        }
        camera.update();
}


    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            player.getBody().applyLinearImpulse(new Vector2(0,4f), player.getBody().getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.D) && player.getBody().getLinearVelocity().x <=3 )
            player.getBody().applyLinearImpulse(new Vector2(0.3f, 0), player.getBody().getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.A) && player.getBody().getLinearVelocity().x >=-3 )
            player.getBody().applyLinearImpulse(new Vector2(-0.3f, 0), player.getBody().getWorldCenter(), true);
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            player.shoot();
        }
        
            
    }
    
    private void reset(){
        game.spaceshipScreen.setPlayerHealth(100);
        game.spaceshipScreen.setWeaponStrength(15);
        game.spaceshipScreen.setAmountOfCrashes(0);
        game.spaceshipScreen.setScore(0);
        game.spaceshipScreen.setGameOver(false);
        game.spaceshipScreen.setFadeOut(false);
        game.spaceshipScreen.setFadeOpacity(0f);
        game.spaceshipScreen.setIsBlinking(false);
        game.spaceshipScreen.clearCandies();
        game.spaceshipScreen.clearEnemies();
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void addEnemyBullet(EnemyBullet bullet) {
        enemyBullets.add(bullet);
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        map.dispose();
        blackTexture.dispose();
        renderer.dispose();
        world.dispose();
        debugRenderer.dispose();
        font.dispose();
        backgroundTexture.dispose();
        atlas.dispose();
        shapeRenderer.dispose();
        player.dispose();
       
    }
    
}
