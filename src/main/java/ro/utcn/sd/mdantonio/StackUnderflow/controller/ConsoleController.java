package ro.utcn.sd.mdantonio.StackUnderflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.*;
import ro.utcn.sd.mdantonio.StackUnderflow.service.PostManagementService;
import ro.utcn.sd.mdantonio.StackUnderflow.service.TagManagementService;
import ro.utcn.sd.mdantonio.StackUnderflow.service.UnderflowUserManagementService;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class ConsoleController implements CommandLineRunner {
    private final Scanner scanner = new Scanner(System.in);
    private UnderflowUser currentUser = null;
    private final PostManagementService postManagementService;
    private final TagManagementService tagManagementService;
    private final UnderflowUserManagementService underflowUserManagementService;

    @Transient
    private final List<CommandHandler> commandHandlers = new ArrayList<>();


    @Override
    public void run(String... args) throws Exception {

        commandHandlers.add(new PostManagementCommandHandler(postManagementService));
        commandHandlers.add(new TagManagementCommandHandler(tagManagementService));
        commandHandlers.add(new UnderflowUserCommandHandler(underflowUserManagementService));

        print("Welcome to StackUnderflow. For more information about the commands please type 'help'.\n" +
                " Use 'exit' to close the program.");
        boolean done = false;
        while (!done) {
            print("Enter a command: ");
            String command = scanner.nextLine();
            try {
                done = handleCommand(command);

            } catch (ObjectNotFoundExpection objectNotFoundExpection) {
                print("The data you provided was not found!");
            }
            catch (BannedUserException e){
                print("This account has been banned!");
            }
            catch (InvalidLoginException e){
                print("Invalid Username or Password!");
            }
            catch (InvalidPermissionException e){
                print("You do not have permission to perform this action!");
            }
            catch (InvalidActionException e){
                print("You cannot perform this action!");
            }
            catch (ObjectAlreadyExistsException e){
                print("This data already exists!");
            }
        }
    }

    private boolean handleCommand(String command) throws Exception{
        switch (command){
            case "exit":
                return true;
            case "help":
                handleHelp();
                return false;
            case "login":
                handleLogin();
                break;
            case "logout":
                currentUser=null;
        }

        if(currentUser==null){
            print("You must be logged in to access this!");
            return false;
        }

        for(CommandHandler handler : commandHandlers){
            if(handler.isCommand(command))
                return handler.handleCommand(currentUser, scanner, command);
        }
        print("Unknown command. Try again.");
        return false;
    }

    private void handleLogin(){
        if(currentUser!=null) {
            print("You are already logged in!");
            return;
        }
        print("Username: ");
        String username = scanner.nextLine().trim();
        print("Password: ");
        String password = scanner.nextLine().trim();
        currentUser = underflowUserManagementService.login(username, password);
    }

    private void handleHelp(){
        for(CommandHandler handler : commandHandlers){
            handler.getCommands().forEach(this::print);
        }
    }

    private void print(String value) {
        System.out.println(value);
    }

}
