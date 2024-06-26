package com.mygdx.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class InteractiveObject {
    public World world;
    public TiledMap map;
    public TiledMapTile tile;
    public Rectangle bounds;
    public Body body;

    // for objects in Tiled map
    public InteractiveObject(World world, TiledMap map, Rectangle bounds){
        this.world = world;
        this.map = map;
        this.bounds = bounds;
    }
    
}
