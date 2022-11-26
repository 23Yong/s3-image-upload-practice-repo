package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class S3Controller {

    private final S3Uploader s3Uploader;
    private final S3Downloader s3Downloader;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ResponseBody
    @PostMapping("/upload")
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }

    @GetMapping("/download/{dirName}/{fileName}")
    public ModelAndView download(@PathVariable(name = "dirName") String dirName,
                                 @PathVariable(name = "fileName") String fileName) {
        ModelAndView mv = new ModelAndView("download");

        String imgPath = s3Downloader.downloadFile(dirName, fileName);
        mv.addObject("imgPath", imgPath);

        return mv;
    }
}
