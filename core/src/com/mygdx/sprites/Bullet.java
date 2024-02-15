package com.mygdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.helpers.Constants;

public class Bullet extends Sprite {
    private World world;
    private Body body;
    private float speed, distanceTraveled;
    private boolean facingRight, toRemove;

    private Vector2 startPos;
    private Texture bulletTexture;
    private TextureRegion bulletTextureRegion;

    public Bullet(World world, float x, float y, boolean facingRight, float speed) {
        this.world = world;
        this.speed = speed;
        this.facingRight = facingRight;
        this.startPos = new Vector2(x, y);
        this.distanceTraveled = 0;
        this.toRemove = false;
        this.bulletTexture = new Texture("bullet-1.png.png");
        this.bulletTextureRegion = new TextureRegion(bulletTexture);

        // add drawn texture to the bullet body
        setRegion(bulletTextureRegion);
        setSize(bulletTexture.getWidth() / (3*Constants.PPM), bulletTexture.getHeight() / (3*Constants.PPM));
        setOrigin(getWidth() / 2, getHeight() / 2);
        defineBullet(x, y);
    }

    private void defineBullet(float x, float y) {
        // defining body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x , y );
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;
        bodyDef.gravityScale = 0; 
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(1 / Constants.PPM);

        // define properties of a fixture of the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BULLET;
        fixtureDef.filter.maskBits = Constants.CATEGORY_GROUND | Constants.CATEGORY_ENEMY;
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
        body.setLinearVelocity((facingRight ? 1 : -1) * speed, 0); 
    }

    public void update(float dt) {
        
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

        // check if bullet has travelled set distance and we need to remove it
        distanceTraveled = startPos.dst(body.getPosition().x, body.getPosition().y);
        if (distanceTraveled > Constants.bulletDistanceLimit) {
            this.toRemove = true; 
        }
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    public Body getBody(){
        return body;
    }

    public void setToRemove(boolean remove){
        this.toRemove = remove;
    }
    public boolean getToRemove(){
        return toRemove;
    }

    public void dispose(){
        getTexture().dispose();
    }
}

