package com.lakesidess.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ResourceUtils;

import com.lakesidess.Constants;
import com.lakesidess.vo.OrderVO;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class EmailService {

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private Configuration config;

	@Autowired
	private Environment env;

	public void sendEmail(List<OrderVO> ordersVO) throws IOException {
		log.info("Starting Prepare Email");
		

		try {

			Map<String, Object> model = new HashMap<>();
			
			// set mediaType
			// Create a default MimeMessage object.
			// This mail has 3 part, the BODY, the embedded image and pdf attachment
			MimeMessage message = null;
			MimeMultipart multipart = null;

			BodyPart lakeSideBodyPart = getImageBodyPart("lakeSide.jpg");
			model.put("lakeSideImage", "lakeSide");

			BodyPart emailOrangeImage = getImageBodyPart("email-orange.jpg");
			model.put("emailOrangeImage", "email-orange");

			BodyPart emailYellowImage = getImageBodyPart("email-yellow.jpg");
			model.put("emailYellowImage", "email-yellow");			

			model.put("lakeSideURL", env.getProperty("freemarker.email.template.property.lakeSideURL"));
			model.put("chargePesos", env.getProperty("freemarker.email.template.property.chargePesos"));
			int count = 1;
			int totalEmails = ordersVO.size();

			for (OrderVO orderVO : ordersVO) {
				log.info(String.format("Sending %d of %d total emails", count, totalEmails));
				message = sender.createMimeMessage();
				multipart = new MimeMultipart("related");
				

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate dateDelivery = LocalDate.parse(orderVO.getDateDelivery(), formatter);
				String dayLibery = dateDelivery.getDayOfWeek().name();

				model.put("dayDelivery", dayLibery);
				model.put("startHour", orderVO.getInitialTime());
				model.put("endHour", orderVO.getEndingTime());

				String year = String.valueOf(dateDelivery.getYear());

				model.put("actualYear", year);
				model.put("lakeSideEmail", env.getProperty("freemarker.email.template.property.lakeSideEmail"));
				model.put("personToCall", env.getProperty("freemarker.email.template.property.personToCall"));
				model.put("cellPhoneNumber", env.getProperty("freemarker.email.template.property.cellPhoneNumber"));				

				// Set From: header field of the header.
				String emailFrom = env.getProperty("spring.mail.username");
				log.info(String.format("Sending from %s to %s ", emailFrom, orderVO.getEmail()));
				message.setFrom(new InternetAddress(emailFrom));

				// Set To: header field of the header.
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(orderVO.getEmail()));

				// Set Subject: header field
				message.setSubject("Order Number " + orderVO.getNumberOrder());

				// first part (the html)
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				Template template = config.getTemplate(env.getProperty("freemarker.email.template.name"));
				String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
				messageBodyPart.setContent(html, "text/html;charset=UTF-8");
		        
				// add it
		        multipart.addBodyPart(messageBodyPart);
		        
		        // add image to the multipart
		        multipart.addBodyPart(lakeSideBodyPart);
		        multipart.addBodyPart(emailOrangeImage);
		        multipart.addBodyPart(emailYellowImage);
				
		        // add attachment
		        addAttachment(multipart, orderVO.getOrderFile());				
				
				// put everything together
		        message.setContent(multipart);

				sender.send(message);
				count++;

			}

		} catch (MessagingException | IOException | TemplateException e) {
			log.error("Mail Sending failure : " + e.getMessage());
			throw new IOException(e);
		}

		log.info("Ending Prepare Email");
	}
	
	private BodyPart getImageBodyPart(String imageName) throws IOException, MessagingException {
		File file;
		try {
			file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "templates/" + imageName);
		} catch (FileNotFoundException e) {
			log.warn("File not found");
			String filePath = Constants.TMP_ABSOULTE_DIRECTORY + "/templates/" + imageName;
			log.warn("Taking file from: " + filePath);
			file = new File(filePath);
		}
		
		// create a new imagePart and add it to multipart so that the image is inline attached in the email
		BodyPart imagePart = new MimeBodyPart();
		DataSource fds = new FileDataSource(file.getAbsolutePath());
        String image = imageName.split("\\.")[0];
        
        imagePart.setDataHandler(new DataHandler(fds));
        imagePart.setHeader("Content-ID", "<"+image+">");
        imagePart.setFileName(imageName);
        imagePart.setDisposition(MimeBodyPart.INLINE);
        
		return imagePart;
	}
	
	private static void addAttachment(Multipart multipart, File file) throws MessagingException{
		String fileName = file.getAbsolutePath();
	    DataSource source = new FileDataSource(fileName);
	    BodyPart attachmentPart = new MimeBodyPart();        
	    attachmentPart.setDataHandler(new DataHandler(source));
	    attachmentPart.setFileName(file.getName());
	    attachmentPart.setDisposition(MimeBodyPart.ATTACHMENT);
	    multipart.addBodyPart(attachmentPart);
	}

}
