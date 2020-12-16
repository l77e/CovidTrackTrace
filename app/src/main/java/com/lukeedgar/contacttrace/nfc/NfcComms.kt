package com.lukeedgar.contacttrace.nfc

import android.nfc.Tag
import android.nfc.tech.MifareUltralight
import java.nio.charset.Charset
import android.nfc.NdefRecord


class NfcComms {

    fun getNdefRecord(): NdefRecord {
        return NdefRecord.createMime(
            "application/com.lukeedgar.usercheckin",
            "Beam me up, Android".toByteArray(Charset.forName("US-ASCII"))
        )
    }

    fun writeTag(tag: Tag, tagText: String) {
        val miTag = MifareUltralight.get(tag)
        miTag.connect()
        Charset.forName("US-ASCII").also { usAscii ->
            miTag.writePage(4, "abcd".toByteArray(usAscii))
            miTag.writePage(5, "efgh".toByteArray(usAscii))
            miTag.writePage(6, "ijkl".toByteArray(usAscii))
            miTag.writePage(7, "mnop".toByteArray(usAscii))
        }
    }


    public fun readTag(tag: Tag): String? {
        val miTag = MifareUltralight.get(tag)
        miTag.connect()
        val payload = miTag.readPages(4)
        return String(payload, Charset.forName("US-ASCII"))

    }
}