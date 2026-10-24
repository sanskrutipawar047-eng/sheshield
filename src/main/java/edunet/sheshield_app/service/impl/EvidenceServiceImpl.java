package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.dto.EvidenceDto;
import edunet.sheshield_app.entity.Evidence;
import edunet.sheshield_app.entity.User;
import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.mapper.EvidenceMapper;
import edunet.sheshield_app.repository.EvidenceRepository;
import edunet.sheshield_app.repository.UserRepository;
import edunet.sheshield_app.service.EvidenceService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Evidence vault.
 * 1. SHA-256 hash of the ORIGINAL file is saved in the database.
 * 2. The file is encrypted with AES-GCM and saved in the uploads folder.
 * 3. Verify = decrypt the file again, hash it, compare with the saved hash.
 */
@Service
public class EvidenceServiceImpl implements EvidenceService {

    private static final int IV_LENGTH = 12;   // random value added before the encrypted bytes

    private final EvidenceRepository evidenceRepository;
    private final UserRepository userRepository;
    private final String uploadDir;
    private final SecretKeySpec secretKey;

    public EvidenceServiceImpl(EvidenceRepository evidenceRepository,
                               UserRepository userRepository,
                               @Value("${sheshield.upload-dir}") String uploadDir,
                               @Value("${sheshield.aes-key}") String aesKey) {
        this.evidenceRepository = evidenceRepository;
        this.userRepository = userRepository;
        this.uploadDir = uploadDir;
        this.secretKey = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), "AES");
    }

    @Override
    public List<EvidenceDto> getEvidence(Long userId) {
        return evidenceRepository.findByUser_UserIdOrderByCapturedAtDesc(userId)
                .stream()
                .map(EvidenceMapper::toDto)
                .toList();
    }

    /** Hashes the file, encrypts it, saves it to disk and stores the details in the database. */
    @Override
    public EvidenceDto upload(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new SheShieldException("Please choose a file.");
        }
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new SheShieldException("User not found."));
        try {
            byte[] original = file.getBytes();
            String hash = sha256(original);
            byte[] encrypted = encrypt(original);

            Path folder = Paths.get(uploadDir);
            Files.createDirectories(folder);
            Path target = folder.resolve(UUID.randomUUID() + ".enc");
            Files.write(target, encrypted);

            Evidence evidence = new Evidence();
            evidence.setUser(owner);
            evidence.setOriginalFileName(file.getOriginalFilename());
            evidence.setFileType(file.getContentType());
            evidence.setStoragePath(target.toString());
            evidence.setSha256Hash(hash);
            evidence.setCapturedAt(LocalDateTime.now());
            // TODO Phase 2: upload the encrypted file to cloud storage as well
            return EvidenceMapper.toDto(evidenceRepository.save(evidence));
        } catch (IOException | GeneralSecurityException e) {
            throw new SheShieldException("Could not save the evidence file.");
        }
    }

    /** True = file is unchanged. False = file is missing, damaged or tampered with. */
    @Override
    public boolean verifyIntegrity(Long userId, Long evidenceId) {
        Evidence evidence = evidenceRepository.findById(evidenceId)
                .orElseThrow(() -> new SheShieldException("Evidence not found."));
        if (!evidence.getUser().getUserId().equals(userId)) {
            throw new SheShieldException("This is not your evidence.");
        }
        try {
            byte[] encrypted = Files.readAllBytes(Paths.get(evidence.getStoragePath()));
            byte[] original = decrypt(encrypted);       // fails if the file was changed
            return sha256(original).equals(evidence.getSha256Hash());
        } catch (Exception e) {
            return false;
        }
    }

    // ---------- small helper methods ----------

    /** SHA-256 hash as a hex text. */
    private String sha256(byte[] data) throws GeneralSecurityException {
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(data);
        return HexFormat.of().formatHex(digest);
    }

    /** AES-GCM encrypt. Result = random IV + encrypted bytes. */
    private byte[] encrypt(byte[] data) throws GeneralSecurityException {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        byte[] cipherText = cipher.doFinal(data);

        byte[] result = new byte[IV_LENGTH + cipherText.length];
        System.arraycopy(iv, 0, result, 0, IV_LENGTH);
        System.arraycopy(cipherText, 0, result, IV_LENGTH, cipherText.length);
        return result;
    }

    /** AES-GCM decrypt. GCM also checks that the bytes were not modified. */
    private byte[] decrypt(byte[] data) throws GeneralSecurityException {
        byte[] iv = Arrays.copyOfRange(data, 0, IV_LENGTH);
        byte[] cipherText = Arrays.copyOfRange(data, IV_LENGTH, data.length);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        return cipher.doFinal(cipherText);
    }
    @Value("${sheshield.upload-dir}")
    private String uploadDir1;
    
}