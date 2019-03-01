package com.ramesh.cbor

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import javax.crypto.Cipher

public class AuthUtils {
    companion object {
        var ALGORITHM: String = "SHA256withECDSA"//SHA256withECDSA

        fun sign(keyPair: KeyPair?, data: kotlin.ByteArray): ByteArray {


            val sig = Signature.getInstance(ALGORITHM)
            sig.initSign(keyPair?.getPrivate())
            sig.update(data)

            val signatureBytes = sig.sign()
            return signatureBytes
        }
        internal fun encrypt(publicKey: PublicKey?, inputData: ByteArray): ByteArray {

            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)

            return cipher.doFinal(inputData)
        }

        fun decrypt(privateKey: PrivateKey?, inputData: ByteArray): ByteArray {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)

            return cipher.doFinal(inputData)
        }


        @Throws(Exception::class)
        internal fun sign(keyPair: KeyPair, message: String) {

            val data = message.toByteArray(StandardCharsets.UTF_8)

            val sig = Signature.getInstance(ALGORITHM)
            sig.initSign(keyPair.private)
            sig.update(data)

            val signatureBytes = sig.sign()

            Utils.log(
                "Signature: " + Base64.encode(
                    signatureBytes,
                    Base64.CRLF or Base64.NO_CLOSE or Base64.NO_PADDING or Base64.NO_WRAP
                )
            )

            sig.initVerify(keyPair.public)
            sig.update(data)

            Utils.log(
                "Verify: " + sig.verify(signatureBytes)
            )

        }
    }


}