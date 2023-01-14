package com.joechamm.gdxtests.controls;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoadingScene extends ApplicationAdapter {
    private Skin skin;

    private Stage stage;

    public void create() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/controls-clean-crispy.json"));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Stack stack = new Stack();

        Image image = new Image(skin, "Starscape00-tiled");
        image.setTouchable(disabled);
        image.setScaling(Scaling.fill);
        stack.addActor(image);

        Table table1 = new Table();
        table1.setTouchable(disabled);
        table1.pad(10.0f);
        table1.align(Align.top);

        image = new Image(skin, "title");
        image.setName("titleImage");
        image.setTouchable(disabled);
        image.setScaling(Scaling.fit);
        table1.add(image).padLeft(20.0f).padRight(20.0f).padTop(40.0f).padBottom(10.0f).grow().uniform();

        table1.row();
        ProgressBar progressBar = new ProgressBar(0.0f, 100.0f, 1.0f, false, skin, "tiled-big");
        progressBar.setName("loadingProgressBar");
        table1.add(progressBar).pad(40.0f).growY().uniform();

        table1.row();
        Label label = new Label("loading images", skin);
        label.setName("countDownLabel");
        label.setAlignment(Align.center);
        label.setEllipsis(true);
        table1.add(label);
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
