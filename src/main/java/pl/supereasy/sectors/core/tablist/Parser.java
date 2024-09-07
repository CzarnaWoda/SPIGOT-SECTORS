package pl.supereasy.sectors.core.tablist;

import org.bukkit.ChatColor;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.tops.enums.TopType;
import pl.supereasy.sectors.core.tops.impl.Top;
import pl.supereasy.sectors.core.tops.impl.TopList;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.List;

public final class Parser implements Colors {


    public static String parsePlace(final String var) {
        if (!var.contains("TOP-")) {
            return null;
        }
        final int index = getIndex(var);
        if(var.contains("GTOP")){
            return ChatUtil.fixColor(getPlaceGuild(index));
        }else
        if(var.contains("PTOP")){
            return ChatUtil.fixColor(getPlaceUser(index));
        }
        return null;
    }
    public static int getIndex(final String var) {
        final StringBuilder sb = new StringBuilder();
        boolean open = false;
        boolean start = false;
        int result = -1;
        for (final char c : var.toCharArray()) {
            boolean end = false;
            switch (c) {
                case '{': {
                    open = true;
                    break;
                }
                case '-': {
                    start = true;
                    break;
                }
                case '}': {
                    end = true;
                    break;
                }
                default: {
                    if (open && start) {
                        sb.append(c);
                        break;
                    }
                    break;
                }
            }
            if (end) {
                break;
            }
        }
        try {
            result = Integer.parseInt(sb.toString());
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }
    private static String getPlaceUser(int i){
        final TopList tops = SectorPlugin.getInstance().getTopManager().getTopList((TabUpdateTask.type == TabListType.KILLS ? TopType.KILLS : TabUpdateTask.type == TabListType.EATKOX ? TopType.EATKOX : TabUpdateTask.type == TabListType.POINTS ? TopType.USERPOINTS : TopType.MINEDSTONE));
        if (tops == null) {
            return ChatUtil.fixColor( "§#13a60d" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "BRAK");
        }
        final List<Top> ranking = tops.getTops();
        if (ranking.size() >= i) {
            String s = ChatUtil.fixColor("§#13a60d" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "");
            if (i < 4) {
                s = ChatUtil.fixColor( "§#13a60d" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "");
            }
            if (i > 9) {
                s = ChatUtil.fixColor( "§#13a60d" + i + TAB_SpecialSigns + " ->> " + "§#105ac2" + "");
            }
            return ChatUtil.fixColor(s + ranking.get(i - 1).getNickName() + " §#8cf777" + ranking.get(i-1).getTopValue());
        }
        String s = ChatUtil.fixColor("§#13a60d" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "BRAK");
        if (i < 4) {
            s = ChatUtil.fixColor("§#13a60d" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "BRAK");
        }
        if (i > 9) {
            s = ChatUtil.fixColor( "§#13a60d" + i + TAB_SpecialSigns + " ->> " + "§#105ac2" + "BRAK");
        }
        return s;
    }

    private static String getPlaceGuild(int i) {
        final TopList tops = SectorPlugin.getInstance().getTopManager().getTopList(TopType.GUILD);
        if (tops == null) {
            return ChatUtil.fixColor("§#13a60d" + "" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "BRAK");
        }
        final List<Top> ranking = tops.getTops();
        if (ranking.size() >= i) {
            final Top top = ranking.get(i - 1);
            String s = ChatUtil.fixColor("§#13a60d" + "" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "");
            if (i < 4) {
                s = ChatUtil.fixColor("§#13a60d" + "" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "");
            }
            if (i > 9) {
                s = ChatUtil.fixColor("§#13a60d" + "" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "");
            }
            return ChatUtil.fixColor(ChatUtil.fixColor(s + top.getNickName().toUpperCase() + " &8" + "[" + "§#13a60d" + "" + top.getTopValue() + "&8]"));
        }
        String s = ChatUtil.fixColor("§#13a60d" + "" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "BRAK");
        if(i < 4){
            s = ChatUtil.fixColor("§#13a60d" + "" + i + TAB_SpecialSigns + "  ->> " + "§#105ac2" + "BRAK");
        }
        if(i > 9){
            s = ChatUtil.fixColor("§#13a60d" + "" + i + TAB_SpecialSigns + " ->> " + "§#105ac2" + "BRAK");
        }
        return s;
    }
}

