package kr.easw.lesson07.controller;

import kr.easw.lesson07.model.dto.AWSKeyDto;
import kr.easw.lesson07.service.AWSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rest/aws")
public class AWSConroller {
    private final AWSService awsController;

    @PostMapping("/auth")
    private ModelAndView onAuth(AWSKeyDto awsKey) {
        try {
            awsController.initAWSAPI(awsKey);
            return new ModelAndView("redirect:/upload"); // 업로드 페이지로 리디렉션
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ModelAndView("redirect:/server-error?errorStatus=" + ex.getMessage());
        }
    }

    @GetMapping("/list")
    private List<String> onFileList() {
        return awsController.getFileList();
    }

    @PostMapping("/upload")
    private ModelAndView onUpload(@RequestParam MultipartFile file) {
        try {
            awsController.upload(file);
            return new ModelAndView("redirect:/upload?success=true"); // 업로드 페이지로 리디렉션
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ModelAndView("redirect:/server-error?errorStatus=" + ex.getMessage());
        }
    }


    //    @GetMapping("/download")
//    private ModelAndView onDownload(@RequestParam("filename") String fileName) {
//        try {
//            log.info("filename={}", fileName);
//            awsController.downloadFile(fileName);
//           throw new IllegalStateException("기능이 구현되지 않았습니다.");
//        } catch (Throwable e) {
//            return new ModelAndView("redirect:/server-error?errorStatus=" + e.getMessage());
//        }
//    }
    @GetMapping("/download")
    public ResponseEntity<?> onDownload(@RequestParam("filename") String fileName) {
        try {
            File file = awsController.downloadFile(fileName);

            if (file == null || !file.exists()) {
                throw new IllegalArgumentException("다운로드할 파일이 존재하지 않습니다.");
            }

            // 파일을 byte 배열로 읽어옵니다.
            byte[] fileBytes = Files.readAllBytes(file.toPath());

            // 파일의 MIME 타입을 설정합니다.
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName); // 파일 다운로드 헤더 설정

            // ResponseEntity를 사용하여 다운로드할 파일과 헤더 정보를 반환합니다.
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("오류");
        }
    }
}