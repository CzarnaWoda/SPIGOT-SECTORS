package pl.supereasy.sectors.core.textcommands.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.textcommands.api.TextCommandManager;
import pl.supereasy.sectors.core.textcommands.data.TextCommand;

import java.util.HashMap;

public class TextCommandManagerImpl implements TextCommandManager {

    private final HashMap<String, TextCommand> textCommands;
    private final SectorPlugin plugin;

    public TextCommandManagerImpl(SectorPlugin plugin){
        textCommands = new HashMap<>();
        this.plugin = plugin;
    }


    @Override
    public HashMap<String, TextCommand> getTextCommands() {
        return textCommands;
    }

    @Override
    public void addTextCommand(TextCommand textCommand) {
        this.textCommands.put(textCommand.getCommand(), textCommand);
    }
}
