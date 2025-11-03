package com.hero.game.game;

import com.hero.game.decorators.DamageBoostDecorator;
import com.hero.game.decorators.HealthBoostDecorator;
import com.hero.game.factories.HeroFactory;
import com.hero.game.heroes.Hero;
import com.hero.game.observers.GameLogger;
import com.hero.game.observers.UIDisplay;
import com.hero.game.strategies.MagicAttack;
import com.hero.game.strategies.MeleeAttack;
import com.hero.game.strategies.RangedAttack;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HeroBattleGame extends Application {
    private Hero player1;
    private Hero player2;
    private Canvas battlefield;
    private GraphicsContext gc;
    private TextArea eventDisplay;
    private HeroFactory factory = new HeroFactory();
    private double fieldWidth;
    private double fieldHeight;
    private final int HERO_SIZE = 50;
    private final int STEP = 10;
    private Stage battleStage;
    private boolean gameOver = false;
    private Stage currentStartStage;
    private boolean healthBoostUsedP1 = false;
    private boolean damageBoostUsedP1 = false;
    private boolean healthBoostUsedP2 = false;
    private boolean damageBoostUsedP2 = false;

    @Override
    public void start(Stage primaryStage) {
        currentStartStage = primaryStage;
        openStartMenu(primaryStage);
    }

    private void openStartMenu(Stage stage) {
        VBox startPanel = new VBox(20);
        startPanel.setPadding(new Insets(20));
        startPanel.setAlignment(Pos.CENTER);

        Label title = new Label("Hero Battle Game");
        title.setFont(Font.font(24));

        HBox selectionBox = new HBox(10);
        selectionBox.setAlignment(Pos.CENTER);
        Label p1Label = new Label("Player 1:");
        ComboBox<String> heroType1 = new ComboBox<>();
        heroType1.getItems().addAll("Warrior", "Mage", "Archer");
        heroType1.setValue("Warrior");

        Label p2Label = new Label("Player 2:");
        ComboBox<String> heroType2 = new ComboBox<>();
        heroType2.getItems().addAll("Warrior", "Mage", "Archer");
        heroType2.setValue("Mage");

        Button startButton = new Button("Start Battle");
        startButton.setOnAction(e -> {
            currentStartStage.close();
            createBattleStage();
            startBattle(heroType1.getValue(), heroType1.getValue(), heroType2.getValue(), heroType2.getValue());
            battleStage.show();
            battleStage.setFullScreen(true);
            battleStage.setFullScreenExitHint("");
            battleStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            battleStage.getScene().getRoot().requestFocus();
        });

        selectionBox.getChildren().addAll(p1Label, heroType1, p2Label, heroType2);

        startPanel.getChildren().addAll(title, selectionBox, startButton);

        Scene startScene = new Scene(startPanel, 600, 300);
        stage.setTitle("Start Menu");
        stage.setScene(startScene);
        stage.show();
    }

    private void createBattleStage() {
        battleStage = new Stage();
        StackPane battleRoot = new StackPane();

        battlefield = new Canvas();
        gc = battlefield.getGraphicsContext2D();

        battlefield.widthProperty().bind(battleRoot.widthProperty());
        battlefield.heightProperty().bind(battleRoot.heightProperty());

        // Event Display overlay top-left
        eventDisplay = new TextArea();
        eventDisplay.setEditable(false);
        eventDisplay.setWrapText(true);
        eventDisplay.setPrefSize(300, 200);
        eventDisplay.setStyle("-fx-background-color: rgba(255,255,255,0.7);");
        eventDisplay.setFocusTraversable(false);

        VBox overlay = new VBox(eventDisplay);
        overlay.setAlignment(Pos.TOP_LEFT);
        overlay.setPadding(new Insets(10));

        battleRoot.getChildren().addAll(battlefield, overlay);

        Scene battleScene = new Scene(battleRoot);
        battleScene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));

        // Update dimensions on resize
        battleScene.widthProperty().addListener((obs, old, newVal) -> {
            fieldWidth = newVal.doubleValue();
            drawBattlefield();
        });
        battleScene.heightProperty().addListener((obs, old, newVal) -> {
            fieldHeight = newVal.doubleValue();
            drawBattlefield();
        });

        battleStage.setTitle("Battlefield");
        battleStage.setScene(battleScene);
    }

    private void startBattle(String type1, String name1, String type2, String name2) {
        player1 = factory.createHero(type1, name1);
        player2 = factory.createHero(type2, name2);

        gameOver = false;
        healthBoostUsedP1 = false;
        damageBoostUsedP1 = false;
        healthBoostUsedP2 = false;
        damageBoostUsedP2 = false;

        // Initial positions relative to field size (set after show, but approximate)
        player1.setPosition(100, 300);
        player2.setPosition(700, 300);

        GameLogger logger = new GameLogger();
        UIDisplay uiDisplay = new UIDisplay(eventDisplay);

        player1.registerObserver(logger);
        player1.registerObserver(uiDisplay);
        player2.registerObserver(logger);
        player2.registerObserver(uiDisplay);

        player1.setAttackStrategy(new MeleeAttack());
        player2.setAttackStrategy(new RangedAttack());

        // Initial messages for controls
        player1.notifyObservers("Player 1 Controls: Move WASD, Attack Space, Strategy 1/2/3 (Melee/Ranged/Magic), Boosts Q(Health)/E(Damage)");
        player2.notifyObservers("Player 2 Controls: Move Arrows, Attack Enter, Strategy 8/9/0 (Melee/Ranged/Magic), Boosts I(Health)/P(Damage)");

        // Draw after setting
        drawBattlefield();
    }

    private void changeStrategy(Hero hero, String strategy) {
        if (hero == null) return;
        switch (strategy) {
            case "Melee":
                hero.setAttackStrategy(new MeleeAttack());
                break;
            case "Ranged":
                hero.setAttackStrategy(new RangedAttack());
                break;
            case "Magic":
                hero.setAttackStrategy(new MagicAttack());
                break;
        }
    }

    private Hero applyHealthBoost(Hero hero, int boost, boolean isPlayer1) {
        if (hero == null) return hero;
        if ((isPlayer1 && healthBoostUsedP1) || (!isPlayer1 && healthBoostUsedP2)) {
            hero.notifyObservers(hero.getName() + " has already used health boost!");
            return hero;
        }
        if (isPlayer1) healthBoostUsedP1 = true;
        else healthBoostUsedP2 = true;
        return new HealthBoostDecorator(hero, boost);
    }

    private Hero applyDamageBoost(Hero hero, int boost, boolean isPlayer1) {
        if (hero == null) return hero;
        if ((isPlayer1 && damageBoostUsedP1) || (!isPlayer1 && damageBoostUsedP2)) {
            hero.notifyObservers(hero.getName() + " has already used damage boost!");
            return hero;
        }
        if (isPlayer1) damageBoostUsedP1 = true;
        else damageBoostUsedP2 = true;
        return new DamageBoostDecorator(hero, boost);
    }

    private void handleKeyPress(KeyCode code) {
        if (player1 == null || player2 == null || gameOver) return;

        // Player 1
        if (player1.isAlive()) {
            switch (code) {
                case W:
                    player1.setPosition(player1.getX(), Math.max(0, player1.getY() - STEP));
                    break;
                case S:
                    player1.setPosition(player1.getX(), (int) Math.min(fieldHeight - HERO_SIZE, player1.getY() + STEP));
                    break;
                case A:
                    player1.setPosition(Math.max(0, player1.getX() - STEP), player1.getY());
                    break;
                case D:
                    player1.setPosition((int) Math.min(fieldWidth - HERO_SIZE, player1.getX() + STEP), player1.getY());
                    break;
                case SPACE:
                    player1.attack(player2);
                    break;
                case DIGIT1:
                    changeStrategy(player1, "Melee");
                    break;
                case DIGIT2:
                    changeStrategy(player1, "Ranged");
                    break;
                case DIGIT3:
                    changeStrategy(player1, "Magic");
                    break;
                case Q:
                    player1 = applyHealthBoost(player1, 20, true);
                    break;
                case E:
                    player1 = applyDamageBoost(player1, 10, true);
                    break;
            }
        }

        // Player 2
        if (player2.isAlive()) {
            switch (code) {
                case UP:
                    player2.setPosition(player2.getX(), Math.max(0, player2.getY() - STEP));
                    break;
                case DOWN:
                    player2.setPosition(player2.getX(), (int) Math.min(fieldHeight - HERO_SIZE, player2.getY() + STEP));
                    break;
                case LEFT:
                    player2.setPosition(Math.max(0, player2.getX() - STEP), player2.getY());
                    break;
                case RIGHT:
                    player2.setPosition((int) Math.min(fieldWidth - HERO_SIZE, player2.getX() + STEP), player2.getY());
                    break;
                case ENTER:
                    player2.attack(player1);
                    break;
                case DIGIT8:
                    changeStrategy(player2, "Melee");
                    break;
                case DIGIT9:
                    changeStrategy(player2, "Ranged");
                    break;
                case DIGIT0:
                    changeStrategy(player2, "Magic");
                    break;
                case I:
                    player2 = applyHealthBoost(player2, 20, false);
                    break;
                case P:
                    player2 = applyDamageBoost(player2, 10, false);
                    break;
            }
        }

        drawBattlefield();
        checkGameOver();
    }

    private void checkGameOver() {
        if (!player1.isAlive() || !player2.isAlive()) {
            gameOver = true;
            showGameOverOverlay();
        }
    }

    private void showGameOverOverlay() {
        VBox gameOverBox = new VBox(10);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        gameOverBox.prefWidthProperty().bind(battleStage.widthProperty());
        gameOverBox.prefHeightProperty().bind(battleStage.heightProperty());

        Label label = new Label("GAME OVER!");
        label.setFont(Font.font(50));
        label.setTextFill(Color.WHITE);

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            battleStage.close();
            Stage newStart = new Stage();
            openStartMenu(newStart);
            currentStartStage = newStart;
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> battleStage.close());

        gameOverBox.getChildren().addAll(label, restartButton, exitButton);

        ((StackPane) battleStage.getScene().getRoot()).getChildren().add(gameOverBox);
    }

    private void drawBattlefield() {
        gc.clearRect(0, 0, battlefield.getWidth(), battlefield.getHeight());
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, battlefield.getWidth(), battlefield.getHeight());

        if (player1 != null) drawHero(player1, Color.BLUE);
        if (player2 != null) drawHero(player2, Color.RED);
    }

    private void drawHero(Hero hero, Color color) {
        if (!hero.isAlive()) return;

        gc.setFill(color);
        gc.fillOval(hero.getX(), hero.getY(), HERO_SIZE, HERO_SIZE);

        // Health bar
        double healthRatio = (double) hero.getHealth() / hero.getMaxHealth();
        gc.setFill(Color.RED);
        gc.fillRect(hero.getX(), hero.getY() - 20, HERO_SIZE, 5);
        gc.setFill(Color.GREEN);
        gc.fillRect(hero.getX(), hero.getY() - 20, HERO_SIZE * healthRatio, 5);

        // Name
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(12));
        gc.fillText(hero.getName(), hero.getX(), hero.getY() - 25);
    }

    public static void main(String[] args) {
        launch(args);
    }
}