package net.voidblock.voidarcade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HigherLowerScreen implements Screen {
    private final VoidArcade game;
    private Texture exitbutton;
    private Texture blackbg;
private Texture higherlowerskeleton;
    private FitViewport viewport;
    private ScreenViewport uiViewport;
    private BitmapFont customFont;
    private int score = 000;

    private float buttonSize = 96f;
    private float x, y;

    public HigherLowerScreen(final VoidArcade game) {
        this.game = game;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PixelOperator.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 96; // size in pixels
        parameter.color = Color.WHITE; //color
        customFont = generator.generateFont(parameter);
        generator.dispose();

        // Viewport for game
        this.viewport = new FitViewport(480, 270);
        // Viewport for UI (follows screens edges)
        this.uiViewport = new ScreenViewport();

        blackbg = new Texture("standard_black_bg.png");
        exitbutton = new Texture("return_button.png");
higherlowerskeleton = new Texture("higher_lower_skeleton.png");

        blackbg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        exitbutton.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                Vector3 worldCoords = uiViewport.unproject(new Vector3(screenX, screenY, 0));

                if (worldCoords.x >= x && worldCoords.x <= x + buttonSize &&
                    worldCoords.y >= y && worldCoords.y <= y + buttonSize) {

                    game.setScreen(new MainMenuScreen(game));
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);


        x = uiViewport.getWorldWidth() - buttonSize - 1;
        y = uiViewport.getWorldHeight() - buttonSize - 1  ;


        viewport.apply();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.batch.begin();
        game.batch.draw(blackbg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.batch.draw(higherlowerskeleton, 0, 0);
        game.batch.end();


        uiViewport.apply();
        game.batch.setProjectionMatrix(uiViewport.getCamera().combined);
        game.batch.begin();
        game.batch.draw(exitbutton, x, y, buttonSize, buttonSize);
        String scoreText = String.format("%03d", score);
        customFont.draw(game.batch, "" + scoreText, 20, uiViewport.getWorldHeight() - 20);

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        blackbg.dispose();
        exitbutton.dispose();
        higherlowerskeleton.dispose();
        customFont.dispose();

    }

    @Override public void pause() {}
    @Override public void resume() {}
}
