package com.mygdx.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.helpers.Constants;

public class EnemyBullet extends Sprite {
    private World world;
    private Player player;
    private Body body;
    private float speed;
    private boolean facingRight;

    private Vector2 startPosition;
    private float distanceLimit;
    private float distanceTraveled;
    public boolean toRemove;
    private Vector2 direction;

    public EnemyBullet(World world, float x, float y, boolean facingRight, float speed, Player player) {
        this.world = world;
        this.player = player;
        this.speed = speed;
        this.facingRight = facingRight;
        this.startPosition = new Vector2(x, y);
        this.distanceLimit = 20;
        this.distanceTraveled = 0;
        this.toRemove = false;

        direction = new Vector2(player.body.getPosition().x - x, player.body.getPosition().y - y).nor();

        defineBullet(x, y);
    }

    private void defineBullet(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x , y );
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 0; 
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / Constants.PPM);

        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_ENEMY_BULLET;
        fixtureDef.filter.maskBits = Constants.CATEGORY_GROUND | Constants.CATEGORY_PLAYER;
        
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();

        body.setLinearVelocity(direction.scl(speed));
        
        //body.setLinearVelocity((facingRight ? 1 : -1) * speed, 0); 
    }

    public void update(float dt) {
        
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

        distanceTraveled = startPosition.dst(body.getPosition().x, body.getPosition().y);
        if (distanceTraveled > distanceLimit) {
            this.toRemove = true; 
        }
    }

    public Body getBody(){
        return body;
    }
}
