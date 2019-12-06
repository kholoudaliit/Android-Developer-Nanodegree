package com.kholoud.javalib;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class JokeGenrator {


    private static List<String> Jokes = asList("Doctor: \"I'm sorry but you suffer from a terminal illness and have only 10 to live.\"\n" +
                    "\n" +
                    "Patient: \"What do you mean, 10? 10 what? Months? Weeks?!\"\n" +
                    "\n" +
                    "Doctor: \"Nine.\" ", "I got another letter from this lawyer today. It said “Final Notice”. Good that he will not bother me anymore.\n",
            "I dreamed I was forced to eat a giant marshmallow. When I woke up, my pillow was gone.",
            "One of the most wonderful things in life is to wake up and enjoy a cuddle with somebody; unless you are in prison.",
            "One company owner asks another: “Tell me, Bill, how come your employees are always on time in the mornings?”\n" + "\n" + "Bill replies: “Easy. 30 employees and 20 parking spaces.”");


    public static String getJokeRandom() {
        Random random = new Random();
        return Jokes.get(random.nextInt(Jokes.size()));
    }


}

