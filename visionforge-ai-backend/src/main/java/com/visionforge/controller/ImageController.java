
package com.visionforge.controller;

import com.visionforge.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService){
        this.imageService=imageService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody Map<String,String> request){

        String prompt=request.get("prompt");

        String image=imageService.generateImage(prompt);

        return ResponseEntity.ok(
                Map.of(
                        "success",true,
                        "image","data:image/jpeg;base64,"+image
                )
        );
    }
}