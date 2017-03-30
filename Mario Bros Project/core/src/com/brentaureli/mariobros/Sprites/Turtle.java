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
 * Created by Alexander on 22/02/2017.
 */
public class Turtle extends Enemy {
    public static final int KICK_LEFT = -2;
    public static final int KICK_RIGHT = 2;
    public enum State { WALKING, STANDING_SHELL, MOVING_SHELL, DEAD, SHELL_CRUSH, SURVIVE }
    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation walkAnimation;
    private Animation surviveAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion shell;

    private boolean destroyed;
    private float deadRotationDegrees;
    private boolean turtleIsSurviving;
    private boolean runSurviveAnimation;
    private boolean turtleIsDead;
    private boolean hitOnHeadTurtle;




    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);

        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;

        deadRotationDegrees = 0;
        hitOnHeadTurtle = false;

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 80, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 80, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 80, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 80, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 80, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 80, 0, 16, 24));

        surviveAnimation = new Animation(0.3f, frames);

        setBounds(getX(), getY(), 16 / MarioBros.PPM, 24 / MarioBros.PPM);
    }

    public void turtleSurvive()
    {
        if(turtleIsSurviving) {
            runSurviveAnimation = true;
            turtleIsSurviving = true;
            turtleIsDead = false;
            setBounds(getX(), getY(), 16 / MarioBros.PPM, 24 / MarioBros.PPM);
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
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

        //aqui la cabeza del enemigo:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / MarioBros.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / MarioBros.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / MarioBros.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 1.8f;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float dt){
        //currentState = getState();
        TextureRegion region;

        switch (currentState){
            case SURVIVE:
                region = (TextureRegion) surviveAnimation.getKeyFrame(stateTime);
                if(surviveAnimation.isAnimationFinished(stateTime)){
                    runSurviveAnimation = false;
                    currentState = State.WALKING;
                    velocity.x = 1;
                }
                break;
            case DEAD:
            case SHELL_CRUSH:
            case MOVING_SHELL:
            case STANDING_SHELL:
                region = shell;
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

        if(currentState == State.STANDING_SHELL && (stateTime > 2 && stateTime < 3))
        {
            currentState = State.SURVIVE;
            turtleIsSurviving = true;
            turtleSurvive();
        }

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

        if(currentState != State.STANDING_SHELL) {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
            hitOnHeadTurtle = true;
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
        currentState = State.MOVING_SHELL;
        MarioBros.manager.get("audio/sounds/kick.wav", Sound.class).play();
    }

    public void onEnemyHit(Enemy enemy)
    {
        if(enemy instanceof Turtle)
        {
            if(((Turtle) enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL)
                killedTurtle();

            else if(currentState == State.MOVING_SHELL)
                return;
            else
                reverseVelocity(true, false);
        }
        else if(enemy instanceof Coquito)
        {
            if((((Coquito) enemy).currentState == Coquito.State.MOVING_COCO && currentState != State.DEAD) || (currentState == State.MOVING_SHELL && (((Coquito) enemy).currentState != Coquito.State.DEAD)))
                killedTurtle();

            else if(currentState == State.MOVING_SHELL)
                return;
        }
        else
            reverseVelocity(true, false);
    }

    public void killedTurtle()
    {
        currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = MarioBros.NOTHING_BIT;

        for(Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
    }
}