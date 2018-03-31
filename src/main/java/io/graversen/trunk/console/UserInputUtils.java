package io.graversen.trunk.console;

import io.graversen.trunk.password.Password;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UserInputUtils
{
    private final char quitChar = 'q';

    public UserInputUtils()
    {

    }

    /**
     * Read the next line of user input and return it's String representation.
     */
    public String readString()
    {
        return System.console().readLine();
    }

    /**
     * Read password from user input, without echoing input back to user
     */
    public Password readPassword()
    {
        return new Password(System.console().readPassword());
    }

    public boolean askQuestion(String question)
    {
        question = question.endsWith("?") ? question : question + "?";
        question = question + yesNoQuitString();

        String response = "";
        while (!response.toLowerCase().startsWith("y") && !response.toLowerCase().startsWith("n") && !response.toLowerCase().startsWith(String.valueOf(quitChar)))
        {
            System.out.print(question);
            response = readString();
        }

        return response.toLowerCase().startsWith("y");
    }

    public int askQuestions(String message, List<String> questions)
    {
        String response = "";
        while ("".equals(response) || !StringUtils.isNumeric(response) || Integer.parseInt(response) < 1 || Integer.parseInt(response) > questions.size())
        {
            System.out.println(message + "?");
            for (String option : questions)
            {
                System.out.println((questions.indexOf(option) + 1) + ": " + option);
            }
            System.out.print("# of selection: ");
            response = readString();
        }

        return Integer.parseInt(response) - 1;
    }

    private String yesNoQuitString()
    {
        return " (y/n/" + quitChar + ")";
    }
}
