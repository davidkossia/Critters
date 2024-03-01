/* CRITTERS GUI <Main.java>
 * EE422C Project 5 submission by
 * David Kossia
 * sdk927
 * 17365
 * Thibault Tonnel
 * tt26585
 * 17360
 * Slip days used: <1>
 * Spring 2022
 */
package assignment5;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.*;
import java.io.*;
public class Main extends Application {

    static String myPackage = Critter.class.getPackage().toString().split(" ")[1];
    
    static GridPane project = new GridPane();
    static GridPane red_line = new GridPane();
    static VBox inputs = new VBox(5);
    static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    static ComboBox<String> critterOptions = new ComboBox<>();
    static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    static boolean critterSelected = false;
    

    static Circle circle = new Circle();
    static Polygon square = new Polygon();
    static Polygon diamond = new Polygon();
    static Polygon triangle = new Polygon();
    static Polygon star = new Polygon();


    static GridPane animationPane = new GridPane();
    static Label anim = new Label("Simulation Speed");
    static Slider speedSlider = new Slider(0,100,1);
    static Label animationVal = new Label(Integer.toString((int)speedSlider.getValue()));
    static Button animationButton = new Button("Simulate");
    static boolean animation = false;
    static Video simulate = new Video((int) speedSlider.getValue());
    static class Video extends AnimationTimer{
        private int steps;

        Video(int steps){
            this.steps = steps;
        }

        @Override
        public void handle(long now) {
			int i =0;
			while (i<Main.speedSlider.getValue()) {
				Critter.worldTimeStep();
				i++;
			}
			Critter.displayWorld(red_line);
            statUpdate();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
    }



    static GridPane numberCrittersPane = new GridPane();
    static Label numberCritters = new Label("Spawn Critters");
    static Slider numberCrittersSlider = new Slider(0,100,1);
    static Label inputCritters = new Label(Integer.toString((int)numberCrittersSlider.getValue()));
    static Button critterSpawn = new Button("Add");


    static GridPane stepCritterPane = new GridPane();
    static Label numberStep = new Label("Steps");
    static Slider stepSlider = new Slider(0,100,1);
    static Label userStepNumber = new Label(Integer.toString((int)stepSlider.getValue()));
    static Button stepButton = new Button("Step");



    static VBox statsBox = new VBox();
    static Button runStats = new Button("Stats");
    static TextArea stat = new TextArea();


    static Label seedLabel = new Label("Seed");
    static GridPane seedPane = new GridPane();
    static Button seedButton = new Button("Plant");
    static TextField seed = new TextField() {
        @Override public void replaceText(int start, int end, String text) { if (text.matches("[0-9]*")) { super.replaceText(start, end, text); } }
        @Override public void replaceSelection(String text) { if (text.matches("[0-9]*")) { super.replaceSelection(text); } }
    };


    static VBox exitBox = new VBox();
    static Button exitButt = new Button("Exit Game");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        updates();
        sliderSetUp();
        shapeSetup();

        ObservableList<String> oList = FXCollections.observableArrayList(getCrittersList());
        critterOptions = new ComboBox<String>(oList);
        critterOptions.setPromptText("Select Critter");

		seedhelper(animationPane);

		animationhelper(animationPane);

		sliderticks(speedSlider, 1000, 10, 1);
        animationPane.add(anim,0,0);
        animationPane.add(speedSlider,0,1);
        animationPane.add(animationButton,1,0);
        animationPane.add(animationVal,1,1);


        statsBox.setAlignment(Pos.CENTER);
        statsBox.setSpacing(5);
        stat.setEditable(false);
        statsBox.setPadding(new Insets(20,0,0,0));
        statsBox.getChildren().addAll(runStats,stat);


        exitBox.setAlignment(Pos.CENTER);
        exitBox.setPadding(new Insets(20, 0, 0, 0));
        exitBox.getChildren().add(exitButt);
        exitButt.setOnAction(new EventHandler<ActionEvent>() { @Override public void handle(ActionEvent e) { Platform.exit(); } });

		animationhelper(seedPane);
		seedhelper(seedPane);
		seedPane.setAlignment(Pos.CENTER);
        seedPane.add(seedLabel, 0, 0);
        seedPane.add(seed, 0, 1);
        seedPane.add(seedButton, 1, 1);

        project.setHgap(10);
        project.setPadding(new Insets(10, 10, 10 ,10));


