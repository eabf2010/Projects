package com.brentaureli.mariobros.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
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
 * Created by Alexander on 24/03/2017.
 */
public class Coquito extends Enemy {

    public static final int KICK_LEFT = -2;
    public static final int KICK_RIGHT = 2;
    public enum State { WALKING, STANDING_COCO, MOVING_COCO, DEAD, COCO_CRUSH }
    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion coco;

    private boolean destroyed;
    private float deadRotationDegrees;
    private boolean coquitoIsDead;
    private boolean hitOnCoquito;

    public Coquito(PlayScreen screen, float x, float y){
        super(screen, x, y);

        frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("coquitos"), 0, 0, 16, 16));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("coquitos"), 16, 0, 16, 16));
        coco = new TextureRegion(screen.getAtlas().findRegion("coquitos"), 32, 0, 16, 16);

        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;

        deadRotationDegrees = 0;
        hitOnCoquito = false;

        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
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

    public TextureRegion getFrame(float dt){
        //currentState = getState();
        TextureRegion region;

        switch (currentState){
            case DEAD:
            case COCO_CRUSH:
            case MOVING_COCO:
            case STANDING_COCO:
                region = coco;
                break;
            case WALKING:
            default:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if(velocity.x > 0 && region.isFlipX() == false){
            region.flip(true, false);
        }
        if(velocity.x < 0 && region.isFlipX() == true){
            region.flip(true, false);
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }


    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));

        if(currentState == State.DEAD)
        {
            deadRotationDegrees += 0;
            rotate(deadRotationDegrees);
            if(stateTime > 3 && !destroyed)
            {
                world.destroyBody(b2body);
                destroyed = true;
            }
        }
        else
            b2body.setLinearVelocity(velocity);

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 /MarioBros.PPM);
    }

    public void hitOnHead(Mario mario) {
        MarioBros.manager.get("audio/sounds/stomp.wav", Sound.class).play();

        if(currentState != State.STANDING_COCO) {
            currentState = State.STANDING_COCO;
            velocity.x = 0;
            hitOnCoquito = true;
        }
        else {
            kick(mario.getX() <= this.getX() ? KICK_RIGHT : KICK_LEFT);
        }
    }

    public State getCurrentState()
    {
        return currentState;
    }

    public void kick(int direction)
    {
        velocity.x = direction;
        currentState = State.MOVING_COCO;
        MarioBros.manager.get("audio/sounds/kick.wav", Sound.class).play();
    }

    public void onEnemyHit(Enemy enemy) {
        /*if(enemy instanceof Coquito && ((Coquito) enemy).currentState == Coquito.State.MOVING_COCO) {
            killedCoquito();
        }
        else
        reverseVelocity(true, false);*/

        if (enemy instanceof Coquito) {
            if(((Coquito) enemy).currentState == State.MOVING_COCO && currentState != State.MOVING_COCO )
                killedCoquito();
            else if (currentState == State.MOVING_COCO)
                return;
            else
                reverseVelocity(true, false);
        }
        /*else
            reverseVelocity(true, false);*/
        /*else if (currentState != State.MOVING_COCO) {
            reverseVelocity(true, false);
        }*/
    }

    public void killedCoquito()
    {
        currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = MarioBros.NOTHING_BIT;

        for(Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
    }
 }

