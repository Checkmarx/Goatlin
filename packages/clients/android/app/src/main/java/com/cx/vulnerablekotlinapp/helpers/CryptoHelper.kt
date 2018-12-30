package com.cx.vulnerablekotlinapp.helpers

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoHelper {
    companion object {
        /**
         * Generates an AES GCM symmetric key on Keystore for encryption and decryption purposes
         * only. `username` is used as key alias
         */
        fun createUserKey(username: String) {
            val keyGenerator: KeyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(username,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()

            keyGenerator.init(parameterSpec)

            keyGenerator.generateKey()
        }

        /**
         * Retrieves user's encryption/decryption key from Keystore
         */
        private fun getUserKey(username: String): SecretKey? {
            val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
                load(null)
            }
            val entry = ks.getEntry(username, null) as? KeyStore.SecretKeyEntry
            // @todo handle null entry
            return entry?.secretKey
        }

        /**
         * Encrypts given `message` using user's (`username`) specific encryption key.
         * Returning value includes the IV as prefix.
         */
        fun encrypt(original: String, username: String): String {
            val entry = getUserKey(username)
            // @todo handle null
            val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")

            cipher.init(Cipher.ENCRYPT_MODE, entry)

            val iv:ByteArray = cipher.iv
            val message: ByteArray = cipher.doFinal(original.toByteArray(Charsets.UTF_8))
            val final: ByteArray = iv+message

            return Base64.encodeToString(final, Base64.DEFAULT)
        }

        /**
         * Decrypts given `message` using user's (`username`) specific encryption key.
         * IV is retrieved from `message` prefix.
         */
        fun decrypt(message: String, username: String): String {
            val secretKey = getUserKey(username)
            //@todo handle null

            val encryptedBA: ByteArray = Base64.decode(message, Base64.DEFAULT)
            val iv: ByteArray = encryptedBA.sliceArray(0..11)
            val message: ByteArray = encryptedBA.sliceArray(12..encryptedBA.lastIndex)

            val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(128, iv)

            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val a = cipher.doFinal(message)

            return a.toString(Charsets.UTF_8)
        }
    }
}