        ColumnConstraints sceneCol1 = new ColumnConstraints();
        sceneCol1.setPercentWidth(25);
        sceneCol1.setHalignment(HPos.CENTER);
        ColumnConstraints sceneCol2 = new ColumnConstraints();
        sceneCol2.setPercentWidth(75);
        sceneCol2.setHalignment(HPos.CENTER);
        project.getColumnConstraints().addAll(sceneCol1, sceneCol2);


        RowConstraints sceneRow1 = new RowConstraints();
        sceneRow1.setPercentHeight(85);
        sceneRow1.setValignment(VPos.CENTER);
        RowConstraints sceneRow2 = new RowConstraints();
        sceneRow2.setPercentHeight(15);
        sceneRow2.setValignment(VPos.TOP);
        project.getRowConstraints().addAll(sceneRow1, sceneRow2);

        red_line.setGridLinesVisible(true);
		{
			int i = 0;
			while (i < Params.WORLD_WIDTH) {
				ColumnConstraints colConst = new ColumnConstraints();
				colConst.setPercentWidth(100.0 / Params.WORLD_WIDTH);
				colConst.setHalignment(HPos.CENTER);
				red_line.getColumnConstraints().add(colConst);
				i++;
			}
		}
		int i = 0;
		while (i < Params.WORLD_HEIGHT) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setPercentHeight(100.0 / Params.WORLD_HEIGHT);
			rowConst.setValignment(VPos.CENTER);
			red_line.getRowConstraints().add(rowConst);
			i++;
		}
		red_line.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

		panehelper(numberCrittersPane);

		sliderticks(numberCrittersSlider, 25, 5, 1000);

        numberCrittersPane.add(numberCritters,0,0);
        numberCrittersPane.add(numberCrittersSlider,0,1);
        numberCrittersPane.add(inputCritters,1,1);
        numberCrittersPane.add(critterSpawn,1,0);

		panehelper(stepCritterPane);

		stepCritterPane.add(numberStep,0,0);
		stepCritterPane.add(stepSlider,0,1);
		stepCritterPane.add(userStepNumber,1,1);
		stepCritterPane.add(stepButton,1,0);

		sliderticks(stepSlider, 25, 5, 1000);

