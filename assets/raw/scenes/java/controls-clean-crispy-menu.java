package com.joechamm.gdxtests.controls;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
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
        table1.setName(baseTable);
        table1.setTouchable(disabled);
        table1.pad(5.0f);

        image = new Image(skin, "title");
        image.setName("menuTitleImage");
        image.setScaling(Scaling.fit);
        table1.add(image).pad(20.0f).spaceBottom(30.0f).fill(true).align(Align.top).uniformX();

        table1.row();
        Table table2 = new Table();
        table2.setName(menuTable);
        table2.pad(10.0f);

        TextButton textButton = new TextButton("New Game", skin);
        textButton.setName("menuNewGameButton");
        table2.add(textButton).pad(5.0f).fillX(true).uniformX();

        table2.row();
        textButton = new TextButton("Preferences", skin);
        textButton.setName("menuPreferencesButton");
        table2.add(textButton).pad(5.0f).fillX(true).uniformX();

        table2.row();
        textButton = new TextButton("Exit", skin);
        textButton.setName("menuExitButton");
        table2.add(textButton).pad(5.0f).fillX(true).uniformX();
        table1.add(table2).fill(true).uniformX();
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
