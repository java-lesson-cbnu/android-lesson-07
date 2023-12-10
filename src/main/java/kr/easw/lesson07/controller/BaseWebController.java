package kr.easw.lesson07.controller;

import kr.easw.lesson07.service.AWSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

// 이 클래스를 컨트롤러로 선언합니다.
@Controller
@RequiredArgsConstructor
public class BaseWebController {
    private final AWSService awsController;

    @RequestMapping("/dashboard")
    public ModelAndView onUserDashboard() {
        return new ModelAndView("user_dashboard.html");
    }

    @RequestMapping("/login")
    public ModelAndView onLogin() {
        return new ModelAndView("login.html");
    }


    @RequestMapping("/register")
    public ModelAndView onRegister() {
        return new ModelAndView("register.html");
    }

    @RequestMapping("/admin")
    public ModelAndView onAdminDashboard() {
        return new ModelAndView("admin_dashboard.html");
    }

    @RequestMapping("/register_success")
    public ModelAndView onRegisterSuccess() {
        return new ModelAndView("register_check.html"); // 파일명 오타 수정
    }

    @RequestMapping("/management")
    public ModelAndView onManagementDashboard() {
        return new ModelAndView("management.html");
    }

    // 이 메서드의 엔드포인트를 /server-error로 설정합니다.
    @RequestMapping("/server-error")
    public ModelAndView onErrorTest() {
        // 에러 페이지로 리다이렉트합니다.
        return new ModelAndView("error.html");
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/another-error")
    public ModelAndView onAnotherError() {
        return new ModelAndView("error.html");
    }


    @RequestMapping("/upload")
    public ModelAndView onUpload() {
        if (awsController.isInitialized()) {
            return new ModelAndView("upload.html");
        }
        return new ModelAndView("request_aws_key.html");
    }

    @RequestMapping("/download")
    public ModelAndView onDownload() {
        if (awsController.isInitialized()) {
            return new ModelAndView("download.html");
        }
        return new ModelAndView("request_aws_key.html");
    }

    @RequestMapping("/")
    public ModelAndView serviceSelection() {
        return new ModelAndView("service_selection");
    }
}

