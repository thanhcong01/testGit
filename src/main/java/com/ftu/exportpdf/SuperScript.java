/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.exportpdf;

/**
 *
 * @author TienPM6
 */
public class SuperScript {

    /**
     * @param args the command line arguments
     */
    private static String[] SPECIAL_CHARACTERS = {"0", "1", "2", "3",
        "4", "5", "6", "7", "8", "9", "®", "+", "-", "=",
        "(", ")", "n", "a", "b", "c", "d", "e", "f", "g", "h", "j",
        "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "v", "w",
        "x", "y", "z", "A", "B", "D", "E", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "R", "T", "U", "W",};
    private static String[] REPLACEMENTS = {"⁰", "¹", "²", "³",
        "⁴", "⁵", "⁶", "⁷", "⁸", "⁹", "\u00ae", "⁺", "⁻", "⁼",
        "⁽", "⁾", "ⁿ", "ᵃ", "ᵇ", "ᶜ", "ᵈ", "ᵉ", "ᶠ", "ᵍ", "ʰ", "ʲ",
        "ᵏ", "ˡ", "ᵐ", "ⁿ", "ᵒ", "ᵖ", "ʳ", "ˢ", "ᵗ", "ᵘ", "ᵛ", "ʷ",
        "ˣ", "ʸ", "ᶻ", "ᴬ", "ᴮ", "ᴰ", "ᴱ", "ᴳ", "ᴴ", "ᴵ", "ᴶ", "ᴷ",
        "ᴸ", "ᴹ", "ᴺ", "ᴼ", "ᴾ", "ᴿ", "ᵀ", "ᵁ", "ᵂ",};

    public static String convertToSuperSrcipt(String source) {
        if (source == null) {
            return "";
        }
        StringBuilder value = new StringBuilder((String) source);
        int i = value.indexOf("^");
        if (i >= 0) {
            for (int j = i + 1; j < value.length(); j++) {
                if (value.charAt(j) == ' ' || value.charAt(j) == '\t' || value.charAt(j) == '\r' || value.charAt(j) == '\n') {
                    break;
                } else {
                    if(value.charAt(j) == '®'){
                    }
                    value.setCharAt(j, SuperScript.toSuperScriptChar(value.charAt(j)));
                }
            }
            value.deleteCharAt(i);
        }
        return value.toString();

    }

    public static char toSuperScriptChar(char s) {
        String str = String.valueOf(s);
        return removeAccent(str).charAt(0);
    }

    public static String toSuperScript(String s) {
        int maxLength = s.length();
        StringBuilder result = new StringBuilder(100);
        for (int i = 0; i < maxLength; i++) {
            String str = s.substring(i, i + 1);
            result.append(removeAccent(str));
        }
        return result.toString();
    }

    public static String removeAccent(String str) {
        int index = -1;
        for (int i = 0; i < SPECIAL_CHARACTERS.length; i++) {
            if (str.equals(SPECIAL_CHARACTERS[i])) {
                index = i;
            }
        }
        if (index >= 0) {
            str = REPLACEMENTS[index];
        }
        return str;
    }
}
