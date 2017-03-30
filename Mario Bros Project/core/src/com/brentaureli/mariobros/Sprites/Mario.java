package com.brentaureli.mariobros.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brentaureli.mariobros.MarioBros;
import com.brentaureli.mariobros.Scenes.Hud;
import com.brentaureli.mariobros.Screens.PlayScreen;

/**
 * Created by Alexander on 02/01/2017.
 */
public class Mario extends Sprite {
    public enum State{ FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD, FLEXED, SHINING, BEFOREfIRE/*SHOOTFIRE*/}
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion marioStand;
    private Animation marioShineStand;
    private Animation marioRun;
    private Animation marioShineRun;
    private Animation fireMarioAnimation;
    private TextureRegion marioJump;
    private Animation marioShineJump;
    private TextureRegion bigMarioStand;
    private Animation bigMarioShineStand;
    private TextureRegion bigMarioFireStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioShineJump;
    private TextureRegion bigMarioFireJump;
    private TextureRegion marioDead;
    private Animation bigMarioRun;
    private Animation bigMarioFireRun;
    private Animation bigMarioShineRun;
    private Animation growMario;
    private Animation growShineMario;
    private TextureRegion bigMarioFlexed;
    private Animation bigMarioShineFlexed;
    private TextureRegion fireMarioFlexed;
    private Animation marioRunFast;
    private Animation marioShineRunFast;
    private Animation bigMarioRunFast;
    private Animation bigMarioFireRunFast;
    private Animation bigMarioShineRunFast;

    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean marioIsFire;
    private boolean runGrowAnimation;
    private boolean runShineAnimation;
    private boolean runBeforeFireAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToDefineFireMario;
    private boolean timeToRedefineMario;
    private boolean timeToDefineShineMario;
    private boolean timeToDefineLittleShineMario;
    private boolean marioIsDead;
    private boolean marioIsRuning;
    private boolean marioIsFlexed;
    private boolean marioIsStanding;
    private boolean marioIsShine;
    private boolean bigMarioShine;
    private float timeToStanding;
    private String beforeState;
    private PlayScreen screen;

    private Array<Fireball> fireballs;


    //constructor
    public  Mario(PlayScreen screen) {
        //super(screen.getAtlas().findRegion("little_mario"));
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        marioIsFlexed = false;
        marioIsStanding = true;
        timeToStanding = 0;
        marioIsShine = false;
        beforeState = "little";
        marioIsRuning = false;
        runShineAnimation = false;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        marioRun = new Animation(0.07f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Little_Shine_Mario"), i * 16, 0, 16, 16));
        marioShineRun = new Animation(0.07f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        bigMarioRun = new Animation(0.07f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fire_Mario"), i * 16, 0, 16, 32));
        bigMarioFireRun = new Animation(0.07f, frames);

        frames.clear();

        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Run"), i * 16, 0, 16, 32));
        bigMarioShineRun = new Animation(0.07f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        marioRunFast = new Animation(0.04f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Little_Shine_Mario"), i * 16, 0, 16, 16));
        marioShineRunFast = new Animation(0.04f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        bigMarioRunFast = new Animation(0.04f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fire_Mario"), i * 16, 0, 16, 32));
        bigMarioFireRunFast = new Animation(0.04f, frames);

        frames.clear();

        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Run"), i * 16, 0, 16, 32));
        bigMarioShineRunFast = new Animation(0.04f, frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Grow"), 16, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Grow"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Grow"), 32, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Grow"), 64, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Grow"), 80, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Grow"), 96, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Grow"), 16, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Grow"), 0, 0, 16, 32));

        growShineMario = new Animation(0.1f, frames);

        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);
        bigMarioFireJump = new TextureRegion(screen.getAtlas().findRegion("fire_Mario"), 80, 0, 16, 32);

        frames.clear();


            frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Little_Shine_Mario"), 64, 0, 16, 16));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Little_Shine_Mario"), 80, 0, 16, 16));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Little_Shine_Mario"), 96, 0, 16, 16));
        marioShineJump = new Animation(0.1f, frames);

