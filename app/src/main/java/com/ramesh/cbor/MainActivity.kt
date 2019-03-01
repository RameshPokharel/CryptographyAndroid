package com.ramesh.cbor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import co.nstant.`in`.cbor.CborBuilder
import co.nstant.`in`.cbor.CborEncoder
import com.ramesh.cbor.AuthUtils.Companion.sign
import com.ramesh.cbor.BufferUtils.concatBuff
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec


class MainActivity : AppCompatActivity() {


    var counter = 3
    var rpId = "hello hello "
    var challenge = "This is challenge"

    var pubKeyX = "9bc5cf94a220383f4c68991419dd5ee5bf8084322f51a74c5fc197c646763deb"
    var pubKeyY = "1786fe1256d50da899e27393ffbfac49f50b3ce033d53291d3f5b9a155b3173e"
    var accessModality = "none"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  manageDecode()

        //generateEcpair()
        credentialsCreate()

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

    private fun credentialsCreate() {

        val generator1 = KeyPairGenerator.getInstance("EC")
        var check = StringUtils.randomId(20)
        generator1.initialize(
            ECGenParameterSpec("secp521r1"),
            check.getRandom()
        )//obtain random id as string from securerandom nextbytes
        val keyPair = generator1.generateKeyPair()
        val publicKey = keyPair.public as ECPublicKey
        val privateKey = keyPair.private as ECPrivateKey

        var createResponse = JSONObject()
        createResponse.put("type", "public-key")
        createResponse.put("id", check.getHexString())
        createResponse.put("rawId", "key.serviceId")
        createResponse.put("getClientExtensionResults", null)


        var clientData = JSONObject()
        clientData.put("hashAlgorithm", "SHA-256")
        clientData.put("type", "webauthn.create")
        clientData.put("origin", "origin")
        clientData.put("clientExtensions", JSONObject())
        clientData.put("challenge", "")

        var clientDataJSON = clientData.toString()
        Log.d("clientDataJson", clientDataJSON)
        var clientDataJSONBase64 = Utils.Base64Encode(clientDataJSON.toByteArray())

        //Hash the clientData
        val clientDataJSONObj = MessageDigest.getInstance("SHA-256")
        clientDataJSONObj.update(clientDataJSON.toByteArray(StandardCharsets.UTF_8));
        var clientDataHash = clientDataJSONObj.digest()

        val rpIdShaObj = MessageDigest.getInstance("SHA-256")
        rpIdShaObj.update(rpId.toByteArray(StandardCharsets.UTF_8));
        var rpIdHashArrayBuff = rpIdShaObj.digest()

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

        var flagBuff = StringUtils.hexStringToByteArray(flagDec.toString(16))// 1 byte flag

        var counterBuff = IntegerUtils.toByteArray(counter)//4 bytes counter

        var aaguidBuff = "d41f5a69b8174144a13c9ebd6d9254d6".toByteArray()
        var credIdBuffer = "key.serviceId".toByteArray()

        var credLenBuff = IntegerUtils.toByteArray(credIdBuffer.size.toShort())


        //    const xVal = pubKey.substring(2, 66);
        //    const yVal = pubKey.substring(66);

        var bufferX =
            StringUtils.hexStringToByteArray(pubKeyX)//


        var bufferY =
            StringUtils.hexStringToByteArray(pubKeyY)//

        val baos = ByteArrayOutputStream()

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

        val pubKeyCboredBuff = baos.toByteArray()

        var finalBuffer = BufferUtils.concatBuff(
            rpIdHashArrayBuff,
            flagBuff,
            counterBuff,
            aaguidBuff,
            credLenBuff,
            credIdBuffer,
            pubKeyCboredBuff
        )
        var signatureBase = concatBuff(finalBuffer, clientDataHash);

        var sig = sign(keyPair, signatureBase)

        var atts = JSONObject()
        atts.put("sig", sig)
        atts.put("alg", -7)

        var authDataStruct = JSONObject()
        authDataStruct.put("fmt", "packed")
        authDataStruct.put("attStmt", atts)

        authDataStruct.put("authData", finalBuffer)
        Log.d(
            "String ", authDataStruct.toString()
        )

        val b = ByteArrayOutputStream()

        CborEncoder(b).encode(
            CborBuilder()
                .add(authDataStruct.toString())
                .build()
        )
        val authDataStructEncoded = b.toByteArray()
        Log.d("cbor", StringUtils.byteArrayToHexString(authDataStructEncoded))

        var authDataStructEncodedBase64 = Utils.Base64Encode(authDataStructEncoded)

        var response = JSONObject()
        response.put("attestationObject", authDataStructEncodedBase64)
        response.put("clientDataJSON", clientDataJSONBase64)


        createResponse.put("response", response)


        var credIdEncoded = StringUtils.byteToString(Utils.Base64Encode("key.serviceId"))

    }


}
