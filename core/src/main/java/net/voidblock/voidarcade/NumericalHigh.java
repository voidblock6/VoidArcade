package net.voidblock.voidarcade;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NumericalHigh extends Game {
    public SpriteBatch batch; // Shared batch between al screens

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Start the game in MainMenu
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // essential, makes current screen get rendered
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