		inputs.getChildren().addAll(critterOptions,numberCrittersPane,stepCritterPane,statsBox,seedPane,exitBox);
        project.add(red_line,1,0);
        project.add(inputs,0,0);
        project.add(animationPane,1,1);
        Scene x = new Scene(project, size.getWidth()-800,size.getHeight()-600);
        primaryStage.setScene(x);
        primaryStage.show();
    }

	private void sliderticks(Slider slider, int major, int minor, int width) {
		slider.setMajorTickUnit(major);
		slider.setMinorTickCount(minor);
		slider.setPrefWidth(width);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
	}

	private void animationhelper(GridPane animationPane) {
		ColumnConstraints animationcolumn1 = new ColumnConstraints();
		animationcolumn1.setPercentWidth(60);
		animationcolumn1.setHalignment(HPos.CENTER);
		ColumnConstraints animationcolumn2 = new ColumnConstraints();
		animationcolumn2.setPercentWidth(20);
		animationcolumn2.setHalignment(HPos.CENTER);
		animationPane.getColumnConstraints().addAll(animationcolumn1, animationcolumn2);
	}

	private void seedhelper(GridPane seedPane) {
		RowConstraints seedrow1 = new RowConstraints();
		seedrow1.setPercentHeight(50);
		seedrow1.setValignment(VPos.BOTTOM);
		RowConstraints seedrow2 = new RowConstraints();
		seedrow2.setPercentHeight(50);
		seedrow2.setValignment(VPos.CENTER);

		seedPane.getRowConstraints().addAll(seedrow1, seedrow2);
	}

	private void panehelper(GridPane stepCritterPane) {
		RowConstraints steprow1 = new RowConstraints();
		steprow1.setPercentHeight(50);
		RowConstraints steprow2 = new RowConstraints();
		steprow2.setPercentHeight(50);
		RowConstraints steprow3 = new RowConstraints();
		steprow3.setPercentHeight(50);

		stepCritterPane.getRowConstraints().addAll(steprow1, steprow2, steprow3);

		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(75);
		column1.setHalignment(HPos.CENTER);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(25);
		column2.setHalignment(HPos.CENTER);
		stepCritterPane.getColumnConstraints().addAll(column1, column2);
	}


	private static ArrayList<String> getCrittersList() throws URISyntaxException, ClassNotFoundException {
        ArrayList<String> listCrits = new ArrayList<String>();
        File directory = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        String pkg = Main.class.getPackage().toString().split(" ")[1];
        directory = new File(directory.getAbsolutePath() + "/" + pkg);
        String [] lfile = directory.list();

		int i = 0;
		while (i < lfile.length) {
			if(!(lfile[i].startsWith(".class", lfile[i].length() - 6))) {
				i++;
				continue;
			}

			if ((Class.forName(pkg + ".Critter").isAssignableFrom(Class.forName(pkg + "." + lfile[i].substring(0, lfile[i].length() - 6))))
					&& !(Modifier.isAbstract(Class.forName(pkg + "." + lfile[i].substring(0, lfile[i].length() - 6)).getModifiers()))) {
				listCrits.add(lfile[i].substring(0, lfile[i].length() - 6));
			}
			i++;
		}
		return listCrits;
    }

    public void sliderSetUp(){

		sliderhelper(numberCrittersSlider, critterSpawn, inputCritters);
		sliderhelper(speedSlider, animationButton, animationVal);
		sliderhelper(stepSlider, stepButton, userStepNumber);

	}

	private void sliderhelper(Slider speedSlider, Button animationButton, Label animationVal) {
		speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					speedSlider.setValue(newValue.intValue());
				animationButton.setDisable(newValue.intValue() < 1);
					animationVal.setText(String.valueOf(newValue.intValue()));
				}
		});
	}
	private static void shapeSetup() {
		circle.setRadius((dimension.getHeight()*0.1)/Params.WORLD_HEIGHT);
		square.getPoints().clear();

		square.getPoints().addAll(0.0,0.0,((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),
				0.0,((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),
				0.0,((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT));

		triangle.getPoints().clear();
		triangle.getPoints().addAll(((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),0.0, 0.0
				,((dimension.getHeight()*0.22)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.22)/Params.WORLD_HEIGHT),
				((dimension.getHeight()*0.22)/Params.WORLD_HEIGHT));

		diamond.getPoints().clear();
		diamond.getPoints().addAll(((dimension.getHeight()*0.06)/Params.WORLD_HEIGHT),0.0,
				((dimension.getHeight()*0.22)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.06)/Params.WORLD_HEIGHT)
				,((dimension.getHeight()*0.06)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.22)/Params.WORLD_HEIGHT),0.0,
				((dimension.getHeight()*0.22)/Params.WORLD_HEIGHT));

		star.getPoints().clear();
		star.getPoints().addAll(((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),(-(dimension.getHeight()*0.02)/Params.WORLD_HEIGHT),
				((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.1)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.23)/Params.WORLD_HEIGHT),
				((dimension.getHeight()*0.05)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),
				((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT)
				,((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT), ((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),
				((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT),((dimension.getHeight()*0.2)/Params.WORLD_HEIGHT));
	}


	public void updates(){

        critterOptions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                critterSelected = true;
            }
        });

        critterSpawn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int num = (int) numberCrittersSlider.getValue();

				int i = 0;
				while (i < num) {
					try {
						Critter.createCritter(critterOptions.getValue());
					} catch (Exception e1) {

					}
					i++;
				}
				Critter.displayWorld(red_line);
                    statUpdate();
            }
        });

        stepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int num = (int) stepSlider.getValue();
				int i = 0;
				while (i<num) {
					Critter.worldTimeStep();
					i++;
				}
				Critter.displayWorld(red_line);
                statUpdate();
            }
        });



		seedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Critter.setSeed(Long.parseLong(seed.getText()));
			}
		});
        animationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                animation = !animation;
				if (!animation) {
					animationButton.setText("Simulate");
					toggleAnimation();
					simulate.stop();
				} else {
					animationButton.setText("Stop");
					toggleAnimation();
					simulate.start();
				}
			}
        });

		runStats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				statUpdate();
			}
		});
    }


    public void toggleAnimation(){

		critterSpawn.setDisable(animation);
		numberCrittersSlider.setDisable(animation);
		speedSlider.setDisable(animation);
		stepSlider.setDisable(animation);
		stepButton.setDisable(animation);
		runStats.setDisable(animation);
		critterOptions.setDisable(animation);
		seed.setDisable(animation);
		seedButton.setDisable(animation);
    }
    public static void statUpdate(){
        stat.clear();
        try {
            String y = (String) Class.forName(myPackage + "." +
					critterOptions.getValue()).getMethod("runStats",
					List.class).invoke(critterOptions.getValue(), Critter.getInstances(critterOptions.getValue()));
            stat.setText(y);
        }
        catch (Exception e) { }
    }



}