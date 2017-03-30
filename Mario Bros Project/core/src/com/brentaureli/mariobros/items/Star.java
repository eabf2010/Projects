package com.brentaureli.mariobros.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.brentaureli.mariobros.MarioBros;
import com.brentaureli.mariobros.Screens.PlayScreen;
import com.brentaureli.mariobros.Sprites.Mario;

/**
 * Created by Alexander on 15/03/2017.
 */
public class Star extends Item {
    private float stateTimer;

    public Star(PlayScreen screen, float x, float y){
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("star"), 0, 0, 16, 16);
        velocity = new Vector2(2f, 2f);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.ITEM_BIT;
        fdef.filter.maskBits = MarioBros.MARIO_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {
        destroy();
        mario.shine();
    }

    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        /*velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);*/

        velocity.y = body.getLinearVelocity().y;

        if (stateTimer > 1f){
            body.applyLinearImpulse(new Vector2(1.5f, 4f),body.getWorldCenter(),true);
            stateTimer = 0;
        }
        stateTimer += dt;
    }
}