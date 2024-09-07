package pl.supereasy.sectors.core.textcommands.data;

import java.util.List;

public class TextCommand {

    private final String command;
    private final List<String> aliases;
    private final List<String> commandText;

    public TextCommand(String command, List<String> aliases, List<String> commandText){
        this.command = command;
        this.aliases = aliases;
        this.commandText  = commandText;
    }
    public List<String> getCommandAndAliases(){
        List<String> commandAndAliases = aliases;
        commandAndAliases.add(command);
        return commandAndAliases;
    }
    public List<String> getAliases() {
        return aliases;
    }

    public List<String> getCommandText() {
        return commandText;
    }

    public String getCommand() {
        return command;
    }
}
