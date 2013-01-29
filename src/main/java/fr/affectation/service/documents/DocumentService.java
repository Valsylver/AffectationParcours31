package fr.affectation.service.documents;

import org.springframework.web.multipart.MultipartFile;

import fr.affectation.web.controller.FileUploadException;

public interface DocumentService {
	
	public void saveResume(String path, String login, MultipartFile file) throws FileUploadException;
	
	public void saveLetterIc(String path, String login, MultipartFile file) throws FileUploadException;
	
	public void saveLetterJs(String path, String login, MultipartFile file) throws FileUploadException;
	
	public boolean hasFilledResume(String path, String login);
	
	public boolean hasFilledLetterIc(String path, String login);
	
	public boolean hasFilledLetterJs(String path, String login);
}
