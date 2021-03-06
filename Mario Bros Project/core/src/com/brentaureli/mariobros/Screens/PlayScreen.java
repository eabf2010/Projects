package com.brentaureli.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brentaureli.mariobros.MarioBros;
import com.brentaureli.mariobros.Scenes.Hud;
import com.brentaureli.mariobros.Sprites.Enemy;
import com.brentaureli.mariobros.Sprites.Mario;
import com.brentaureli.mariobros.Tools.B2WorldCreator;
import com.brentaureli.mariobros.Tools.WorldContactListener;
import com.brentaureli.mariobros.items.Flower;
import com.brentaureli.mariobros.items.Item;
import com.brentaureli.mariobros.items.ItemDef;
import com.brentaureli.mariobros.items.Life;
import com.brentaureli.mariobros.items.Mushroom;
import com.brentaureli.mariobros.items.Star;
import java.util.concurrent.LinkedBlockingQueue;


//import jdk.internal.util.xml.impl.Input;

/**
 * Created by Alexander on 28/12/2016.
 */
public class PlayScreen implements Screen{
    private MarioBros game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Mario player;
    //private Goomba goomba;

    //music
    private Music music;

    private boolean marioIsRuning;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    //constructor
    public PlayScreen(MarioBros game){
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gamecam);

        hud = new Hud(game.batch);

        marioIsRuning = false;

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        //music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        //goomba = new Goomba(this, 5.64f, .16f);
    }

    public void spawItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();

            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
            if(idef.type == Flower.class){
                items.add(new Flower(this, idef.position.x, idef.position.y));
            }
            if(idef.type == Star.class){
                items.add(new Star(this, idef.position.x, idef.position.y));
            }
            if(idef.type == Life.class){
                items.add(new Life(this, idef.position.x, idef.position.y));
            }
        }
    }

    //methods
    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {
    }

    public void handleInput(float dt){
        /*if(Gdx.input.justTouched())
            player.b2body.applyLinearImpulse(0, 4f, 0, 0, true);*/
     /*if(player.currentState != Mario.State.DEAD) {
         if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
             player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
         if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
             player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
         if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
             player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
     }*/

        if(player.currentState != Mario.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.jump();
            /*if (Gdx.input.isKeyJustPressed(Input.Keys.A))
                player.jump();*/
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                if (player.isBig() || player.isFire())
                    player.flexed();

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                if (player.isFire())
                    player.fire();
            }

            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                marioIsRuning = true;
                player.isRunning();
            }
            else {
                marioIsRuning = false;
                player.isNotRunning();
            }

            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (marioIsRuning && player.b2body.getLinearVelocity().x <= 4)
                    player.b2body.applyLinearImpulse(new Vector2(0.24f, 0), player.b2body.getWorldCenter(), true);
                else if(player.b2body.getLinearVelocity().x <= 2)
                    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

                if (marioIsRuning && player.b2body.getLinearVelocity().x >= -4)
                    player.b2body.applyLinearImpulse(new Vector2(-0.24f, 0), player.b2body.getWorldCenter(), true);
                else if(player.b2body.getLinearVelocity().x >= -2)
                    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            /*if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2 && Gdx.input.isKeyPressed(Input.Keys.S))
                player.b2body.applyLinearImpulse(new Vector2(-1.8f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2 && Gdx.input.isKeyPressed(Input.Keys.S))
                player.b2body.applyLinearImpulse(new Vector2(1.8f, 0), player.b2body.getWorldCenter(), true);
            else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);*/

        }
    }

    /*public void checkGameOver()
    {
        if(player.currentState == Mario.State.DEAD && player.getStateTimer()
                > 3)
        {
            dispose();
            game.setScreen(new GameOverScreen(game));
        }
    }*/

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();

        world.step(1 / 62f, 6, 2);

        player.update(dt);
        //goomba.update(dt);
        /*for (Enemy enemy : creator.getGoombas()){
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / MarioBros.PPM)
                enemy.b2body.setActive(true);
        }*/

        for (Enemy enemy : creator.getEnemies()){
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 230 / MarioBros.PPM)
                enemy.b2body.setActive(true);
        }

        for (Item item : items)
             item.update(dt);

        hud.update(dt);

        //esto es para mover la camara solo cuando mario no este muerto
        if(player.currentState != Mario.State.DEAD)
            gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update();

        //renderiza solo lo que la camara esta capturando
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        //para limpiar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //para renderizar el mapa del juego
        renderer.render();

        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        //goomba.draw(game.batch);
        //for(Enemy enemy : creator.getGoombas())
            //enemy.draw(game.batch);

        for(Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);

        for (Item item : items)
            item.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver())
        {
            game.setScreen(new GameOverSrcreen(game));
            dispose();
        }
    }

    public boolean gameOver()
    {
        if(player.currentState == Mario.State.DEAD && player.getStateTimer() > 3)
            return true;
        else
            return false;
    }

    @Override
    public void resize(int width, int height)
    {
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }

    public void setPlayer(Mario player)
    {
        this.player = player;
    }

    public Hud getHud(){ return hud; }
}