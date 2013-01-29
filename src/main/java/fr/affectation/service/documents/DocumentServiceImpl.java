package fr.affectation.service.documents;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.affectation.web.controller.FileUploadException;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Override
	public void saveLetterIc(String path, String login, MultipartFile file) throws FileUploadException{
		String where = path + "/resources/lettres/parcours/lettre_parcours_" + login + ".pdf";
		saveFile(where, file);
	}

	@Override
	public void saveLetterJs(String path, String login, MultipartFile file) throws FileUploadException{
		String where = path + "/resources/lettres/filieres/lettre_filiere_" + login + ".pdf";
		saveFile(where, file);
	}

	@Override
	public void saveResume(String path, String login, MultipartFile file) throws FileUploadException{
		String where = path + "/resources/cv/cv_" + login + ".pdf";
		saveFile(where, file);
	}
	
	public void saveFile(String path, MultipartFile fileToUpload) throws FileUploadException{
		try{
			File file = new File(path);
			FileUtils.writeByteArrayToFile(file, fileToUpload.getBytes());
		}
		catch (IOException e){
			throw new FileUploadException(e.getMessage());
		}
	}

	@Override
	public boolean hasFilledLetterIc(String path, String login) {
		File letterIc = new File(path + "/resources/lettres/parcours/lettre_parcours_" + login + ".pdf");
		return letterIc.exists();
	}

	@Override
	public boolean hasFilledLetterJs(String path, String login) {
		File letterJs = new File(path + "/resources/lettres/filieres/lettre_filiere_" + login + ".pdf");
		return letterJs.exists();
	}

	@Override
	public boolean hasFilledResume(String path, String login) {
		File resume = new File(path + "/resources/cv/cv_" + login +".pdf");
		return resume.exists();
	}

}
