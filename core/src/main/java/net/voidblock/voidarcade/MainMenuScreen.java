package net.voidblock.voidarcade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen implements Screen {
    private final NumericalHigh game;
    private FitViewport viewport;
    private float buttonX, buttonY;
    private float inventoryX, inventoryY;
    private float quitX, quitY;
    private float optionsX, optionsY;
    private Texture titlescreen, playbutton, inventorybutton, quitbutton, optionsbutton;


    public MainMenuScreen(final NumericalHigh game) {
        this.game = game;
        this.viewport = new FitViewport(480, 270);

        titlescreen = new Texture("titlescreen.png");
        playbutton = new Texture("play_button.png");
        inventorybutton = new Texture("inventory_button.png");
        optionsbutton = new Texture("options_button.png");
        quitbutton = new Texture("quit_button.png");

        titlescreen.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        playbutton.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        inventorybutton.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        quitbutton.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        optionsbutton.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

    }

    @Override
    public void show() {
        // set the input as soon as screen is visible
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));

                if (worldCoords.x >= buttonX && worldCoords.x <= buttonX + playbutton.getWidth() &&
                    worldCoords.y >= buttonY && worldCoords.y <= buttonY + playbutton.getHeight()) {

                    System.out.println("Starting the game");

                    game.setScreen(new PlayScreen(game));
                }


                if (worldCoords.x >= inventoryX && worldCoords.x <= inventoryX + inventorybutton.getWidth() &&
                    worldCoords.y >= inventoryY && worldCoords.y <= inventoryY + inventorybutton.getHeight()) {

                    System.out.println("Opening Inventory");

                    game.setScreen(new InventoryScreen(game));
                }

                if (worldCoords.x >= quitX && worldCoords.x <= quitX + quitbutton.getWidth() &&
                    worldCoords.y >= quitY && worldCoords.y <= quitY + quitbutton.getHeight()) {

                    System.out.println("Quitting game");

                    Gdx.app.exit();
                }

                if (worldCoords.x >= optionsX && worldCoords.x <= optionsX + optionsbutton.getWidth() &&
                    worldCoords.y >= optionsY && worldCoords.y <= optionsY + optionsbutton.getHeight()) {

                    System.out.println("Opening Options");

                    game.setScreen(new OptionsScreen(game));
                }


                return true;

            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1f);
        buttonX = (viewport.getWorldWidth() - playbutton.getWidth()) / 2f;
        inventoryX = (viewport.getWorldWidth() - inventorybutton.getWidth()) / 2f;
        quitX = (viewport.getWorldWidth() - quitbutton.getWidth()) / 2f;
        optionsX = (viewport.getWorldWidth() - optionsbutton.getWidth()) / 2f;

        //choose button height
        buttonY = 174;
        inventoryY = 132;
        optionsY = 92;
        quitY = 52;

        viewport.apply();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        game.batch.begin();
        game.batch.draw(titlescreen, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.batch.draw(playbutton, buttonX, buttonY);
        game.batch.draw(inventorybutton, inventoryX, inventoryY);
        game.batch.draw(quitbutton, quitX, quitY);
        game.batch.draw(optionsbutton, optionsX, optionsY);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        titlescreen.dispose();
        playbutton.dispose();
        inventorybutton.dispose();
        quitbutton.dispose();
        optionsbutton.dispose();
    }


    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
