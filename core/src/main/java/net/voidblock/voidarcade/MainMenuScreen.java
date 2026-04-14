package net.voidblock.voidarcade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen implements Screen {
    private final VoidArcade game;
    private FitViewport viewport;
    private float buttonX, buttonY;
    private Texture titlescreen, holbutton;

    public MainMenuScreen(final VoidArcade game) {
        this.game = game;
        this.viewport = new FitViewport(480, 270);

        titlescreen = new Texture("voidarcade_titlescreen.png");
        holbutton = new Texture("higher_lower_button.png");

        titlescreen.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        holbutton.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public void show() {
        // set the input as soon as screen is visible
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));

                if (worldCoords.x >= buttonX && worldCoords.x <= buttonX + holbutton.getWidth() &&
                    worldCoords.y >= buttonY && worldCoords.y <= buttonY + holbutton.getHeight()) {

                    System.out.println("Switching to Higher or Lower!");
                    // switch classes
                    game.setScreen(new HigherLowerScreen(game));
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1f);
        buttonX = (viewport.getWorldWidth() - holbutton.getWidth()) / 2f;
        buttonY = 28;

        viewport.apply();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        game.batch.begin();
        game.batch.draw(titlescreen, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.batch.draw(holbutton, buttonX, buttonY);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        titlescreen.dispose();
        holbutton.dispose();
    }


    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
