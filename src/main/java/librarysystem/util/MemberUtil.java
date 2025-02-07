package librarysystem.util;

import java.util.Random;

public class MemberUtil {
    private static final Random random = new Random();

    /**
     * Generates a unique member ID that is a random 4-digit number
     * between 1005 and 9999, excluding the numbers 1001-1004.
     * @return A string representing the member ID.
     */
    public static String generateMemberId() {
        int memberId;

        do {
            // Generate a random number between 1005 and 9999
            memberId = random.nextInt(9000) + 1005;
        } while (memberId >= 1001 && memberId <= 1004);  // Exclude 1001-1004

        return String.valueOf(memberId);  // Return the member ID as a string
    }
}
