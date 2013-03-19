package fr.affectation.service.documents;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Override
	public boolean saveLetterIc(String path, String login, MultipartFile file) {
		String where = path + "WEB-INF/resources/lettres/parcours/lettre_parcours_" + login + ".pdf";
		return saveFile(where, file);
	}

	@Override
	public boolean saveLetterJs(String path, String login, MultipartFile file) {
		String where = path + "WEB-INF/resources/lettres/filieres/lettre_filiere_" + login + ".pdf";
		return saveFile(where, file);
	}

	@Override
	public boolean saveResume(String path, String login, MultipartFile file) {
		String where = path + "WEB-INF/resources/cv/cv_" + login + ".pdf";
		return saveFile(where, file);
	}

	public boolean saveFile(String path, MultipartFile fileToUpload) {
		try {
			File file = new File(path);
			FileUtils.writeByteArrayToFile(file, fileToUpload.getBytes());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean hasFilledLetterIc(String path, String login) {
		File letterIc = new File(path + "WEB-INF/resources/lettres/parcours/lettre_parcours_" + login + ".pdf");
		return letterIc.exists();
	}

	@Override
	public boolean hasFilledLetterJs(String path, String login) {
		File letterJs = new File(path + "WEB-INF/resources/lettres/filieres/lettre_filiere_" + login + ".pdf");
		return letterJs.exists();
	}

	@Override
	public boolean hasFilledResume(String path, String login) {
		File resume = new File(path + "WEB-INF/resources/cv/cv_" + login + ".pdf");
		return resume.exists();
	}

	@Override
	public boolean validatePdf(MultipartFile file) {
		try {
			byte[] b = file.getBytes();
			if (!(b.length>3)){
				return false;
			}
			else{
				byte[] bytes = {b[0], b[1], b[2], b[3]};
				return (new String(bytes, "utf-8")).equals("%PDF");
			}
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean deleteResume(String path, String login) {
		File resume = new File(path + "WEB-INF/resources/cv/cv_" + login + ".pdf");
		return resume.delete();
	}

	@Override
	public boolean deleteLetterIc(String path, String login) {
		File letterIc = new File(path + "WEB-INF/resources/lettres/parcours/lettre_parcours_" + login + ".pdf");
		return letterIc.delete();
	}

	@Override
	public boolean deleteLetterJs(String path, String login) {
		File letterJs = new File(path + "WEB-INF/resources/lettres/filieres/lettre_filiere_" + login + ".pdf");
		return letterJs.delete();
	}

	@Override
	public void deleteAll(String path) {
		File letterJs = new File(path + "WEB-INF/resources/lettres/filieres");
		for (File file : letterJs.listFiles()){
			file.delete();
		}
		File letterIc = new File(path + "WEB-INF/resources/lettres/parcours");
		for (File file : letterIc.listFiles()){
			file.delete();
		}
		File resume = new File(path + "WEB-INF/resources/cv");
		for (File file : resume.listFiles()){
			file.delete();
		}
	}

}
