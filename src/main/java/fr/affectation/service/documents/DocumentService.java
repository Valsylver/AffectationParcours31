package fr.affectation.service.documents;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
	
	public boolean saveResume(String path, String login, MultipartFile file);
	
	public boolean saveLetterIc(String path, String login, MultipartFile file);
	
	public boolean saveLetterJs(String path, String login, MultipartFile file);
	
	public boolean hasFilledResume(String path, String login);
	
	public boolean hasFilledLetterIc(String path, String login);
	
	public boolean hasFilledLetterJs(String path, String login);
	
	public boolean validatePdf(MultipartFile file);
	
	public boolean deleteResume(String path, String resume);
	
	public boolean deleteLetterIc(String path, String letterIc);
	
	public boolean deleteLetterJs(String path, String letterJs);
	
	public void deleteAll(String path);
}
