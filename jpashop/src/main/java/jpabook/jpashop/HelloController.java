package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){   // 어떤 data를 view에 넘길 때 model에 넣어서 넘김
        model.addAttribute("data", "hello!!!");
        return "hello";
    }
}
