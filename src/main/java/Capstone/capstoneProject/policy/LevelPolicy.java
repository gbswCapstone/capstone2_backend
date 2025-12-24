package Capstone.capstoneProject.policy;

public final class LevelPolicy {

    private static final int[] LEVEL_XP = {
            0, 0, 100, 250, 430, 650, 920, 1240, 1620, 2070,
            2600, 3210, 3910, 4700, 5580, 6560, 7640, 8830,
            10120, 11520, 13030, 14650, 16380, 18220, 20170,
            22230, 24400, 26680, 29070, 31570, 34180,
            36900, 39730, 42670, 45720, 48880, 52150,
            55530, 59020, 62620, 66330, 70150, 74080,
            78120, 82270, 86530, 90900, 95380, 99970,
            104570, 109180
    };

    private LevelPolicy() {}

    public static int calculateLevel(int totalExperience) {
        int level = 1;
        for (int i = 1; i < LEVEL_XP.length; i++) {
            if (totalExperience >= LEVEL_XP[i]) {
                level = i;
            } else {
                break;
            }
        }
        return level;
    }
}
