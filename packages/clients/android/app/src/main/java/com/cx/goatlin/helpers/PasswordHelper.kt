package com.cx.goatlin.helpers

object PasswordHelper {

    /**
     * Performs given password validation according to OWASP proper password strength
     * @link https://www.owasp.org/index.php/Authentication_Cheat_Sheet#Implement_Proper_Password_Strength_Controls
     */
    fun strength (password: String): Boolean {
        var complexityRulesMatches: Int = 0

        if (!length(password)) {
            return false
        }

        // Password must meet at least 3 out of the following 4 complexity rules
        if (hasAtLeastOneUppercaseLetter(password)) {
            complexityRulesMatches++
        }

        if (hasAtLeastOneLowercaseLetter(password)) {
            complexityRulesMatches++
        }

        if (hasAtLeastOneDigit(password)) {
            complexityRulesMatches++
        }

        if (hasAtLeastOneSpecialChar(password)) {
            complexityRulesMatches++
        }

        if (complexityRulesMatches < 3) {
            return false
        }
        //

        if (!noMoreThanTwoIdenticalCharsInARow(password)) {
            return false
        }

        return true
    }

    /**
     * Whether password length is between 10 and 128
     * @see http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
     *
     */
    private fun length (password: String): Boolean {
        return password.length in 10..128
    }

    /**
     * Whether given password has at least one upper case letter
     */
    private fun hasAtLeastOneUppercaseLetter (password: String): Boolean {
        val regex: Regex = Regex("[A-Z]+")
        return regex.containsMatchIn(password)
    }

    /**
     * Whether given password has at least one lower case letter
     */
    private fun hasAtLeastOneLowercaseLetter (password: String): Boolean {
        val regex: Regex = Regex("[a-z]+")
        return regex.containsMatchIn(password)
    }

    /**
     * Whether given password has at least one digit
     */
    private fun hasAtLeastOneDigit (password: String): Boolean {
        val regex: Regex = Regex("[0-9]+")
        return regex.containsMatchIn(password)
    }

    /**
     * Whether given password has at least a special character
     * @see https://www.owasp.org/index.php/Password_special_characters
     */
    private fun hasAtLeastOneSpecialChar (password: String): Boolean {
        val regex: Regex = Regex("[ !\"#\$%&'()*+,-.\\/:;<=>?@\\[\\\\\\]^_`{|}~]+")
        return regex.containsMatchIn(password)
    }

    /**
     * Whether given password has not more than 2 identical characters in a row
     */
    private fun noMoreThanTwoIdenticalCharsInARow(password: String): Boolean {
        val regex: Regex = Regex("^((.)\\2?(?!\\2))+\$", RegexOption.IGNORE_CASE)
        return regex.containsMatchIn(password)
    }
}
