package com.brentaureli.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brentaureli.mariobros.MarioBros;
import com.brentaureli.mariobros.Screens.PlayScreen;
import com.brentaureli.mariobros.Sprites.Brick;
import com.brentaureli.mariobros.Sprites.Coin;
import com.brentaureli.mariobros.Sprites.Enemy;
import com.brentaureli.mariobros.Sprites.Goomba;
import com.brentaureli.mariobros.Sprites.SecretCoin;
import com.brentaureli.mariobros.Sprites.Turtle;

/**
 * Created by Alexander on 09/01/2017.
 */
public class B2WorldCreator {
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;
    //private Array<Coquito> coquitos;
    //constructor
    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //para crear suelos, cuerpos y accesorios en cuadros
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

        }

        //para crear las tuberias en cuadros
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
            body.createFixture(fdef);

        }

        //para crear las cajas de monedas en cuadros
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            //Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, object);
            //new Coin(screen, rect);
        }

        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){

            new SecretCoin(screen, object);
        }

        //para crear los ladrillos en cuadros
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            //Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Brick(screen, object);
            //new Brick(screen, rect);
        }

        //crear los goombas
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }

        //crear las totugas
        turtles = new Array<Turtle>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }

        //crear los cocos
        /*coquitos = new Array<Coquito>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            coquitos.add(new Coquito(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }*/
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        //enemies.addAll(coquitos);
        return enemies;
    }

}
