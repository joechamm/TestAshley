package com.joechamm.gdxtests.controls;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PreferencesScene extends ApplicationAdapter {
    private Skin skin;

    private Stage stage;

    public void create() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/controls-clean-crispy.json"));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Stack stack = new Stack();

        Image image = new Image(skin, "Starscape00");
        image.setTouchable(disabled);
        image.setScaling(Scaling.fill);
        stack.addActor(image);

        Table table1 = new Table();
        table1.setName(uiBaseTable);
        table1.setTouchable(enabled);
        table1.pad(5.0f);

        Table table2 = new Table();
        table2.setName(uiUpperLeftTable);

        Label label = new Label("Lives", skin);
        label.setName("uiLivesLabel");
        table2.add(label);
        table1.add(table2).pad(5.0f);

        table1.add().growX();

        table2 = new Table();
        table2.setName(uiUpperRightTable);

        label = new Label("Score: ", skin);
        label.setName("uiScoreLabel");
        table2.add(label);
        table1.add(table2).pad(5.0f);

        table1.row();
        table1.add().growY();

        table1.add().grow();

        table1.add().growY();

        table1.row();
        table2 = new Table();
        table2.setName(uiTouchPadTable);

        Touchpad touchPad = new Touchpad(1.0f, skin);
        touchPad.setName("uiTouchPad");
        table2.add(touchPad).pad(5.0f);
        table1.add(table2).pad(5.0f);

        table1.add().growX();

        table2 = new Table();
        table2.setName(uiButtonTable);

        TextButton textButton = new TextButton("A", skin, "arcade");
        textButton.setName("uiButtonA");
        table2.add(textButton).padLeft(5.0f).padRight(5.0f);

        textButton = new TextButton("B", skin, "arcade");
        textButton.setName("uiButtonB");
        table2.add(textButton).padLeft(5.0f).padRight(5.0f);
        table1.add(table2).pad(5.0f);
        stack.addActor(table1);
        table.add(stack);
        stage.addActor(table);
    }

    public void render() {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
