package com.lakesidess.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lakesidess.Constants;
import com.lakesidess.service.EmailService;
import com.lakesidess.service.LoadDataService;
import com.lakesidess.vo.OrderVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.extern.log4j.Log4j2;
import net.rgielen.fxweaver.core.FxmlView;

@Controller
@FxmlView("file-excel-stage.fxml")
@Log4j2
public class FileExcelController {
	@FXML
	private Pane idMainPanel;
	@FXML
	private TextField idInputFile;
	@FXML
	private TableView<OrderVO> idTable;
	
	@Autowired
	private EmailService emailService;
	@Autowired
	private LoadDataService loadDataService;
	
	private File file;

	@FXML
	public void initialize() {
		idInputFile.setEditable(false);
		idInputFile.setStyle("-fx-background-color: transparent;");
	}
	
	//FX actionButton
	public void findPathFile(ActionEvent actionEvent) {
		Node source = (Node) actionEvent.getSource();
		Window stage = source.getScene().getWindow();
		
		file = getPathFile(stage);

		if (file != null) {
			idInputFile.setText(file.getAbsolutePath());
		}
	}
	
	private File getPathFile(Window stage) {
		// create a File chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Excel File");

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("EXCEL files (*.xls | *.xlsx)", "*.xls",
				"*.xlsx");
		fileChooser.getExtensionFilters().add(extFilter);

		// Last Directory
		if (file != null) {
			String parentDirectory = file.getAbsoluteFile().getParent();
			fileChooser.setInitialDirectory(new File(parentDirectory));
		} else {
			fileChooser.setInitialDirectory(new File(Constants.USER_HOME));
		}

		// get the file selected
		return fileChooser.showOpenDialog(stage);
	}
	
	//FX actionButton
	public void loadTable(ActionEvent actionEvent) {
		String pathFile = idInputFile.getText();
		
		if(pathFile != null) {
			File file = new File(pathFile);
			String path = file.getAbsoluteFile().getParent();
			String nameXlsx = file.getName();
			
			try {
				List<OrderVO> orders=loadDataService.prepareData(path, nameXlsx);				
				displayTable(orders);
				
			} catch (IOException error) {
				log.error("Error Trying to read XLS File",error);
				createExceptionDialog(error);
			}
			
		}
		
	}
	
	private void displayTable(List<OrderVO> orders){
		ObservableList<OrderVO> data = FXCollections.observableArrayList(orders);
		idTable.getColumns().clear();
		
		TableColumn<OrderVO, Integer> column0 = new TableColumn<>("Id");
		column0.setCellValueFactory(new PropertyValueFactory<>("rowIndex"));
		
		TableColumn<OrderVO, SimpleStringProperty> column1 = new TableColumn<>("Fecha Entrega");
		column1.setCellValueFactory(new PropertyValueFactory<>("dateDelivery"));

		TableColumn<OrderVO, String> column2 = new TableColumn<>("Tiempo Inicio");
		column2.setCellValueFactory(new PropertyValueFactory<>("initialTime"));
		
		TableColumn<OrderVO, String> column3 = new TableColumn<>("Tiempo Fin");
		column3.setCellValueFactory(new PropertyValueFactory<>("endingTime"));
		
		TableColumn<OrderVO, Long> column4 = new TableColumn<>("Orden");
		column4.setCellValueFactory(new PropertyValueFactory<>("numberOrder"));
		
		TableColumn<OrderVO, String> column5 = new TableColumn<>("Nombre");
		column5.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<OrderVO, String> column6 = new TableColumn<>("Correo");
		column6.setCellValueFactory(new PropertyValueFactory<>("email"));
		
		TableColumn<OrderVO, File> column7 = new TableColumn<>("Orden File");
		column7.setCellValueFactory(new PropertyValueFactory<>("orderFile"));
		
		idTable.getColumns().addAll(column0,column1,column2, column3, column4, column5, column6, column7);
		idTable.setItems(data);				
		idTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		idTable.getSelectionModel().selectAll();			
		idTable.refresh();
	}
	
	//FX actionButton
	public void sendEmail(ActionEvent actionEvent) {
		List<OrderVO> orderVOs = idTable.getSelectionModel().getSelectedItems();
				
		String title       = "Ventana de Confirmacion";
		String textHeader  = "Enviando Emails";
		String contentText = String.format("¿Esta de acuerdo con enviar %d correos?", orderVOs.size());

		Optional<ButtonType> result = createConfirmationDialog(title,textHeader, contentText);
		
		if (result.get() == ButtonType.OK){			
			try {
				log.info(String.format("Total Emails %d", orderVOs.size()));
				emailService.sendEmail(orderVOs);				
			} catch (Exception ex) {
				log.error("Error sending email");				
				createExceptionDialog(ex);
			}
		} else {
		    log.info("Operation canceled");
		}		
	}
	
	
	
	private Optional<ButtonType> createConfirmationDialog(String title, String header, String contentText) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(contentText);
		return alert.showAndWait();
	}
	
	private void createExceptionDialog(Exception ex) {
		String title       = "Mensaje de Exepctiones";
		String textHeader  = "Ha ocurrido un error o excepcion en el sistema!!!";
		String contentText = ex.getMessage();
		createExceptionDialog(title, textHeader, contentText, ex);
	}
	
	private void createExceptionDialog(String title, String header, String contentText, Exception ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(contentText);		

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("El stacktrace de la excepcion fue:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
	

}
