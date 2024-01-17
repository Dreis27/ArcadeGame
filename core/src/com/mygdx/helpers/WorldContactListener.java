package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.sprites.Bullet;
import com.mygdx.sprites.Enemy;
import com.mygdx.sprites.Ground;
import com.mygdx.sprites.InteractiveObject;

public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'beginContact'");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        
        if (fixA.getUserData() instanceof Enemy || fixB.getUserData() instanceof Enemy) {
            Fixture enemyFixture = (fixA.getUserData() instanceof Enemy) ? fixA : fixB;
            Fixture otherFixture = enemyFixture == fixA ? fixB : fixA;

        
            if ("ground".equals(otherFixture.getUserData())) {
                Enemy enemy = (Enemy) enemyFixture.getUserData();
                enemy.jump();
            }
        }

        if (fixA.getUserData() instanceof Bullet || fixB.getUserData() instanceof Bullet) {
            Fixture bulletFixture = (fixA.getUserData() instanceof Bullet) ? fixA : fixB;
            Fixture other = bulletFixture == fixA ? fixB: fixA;
            Bullet bullet = (Bullet) bulletFixture.getUserData();
            bullet.toRemove = true;

            if(other.getUserData() instanceof Enemy){
                Enemy enemy = (Enemy) other.getUserData();
                enemy.takeDamage(10);
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'endContact'");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'preSolve'");
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'postSolve'");
    }
    
}
