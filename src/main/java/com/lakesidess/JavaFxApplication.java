package com.lakesidess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import com.github.ulisesbocchio.jar.resources.JarResourceLoader;
import com.lakesidess.controller.FileExcelController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import net.rgielen.fxweaver.core.FxWeaver;

@Log4j2
public class JavaFxApplication extends Application {
    private ConfigurableApplicationContext applicationContext;   

    @Override
    public void init() {    	
    	List<String> parameters = getParameters().getRaw();
        String[] args = parameters.toArray(new String[0]);
        String nameProperties = parameters.size() == 0 ? Constants.APPLICATION_PROPERTIES_NAME : "";
        
        SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder();
        applicationBuilder.sources(SpringBootJavaFXApplication.class);
        //Export images and template mail out the JAR File
        applicationBuilder.resourceLoader(new JarResourceLoader(Constants.TMP_ABSOULTE_DIRECTORY)); 
        
        //Load external Properties
        Properties properties = loadProperties(nameProperties);        
        applicationBuilder.properties(properties);        
        
        this.applicationContext = applicationBuilder.run(args);
      
    }
    
    public Properties loadProperties(String nameProperties) {    	
    	
    	File actualDirectory = new ApplicationHome().getDir();
    	String directoryString = actualDirectory.getAbsolutePath();
    	String propertiesDirectory = "";
    	
    	if(StringUtils.isEmpty(nameProperties)) {
    		propertiesDirectory = lookingPropertiesPathOnEnvironmentsVariables();
    	}else {
    		propertiesDirectory = String.format("%s/%s", directoryString, nameProperties);    		
    	}
    	
    	Properties properties = new Properties();
		try (InputStream inStream = new FileInputStream(propertiesDirectory)){
	    	properties.load(inStream);
	    	log.info("Loading the properties: "+propertiesDirectory);
	    	
		} catch (IOException e) {
			log.error("Error loading the properties: "+e.getMessage());
			log.info("Loading the properties: "+propertiesDirectory);
		}
    	
    	return properties;
    }
    
    private String lookingPropertiesPathOnEnvironmentsVariables() {
    	Optional<String> result = getParameters().getRaw()
    			.stream()
    			.filter(x -> x.contains("spring.config.location"))
    			.findFirst();
    	if(result.isPresent()) {
    		String[] properties = result.get().substring(2).split("=");
    		String property = properties[1].substring(8);    		
    		return property;
    	}else {
    		return "";
    	}    	
    }

    @Override
    public void start(Stage stage) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(FileExcelController.class);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

}
