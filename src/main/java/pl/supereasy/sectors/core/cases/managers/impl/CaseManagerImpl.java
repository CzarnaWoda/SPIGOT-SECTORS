package pl.supereasy.sectors.core.cases.managers.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.cases.api.Case;
import pl.supereasy.sectors.core.cases.managers.CaseManager;

import java.util.HashMap;

public class CaseManagerImpl  implements CaseManager {

    private final SectorPlugin plugin;
    private final HashMap<String, Case> cases;

    public CaseManagerImpl(SectorPlugin plugin, Case... casesToRegister){
        this.plugin = plugin;
        this.cases = new HashMap<>();

        for(Case c : casesToRegister){
            registerCase(c);
        }
    }

    @Override
    public void registerCase(Case addedCase) {
        this.cases.put(addedCase.getCaseName(), addedCase);
    }

    @Override
    public void unregisterCase(Case deleteCase) {
        this.cases.remove(deleteCase.getCaseName());
    }

    @Override
    public HashMap<String, Case> getCases() {
        return cases;
    }

}
