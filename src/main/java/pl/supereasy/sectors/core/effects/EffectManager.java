package pl.supereasy.sectors.core.effects;

import java.util.ArrayList;
import java.util.List;

public class EffectManager {

    private final List<Effect> effects;

    public EffectManager(){
        this.effects =  new ArrayList<>();
    }

    public void registerEffect(Effect effect){
        effects.add(effect);
        effect.build();
    }

    public List<Effect> getEffects() {
        return effects;
    }
}
