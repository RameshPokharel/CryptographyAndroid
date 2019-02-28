package com.ramesh.cbor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import co.nstant.`in`.cbor.CborBuilder
import co.nstant.`in`.cbor.CborEncoder
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.*
import java.util.*
import javax.crypto.Cipher


class MainActivity : AppCompatActivity() {


    var rpId = "hello hello "
    var challenge = "This is challenge"

    var pubKeyX = "9bc5cf94a220383f4c68991419dd5ee5bf8084322f51a74c5fc197c646763deb"
    var pubKeyY = "1786fe1256d50da899e27393ffbfac49f50b3ce033d53291d3f5b9a155b3173e"
    var ALGORITHM: String = "RSA"
    var accessModality = "none"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  manageDecode()

        //generateEcpair()
        signData()

        //stringConversionMange()

    }

    private fun stringConversionMange() {
        var originalString = "convertThisToHex"
        var flagHex = StringUtils.toHex(originalString)
        var byteArrayOriginalStringArray = StringUtils.hexStringToByteArray(flagHex)

        var hexString = StringUtils.byteArrayToHexString(byteArrayOriginalStringArray)
        var baseString = StringUtils.hexStringToString(hexString)

        var hexStringOriginal = StringUtils.byteToString(byteArrayOriginalStringArray)


        Utils.log(baseString)
        Utils.log(hexStringOriginal)

    }

    private fun signData() {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(1048, StringUtils.randomId(20))// obtain random id as string from securerandom nextbytes


        val keyPair = generator.generateKeyPair()
        //sign challenge
        sign(keyPair, challenge)


        var encryptData = encrypt(keyPair?.public, challenge.toByteArray())
        Utils.log("encryptedData:" + String(encryptData, StandardCharsets.UTF_8))

        var decryptedData = decrypt(keyPair?.private, encryptData)

        Utils.log("decrypted:" + String(decryptedData, StandardCharsets.UTF_8))


        var clientData = JSONObject()
        clientData.put("hashAlgorithm", "SHA-256")
        clientData.put("type", "webauthn.create")
        clientData.put("origin", "origin")
        clientData.put("clientExtensions", JSONObject())
        clientData.put("challenge", "")

        var clientDataJSON = clientData.toString()
        // var clientDataJSONBase64 = Base64.encode(clientDataJSON.toByteArray(), Base64.URL_SAFE)

        //Hash the clientData
        val clientDataJSONObj = MessageDigest.getInstance("SHA-256")
        clientDataJSONObj.update(clientDataJSON.toByteArray(StandardCharsets.UTF_8));
        var clientDataHash = clientDataJSONObj.digest()

        val rpIdShaObj = MessageDigest.getInstance("SHA-256")
        rpIdShaObj.update(rpId.toByteArray(StandardCharsets.UTF_8));
        var rpIdHashArrayBuff = rpIdShaObj.digest()



        manageDecode()

    }


    fun sign(keyPair: KeyPair?, message: String) {

        val data = message.toByteArray(StandardCharsets.UTF_8)

        val sig = Signature.getInstance("SHA1WithRSA")
        sig.initSign(keyPair?.getPrivate())
        sig.update(data)

        val signatureBytes = sig.sign()

        Utils.log(
            "Signature: " + Base64.encode(
                signatureBytes,
                Base64.CRLF or Base64.NO_CLOSE or Base64.NO_PADDING or Base64.NO_WRAP
            )
        )

        sig.initVerify(keyPair?.getPublic())
        sig.update(data)

        Utils.log(
            "Verify: " + sig.verify(signatureBytes)
        )

    }

    fun encrypt(publicKey: PublicKey?, inputData: ByteArray): ByteArray {

        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        return cipher.doFinal(inputData)
    }

    fun decrypt(privateKey: PrivateKey?, inputData: ByteArray): ByteArray {


        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        return cipher.doFinal(inputData)
    }

    public fun cborEcoding() {

        var bufferX =
            StringUtils.hexStringToByteArray(pubKeyX)//


        var bufferY =
            StringUtils.hexStringToByteArray(pubKeyY)//

        val baos = ByteArrayOutputStream()
        //    let dict = [CBOR(integerLiteral: 1): CBOR(integerLiteral: 2), CBOR(integerLiteral: 3) : CBOR(integerLiteral: -7), CBOR(integerLiteral: -1) : CBOR(integerLiteral: 1), CBOR(integerLiteral: -2) : CBOR.byteString(bufferX), -3 : CBOR.byteString(bufferY)]

        CborEncoder(baos).encode(
            CborBuilder()
                .addMap()
                .put(1, 2)
                .put(3, -7)
                .put(-1, 1)
                .put(-2, bufferX)
                .put(-3, bufferY)
                .end()
                .build()
        )

        val encodedBytes = baos.toByteArray()
        text.setText("" + Arrays.toString(encodedBytes))
/*

        var decodeByte = CborDecoder.decode(encodedBytes)

        Log.d("DecodeByte", "" + decodeByte)
*/
    }

    public fun manageDecode() {

        var up = 0
        var uv = 0
        var at = 1
        var ed = 0

        if (accessModality != "none") {
            up = 1;
            uv = 1;
        }

        var upFlag = 1 * up
        var uvFlag = 4 * uv
        var atFlag = 64 * at
        var edFlag = 128 * ed

        var flagDec = upFlag or uvFlag or atFlag or edFlag

        var flagHex = flagDec.toString(16)

        var flagBuff = flagHex.toByteArray()

    }


}
