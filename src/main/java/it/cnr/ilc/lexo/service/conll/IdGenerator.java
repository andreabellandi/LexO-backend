/**
 * @author Enrico Carniani
 */

package it.cnr.ilc.lexo.service.conll;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class IdGenerator {
    private final static String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final static int divisor = alphabet.length();
    private final static char encodedZero = alphabet.charAt(0);
    private final int[] indexes;

    public IdGenerator() {
        this.indexes = new int[128];
        Arrays.fill(indexes, -1);
        for (int i = 0; i < divisor; i++) {
            indexes[alphabet.charAt(i)] = i;
        }
    }

	public String getId(String string) {
		byte[] hash;
		string = string.toLowerCase();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
		}
		catch(Exception e) {
			hash = string.getBytes(StandardCharsets.UTF_8);
		}
		return encode(hash);
	}

    /**
     * Encodes the given bytes as a baseX string (no checksum is appended).
     *
     * @param input the bytes to encode
     * @return the baseX-encoded string
     */
    private String encode(byte[] input) {
        if (input.length == 0) {
            return "";
        }
        // Count leading zeros.
        int zeros = 0;
        while (zeros < input.length && input[zeros] == 0) {
            ++zeros;
        }
        // Convert base-256 digits to base-X digits (plus conversion to ASCII characters)
        input = Arrays.copyOf(input, input.length); // since we modify it in-place
        char[] encoded = new char[input.length * 2]; // upper bound
        int outputStart = encoded.length;
        for (int inputStart = zeros; inputStart < input.length; ) {
            encoded[--outputStart] = alphabet.charAt(divmod(input, inputStart, 256, divisor));
            if (input[inputStart] == 0) {
                ++inputStart; // optimization - skip leading zeros
            }
        }
        // Preserve exactly as many leading encoded zeros in output as there were leading zeros in input.
        while (outputStart < encoded.length && encoded[outputStart] == encodedZero) {
            ++outputStart;
        }
        while (--zeros >= 0) {
            encoded[--outputStart] = encodedZero;
        }
        // Return encoded string (including encoded leading zeros).
        return new String(encoded, outputStart, encoded.length - outputStart);
    }

    /**
     * Divides a number, represented as an array of bytes each containing a single digit
     * in the specified base, by the given divisor. The given number is modified in-place
     * to contain the quotient, and the return value is the remainder.
     *
     * @param number the number to divide
     * @param firstDigit the index within the array of the first non-zero digit
     *        (this is used for optimization by skipping the leading zeros)
     * @param base the base in which the number's digits are represented (up to 256)
     * @param divisor the number to divide by (up to 256)
     * @return the remainder of the division operation
     */
    private static byte divmod(byte[] number, int firstDigit, int base, int divisor) {
        // this is just long division which accounts for the base of the input digits
        int remainder = 0;
        for (int i = firstDigit; i < number.length; i++) {
            int digit = (int) number[i] & 0xFF;
            int temp = remainder * base + digit;
            number[i] = (byte) (temp / divisor);
            remainder = temp % divisor;
        }
        return (byte) remainder;
    }
}