package pl.supereasy.sectors.core.textcommands.api;

import io.vavr.control.Option;
import pl.supereasy.sectors.core.textcommands.data.TextCommand;

import java.util.HashMap;

public interface TextCommandManager {

    HashMap<String, TextCommand> getTextCommands();

    void addTextCommand(TextCommand textCommand);
}
