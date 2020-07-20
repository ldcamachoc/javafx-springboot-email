package com.lakesidess.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.lakesidess.vo.OrderVO;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import com.poiji.option.PoijiOptions.PoijiOptionsBuilder;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExcelUtil {
	

	public List<OrderVO> mappingToOrderVO(String nameXlsx) throws  IOException {
		log.info("Mapping Excel to OrderVO");
		InputStream stream = new FileInputStream(new File(nameXlsx));
		
		PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
				.withCasting(new ExcelCasting())
				.build();
		
		List<OrderVO> employees = Poiji.fromExcel(stream, PoijiExcelType.XLS,OrderVO.class, options);
		log.info("End Mapping Excel to OrderVO");
		return employees;
	}
	
	

}
