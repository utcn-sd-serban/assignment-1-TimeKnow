package ro.utcn.sd.mdantonio.StackUnderflow.controller;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.service.ManagementService;
import ro.utcn.sd.mdantonio.StackUnderflow.service.TagManagementService;
import ro.utcn.sd.mdantonio.StackUnderflow.service.UnderflowUserManagementService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UnderflowUserCommandHandler implements CommandHandler{
    private static final List<String> commandList = Arrays.asList(
            "login",
            "logout",
            "ban user",
            "unban user",
            "list all users"
    );
    private UnderflowUserManagementService managementService;

    public UnderflowUserCommandHandler(ManagementService managementService) {
        this.managementService = (UnderflowUserManagementService)managementService;
    }

    @Override
    public boolean isCommand(String command) {
        return commandList.contains(command);
    }

    @Override
    public boolean handleCommand(UnderflowUser currentUser, Scanner scanner, String command) throws Exception {
        switch (command) {
            case "list all users":
                handleListUsers();
                break;
            case  "unban user":
                handleRemoveBanFromUser(currentUser, scanner);
                break;
            case  "ban user":
                handleAddBanToUser(currentUser, scanner);
                break;
        }
        return false;
    }

    @Override
    public List<String> getCommands() {
        return commandList;
    }



    private void handleListUsers(){
        print("..................Listing users..........................");
        managementService.findAll().forEach(x->print(x.toString()));
        print(".........................................................");
    }

    private void handleRemoveBanFromUser(UnderflowUser currentUser, Scanner scanner){
        print("User ID: ");
        int userId = Integer.valueOf(scanner.nextLine());
        managementService.changeUserBannedStatus(currentUser.getUserid(), userId, false);
    }

    private void handleAddBanToUser(UnderflowUser currentUser, Scanner scanner){
        print("User ID: ");
        int userId = Integer.valueOf(scanner.nextLine());
        managementService.changeUserBannedStatus(currentUser.getUserid(), userId, true);
    }

    private void print(String value) {
        System.out.println(value);
    }
}
