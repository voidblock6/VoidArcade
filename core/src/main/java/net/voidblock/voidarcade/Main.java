package net.voidblock.voidarcade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private FitViewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("voidarcade_titlescreen.png");


        image.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


        viewport = new FitViewport(480, 270);
    }

    @Override
    public void render() {

        ScreenUtils.clear(0, 0, 0, 1f);


        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();


        float x = (viewport.getWorldWidth() - image.getWidth()) / 2f;
        float y = (viewport.getWorldHeight() - image.getHeight()) / 2f;

        batch.draw(image, x, y);

        batch.end();
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
