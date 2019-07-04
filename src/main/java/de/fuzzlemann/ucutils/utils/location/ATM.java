package de.fuzzlemann.ucutils.utils.location;

/**
 * @author Fuzzlemann
 */
public enum ATM {
    ATM_1(1, 114, 69, 233),
    ATM_2(2, -7, 70, 196),
    ATM_3(3, -269, 69, 235),
    ATM_4(4, -358, 70, 360),
    ATM_5(5, 855, 69, 359),
    ATM_6(6, 296, 69, 49),
    ATM_7(7, 94, 70, -207),
    ATM_8(8, 232, 70, 264),
    ATM_9(9, -153, 70, -45),
    ATM_14(14, 162, 70, 346),
    ATM_15(15, 33, 69, 345),
    ATM_16(16, -86, 70, -404),
    ATM_17(17, 188, 70, -410),
    ATM_18(18, -170, 70, -374),
    ATM_19(19, -169, 70, -504),
    ATM_20(20, 754, 70, 390),
    ATM_21(21, -120, 70, -298),
    ATM_22(22, -59, 70, -358),
    ATM_23(23, -59, 69, -355),
    ATM_24(24, -59, 70, -352),
    ATM_25(25, -228, 70, 5),
    ATM_26(26, 613, 69, 153),
    ATM_27(27, 76, 69, 380),
    ATM_28(28, -368, 69, 533),
    ATM_29(29, 782, 69, 48),
    ATM_30(30, 1042, 70, -81),
    ATM_31(31, 1214, 69, -35),
    ATM_32(32, 1163, 70, -176),
    ATM_33(33, 1146, 69, -283),
    ATM_34(34, 1159, 69, 235),
    ATM_35(35, 1018, 69, 203),
    ATM_36(36, 777, 69, 288),
    ATM_37(37, -510, 70, 336),
    ATM_38(38, 730, 70, 502),
    ATM_39(39, 719, 70, 575),
    ATM_40(40, 834, 53, 264),
    ATM_41(41, 1491, 69, 244),
    ATM_42(42, 1542, 69, 357),
    ATM_43(43, 1671, 69, 480),
    ATM_44(44, 1628, 70, 425),
    ATM_45(45, 160, 70, 378);

    private final int id;
    private final int x;
    private final int y;
    private final int z;

    ATM(int id, int x, int y, int z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getID() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
