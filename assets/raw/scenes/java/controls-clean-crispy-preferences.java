package com.joechamm.gdxtests.controls;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
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

        Label label = new Label("PREFERENCES", skin);
        label.setName("prefSceneLabel");
        label.setAlignment(Align.center);
        label.setColor(skin.getColor("CYAN"));
        table1.add(label).pad(5.0f).fill(true).uniform().colspan(3);

        table1.row();
        Table table2 = new Table();
        table2.setName(sliderTable);
        table2.setTouchable(enabled);

        label = new Label("Volume", skin);
        label.setAlignment(Align.center);
        table2.add(label).pad(5.0f).align(Align.top).colspan(3);

        table2.row();
        label = new Label("Sound Effects", skin);
        label.setAlignment(Align.center);
        table2.add(label).pad(5.0f).spaceRight(5.0f).align(Align.right);

        Slider slider = new Slider(0.0f, 100.0f, 1.0f, false, skin, "default-horizontal");
        slider.setName("volumeSoundSlider");
        table2.add(slider).pad(5.0f).colspan(2);

        table2.row();
        label = new Label("Music", skin);
        table2.add(label).pad(5.0f).spaceRight(5.0f).align(Align.right);

        slider = new Slider(0.0f, 100.0f, 1.0f, false, skin, "default-horizontal");
        slider.setName("volumeMusicSlider");
        table2.add(slider).pad(5.0f).colspan(2);
        table1.add(table2).pad(5.0f).fill(true).uniform();

        table1.row();
        table2 = new Table();
        table2.setName(checkboxTable);
        table2.setTouchable(enabled);

        label = new Label("Enable", skin);
        label.setName("enabledLabel");
        table2.add(label).pad(5.0f).uniform().colspan(2);

        table2.row();
        CheckBox checkBox = new CheckBox(" Sound Effects", skin);
        checkBox.setName("soundCheckbox");
        checkBox.setChecked(true);
        checkBox.setColor(skin.getColor("WHITE"));
        table2.add(checkBox).pad(5.0f).uniform();

        checkBox = new CheckBox(" Music", skin);
        checkBox.setName("musicCheckbox");
        checkBox.setChecked(true);
        table2.add(checkBox).pad(5.0f).uniform();
        table1.add(table2).pad(5.0f).fill(true).uniform();

        table1.row();
        Container container = new Container();
        container.setName("backButtonContainer");

        TextButton textButton = new TextButton("Back", skin);
        textButton.setName("backTextButton");
        container.setActor(textButton);
        table1.add(container).pad(5.0f).fill(true).uniform();
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
