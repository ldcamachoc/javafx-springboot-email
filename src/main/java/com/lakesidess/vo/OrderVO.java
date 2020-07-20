package com.lakesidess.vo;

import java.io.File;
import java.time.LocalDateTime;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class OrderVO {
	
	@ExcelRow
	private int rowIndex;
	@ExcelCellName("FechaEntrega")
	private SimpleStringProperty dateDelivery;
	@ExcelCellName("TiempoInicio")	
	private SimpleStringProperty initialTime;
	@ExcelCellName("TiempoFin")
	private SimpleStringProperty endingTime;
	@ExcelCellName("ORDEN")
	private SimpleLongProperty numberOrder;
	@ExcelCellName("NOMBRE")
	private SimpleStringProperty name;
	@ExcelCellName("CORREO")
	private SimpleStringProperty email;
	@Setter
	private File orderFile;
	
	public int getRowIndex() {
		return rowIndex;
	}
	public String getDateDelivery() {
		return dateDelivery.get();
	}
	public String getInitialTime() {
		return initialTime.get();
	}
	public String getEndingTime() {
		return endingTime.get();
	}
	public Long getNumberOrder() {
		return numberOrder.get();
	}
	public String getName() {
		return name.get();
	}
	public String getEmail() {
		return email.get();
	}
	public File getOrderFile() {
		return orderFile;
	}
	
	
}
