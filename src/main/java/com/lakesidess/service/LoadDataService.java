package com.lakesidess.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lakesidess.Constants;
import com.lakesidess.util.ExcelUtil;
import com.lakesidess.vo.OrderVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LoadDataService {	

	public List<OrderVO> prepareData(String path, String nameXlsx) throws IOException {
		log.info("Starting Prepare Data");
		
		Map<String, Object> mappingData;
		Map<Long, File> mappingFiles;
		try {
			mappingData = readFiles(path);
			mappingFiles = (Map<Long, File>) mappingData.get(Constants.MAP_DATA_CORRECT);

		} catch (IOException e) {
			log.error("Error reading the path or recovering the pdf files");
			throw new IOException(e);

		}

		nameXlsx = path +"\\" + nameXlsx;

		try {
			ExcelUtil excelUtil = new ExcelUtil();
			List<OrderVO> ordersVO = excelUtil.mappingToOrderVO(nameXlsx);
			ordersVO.forEach(orderVO -> {
				Long numberOrder = orderVO.getNumberOrder();
				File file = mappingFiles.get(numberOrder);
				orderVO.setOrderFile(file);
			});

			log.info("Ending Prepare Data");
			return ordersVO;

		} catch (IOException e) {
			log.error("Error reading the xlsxf file");
			throw new IOException(e);
		}

	}
	
	public Map<String, Object> readFiles(String path) throws IOException {
		log.info("Starting to read Orders on PDF");

		Map<String, Object> mappingData = new HashMap<>();

		final Map<Long, File> mappingCorrectFile = new HashMap<>();
		final List<File> mappingNotCorrectFile = new ArrayList<>();

		Files.list(Paths.get(path)).filter(file -> file.toString().endsWith(Constants.PDF_EXTENSIONS))
				.map(file -> file.toFile()).collect(Collectors.toList()).forEach(file -> {
					String nameFile = file.getName();

					Pattern patternOrder = Pattern.compile("#[\\s]*(?<order>\\d+)");
					Matcher matcherOrder = patternOrder.matcher(nameFile);

					if (matcherOrder.find()) {
						Long order = Long.valueOf(matcherOrder.group("order"));
						mappingCorrectFile.put(order, file);
					} else {
						mappingNotCorrectFile.add(file);
					}
				});

//		 mappingCorrectFile.entrySet().stream().forEach(System.out::println);
//		 mappingNotCorrectFile.forEach(System.out::println);
		mappingData.put(Constants.MAP_DATA_CORRECT, mappingCorrectFile);
		mappingData.put(Constants.LIST_DATA_NOT_CORRECT, mappingNotCorrectFile);

		log.info("Total the orders detected: " + mappingCorrectFile.size());
		log.info("Total the orders no detected: " + mappingNotCorrectFile.size());
		log.info("");
		log.info("Ending to read Orders on PDF");

		return mappingData;
	}
}
