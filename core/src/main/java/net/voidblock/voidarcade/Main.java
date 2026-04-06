package net.voidblock.voidarcade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private FitViewport viewport;


    private Texture titlescreen;
    private Texture holbutton;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(480, 270);


        titlescreen = new Texture("voidarcade_titlescreen.png");
        holbutton = new Texture("higher_or_lower_button.png");

        titlescreen.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        holbutton.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1f);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();



        batch.draw(titlescreen, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());


        float buttonX = (viewport.getWorldWidth() - holbutton.getWidth()) / 2f;
        float buttonY = 28;
        batch.draw(holbutton, buttonX, buttonY);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();

        titlescreen.dispose();
        holbutton.dispose();
    }
}
