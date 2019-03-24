package ro.utcn.sd.mdantonio.StackUnderflow.controller;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidCastException;
import ro.utcn.sd.mdantonio.StackUnderflow.service.ManagementService;

import java.util.List;
import java.util.Scanner;

public interface CommandHandler {
    boolean isCommand(String command);
    boolean handleCommand(UnderflowUser currentUser, Scanner scanner, String command) throws Exception;
    List<String> getCommands();
}
