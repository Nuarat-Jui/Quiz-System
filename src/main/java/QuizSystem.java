
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class QuizSystem {
    public static void main(String[] args) throws IOException, ParseException {
         String userfilepath="./src/main/resources/user.json";
         String quizfilepath="./src/main/resources/quiz.json";

         Scanner input=new Scanner(System.in);
         String name,pass;
         System.out.println("Enter your username: ");
         name=input.nextLine();
         System.out.println("Enter password: ");
         pass=input.nextLine();

         String role=login(userfilepath,name,pass);
         if(role.equals("admin")){
             userAdmin(quizfilepath,input);
         }
         else if(role.equals("student")){
             System.out.println("Welcome " + name + " to the quiz! We will throw you 10 questions. Each MCQ mark is 1 and no negative marking. Are you ready? Press 's' to start.");
             String choice = input.nextLine();
             if (choice.equalsIgnoreCase("s")) {
                 userStudent(quizfilepath,name,input);
             }
         }
         else{
             System.out.println("Invalid");
         }


    }
    public static String login(String userfilepath, String name,String pass) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(userfilepath));

        String userrole = null;
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            String username = (String) jsonObject.get("username");
            String password = (String) jsonObject.get("password");
            if (username.equals(name) && password.equals(pass)) {
                userrole = (String) jsonObject.get("role");
                break;
            }
        }

        if (userrole != null) {
            return userrole;
        }

        else {
            return "none";
        }


    }
    public static void userAdmin(String quizfilepath,Scanner input) throws IOException, ParseException {
        System.out.println("Welcome admin! Please create new questions in the question bank.");
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(quizfilepath));
        while (true) {
            System.out.println("Input your question");
            String question = input.nextLine();
            System.out.println("Input option 1:");
            String option1 = input.nextLine();
            System.out.println("Input option 2:");
            String option2 = input.nextLine();
            System.out.println("Input option 3:");
            String option3 = input.nextLine();
            System.out.println("Input option 4:");
            String option4 = input.nextLine();
            System.out.println("What is the answer key?");
            int answerKey = input.nextInt();
            input.nextLine(); // Consume the newline

            JSONObject questionObj = new JSONObject();
            questionObj.put("question", question);
            questionObj.put("option 1", option1);
            questionObj.put("option 2", option2);
            questionObj.put("option 3", option3);
            questionObj.put("option 4", option4);
            questionObj.put("answerkey", answerKey);

            jsonArray.add(questionObj);

            System.out.println("Saved successfully! Do you want to add more questions? (press s for start and q for quit)");
            String choice = input.nextLine();
            if (choice.equalsIgnoreCase("q")) {
                break;
            }
        }


        FileWriter writer = new FileWriter(quizfilepath);
        writer.write(jsonArray.toJSONString());
        writer.flush();
        writer.close();


    }
    public static void userStudent(String quizfilepath,String name,Scanner input) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(quizfilepath));
        Random random = new Random();

        int score = 0;
        for (int i=0;i<10;i++) {
            int index = random.nextInt(jsonArray.size());
            JSONObject question = (JSONObject) jsonArray.get(index);

            System.out.println("[Question "+(i+1)+"]"+question.get("question"));
            System.out.println("1. "+question.get("option 1"));
            System.out.println("2. "+question.get("option 2"));
            System.out.println("3. "+question.get("option 3"));
            System.out.println("4. "+question.get("option 4"));

            System.out.print("Ans: ");
            int answer = input.nextInt();
            input.nextLine();

            int correctAns = Integer.parseInt(question.get("answerkey").toString());
            if (answer == correctAns) {
                score++;
            }
        }

        if (score >= 8) {
            System.out.println("Excellent! You have got "+score+" out of 10");
        } else if (score >= 5) {
            System.out.println("Good. You have got "+score+" out of 10");
        } else if (score >= 2) {
            System.out.println("Very poor! You have got "+score+" out of 10");
        } else {
            System.out.println("Very sorry you are failed. You have got "+score+" out of 10");
        }

        System.out.println("Would you like to start again? Press 's' for start or 'q' for quit.");
        String choice = input.nextLine();
        if (choice.equalsIgnoreCase("s")) {
            userStudent(quizfilepath,name,input);
        }
    }
}