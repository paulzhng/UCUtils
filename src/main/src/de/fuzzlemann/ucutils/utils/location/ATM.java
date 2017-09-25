package de.fuzzlemann.ucutils.utils.location;

/**
 * @author Fuzzlemann
 */
public enum ATM {
    ATM_1(1, 105, 70, 226),
    ATM_2(2, -7, 70, 196),
    ATM_3(3, -268, 70, 235),
    ATM_4(4, -358, 70, 360),
    ATM_5(5, 75, 70, 428),
    ATM_6(6, 296, 70, 49),
    ATM_7(7, 94, 70, -207),
    ATM_8(8, 232, 70, 264),
    ATM_9(9, -153, 70, -45),
    ATM_10(10, 27, 81, 311),
    ATM_11(11, 94, 91, 303),
    ATM_12(12, 17, 91, 303),
    ATM_13(13, 70, 70, 311),
    ATM_14(14, 33, 70, 345),
    ATM_15(15, -86, 70, -404),
    ATM_16(16, 188, 70, -410),
    ATM_17(17, -170, 70, -374),
    ATM_18(18, -169, 70, -504),
    ATM_19(19, 754, 70, 390),
    ATM_20(20, -85, 70, -284),
    ATM_21(21, -59, 70, -358),
    ATM_22(22, -59, 70, -355),
    ATM_23(23, -59, 70, -352),
    ATM_24(24, -228, 70, 5),
    ATM_25(25, 613, 70, 153),
    ATM_26(26, 76, 70, 380),
    ATM_27(27, -368, 70, 533),
    ATM_28(28, 782, 70, 50);

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

    public int getId() {
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
