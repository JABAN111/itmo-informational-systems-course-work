package lab.`is`.bank

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/hi")
    fun helloWorld(): String {
        return "Hello World!"
    }
}