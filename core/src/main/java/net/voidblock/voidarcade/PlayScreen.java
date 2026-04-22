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
    private Texture exitbutton, blackbackground, gamebackground, shieldcard;

    private FitViewport viewport;
    private ScreenViewport uiViewport;
    private BitmapFont customFont;
    private int score = 1024;
    private float quitButtonSize = 96f;
    private float quitX, quitY;
    private float shieldCardX, shieldCardY;
    private float shieldWidth = 60f;
    private float shieldHeight = 84f;
    private String inputNumber = "";
    private final int MAX_LENGTH = 7;
    private GlyphLayout layout = new GlyphLayout();
    private float textWidth;
    private int numberOfGuesses;
    private boolean isWaiting = false;
    public int hasShield;
    private int randomNumber;
    private Random rand = new Random();
    private int shieldPlayed;


    public PlayScreen(final NumericalHigh game) {
        this.game = game;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PixelOperator.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 96;
        parameter.color = Color.WHITE;
        customFont = generator.generateFont(parameter);
        generator.dispose();

        hasShield = 1;

        this.viewport = new FitViewport(480, 270);
        this.uiViewport = new ScreenViewport();

        blackbackground = new Texture("standard_black_bg.png");
        exitbutton = new Texture("return_button.png");
        gamebackground = new Texture("game_bg.png");
        shieldcard = new Texture("shield_upgrade_card.png");

        blackbackground.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        exitbutton.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        gamebackground.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        shieldcard.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

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

                if (worldCoords.x >= quitX && worldCoords.x <= quitX + quitButtonSize &&
                    worldCoords.y >= quitY && worldCoords.y <= quitY + quitButtonSize) {
                    game.setScreen(new MainMenuScreen(game));
                }

                if (hasShield > 0 && worldCoords.x >= shieldCardX && worldCoords.x <= shieldCardX + (shieldWidth * (uiViewport.getWorldWidth() / viewport.getWorldWidth())) &&
                    worldCoords.y >= shieldCardY && worldCoords.y <= shieldCardY + (shieldHeight * (uiViewport.getWorldHeight() / viewport.getWorldHeight()))) {


                        System.out.println("Shield card played");
                        shieldPlayed += 1;
                        hasShield -= 1;

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
                if (shieldPlayed > 0) {
                    shieldPlayed -= 1;
                } else {
                    score /= 2;
                }
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

        quitX = uiViewport.getWorldWidth() - quitButtonSize - 1;
        quitY = uiViewport.getWorldHeight() - quitButtonSize - 1;

        shieldCardX = 0;
        shieldCardY = 0;

        viewport.apply();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.batch.begin();
        game.batch.draw(blackbackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.batch.draw(gamebackground, 0, 0);

        if (hasShield > 0) {
            game.batch.draw(shieldcard, 0, 0, shieldWidth, shieldHeight);
        }

        game.batch.end();

        uiViewport.apply();
        game.batch.setProjectionMatrix(uiViewport.getCamera().combined);
        game.batch.begin();
        game.batch.draw(exitbutton, quitX, quitY, quitButtonSize, quitButtonSize);

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
        blackbackground.dispose();
        exitbutton.dispose();
        gamebackground.dispose();
        customFont.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
}
