package fr.affectation.web.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FilesController {

	@RequestMapping(value = "/files/{file_name}", method = RequestMethod.GET)
	public void getFile(@PathVariable("file_name") String fileName,
			HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/";
		if (fileName.substring(0, 2).equals("cv")){
			System.out.println("cv");
			realPath += "cv/" + fileName;
		}
		else{
			if (fileName.substring(0, 6).equals("lettre")){
				realPath += "lettres/";
				if (fileName.substring(7, 8).equals("p")){
					realPath += "parcours/" + fileName;
				}
				else{
					System.out.println("lettre filieres");
					realPath += "filieres/" + fileName;
				}
			}
			else{
				realPath += "pdf/resultats_affectation";
			}
		}
		realPath += ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}

	}
}
