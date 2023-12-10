package kr.easw.lesson07.controller;

import kr.easw.lesson07.model.dto.TextDataDto;
import kr.easw.lesson07.service.TextDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

// @RestController 어노테이션을 사용하여 이 클래스가 REST 컨트롤러임을 선언합니다.
@RestController
// @RequestMapping 어노테이션을 사용하여 이 클래스의 기반 엔드포인트를 /api/v1/data/admin으로 설정합니다.
@RequestMapping("/api/v1/data/admin")
// final로 지정된 모든 필드를 파라미터로 가지는 생성자를 생성합니다.
@RequiredArgsConstructor
public class AdminDataRestEndpoint {
    private final TextDataService textDataService;

    // 이 메서드의 엔드포인트를 /api/v1/data/add로 설정합니다. POST만 허용됩니다.
    @PostMapping("/add")
    public ModelAndView addText(@RequestParam("text") String text) {
        textDataService.addText(new TextDataDto(0L, text));
        return new ModelAndView("redirect:/admin?success=true");
    }

}