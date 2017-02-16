package com.brentaureli.mariobros.items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Alexander on 03/02/2017.
 */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    //constructor
    public ItemDef(Vector2 position, Class<?> type){
        this.position = position;
        this.type = type;
    }
}
