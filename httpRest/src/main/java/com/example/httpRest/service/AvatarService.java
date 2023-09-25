package com.example.httpRest.service;

import com.example.httpRest.dto.AvatarDto;
import com.example.httpRest.model.Avatar;
import com.example.httpRest.model.Student;
import com.example.httpRest.repository.AvatarRepository;
import com.example.httpRest.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    @Value("@{path.to.avatars.folder}")
    private Path pathToAvatars;


    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Avatar getById(Long id) {
        logger.info("invoked method getById");
        return avatarRepository.findById(id).orElseThrow();
    }

    public Long save(Long studentId, MultipartFile multipartFile) throws IOException {
        logger.info("invoked method save");

        String absolutePath = saveToDisk(studentId, multipartFile);

        Avatar avatar = saveToDB(studentId, multipartFile, absolutePath);


        return avatar.getId();
    }

    private Avatar saveToDB(Long studentId, MultipartFile multipartFile, String absolutePath) throws IOException {
        Student studentReference = studentRepository.getReferenceById(studentId);
        Avatar avatar = avatarRepository.findByStudent(studentReference).orElse(new Avatar());

        avatar.setStudent(studentReference);
        avatar.setFilePath(absolutePath);
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setFileSize(multipartFile.getSize());
        avatar.setData(multipartFile.getBytes());
        avatarRepository.save(avatar);
        return avatar;
    }

    public List<AvatarDto> getPage(int pageNum) {
        logger.info("invoked method getPage");
        PageRequest pageRequest = PageRequest.of(pageNum, 3);
        List<Avatar> avatars = avatarRepository.findAll(pageRequest).getContent();
        return avatars.stream()
                .map(AvatarDto::fromEntity)
                .collect(Collectors.toList());
    }

    private String saveToDisk(Long studentId, MultipartFile multipartFile) throws IOException {
        Files.createDirectories(pathToAvatars);
        String originalFilename = multipartFile.getOriginalFilename();
        int dotIndex = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(dotIndex);
        String fileName = studentId + extension;
        String absolutePath = pathToAvatars.toAbsolutePath().toFile() + "/" + fileName;
        FileOutputStream fos = new FileOutputStream(absolutePath);
        multipartFile.getInputStream().transferTo(fos);
        fos.close();
        return absolutePath;
    }

}