        frames.clear();

        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Jump"), i * 16, 0, 16, 32));
        bigMarioShineJump = new Animation(0.1f, frames);

        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 1, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
        bigMarioFireStand = new TextureRegion(screen.getAtlas().findRegion("fire_Mario"), 0, 0, 16, 32);

        frames.clear();

            frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Little_Shine_Mario"), 0, 0, 16, 16));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Little_Shine_Mario"), 112, 0, 16, 16));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Little_Shine_Mario"), 128, 0, 16, 16));
        marioShineStand = new Animation(0.1f, frames);

        frames.clear();

        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Shine_Stand"), i * 16, 0, 16, 32));
        bigMarioShineStand = new Animation(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Before_Fire"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Before_Fire"), 16, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Before_Fire"), 32, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Before_Fire"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Before_Fire"), 16, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Before_Fire"), 32, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Before_Fire"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Before_Fire"), 16, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_Mario_Before_Fire"), 32, 0, 16, 32));

        fireMarioAnimation = new Animation(0.1f, frames);

        bigMarioFlexed = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 96, 0, 16, 32);
        fireMarioFlexed = new TextureRegion(screen.getAtlas().findRegion("fire_Mario"), 96, 0, 16, 32);

        frames.clear();

        //for(int i = 0; i < 3; i++)
        frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Big_Mario_Shine"), 96, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Big_Mario_Shine"), 112, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("somes_Big_Mario_Shine"), 128, 0, 16, 32));
        bigMarioShineFlexed = new Animation(0.1f, frames);

        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        defineMario();

        setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(marioStand);

        fireballs = new Array<Fireball>();

    }

    public boolean isBig(){
        return marioIsBig;
    }

    public boolean isShine(){
        return marioIsShine;
    }

    public boolean isFire(){return marioIsFire;}

    public boolean flexed()
    {
        //Gdx.app.log("mario","agachado");
        currentState = State.FLEXED;
        marioIsStanding = true;
        timeToStanding = 1;
        return marioIsFlexed;
    }

    public boolean standing(){
        //Gdx.app.log("mario: ","de pie");
        currentState = State.STANDING;
        timeToStanding = 0;
        return marioIsStanding;
    }


    public void update(float dt) {
        /*if (screen.getHud().isTimeUp() && !isDead()) {
            //die();
            Gdx.app.log("aqui","muere mario por tiempo");
        }
        else*/
        if(marioIsShine){
            //Gdx.app.log("el tiempo es: ",String.valueOf(stateTimer));
            if(stateTimer < 6){
                if(marioIsBig)
                    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 4 / MarioBros.PPM);
                else
                    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

                marioIsShine = true;
            }
            else{
                MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).play();
                MarioBros.manager.get("audio/sounds/starman.mp3", Sound.class).stop();
                marioIsShine = false;
            }
        }
        else if (marioIsFire)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 4 / MarioBros.PPM);
        else if(marioIsBig)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 / MarioBros.PPM);
        else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        
        if(timeToDefineBigMario)
            defineBigMario();
        if(timeToDefineFireMario)
            defineFireMario();
        if(timeToDefineShineMario)
            defineBigShineMario();
        if(timeToDefineLittleShineMario)
            defineLittleShineMario();
        if(timeToRedefineMario)
            redefineMario();

        for (Fireball ball : fireballs){
            ball.update(dt);
            if(ball.isDestroyed())
                fireballs.removeValue(ball, true);
        }


    }

    //metodo getFrame
    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;

        switch(currentState) {
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                if (marioIsShine) {
                    region = (TextureRegion) growShineMario.getKeyFrame(stateTimer, true);
                    if (growShineMario.isAnimationFinished(stateTimer))
                        runGrowAnimation = false;
                    timeToDefineShineMario = true;
                } else {
                    region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                    if (growMario.isAnimationFinished(stateTimer))
                        runGrowAnimation = false;
                }
                break;
            /*case SHOOTFIRE:*/
            case JUMPING:
                if (marioIsShine) {
                    if (bigMarioShine) {
                        region = (TextureRegion) bigMarioShineJump.getKeyFrame(stateTimer, true);
                        if (beforeState == "Big")
                            region = bigMarioJump;
                        else if (beforeState == "Fire")
                            region = bigMarioFireJump;
                    } else {
                        region = (TextureRegion) marioShineJump.getKeyFrame(stateTimer, true);
                       /*if(beforeState == "little")
                           region = marioJump;*/
                        //timeToRedefineMario = true;
                    }
                } else if (marioIsFire)
                    region = bigMarioFireJump;
                else
                    region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case FLEXED:
                    if (marioIsShine) {
                        region = (TextureRegion) bigMarioShineFlexed.getKeyFrame(stateTimer, true);
                        if (beforeState == "Big")
                            timeToDefineBigMario = true;
                        else if (beforeState == "Fire")
                            timeToDefineFireMario = true;
                    } else if (marioIsFire)
                        region = fireMarioFlexed;
                    else
                        region = bigMarioFlexed;

                if (marioIsStanding && currentState == State.FLEXED && timeToStanding > 0)
                    standing();
                break;
            case RUNNING:
                if(marioIsShine) {
                    if (bigMarioShine){
                        if (marioIsRuning)
                            region = (TextureRegion) bigMarioShineRunFast.getKeyFrame(stateTimer, true);
                        else
                            region = (TextureRegion) bigMarioShineRun.getKeyFrame(stateTimer, true);
                    }
                    else {
                        if (marioIsRuning)
                            region = (TextureRegion) marioShineRunFast.getKeyFrame(stateTimer, true);
                        else
                            region = (TextureRegion) marioShineRun.getKeyFrame(stateTimer, true);
                    }
                }
                else if(marioIsFire) {
                    if (marioIsRuning)
                        region = (TextureRegion) (bigMarioFireRunFast.getKeyFrame(stateTimer, true));
                    else
                        region = (TextureRegion) (bigMarioFireRun.getKeyFrame(stateTimer, true));
                }
                else {
                    if (marioIsRuning)
                        region = (TextureRegion) (marioIsBig ? bigMarioRunFast.getKeyFrame(stateTimer, true) : marioRunFast.getKeyFrame(stateTimer, true));
                    else
                        region = (TextureRegion) (marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true));
                }
                break;
            case BEFOREfIRE:
                    region = (TextureRegion) fireMarioAnimation.getKeyFrame(stateTimer);
                if(fireMarioAnimation.isAnimationFinished(stateTimer)){
                    runBeforeFireAnimation = false;
                    region = bigMarioFireStand;
                }
                break;
            case FALLING:
            case STANDING:
            default:
                if(marioIsShine){
                    if(bigMarioShine)
                        region = (TextureRegion) bigMarioShineStand.getKeyFrame(stateTimer, true);
                    else
                        region = (TextureRegion) marioShineStand.getKeyFrame(stateTimer, true);
                }
                else if(marioIsFire)
                    region = bigMarioFireStand;
                else
                    region = marioIsBig ? bigMarioStand : marioStand;
                 break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    //metodo getState
    public State getState(){
        if (marioIsDead)
            return State.DEAD;
        else if(runGrowAnimation)
            return State.GROWING;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else if(runShineAnimation)
            return State.SHINING;
        else if(currentState == State.FLEXED)
            return State.FLEXED;
        else if(runBeforeFireAnimation)
            return State.BEFOREfIRE;
        else
            return State.STANDING;
    }

    //metodo grow
    public void grow(){
        if( !isBig() ) {
            if(isShine())
                timeToDefineShineMario = true;
            else
                timeToDefineBigMario = true;
        }
        else
            fireMario();

        runGrowAnimation = true;
        marioIsBig = true;
        beforeState = "Big";

        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        MarioBros.manager.get("audio/sounds/powerup.wav", Sound.class).play();
    }

    public void life(){
        MarioBros.manager.get("audio/sounds/1-up.wav", Sound.class).play();
        Hud.addScore(100);
    }

    public void shine()
    {
        if( !isShine() ) {
            beforeState = "Shine";
            marioIsShine = true;
            if (marioIsBig){
                bigMarioShine = true;
                timeToDefineShineMario = true;
            }
            else{
                timeToDefineLittleShineMario = true;
            }

            MarioBros.manager.get("audio/sounds/starman.mp3", Sound.class).play();
            MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
        }
    }

    public void fireMario()
    {
        if( !isFire() ) {
            if(marioIsBig){
                runBeforeFireAnimation = true;
                marioIsFire = true;
                timeToDefineFireMario = true;
            }
            else {
                grow();
            }
            MarioBros.manager.get("audio/sounds/powerup.wav", Sound.class).play();
        }
    }

    public void die() {
        if (!isDead()) {

            MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            marioIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = MarioBros.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 6f), b2body.getWorldCenter(), true);
        }
    }

    public boolean isDead()
    {
        return marioIsDead;
    }

    public boolean isRunning(){
        marioIsRuning = true;
        return marioIsRuning; }

    public boolean isNotRunning(){
        marioIsRuning = false;
        return marioIsRuning;
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            if(marioIsBig || marioIsFire)
                MarioBros.manager.get("audio/sounds/jump-super.wav", Sound.class).play();
            else
                MarioBros.manager.get("audio/sounds/jump-small.wav", Sound.class).play();

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public float getStateTimer()
    {
        return stateTimer;
    }

    public void hit(Enemy enemy) {
        if (enemy instanceof Turtle && (((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL) || enemy instanceof Turtle && (((Turtle) enemy).getCurrentState() == Turtle.State.SURVIVE))
        {
            ((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT : Turtle.KICK_LEFT);
        }
        else if(enemy instanceof Coquito && ((Coquito) enemy).getCurrentState() == Coquito.State.STANDING_COCO)
            ((Coquito) enemy).kick(this.getX() <= enemy.getX() ? Coquito.KICK_RIGHT : Coquito.KICK_LEFT);
        else {
            if (marioIsBig || marioIsFire) {
                marioIsBig = false;
                marioIsFire = false;
                beforeState = "little";
                timeToRedefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                MarioBros.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
                MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
                marioIsDead = true;
                Filter filter = new Filter();
                filter.maskBits = MarioBros.NOTHING_BIT;
                //Gdx.app.log("mario", "Die");
                for (Fixture fixture : b2body.getFixtureList())
                    fixture.setFilterData(filter);
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            }
        }
    }

    public void redefineMario()
    {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef  bdef = new BodyDef();
        bdef.position.set(position);//para que quede en la misma posicion del bigMario
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        //b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);


        //funcion para el sensor en la cabeza de Mario
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 9 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 9 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        //b2body.createFixture(fdef).setUserData("head");
        b2body.createFixture(fdef).setUserData(this);

        beforeState = "little";
        timeToRedefineMario = false;
    }

    public void defineBigMario()
    {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef  bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / MarioBros.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        //b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / MarioBros.PPM));
        b2body.createFixture(fdef).setUserData(this);


        //funcion para el sensor en la cabeza de Mario
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 9 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 9 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
    }

    public void defineFireMario()
    {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef  bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / MarioBros.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        //b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / MarioBros.PPM));
        b2body.createFixture(fdef).setUserData(this);


        //funcion para el sensor en la cabeza de Mario
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 9 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 9 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineFireMario = false;
    }

    public void defineBigShineMario()
    {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef  bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / MarioBros.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        //b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / MarioBros.PPM));
        b2body.createFixture(fdef).setUserData(this);


        //funcion para el sensor en la cabeza de Mario
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 9 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 9 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineShineMario = false;
    }

    public void defineLittleShineMario(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef  bdef = new BodyDef();
        bdef.position.set(position);//para que quede en la misma posicion del bigMario
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        //b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);


        //funcion para el sensor en la cabeza de Mario
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 9 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 9 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        //b2body.createFixture(fdef).setUserData("head");
        b2body.createFixture(fdef).setUserData(this);

        beforeState = "little";
        timeToDefineLittleShineMario = false;
    }

    //funcion
    public void defineMario(){
        BodyDef  bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        //b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);


        //funcion para el sensor en la cabeza de Mario
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 9 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 9 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        //b2body.createFixture(fdef).setUserData("head");
        b2body.createFixture(fdef).setUserData(this);

    }

    public void fire(){
        fireballs.add(new Fireball(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
    }

    public void draw(Batch batch){
        super.draw(batch);
        for (Fireball ball : fireballs)
            ball.draw(batch);
    }

}
