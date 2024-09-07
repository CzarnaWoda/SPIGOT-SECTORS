package pl.supereasy.sectors.core.user.enums;

public enum UserStats {
    POINTS("points"),
    KILLS("kills"),
    ASSISTS("assists"),
    DEATHS("deaths"),
    LOGOUTS("logouts"),
    EATKOX("eatKox"),
    EATREF("eatRef"),
    THROWPEARL("throwPearl"),
    OPENEDCASE("openedCase"),
    MINEDSTONE("minedStone"),
    SPENDMONEY("spendMoney");

    private final String tableName;

    UserStats(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
