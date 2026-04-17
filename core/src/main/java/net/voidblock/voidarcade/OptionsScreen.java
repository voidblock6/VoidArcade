package net.voidblock.voidarcade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.utils.ScreenUtils;


public class OptionsScreen implements Screen {
    private final NumericalHigh game;


    public OptionsScreen(final NumericalHigh game) {
        this.game = game;

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);



    }

    @Override
    public void resize(int width, int height) {


    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }

    @Override public void pause() {}
    @Override public void resume() {}
}
