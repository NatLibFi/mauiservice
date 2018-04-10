package fi.nationallibrary.mauiservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyzeController {

	@RequestMapping("/maui/foo/analyze")
	public String analyze() {
		return "Hello world";
	}
}
