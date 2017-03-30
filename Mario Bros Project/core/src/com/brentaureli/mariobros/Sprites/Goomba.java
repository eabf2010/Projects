package com.brentaureli.mariobros.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.brentaureli.mariobros.MarioBros;
import com.brentaureli.mariobros.Screens.PlayScreen;

/**
 * Created by Alexander on 23/01/2017.
 */
public class Goomba extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    public enum State { WALKING, SHELL_CRUSH, CRUSHED }
    public State currentState;
    public State previousState;
    private boolean destroyed;
    private TextureRegion shellStop;
    private TextureRegion shellCrushed;
    float angle;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        walkAnimation = new Animation(0.4f, frames);
        currentState = previousState = State.WALKING;

        shellCrushed = new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16);
        shellStop = new TextureRegion(screen.getAtlas().findRegion("goomba"), 0, 0, 16, 16);

        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        angle = 0;

    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (currentState){
            case CRUSHED:
                region = shellCrushed;
                break;
            case SHELL_CRUSH:
                region = shellStop;
                break;
            case WALKING:
            default:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    public void update(float dt){
        setRegion(getFrame(dt));

        if((currentState == State.SHELL_CRUSH) || (currentState == State.CRUSHED)) {
            if (!destroyed) {
                world.destroyBody(b2body);
                destroyed = true;
            }
        }
        else
            b2body.setLinearVelocity(velocity);

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        //bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / MarioBros.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / MarioBros.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / MarioBros.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.3f;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    public void onEnemyHit(Enemy enemy)
    {
        if((enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL) || (enemy instanceof Coquito && ((Coquito) enemy).currentState == Coquito.State.MOVING_COCO)) {
            punchGoomba();
        }
        else {
            reverseVelocity(true, false);
        }
    }

    @Override
    public void hitOnHead(Mario mario) {
        currentState = State.CRUSHED;
        MarioBros.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    public void punchGoomba()
    {
        currentState = State.SHELL_CRUSH;
        MarioBros.manager.get("audio/sounds/kick.wav", Sound.class).play();
        //Gdx.app.log("Goomba","crushed");

        Filter filter = new Filter();
        filter.maskBits = MarioBros.NOTHING_BIT;

        for(Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
        destroyed = true;
    }
}
