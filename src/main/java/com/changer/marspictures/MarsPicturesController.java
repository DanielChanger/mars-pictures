package com.changer.marspictures;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/mars/pictures")
@RequiredArgsConstructor
public class MarsPicturesController {

    private final MarsPicturesService marsPicturesService;

    @GetMapping(value = "/largest", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> findLargestPicture(@RequestParam("sol") int sol,
                                                     @RequestParam(value = "camera", required = false) String camera) {
        Optional<byte[]> largestPicture = marsPicturesService.findLargestPicture(sol, camera);
        if (largestPicture.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(largestPicture.get());
    }
}
