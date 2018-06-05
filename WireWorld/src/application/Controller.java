package application;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import data.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class Controller implements Initializable {

	private MainWindow wireWorldFunctionality;

	// i tak mamy inne ?cie?ki, bo robimy w innych ?rodowiskach

	// public static final String DEFAULT_TEXT_FILE_PATH =

	// "C:\\Users\\Dell\\IdeaProjects\\2018L_JIMP2_repozytorium_gr11\\WireWorld\\Program\\src\\defaultInputFile.txt";
	public static final String DEFAULT_TEXT_FILE_PATH = "C:\\Users\\sucha_rakzuks\\Desktop\\defaultInputFile.txt";

	private final String FILE_CHOOSER_TITLE = "Wybierz plik wej?ciowy";

	private FileChooser fileChooser;

	int cellType;

	@FXML
	private Canvas canvas;
	@FXML
	private Button selectInputFileButton;
	@FXML
	private Button startButton;
	@FXML
	private Button stopButton;
	@FXML
	private Button pauseButton;
	@FXML
	private Button saveButton;
	@FXML
	private TextArea currentInputFileTextAreaView;
	@FXML
	private ToggleGroup toggleGroup;
	@FXML
	private RadioButton electronHeadRadioButton;
	@FXML
	private RadioButton electronTailRadioButton;
	@FXML
	private RadioButton conductorRadioButton;
	@FXML
	private RadioButton emptyCellRadioButton;
	@FXML
	private RadioButton diodeRadioButton;
	@FXML
	private CheckBox userDrawingCheckBox;
	@FXML
	private VBox radioButtonsVBox;
	@FXML
	private Slider speedSlider;

	@FXML
	protected void handleFileChooseButton(ActionEvent event) {

		Node source = (Node) event.getSource();
		Window stage = source.getScene().getWindow();

		String filePath;
		File file = fileChooser.showOpenDialog(stage);
		if (file == null) {
			return;
		} else {
			filePath = file.getAbsolutePath();
		}

		try {
			wireWorldFunctionality.setBoardFromFile(canvas, filePath); // tu powinni?my obs?u?y? jak?? warto?? zwracan?
																		// w razie b??dnego
			// formatu pliku
			startButton.setDisable(false);
			pauseButton.setDisable(false);
			stopButton.setDisable(false);
			speedSlider.setDisable(false);

			currentInputFileTextAreaView.setText(filePath);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (WrongInputFileException e) {
			//TODO!!!
		}

	}

	@FXML
	protected void handleSaveButton(ActionEvent event) {

		Node source = (Node) event.getSource();
		Window stage = source.getScene().getWindow();

		String savedFilePath;
		File savedFile = fileChooser.showSaveDialog(stage);
		if (savedFile == null) {
			return;
		} else {
			savedFilePath = savedFile.getAbsolutePath();
		}
		try {
			wireWorldFunctionality.saveGeneration(savedFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block //obs?u?y? ten b??d !!!
			e.printStackTrace();
		}
	}

	@FXML
	protected void handleStartButton(ActionEvent event) {
		saveButton.setDisable(true);

		if (toggleGroup.getSelectedToggle() != null)
			toggleGroup.getSelectedToggle().setSelected(false);
		radioButtonsVBox.setDisable(true);
		userDrawingCheckBox.setSelected(false);
		userDrawingCheckBox.setDisable(true);

		wireWorldFunctionality.simulate(canvas, 100);
	}

	@FXML
	protected void handlePauseButton(ActionEvent event) {
		wireWorldFunctionality.pauseSimulation();
		userDrawingCheckBox.setDisable(false);
		saveButton.setDisable(false);
	}

	@FXML
	protected void handleStopButton(ActionEvent event) {
		try {
			userDrawingCheckBox.setDisable(false);
			saveButton.setDisable(false);
			saveButton.setDisable(true);
			wireWorldFunctionality.returnToFirstBoardState(canvas);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(WrongInputFileException e) {
			//TODO !!!
		}
	}

	@FXML
	protected void handleuserDrawingCheckBox(ActionEvent event) {
		if (userDrawingCheckBox.isSelected() == true) {
			radioButtonsVBox.setDisable(false);
		} else {
			if (toggleGroup.getSelectedToggle() != null)
				toggleGroup.getSelectedToggle().setSelected(false);
			radioButtonsVBox.setDisable(true);
		}

	}

	@FXML
	protected void handleEmptyCellRadioButton(ActionEvent event) {
		cellType = Element.EMPTY_CELL;
	}

	@FXML
	protected void handleElectronTailRadioButton(ActionEvent event) {
		cellType = Element.ELECTRON_TAIL;
	}

	@FXML
	protected void handleElectronHeadRadioButton(ActionEvent event) {
		cellType = Element.ELECTRON_HEAD;
	}

	@FXML
	protected void handleConductorRadioButton(ActionEvent event) {
		cellType = Element.CONDUCTOR;
	}

	@FXML
	protected void handleDiodeRadioButton(ActionEvent event) {
		cellType = Element.DIODE;
	}

	@FXML
	protected void handleAndGateRadioButton(ActionEvent event) {
		cellType = Element.AND_GATE;
	}

	@FXML
	protected void handleOrGateRadioButton(ActionEvent event) {
		cellType = Element.OR_GATE;
	}

	@FXML
	protected void handleNorGateRadioButton(ActionEvent event) {
		cellType = Element.NOR_GATE;
	}

	@FXML
	protected void handleWireRadioButton(ActionEvent event) {
		cellType = Element.WIRE;
	}

	@FXML
	protected void handleClearBoardButton(ActionEvent event) {
		wireWorldFunctionality.drawEmptyBoard(canvas);
	}

	@FXML
	protected void handleMouseClickedOnCanvas(MouseEvent event) {
		if (userDrawingCheckBox.isSelected() == true && toggleGroup.getSelectedToggle() != null) {

			wireWorldFunctionality.addSelectedToBoard(event.getX(), event.getY(), cellType, canvas);

			startButton.setDisable(false);
			speedSlider.setDisable(false);
			pauseButton.setDisable(false);
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initFileChooser();
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleMouseClickedOnCanvas(event);

			}

		});

		speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				wireWorldFunctionality.setInterval((int) (1000 / newValue.doubleValue()));
				System.out.println(wireWorldFunctionality.getInterval());
			}
		});

	}

	public void setWireWorldFunctionality(MainWindow functionallity) {
		this.wireWorldFunctionality = functionallity;
	}

	private void initFileChooser() {
		fileChooser = new FileChooser();
		fileChooser.setTitle(FILE_CHOOSER_TITLE);
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
	}

	public void drawEmptyBoard() {
		wireWorldFunctionality.drawEmptyBoard(canvas);
	}

}
