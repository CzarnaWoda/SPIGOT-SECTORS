package pl.supereasy.sectors.core.tops.enums;

public enum TopType {
    KILLS("userKills", "graczy", "getKills"),
    ASSISTS("userAssists", "asyst", "getAssists"),
    EATKOX("eatKox", "zjedzonych koxow", "getEatKox"),
    EATREF("eatRef", "zjedzonych refili", "getEatRef"),
    THROWPEARL("throwPearl", "wyrzuconych perel", "getThrowPearl"),
    OPENEDCASE("openedCase", "otworzonych premiumcase", "getOpenedCase"),
    MINEDSTONE("minedStone", "wykopanego kamienia", "getMinedStone"),
    SPENDMONEY("spendMoney", "wydanego hajsu", "getSpendMoney"),
    USERPOINTS("userPoints", "zdobytych punktow", "getPoints"),
    GUILD("guilds", "top gildii", "getPoints");


    private final String tableName;
    private final String description;
    private final String methodName;

    TopType(String tableName, String description, final String methodName) {
        this.tableName = tableName;
        this.description = description;
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getDescription() {
        return description;
    }

    public String getTableName() {
        return tableName;
    }
}
