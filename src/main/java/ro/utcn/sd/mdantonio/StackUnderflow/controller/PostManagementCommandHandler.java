package ro.utcn.sd.mdantonio.StackUnderflow.controller;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidCastException;
import ro.utcn.sd.mdantonio.StackUnderflow.service.ManagementService;
import ro.utcn.sd.mdantonio.StackUnderflow.service.PostManagementService;

import java.util.*;

import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.ANSWERID;
import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.QUESTIONID;

public class PostManagementCommandHandler implements CommandHandler{
    private static final List<String> commandList = Arrays.asList(
            "list all questions",
            "list all answers",
            "list all questions -filter tag",
            "list all questions -filter title",
            "post question",
            "post answer",
            "vote post",
            "delete post",
            "update post"
    );

    private static final List<String> helpDocumentation = Arrays.asList(
            "Displays the list of all Questions",
            "Displays the Answers for a specific Question",
            "Displays a list of Questions filtered by a Tag",
            "Displays a list of Questions filtered by the Question Title",
            "Post a Question to the website",
            "Responds to a Question",
            "Upvote or Downvote a Post",
            "Deletes a Post",
            "Update a Answer"
    );


    private ManagementService managementService;

    public PostManagementCommandHandler(ManagementService managementService) {
        this.managementService = managementService;
    }

    @Override
    public boolean isCommand(String command) {
        return commandList.contains(command);
    }

    @Override
    public boolean handleCommand(UnderflowUser currentUser, Scanner scanner, String command)
            throws Exception{
        PostManagementService postManagementService = (PostManagementService)managementService;
        switch (command) {
            case "list all questions":
                handleListQuestions(postManagementService, scanner);
                break;
            case "list all answers":
                handleListAnswers(postManagementService, scanner);
                break;
            case "list all questions -filter tag":
                handleListQuestionsByTag(postManagementService, scanner);
                break;
            case "list all questions -filter title":
                handleListQuestionsByTitle(postManagementService, scanner);
                break;
            case "post question":
                handlePostQuestion(currentUser, postManagementService, scanner);
                break;
            case "post answer":
                handlePostAnswer(currentUser, postManagementService, scanner);
                break;
            case "delete post":
                handleDeletePost(currentUser, postManagementService, scanner);
                break;
            case "update post":
                handleUpdatePost(currentUser, postManagementService, scanner);
                break;
            case "vote post":
                handleVotePost(currentUser, postManagementService, scanner);
                break;
        }
        return false;
    }

    @Override
    public List<String> getCommands() {
        List<String> response = new ArrayList<>();
        for(int i=0;i<commandList.size();i++)
            response.add(commandList.get(i) + " : " +helpDocumentation.get(i));
        return response;
    }

    private void handleListQuestions(PostManagementService postManagementService, Scanner scanner){
        print("..................Listing questions......................");
        postManagementService.listQuestions().forEach(x->print(x.toString()));
        print(".........................................................");
    }

    private void handleListAnswers(PostManagementService postManagementService, Scanner scanner){
        print("Question id:");
        int questionId = Integer.valueOf(scanner.nextLine());
        print("..................Listing answers........................");
        postManagementService.listQuestionResponses(questionId).forEach(x->print(x.toString()));
        print(".........................................................");
    }

    private void handleListQuestionsByTag(PostManagementService postManagementService, Scanner scanner){
        print("Tag ID: ");
        int tagId = Integer.valueOf(scanner.nextLine());
        print("..................Filtered questions.....................");
        postManagementService.listPostByTag(tagId).forEach(x->print(x.toString()));
        print(".........................................................");
    }

    private void handleListQuestionsByTitle(PostManagementService postManagementService, Scanner scanner){
        print("Title: ");
        String title = scanner.nextLine();
        print("..................Filtered questions.....................");
        postManagementService.listPostByTitle(title).forEach(x->print(x.toString()));
        print(".........................................................");
    }

    private void handlePostQuestion(UnderflowUser currentUser, PostManagementService postManagementService, Scanner scanner){
        print("Question Title: ");
        String title = scanner.nextLine();
        print("Question Body: ");
        String body = scanner.nextLine();
        postManagementService.addPost(QUESTIONID, currentUser.getUserid(), null, title, body, Calendar.getInstance().getTime());
    }

    private void handlePostAnswer(UnderflowUser currentUser, PostManagementService postManagementService, Scanner scanner){
        print("Question ID: ");
        int questionId = Integer.valueOf(scanner.nextLine());
        print("Answer Body: ");
        String body = scanner.nextLine();
        postManagementService.addPost(ANSWERID, currentUser.getUserid(), questionId, "", body, Calendar.getInstance().getTime());
    }

    private void handleDeletePost(UnderflowUser currentUser, PostManagementService postManagementService, Scanner scanner){
        print("Post ID: ");
        int postid = Integer.valueOf(scanner.nextLine());
        postManagementService.removePost(currentUser.getUserid(), postid);
    }

    private void handleUpdatePost(UnderflowUser currentUser, PostManagementService postManagementService, Scanner scanner){
        print("Post ID: ");
        int postid = Integer.valueOf(scanner.nextLine());
        print("New Title:");
        String title = scanner.nextLine();
        print("New Body:");
        String body = scanner.nextLine();
        postManagementService.updatePost(currentUser.getUserid(), postid, title, body);
    }

    private void handleVotePost(UnderflowUser currentUser, PostManagementService postManagementService, Scanner scanner){
        print("Question ID: ");
        int questionId = Integer.valueOf(scanner.nextLine());
        print("Will you upvote this post(y/n)?: ");
        String response = scanner.nextLine();
        boolean isUpvote = response.equals("y");
        postManagementService.votePost(currentUser.getUserid(), questionId, isUpvote);
    }

    private void print(String value) {
        System.out.println(value);
    }
}
