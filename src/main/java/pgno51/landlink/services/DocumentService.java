package pgno51.landlink.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pgno51.landlink.models.Document;
import pgno51.landlink.models.Property;
import pgno51.landlink.repositories.DocumentRepository;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Optional;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final SecretKey aesSecretKey;

    public DocumentService(DocumentRepository documentRepository, SecretKey aesSecretKey) {
        this.documentRepository = documentRepository;
        this.aesSecretKey = aesSecretKey;
    }

    public Document encryptAndSave(MultipartFile file, Property property) throws Exception {
        byte[] content = file.getBytes();
        Cipher cipher = Cipher.getInstance("AES"); // ECB for brevity; prefer GCM in production
        cipher.init(Cipher.ENCRYPT_MODE, aesSecretKey);
        byte[] encrypted = cipher.doFinal(content);

        Document doc = new Document();
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setEncryptedData(encrypted);
        doc.setProperty(property);
        return documentRepository.save(doc);
    }

    public byte[] decrypt(Document document) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesSecretKey);
        return cipher.doFinal(document.getEncryptedData());
    }

    public Optional<Document> findById(Long id) { return documentRepository.findById(id); }
}
