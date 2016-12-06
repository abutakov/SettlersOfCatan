
/*  
                    Client User Interface - Settlers of Catan

Class:      Adjvanced Java - CIT-285-01
            Professor Miller, Fall 2016

Group:      RARA - Settlers of Catan
            Ruchelly Almeida
            Alessandra Shipman     
            Oleksii Butakov
            Andrew Thomas

Files:      Bank.java
            Boundary.java
            ClientUI.java (Current File)
            Coordinate.java 
            DevelopmentCard.java 
            GameManager.java
            HexTile.java
            Intersection.java 
            Player.java
            Trade.java 
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg


Classes:    ClientUI

                                    Summary:
This class sets up the client-side user interface. It displays the game board,
player information, dialog box prompts, buttons for play and various menus.

 
Activity:	  -Date-             -Person-               -Updates-
            November 20, 2016		AS          * Created ClientUI class
                                                    * Created primaryStage titled
                                                      "Settlers of Catan"
                                                    * Created StackPane gameBoard
                                        
                                        AT          * Added lines and circles from
                                                      Boundary and Intersection
                                                      classes to draw gameboard
                                                    * Added scaleFactor, circleSize,
                                                      xOffset, and yOffset properties
                                                      for ease of UI manipulation
                                                    * Added findBuildableRoads
						      and buildARoad method
                                                      to allow UI-driven buying of
                                                      roads

            November 23, 2016           AT          * Added comments to every method
                                                    * Fixed some small logical errors
                                                      in build functions
                                                    * Added known issue of not sizing clickable
                                                      nodes after they are clicked to tracker

            November 30, 2016           OB          * Added Resources Panel 
                                                    * Added CSS styling for buttons 

                                                    
	    November 26, 2016		RA	    * Created createStatsPanel method, lines 164
	    					      to 212 and placed it in a StackPane 
						      Added panels to scene 	

            November 27, 2016           AT          * Added background
                                                    * Added new gameboard button 
                                                      to allow users to randomly
                                                      generate new gameboard if 
                                                      desired
                                                    * Added src Images file
                                        
                                        AS&AT       * Edited images to better fit
                                                      HexTiles

            December 5, 2016           OB          * Added methods for various types
                                                     of pop-up windows
                                                   * Added numbered circles for each tile
                                                   
                                                    

 */
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import static javafx.scene.layout.BackgroundPosition.*;
import static javafx.scene.layout.BackgroundRepeat.*;
import static javafx.scene.layout.BorderStroke.*;
import static javafx.scene.layout.BorderStrokeStyle.*;
import static javafx.scene.layout.CornerRadii.*;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class ClientUI extends Application {

    // Scale factor multiplies the coordinates of each point by a consistent ratio
    // To scale UI up to desired size
    static double scaleFactor = 35;
    // X and Y offsets are a hacky way to center the game board
    // Will hopefully come up with a better and more scalable way to do this
    static double xOffset = 1.3;
    static double yOffset = .5;
    // Default size for circles
    static double circleSize = 5.0;
    static double hexCircleSize = 20.0;
    
    // Window sizes
    private double maxSizeX = 900;
    private double minSizeX = 700;
    private double maxSizeY = 800;
    private double minSizeY = 600;


        
    Insets insets = new Insets(12);
    DropShadow ds = new DropShadow();

    ArrayList<Circle> circles = new ArrayList<>();
    ArrayList<Line> lines = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Settlers of Catan");
        Pane bgPane = new Pane();
        BorderPane bp = new BorderPane();
        Pane gameBoard = new Pane();


        bgPane.getChildren().add(bp);

        
        // Panel to hold Player's information
        StackPane player1Panel = new StackPane();
        StackPane player2Panel = new StackPane();
        StackPane player3Panel = new StackPane();
        StackPane player4Panel = new StackPane();

        try {
            // Creates player's information panels (Pane, playerId, background)
            createStatsPanel(player1Panel, 0, "Images/bluePlayer3.png");
            createStatsPanel(player2Panel, 1, "Images/redPlayer3.png");
            createStatsPanel(player3Panel, 2, "Images/greenPlayer2.png");
            createStatsPanel(player4Panel, 3, "Images/yellowPlayer.png");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Displays panels of players 1 and  3
        VBox left = new VBox();
        left.getChildren().addAll(player1Panel, player3Panel);
        left.setAlignment(Pos.TOP_LEFT);
        left.setSpacing(230);

        // Displays panels of players 2 and 4
        VBox right = new VBox();
        right.getChildren().addAll(player2Panel, player4Panel);
        right.setAlignment(Pos.TOP_RIGHT);
        right.setSpacing(230);

        // Min size and max size are currently the same
        // Will hopefully allow resizing eventually
        gameBoard.setMaxSize(900, 800);
        gameBoard.setMinSize(700, 600);

        // Creates a black boundary
        Border b = new Border(new BorderStroke(BLACK, SOLID, EMPTY, DEFAULT_WIDTHS));
        gameBoard.setBorder(b);

        // Iterate over Hexes and adds them to the GUI
        for (HexTile tile : GameManager.tiles) {
            Polygon hex = tile.hexagon;

            gameBoard.getChildren().add(hex);

        }

        // Iterate over Hexes and adds numbered circles for each hex
        for (HexTile tile : GameManager.tiles) {

            double centerX = tile.centerCoordinates.getUIX();
            double centerY = tile.centerCoordinates.getUIY();

            if (!tile.isCenter()) {
                Circle circle = new Circle(centerX, centerY, hexCircleSize);
                circle.setFill(WHITE);
                circle.setStroke(Color.web("black", 1.0));
                circle.setStrokeWidth(2);

                Text text = new Text(String.valueOf(tile.getNumRoll()));
                text.setFont(Font.font(null, FontWeight.BOLD, 16));

                text.setX(centerX - 5);
                text.setY(centerY + 3);

                text.setBoundsType(TextBoundsType.VISUAL);

                gameBoard.getChildren().addAll(circle, text);
            }
        }

        // Iterate over all boundaries and add their respective lines to the GUI
        for (Boundary boundary : GameManager.boundaries) {
            lines.add(boundary.getLine());
            gameBoard.getChildren().add(boundary.getLine());
        }

        // Iterate over intersections and add their circles to the GUI
        for (Intersection intersection : GameManager.intersections) {
            Circle circle = intersection.getCircle();

            // Set all circles to hollow black
            circle.setStroke(BLACK);
            circle.setFill(WHITE);

            circles.add(circle);

            gameBoard.getChildren().add(circle);
        }

        // Put game board at center of GUI frame
        bp.setCenter(gameBoard);

        // ___________________________  Buttons ___________________________
        // Button styling using CSS. 
        String btnStyle
                = "-fx-text-fill: white;\n"
                + "-fx-font-family: \"Arial Narrow\";\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 11pt;\n"
                + "-fx-background-color: linear-gradient(#61a2b1, #2A5058);\n"
                + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n"
                + "-fx-background-color: linear-gradient(#2A5058, #61a2b1);\n";

        HBox hBoxButtons = new HBox(25);

        Button btnRoll = new Button("Roll");
        // Applying predefined Style for each button
        btnRoll.setStyle(btnStyle);
        btnRoll.setOnAction(e
                -> {
            int diceRoll = GameManager.rollDice();
            for (HexTile tile : GameManager.tiles) {
                if (tile.getNumRoll() == diceRoll) {
                    tile.yieldResources();
                }
            }

        });

        Button btnBuild = new Button("Build Improvements");
        btnBuild.setStyle(btnStyle);
        btnBuild.setOnAction(e -> openBuildMenu());
        Button btnDevCards = new Button("Development Cards");
        btnDevCards.setStyle(btnStyle);

        Button btnTrade = new Button("Trade");
        btnTrade.setStyle(btnStyle);
        btnTrade.setOnAction(e -> {
            // your statement
        });

        Button btnEndTurn = new Button("End Turn");
        btnEndTurn.setOnAction(e -> GameManager.endTurn(GameManager.isSetUpPhase));
        btnEndTurn.setStyle(btnStyle);
        btnEndTurn.setOnAction(e -> {
            // your statement

        });

        /*
        Delete This?
        For Debugging only
         */
        Button btnNewBoard = new Button("New Board");
        btnNewBoard.setOnAction(e -> {
            GameManager.gm1.buildGameboard();
            this.start(primaryStage);
        });
        btnNewBoard.setStyle(btnStyle);

        Button btnTest = new Button("TEST");
        btnTest.setOnAction(e -> {
            showWarningDialog("TEST");
            showErrorDialog("TEST");
        });
        btnNewBoard.setStyle(btnStyle);

        hBoxButtons.getChildren().addAll(btnRoll, btnBuild, btnDevCards, btnTrade, btnEndTurn, btnNewBoard, btnTest);
        hBoxButtons.setAlignment(Pos.CENTER);

        // ________________________  Resource Panel ____________________________
        // VBox that holds Button and Available Resources Panels, 
        // and located at the bottom of BorderPanel
        VBox vBoxBottom = new VBox(5);

        // This HBox holds StackPane that displays available resources 
        // for the Active Player
        HBox resourcesHBox = new HBox(300);

        // Panel that deisplays available resources to the active player
        StackPane resourcePanel = new StackPane();
        resourcePanel.setAlignment(new Label("Available Resources"), Pos.CENTER);

        // Calling method that fills our Resource Pane with up-to-date information
        createResoursePanel(resourcePanel);

        resourcesHBox.getChildren().add(resourcePanel);
        resourcesHBox.setAlignment(Pos.CENTER);

        // Adding Resources and Button panels to the VBox
        vBoxBottom.getChildren().addAll(resourcesHBox, hBoxButtons);
        // _____________________________________________________________________

        // Put players 1 and 3 information panels on the left of frame
        bp.setLeft(left);
        // Put players 2 and 4 information panels on the right of frame
        bp.setRight(right);
        // vBoxBottom contains buttons and current player's Resource panel
        bp.setBottom(vBoxBottom);

       
        Image woodImg = new Image(this.getClass().getClassLoader().getResourceAsStream("Images/background.jpg"));
        BackgroundImage bgWood = new BackgroundImage(woodImg, NO_REPEAT, NO_REPEAT, CENTER, BackgroundSize.DEFAULT);
        
        bgPane.setBackground(new Background(bgWood));
        
        // Set up scene size
        Scene scene = new Scene(bgPane, 1210, 720); // previous width is 1280
        
        primaryStage.setScene(scene);
        

        

        primaryStage.show();
    }

    // Creates panels with player's information during game
    public void createStatsPanel(Pane pane, int playerId, String backgroundAddress) throws FileNotFoundException {

        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(backgroundAddress));
        ImageView imageView = new ImageView(image);

        // Creates text for player number
        Text txtPlayer = new Text("   Player " + (playerId + 1));
        txtPlayer.setFill(WHITE);
        txtPlayer.setCache(true);
        txtPlayer.setEffect(ds);
        txtPlayer.setFont(Font.font(null, FontWeight.BOLD, 18));
        txtPlayer.setTextAlignment(TextAlignment.CENTER);

        // Creates grid to hold player's informations
        GridPane gridPane = new GridPane();
        gridPane.setPadding(insets);
        gridPane.add(txtPlayer, 0, 0);
        gridPane.add(new Label(" "), 0, 1);
        gridPane.add(new Text("Resource Count: "), 0, 2);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getResourceTotal())), 1, 2);
        gridPane.add(new Text("Dev. Cards: "), 0, 3);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getDevelopmentCardCount())), 1, 3);
        gridPane.add(new Text("Victory Points: "), 0, 4);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getVisibleVictoryPoints())), 1, 4);
        gridPane.add(new Text("Knight Cards: "), 0, 5);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getKnightCards())), 1, 5);
        gridPane.add(new Text("Roads Count: "), 0, 6);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getRoadCount())), 1, 6);

        // Adds a border to the pane(panel)
        final String cssDefault = "-fx-border-color: firebrick;\n"
                + "-fx-border-insets: 2;\n"
                + "-fx-border-width: 10;\n"
                + "-fx-background-radius: 5;\n";
        pane.setStyle(cssDefault);

        // Adds background(imageView) and player's info(gridPane) to pane
        pane.getChildren().add(imageView);
        pane.getChildren().add(gridPane);

    }

    // Creates Pane that contains information about the resources 
    // of the current player
    public void createResoursePanel(Pane pane) {
        // Creates grid to hold player's informations
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));

        gridPane.add(new Label("Brick"), 0, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.BRICK])), 0, 1);

        gridPane.add(new Label("Lumber"), 1, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.LUMBER])), 1, 1);

        gridPane.add(new Label("Ore"), 2, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.ORE])), 2, 1);

        gridPane.add(new Label("Wheat"), 3, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.WHEAT])), 3, 1);

        gridPane.add(new Label("Wool"), 4, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.WOOL])), 4, 1);

        // Adds a border to the pane(panel)
        
        /*
        final String cssDefault
                = "-fx-border-color: #C8C8C8;\n"
                + "-fx-border-insets: 2;\n"
                + "-fx-font-size: 12pt;\n"
                + "-fx-border-width: 5;\n"
                + "-fx-box-shadow: 5px;\n"
                + "-fx-background-color: linear-gradient(brown,#DDDDDD);\n"
                + "-fx-background-radius: 5;\n";
        */
        
        String cssDefault = 
                "-fx-text-fill: #2A5058;\n"
                + "-fx-font-family: \"Arial Narrow\";\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 14pt;\n"
                + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n"
                + "-fx-background-color: linear-gradient(#61a2b1, #2A5058);\n"
                + "-fx-background-radius: 5;\n";
        
        pane.setStyle(cssDefault);

        pane.getChildren().add(gridPane);
    }

    void showErrorDialog(String text) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("An Error Accured");
        alert.setContentText(text);

        alert.showAndWait();
    }

    void showWarningDialog(String text) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Warning!");
        alert.setContentText(text);

        alert.showAndWait();
    }

    ArrayList<Boundary> findBuildableRoads(int currentPlayerID) {

        // Create an ArrayList to hold roads the active player can build on
        ArrayList<Boundary> buildableRoads = new ArrayList<>();

        for (Boundary b : GameManager.boundaries) {
            if (b.isOccupiable(currentPlayerID)) {
                // For each boundary, if it is buildable, add it to the ArrayList
                buildableRoads.add(b);
            }
        }

        // Return the ArrayList
        return buildableRoads;
    }

    void buildARoad(ArrayList<Boundary> buildableRoads, Boundary[] boundaries, int activePlayerID) {

        // For each boundary in the game,
        for (Boundary b : boundaries) {
            // Check against every buildableRoad (number should be relatively low)
            for (Boundary road : buildableRoads) {
                // Create a Line object for ease of use
                Line line = b.getLine();

                // If the buildable road is equal to the current road
                if (road.getLine() == line) {
                    // Make it wider and setOnClick to GUIBuildRoad method
                    line.setStrokeWidth(4);
                    line.setOnMouseClicked(e -> {
                        Bank.GUIBuildRoad(activePlayerID, road);
                        restoreUIElements(circles, lines);
                    });
                }
            }
        }
    }

    ArrayList<Intersection> findBuildableSettlements(int currentPlayerID, boolean setUpPhase) {

        // Create an ArrayList to hold Intersections the active player can build on
        ArrayList<Intersection> buildableIntersections = new ArrayList<>();

        for (Intersection i : GameManager.intersections) {
            if (i.isOccupiable(currentPlayerID, setUpPhase)) {
                // If the intersection is occupiable, add it
                buildableIntersections.add(i);
            }
        }

        // Return list
        return buildableIntersections;
    }

    void buildASettlement(ArrayList<Intersection> buildableSettlements, Intersection[] intersections, int activePlayerID) {

        for (Intersection i : intersections) {
            // For each intersection in the game, create a Circle from its circle
            Circle circle = i.getCircle();

            for (Intersection intersection : buildableSettlements) {
                // Compare each circle to buildableSettlements
                if (intersection.getCircle() == circle) {
                    // Make stroke thicker and set clickable if its buildable
                    circle.setStrokeWidth(4);
                    circle.setOnMouseClicked(e -> {
                        Bank.GUIBuildSettlement(activePlayerID, intersection);
                        restoreUIElements(circles, lines);
                    });
                }
            }
        }
    }

    ArrayList<Intersection> findBuildableCities(int currentPlayerID) {

        ArrayList<Intersection> buildableCities = new ArrayList<>();

        for (Intersection i : GameManager.intersections) {
            if (i.getPlayer() == currentPlayerID) {
                // You can only build a city where you already have a settlement
                buildableCities.add(i);
            }
        }
        // Return ArrayList of all buildable locations for cities
        return buildableCities;
    }

    void buildACity(ArrayList<Intersection> buildableCities, Intersection[] intersections, int activePlayerID) {

        for (Intersection i : intersections) {
            // For every intersection create a circle from its circle
            Circle circle = i.getCircle();
            // Compare to the buildable cities
            for (Intersection intersection : buildableCities) {
                if (intersection.getCircle() == circle) {
                    // Make it bigger and clickable
                    circle.setStrokeWidth(4);
                    circle.setOnMouseClicked(e -> {
                        Bank.GUIBuildCity(activePlayerID, intersection);
                        restoreUIElements(circles, lines);
                    });
                }
            }
        }
    }

    private void openBuildMenu() {
        Stage buildMenu = new Stage();

        HBox btnBox = new HBox(25);
        Button btnBuildRoad = new Button("Build a Road");
        btnBuildRoad.setOnAction(e1 -> {
            ArrayList<Boundary> buildableRoads = findBuildableRoads(GameManager.activePlayerID);
            buildARoad(buildableRoads, GameManager.boundaries, GameManager.activePlayerID);
            buildMenu.close();
        });

        Button btnBuildSettlement = new Button("Build a Settlement");
        btnBuildSettlement.setOnAction(e1 -> {
            ArrayList<Intersection> buildableSettlements = findBuildableSettlements(GameManager.activePlayerID, GameManager.isSetUpPhase);
            buildASettlement(buildableSettlements, GameManager.intersections, GameManager.activePlayerID);
            buildMenu.close();

        });

        Button btnBuildCity = new Button("Build a City");

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> buildMenu.close());

        btnBox.getChildren().addAll(btnBuildRoad, btnBuildSettlement, btnBuildCity, btnCancel);

        Text txtBuild = new Text("Select a type of Improvement to Build:");
        txtBuild.setFont(new Font(14));
        txtBuild.setTextAlignment(TextAlignment.CENTER);

        BorderPane boPa = new BorderPane();
        boPa.setCenter(btnBox);
        boPa.setTop(txtBuild);

        buildMenu.initModality(Modality.APPLICATION_MODAL);

        buildMenu.setScene(new Scene(boPa));
        buildMenu.setTitle("Build Menu");
        buildMenu.show();
    }

    private void restoreUIElements(ArrayList<Circle> intersections, ArrayList<Line> boundaries) {
        for (Circle i : intersections) {
            if (i.getStroke() == BLACK) {
                i.setStrokeWidth(1);
            }
            i.setOnMouseClicked(e -> doNothing());

        }
        for (Line l : boundaries) {
            l.setStrokeWidth(4);
            l.setOnMouseClicked(e -> doNothing());
        }
    }

    private void doNothing() {

    }

}
