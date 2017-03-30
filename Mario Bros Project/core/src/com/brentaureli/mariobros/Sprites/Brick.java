package com.brentaureli.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.brentaureli.mariobros.MarioBros;
import com.brentaureli.mariobros.Scenes.Hud;
import com.brentaureli.mariobros.Screens.PlayScreen;
import com.brentaureli.mariobros.items.ItemDef;
import com.brentaureli.mariobros.items.Star;

/**
 * Created by Alexander on 09/01/2017.
 */
public class Brick extends InteractiveTileObject {
    //constructor
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public  Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("52571");
        fixture.setUserData(this);
        //constructor
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if (object.getProperties().containsKey("star")) {
            Gdx.app.log("aqui", "la estrella");
            screen.spawItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioBros.PPM),
                    Star.class));
            MarioBros.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();

            getCell().setTile(tileSet.getTile(BLANK_COIN));
            Hud.addScore(500);
        }
        else {
            if (mario.isBig() || mario.isFire()) {
                Gdx.app.log("Brick", "Collision");
                setCategoryFilter(MarioBros.DESTROYED_BIT);
                getCell().setTile(null);
                Hud.addScore(200);
                MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
            }
            else {
                MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
            }
        }
    }
}
