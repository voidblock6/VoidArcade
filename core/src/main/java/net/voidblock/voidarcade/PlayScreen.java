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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Timer;

import java.util.Random;

public class PlayScreen implements Screen {
    private final NumericalHigh game;
    private Texture exitbutton;
    private Texture blackbg;
    private Texture higherlowerskeleton;
    private FitViewport viewport;
    private ScreenViewport uiViewport;
    private BitmapFont customFont;
    private int score = 1024;
    private float buttonSize = 96f;
    private float x, y;
    private String inputNumber = "";
    private final int MAX_LENGTH = 7;
    private GlyphLayout layout = new GlyphLayout();
    private float textWidth;
    private int numberOfGuesses;
    private boolean isWaiting = false;
    public int shield;

    private int randomNumber;
    private Random rand = new Random();

    public PlayScreen(final NumericalHigh game) {
        this.game = game;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PixelOperator.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 96;
        parameter.color = Color.WHITE;
        customFont = generator.generateFont(parameter);
        generator.dispose();

        this.viewport = new FitViewport(480, 270);
        this.uiViewport = new ScreenViewport();

        blackbg = new Texture("standard_black_bg.png");
        exitbutton = new Texture("return_button.png");
        higherlowerskeleton = new Texture("game_bg.png");

        blackbg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        exitbutton.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        startNewRound();
    }

    private void startNewRound() {
        inputNumber = "";
        randomNumber = rand.nextInt(101);
        numberOfGuesses = 0;
        score = 1024;
        isWaiting = false;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character) {

                if (isWaiting) return false;

                if (character == '\r' || character == '\n') {
                    if (!inputNumber.isEmpty()) {
                        processGuess();
                    }
                    return true;
                }

                if (character == '\b' || character == '\u007f') {
                    if (inputNumber.length() > 0) {
                        inputNumber = inputNumber.substring(0, inputNumber.length() - 1);
                    }
                    return true;
                }

                if (Character.isDigit(character) && inputNumber.length() < MAX_LENGTH) {
                    inputNumber += character;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Gdx.input.setOnscreenKeyboardVisible(true, com.badlogic.gdx.Input.OnscreenKeyboardType.NumberPad);
                Vector3 worldCoords = uiViewport.unproject(new Vector3(screenX, screenY, 0));
                if (worldCoords.x >= x && worldCoords.x <= x + buttonSize &&
                    worldCoords.y >= y && worldCoords.y <= y + buttonSize) {
                    game.setScreen(new MainMenuScreen(game));
                }
                return true;
            }
        });
    }

    private void processGuess() {
        try {
            int guessedNumber = Integer.parseInt(inputNumber);
            numberOfGuesses += 1;

            if (numberOfGuesses != 1) {
                score /= 2;
            }

            isWaiting = true;

            if (guessedNumber == randomNumber) {
                inputNumber = "correct";
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        startNewRound();
                    }
                }, 1);
            } else if (guessedNumber < randomNumber) {
                inputNumber = "higher";
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        inputNumber = "";
                        isWaiting = false;
                    }
                }, 1);
            } else {
                inputNumber = "lower";
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        inputNumber = "";
                        isWaiting = false;
                    }
                }, 1);
            }
        } catch (NumberFormatException e) {
            isWaiting = false;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        x = uiViewport.getWorldWidth() - buttonSize - 1;
        y = uiViewport.getWorldHeight() - buttonSize - 1;

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

        String scoreText = String.format("%04d", score);
        customFont.draw(game.batch, scoreText, 20, uiViewport.getWorldHeight() - 20);

        layout.setText(customFont, inputNumber);
        textWidth = layout.width;
        customFont.draw(game.batch, inputNumber,
            (uiViewport.getWorldWidth() / 2) - (textWidth / 2),
            (uiViewport.getWorldHeight() / 2));
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
