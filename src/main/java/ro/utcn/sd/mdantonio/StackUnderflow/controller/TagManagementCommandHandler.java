package ro.utcn.sd.mdantonio.StackUnderflow.controller;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.service.ManagementService;
import ro.utcn.sd.mdantonio.StackUnderflow.service.PostManagementService;
import ro.utcn.sd.mdantonio.StackUnderflow.service.TagManagementService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TagManagementCommandHandler implements CommandHandler{
    private static final List<String> commandList = Arrays.asList(
            "list all tags",
            "list all tags -filter question",
            "create tag",
            "attach tag"
    );

    private static final List<String> helpDocumentation = Arrays.asList(
            "Displays a list of all the currently available Tags",
            "Displays a list of Tags that were attached to a specific Question",
            "Creates a Tag",
            "Attaches a tag to a Question"
    );

    private ManagementService managementService;

    public TagManagementCommandHandler(ManagementService managementService) {
        this.managementService = managementService;
    }

    @Override
    public boolean isCommand(String command) {
        return commandList.contains(command);
    }

    @Override
    public boolean handleCommand(UnderflowUser currentUser, Scanner scanner, String command) throws Exception {
        TagManagementService tagManagementService = (TagManagementService)managementService;
        switch (command) {
            case "list all tags":
                handleListTags(tagManagementService);
                break;
            case  "list all tags -filter question":
                handleListQuestionTags(tagManagementService, scanner);
                break;
            case  "create tag":
                handleCreateTag(tagManagementService, scanner);
                break;
            case  "attach tag":
                handleAttachTag(currentUser, tagManagementService, scanner);
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

    private void handleListTags(TagManagementService tagManagementService){
        print("..................Listing tags...........................");
        tagManagementService.listAllTags().forEach(x->print(x.toString()));
        print(".........................................................");
    }

    private void handleListQuestionTags(TagManagementService tagManagementService, Scanner scanner){
        print("Question id:");
        int questionId = Integer.valueOf(scanner.nextLine());
        print("..................Listing Tags...........................");
        tagManagementService.findTagsForPost(questionId).forEach(x->print(x.toString()));
        print(".........................................................");
    }

    private void handleCreateTag(TagManagementService tagManagementService, Scanner scanner){
        print("Tag Name:");
        String title = scanner.nextLine();
        tagManagementService.createTag(title);
    }

    private void handleAttachTag(UnderflowUser currentUser, TagManagementService tagManagementService, Scanner scanner){
        print("Question ID: ");
        int questionId = Integer.valueOf(scanner.nextLine());
        print("Tag ID: ");
        int tagId = Integer.valueOf(scanner.nextLine());
        tagManagementService.attachTagToPost(currentUser.getUserid(), tagId, questionId);
    }

    private void print(String value) {
        System.out.println(value);
    }
}
