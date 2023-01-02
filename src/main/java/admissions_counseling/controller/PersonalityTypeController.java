package admissions_counseling.controller;

import admissions_counseling.model.Block;
import admissions_counseling.model.PersonalityType;
import admissions_counseling.model.Question;
import admissions_counseling.model.QuestionList;
import admissions_counseling.service.CareerService;
import admissions_counseling.service.PersonalityTypeService;
import admissions_counseling.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/your-personality-type")
public class PersonalityTypeController {

    private final PersonalityTypeService personalityTypeService;

    private final QuestionService questionService;

    private final CareerService careerService;

    @GetMapping("")
    public ModelAndView initView() {
        ModelAndView modelAndView = new ModelAndView("PersonalityForm");

        QuestionList questionList = new QuestionList();
        questionList.setQuestions(questionService.getAllQuestion());
        List<Question> questions = questionList.getQuestions();
        List<PersonalityType> typeList = personalityTypeService.getAllPersonalityType();

        int questionStat = 0;
        while (questionStat/6 < 6) {
            typeList.get(questionStat/6).setTotalAnswer(
                    typeList.get(questionStat/6).getTotalAnswer() + questionStat);
//                            Integer.parseInt(questions.get(questionStat).getAnswer()));
            questionStat ++;
        }

        PersonalityType personalityType = new PersonalityType();
        Integer maxQuestionScore = 0;
        for (PersonalityType type: typeList) {
            if (type.getTotalAnswer() > maxQuestionScore) {
                maxQuestionScore = type.getTotalAnswer();
                personalityType = type;
            }
        }
        modelAndView.addObject("type", personalityType);
        modelAndView.addObject("typeList", typeList);
        modelAndView.addObject("careerList", careerService.getCareerByTypeId(personalityType.getTypeId()));
        modelAndView.addObject("estimateScore", new Block());
        return modelAndView;
    }
}