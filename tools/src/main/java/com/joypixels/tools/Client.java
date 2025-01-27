
package com.joypixels.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Client {

    private boolean ascii = true;                // convert ascii smileys? =)
    private boolean riskyMatchAscii = true;      // set true to match ascii without leading/trailing space char
    private boolean shortcodes = true;           // convert shortcodes? :joy:
    private boolean greedyMatch = false;         // when true, matches non-fully-qualified Unicode values

    private String imagePathPNG = "https://cdn.jsdelivr.net/joypixels/assets/";
    private String emojiVersion = "8.0";
    private String emojiDownloadSize = "128";

    private String unicodeRegexp = "(?:[#*0-9]\\uFE0F\\u20E3?|[\\xA9\\xAE\\u203C\\u2049\\u2122\\u2139\\u2194-\\u2199\\u21A9\\u21AA\\u2328\\u23CF\\u23ED-\\u23EF\\u23F1\\u23F2\\u23F8-\\u23FA\\u24C2\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB\\u25FC\\u2600-\\u2604\\u260E\\u2611\\u2618\\u2620\\u2622\\u2623\\u2626\\u262A\\u262E\\u262F\\u2638-\\u263A\\u2640\\u2642\\u265F\\u2660\\u2663\\u2665\\u2666\\u2668\\u267B\\u267E\\u2692\\u2694-\\u2697\\u2699\\u269B\\u269C\\u26A0\\u26B0\\u26B1\\u26C8\\u26CF\\u26D1\\u26D3\\u26E9\\u26F0\\u26F1\\u26F4\\u26F7\\u26F8\\u2702\\u2708\\u2709\\u270F\\u2712\\u2714\\u2716\\u271D\\u2721\\u2733\\u2734\\u2744\\u2747\\u2763\\u27A1\\u2934\\u2935\\u2B05-\\u2B07\\u3030\\u303D\\u3297\\u3299\\x{1F170}\\x{1F171}\\x{1F17E}\\x{1F17F}\\x{1F202}\\x{1F237}\\x{1F321}\\x{1F324}-\\x{1F32C}\\x{1F336}\\x{1F37D}\\x{1F396}\\x{1F397}\\x{1F399}-\\x{1F39B}\\x{1F39E}\\x{1F39F}\\x{1F3CD}\\x{1F3CE}\\x{1F3D4}-\\x{1F3DF}\\x{1F3F5}\\x{1F3F7}\\x{1F43F}\\x{1F4FD}\\x{1F549}\\x{1F54A}\\x{1F56F}\\x{1F570}\\x{1F573}\\x{1F576}-\\x{1F579}\\x{1F587}\\x{1F58A}-\\x{1F58D}\\x{1F5A5}\\x{1F5A8}\\x{1F5B1}\\x{1F5B2}\\x{1F5BC}\\x{1F5C2}-\\x{1F5C4}\\x{1F5D1}-\\x{1F5D3}\\x{1F5DC}-\\x{1F5DE}\\x{1F5E1}\\x{1F5E3}\\x{1F5E8}\\x{1F5EF}\\x{1F5F3}\\x{1F5FA}\\x{1F6CB}\\x{1F6CD}-\\x{1F6CF}\\x{1F6E0}-\\x{1F6E5}\\x{1F6E9}\\x{1F6F0}\\x{1F6F3}]\\uFE0F|[\\u261D\\u270C\\u270D\\x{1F574}\\x{1F590}][\\uFE0F\\x{1F3FB}-\\x{1F3FF}]|[\\u26F9\\x{1F3CB}\\x{1F3CC}\\x{1F575}][\\uFE0F\\x{1F3FB}-\\x{1F3FF}](?:\\u200D[\\u2640\\u2642]\\uFE0F)?|[\\u270A\\u270B\\x{1F385}\\x{1F3C2}\\x{1F3C7}\\x{1F442}\\x{1F443}\\x{1F446}-\\x{1F450}\\x{1F466}\\x{1F467}\\x{1F46B}-\\x{1F46D}\\x{1F472}\\x{1F474}-\\x{1F476}\\x{1F478}\\x{1F47C}\\x{1F483}\\x{1F485}\\x{1F48F}\\x{1F491}\\x{1F4AA}\\x{1F57A}\\x{1F595}\\x{1F596}\\x{1F64C}\\x{1F64F}\\x{1F6C0}\\x{1F6CC}\\x{1F90C}\\x{1F90F}\\x{1F918}-\\x{1F91F}\\x{1F930}-\\x{1F934}\\x{1F936}\\x{1F977}\\x{1F9B5}\\x{1F9B6}\\x{1F9BB}\\x{1F9D2}\\x{1F9D3}\\x{1F9D5}\\x{1FAC3}-\\x{1FAC5}\\x{1FAF0}\\x{1FAF2}-\\x{1FAF8}][\\x{1F3FB}-\\x{1F3FF}]?|[\\x{1F3C3}\\x{1F3C4}\\x{1F3CA}\\x{1F46E}\\x{1F470}\\x{1F471}\\x{1F473}\\x{1F477}\\x{1F481}\\x{1F482}\\x{1F486}\\x{1F487}\\x{1F645}-\\x{1F647}\\x{1F64B}\\x{1F64D}\\x{1F64E}\\x{1F6A3}\\x{1F6B4}-\\x{1F6B6}\\x{1F926}\\x{1F935}\\x{1F937}-\\x{1F939}\\x{1F93D}\\x{1F93E}\\x{1F9B8}\\x{1F9B9}\\x{1F9CD}-\\x{1F9CF}\\x{1F9D4}\\x{1F9D6}-\\x{1F9DD}][\\x{1F3FB}-\\x{1F3FF}]?(?:\\u200D[\\u2640\\u2642]\\uFE0F)?|[\\x{1F408}\\x{1F426}](?:\\u200D\\u2B1B)?|[\\x{1F46F}\\x{1F93C}\\x{1F9DE}\\x{1F9DF}](?:\\u200D[\\u2640\\u2642]\\uFE0F)?|[\\u231A\\u231B\\u23E9-\\u23EC\\u23F0\\u23F3\\u25FD\\u25FE\\u2614\\u2615\\u2648-\\u2653\\u267F\\u2693\\u26A1\\u26A7\\u26AA\\u26AB\\u26BD\\u26BE\\u26C4\\u26C5\\u26CE\\u26D4\\u26EA\\u26F2\\u26F3\\u26F5\\u26FA\\u26FD\\u2705\\u2728\\u274C\\u274E\\u2753-\\u2755\\u2757\\u2795-\\u2797\\u27B0\\u27BF\\u2B1B\\u2B1C\\u2B50\\u2B55\\x{1F004}\\x{1F0CF}\\x{1F18E}\\x{1F191}-\\x{1F19A}\\x{1F201}\\x{1F21A}\\x{1F22F}\\x{1F232}-\\x{1F236}\\x{1F238}-\\x{1F23A}\\x{1F250}\\x{1F251}\\x{1F300}-\\x{1F320}\\x{1F32D}-\\x{1F335}\\x{1F337}-\\x{1F37C}\\x{1F37E}-\\x{1F384}\\x{1F386}-\\x{1F393}\\x{1F3A0}-\\x{1F3C1}\\x{1F3C5}\\x{1F3C6}\\x{1F3C8}\\x{1F3C9}\\x{1F3CF}-\\x{1F3D3}\\x{1F3E0}-\\x{1F3F0}\\x{1F3F8}-\\x{1F407}\\x{1F409}-\\x{1F414}\\x{1F416}-\\x{1F425}\\x{1F427}-\\x{1F43A}\\x{1F43C}-\\x{1F43E}\\x{1F440}\\x{1F444}\\x{1F445}\\x{1F451}-\\x{1F465}\\x{1F46A}\\x{1F479}-\\x{1F47B}\\x{1F47D}-\\x{1F480}\\x{1F484}\\x{1F488}-\\x{1F48E}\\x{1F490}\\x{1F492}-\\x{1F4A9}\\x{1F4AB}-\\x{1F4FC}\\x{1F4FF}-\\x{1F53D}\\x{1F54B}-\\x{1F54E}\\x{1F550}-\\x{1F567}\\x{1F5A4}\\x{1F5FB}-\\x{1F62D}\\x{1F62F}-\\x{1F634}\\x{1F637}-\\x{1F644}\\x{1F648}-\\x{1F64A}\\x{1F680}-\\x{1F6A2}\\x{1F6A4}-\\x{1F6B3}\\x{1F6B7}-\\x{1F6BF}\\x{1F6C1}-\\x{1F6C5}\\x{1F6D0}-\\x{1F6D2}\\x{1F6D5}-\\x{1F6D7}\\x{1F6DC}-\\x{1F6DF}\\x{1F6EB}\\x{1F6EC}\\x{1F6F4}-\\x{1F6FC}\\x{1F7E0}-\\x{1F7EB}\\x{1F7F0}\\x{1F90D}\\x{1F90E}\\x{1F910}-\\x{1F917}\\x{1F920}-\\x{1F925}\\x{1F927}-\\x{1F92F}\\x{1F93A}\\x{1F93F}-\\x{1F945}\\x{1F947}-\\x{1F976}\\x{1F978}-\\x{1F9B4}\\x{1F9B7}\\x{1F9BA}\\x{1F9BC}-\\x{1F9CC}\\x{1F9D0}\\x{1F9E0}-\\x{1F9FF}\\x{1FA70}-\\x{1FA7C}\\x{1FA80}-\\x{1FA88}\\x{1FA90}-\\x{1FABD}\\x{1FABF}-\\x{1FAC2}\\x{1FACE}-\\x{1FADB}\\x{1FAE0}-\\x{1FAE8}]|\\u2764\\uFE0F(?:\\u200D[\\x{1F525}\\x{1FA79}])?|\\x{1F1E6}[\\x{1F1E8}-\\x{1F1EC}\\x{1F1EE}\\x{1F1F1}\\x{1F1F2}\\x{1F1F4}\\x{1F1F6}-\\x{1F1FA}\\x{1F1FC}\\x{1F1FD}\\x{1F1FF}]?|\\x{1F1E7}[\\x{1F1E6}\\x{1F1E7}\\x{1F1E9}-\\x{1F1EF}\\x{1F1F1}-\\x{1F1F4}\\x{1F1F6}-\\x{1F1F9}\\x{1F1FB}\\x{1F1FC}\\x{1F1FE}\\x{1F1FF}]?|\\x{1F1E8}[\\x{1F1E6}\\x{1F1E8}\\x{1F1E9}\\x{1F1EB}-\\x{1F1EE}\\x{1F1F0}-\\x{1F1F5}\\x{1F1F7}\\x{1F1FA}-\\x{1F1FF}]?|\\x{1F1E9}[\\x{1F1EA}\\x{1F1EC}\\x{1F1EF}\\x{1F1F0}\\x{1F1F2}\\x{1F1F4}\\x{1F1FF}]?|\\x{1F1EA}[\\x{1F1E6}\\x{1F1E8}\\x{1F1EA}\\x{1F1EC}\\x{1F1ED}\\x{1F1F7}-\\x{1F1FA}]?|\\x{1F1EB}[\\x{1F1EE}-\\x{1F1F0}\\x{1F1F2}\\x{1F1F4}\\x{1F1F7}]?|\\x{1F1EC}[\\x{1F1E6}\\x{1F1E7}\\x{1F1E9}-\\x{1F1EE}\\x{1F1F1}-\\x{1F1F3}\\x{1F1F5}-\\x{1F1FA}\\x{1F1FC}\\x{1F1FE}]?|\\x{1F1ED}[\\x{1F1F0}\\x{1F1F2}\\x{1F1F3}\\x{1F1F7}\\x{1F1F9}\\x{1F1FA}]?|\\x{1F1EE}[\\x{1F1E8}-\\x{1F1EA}\\x{1F1F1}-\\x{1F1F4}\\x{1F1F6}-\\x{1F1F9}]?|\\x{1F1EF}[\\x{1F1EA}\\x{1F1F2}\\x{1F1F4}\\x{1F1F5}]?|\\x{1F1F0}[\\x{1F1EA}\\x{1F1EC}-\\x{1F1EE}\\x{1F1F2}\\x{1F1F3}\\x{1F1F5}\\x{1F1F7}\\x{1F1FC}\\x{1F1FE}\\x{1F1FF}]?|\\x{1F1F1}[\\x{1F1E6}-\\x{1F1E8}\\x{1F1EE}\\x{1F1F0}\\x{1F1F7}-\\x{1F1FB}\\x{1F1FE}]?|\\x{1F1F2}[\\x{1F1E6}\\x{1F1E8}-\\x{1F1ED}\\x{1F1F0}-\\x{1F1FF}]?|\\x{1F1F3}[\\x{1F1E6}\\x{1F1E8}\\x{1F1EA}-\\x{1F1EC}\\x{1F1EE}\\x{1F1F1}\\x{1F1F4}\\x{1F1F5}\\x{1F1F7}\\x{1F1FA}\\x{1F1FF}]?|\\x{1F1F4}\\x{1F1F2}?|\\x{1F1F5}[\\x{1F1E6}\\x{1F1EA}-\\x{1F1ED}\\x{1F1F0}-\\x{1F1F3}\\x{1F1F7}-\\x{1F1F9}\\x{1F1FC}\\x{1F1FE}]?|\\x{1F1F6}\\x{1F1E6}?|\\x{1F1F7}[\\x{1F1EA}\\x{1F1F4}\\x{1F1F8}\\x{1F1FA}\\x{1F1FC}]?|\\x{1F1F8}[\\x{1F1E6}-\\x{1F1EA}\\x{1F1EC}-\\x{1F1F4}\\x{1F1F7}-\\x{1F1F9}\\x{1F1FB}\\x{1F1FD}-\\x{1F1FF}]?|\\x{1F1F9}[\\x{1F1E6}\\x{1F1E8}\\x{1F1E9}\\x{1F1EB}-\\x{1F1ED}\\x{1F1EF}-\\x{1F1F4}\\x{1F1F7}\\x{1F1F9}\\x{1F1FB}\\x{1F1FC}\\x{1F1FF}]?|\\x{1F1FA}[\\x{1F1E6}\\x{1F1EC}\\x{1F1F2}\\x{1F1F3}\\x{1F1F8}\\x{1F1FE}\\x{1F1FF}]?|\\x{1F1FB}[\\x{1F1E6}\\x{1F1E8}\\x{1F1EA}\\x{1F1EC}\\x{1F1EE}\\x{1F1F3}\\x{1F1FA}]?|\\x{1F1FC}[\\x{1F1EB}\\x{1F1F8}]?|\\x{1F1FD}\\x{1F1F0}?|\\x{1F1FE}[\\x{1F1EA}\\x{1F1F9}]?|\\x{1F1FF}[\\x{1F1E6}\\x{1F1F2}\\x{1F1FC}]?|\\x{1F3F3}\\uFE0F(?:\\u200D(?:\\u26A7\\uFE0F|\\x{1F308}))?|\\x{1F3F4}(?:\\u200D\\u2620\\uFE0F|\\x{E0067}\\x{E0062}(?:\\x{E0065}\\x{E006E}\\x{E0067}|\\x{E0073}\\x{E0063}\\x{E0074}|\\x{E0077}\\x{E006C}\\x{E0073})\\x{E007F})?|\\x{1F415}(?:\\u200D\\x{1F9BA})?|\\x{1F43B}(?:\\u200D\\u2744\\uFE0F)?|\\x{1F441}\\uFE0F(?:\\u200D\\x{1F5E8}\\uFE0F)?|\\x{1F468}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F468}\\x{1F469}]\\u200D(?:\\x{1F466}(?:\\u200D\\x{1F466})?|\\x{1F467}(?:\\u200D[\\x{1F466}\\x{1F467}])?)|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F468}|\\x{1F466}(?:\\u200D\\x{1F466})?|\\x{1F467}(?:\\u200D[\\x{1F466}\\x{1F467}])?)|\\x{1F3FB}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F468}[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D\\x{1F468}[\\x{1F3FC}-\\x{1F3FF}]))?|\\x{1F3FC}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F468}[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D\\x{1F468}[\\x{1F3FB}\\x{1F3FD}-\\x{1F3FF}]))?|\\x{1F3FD}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F468}[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D\\x{1F468}[\\x{1F3FB}\\x{1F3FC}\\x{1F3FE}\\x{1F3FF}]))?|\\x{1F3FE}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F468}[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D\\x{1F468}[\\x{1F3FB}-\\x{1F3FD}\\x{1F3FF}]))?|\\x{1F3FF}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F468}[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D\\x{1F468}[\\x{1F3FB}-\\x{1F3FE}]))?)?|\\x{1F469}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?[\\x{1F468}\\x{1F469}]|\\x{1F466}(?:\\u200D\\x{1F466})?|\\x{1F467}(?:\\u200D[\\x{1F466}\\x{1F467}])?|\\x{1F469}\\u200D(?:\\x{1F466}(?:\\u200D\\x{1F466})?|\\x{1F467}(?:\\u200D[\\x{1F466}\\x{1F467}])?))|\\x{1F3FB}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:[\\x{1F468}\\x{1F469}]|\\x{1F48B}\\u200D[\\x{1F468}\\x{1F469}])[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D[\\x{1F468}\\x{1F469}][\\x{1F3FC}-\\x{1F3FF}]))?|\\x{1F3FC}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:[\\x{1F468}\\x{1F469}]|\\x{1F48B}\\u200D[\\x{1F468}\\x{1F469}])[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D[\\x{1F468}\\x{1F469}][\\x{1F3FB}\\x{1F3FD}-\\x{1F3FF}]))?|\\x{1F3FD}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:[\\x{1F468}\\x{1F469}]|\\x{1F48B}\\u200D[\\x{1F468}\\x{1F469}])[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D[\\x{1F468}\\x{1F469}][\\x{1F3FB}\\x{1F3FC}\\x{1F3FE}\\x{1F3FF}]))?|\\x{1F3FE}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:[\\x{1F468}\\x{1F469}]|\\x{1F48B}\\u200D[\\x{1F468}\\x{1F469}])[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D[\\x{1F468}\\x{1F469}][\\x{1F3FB}-\\x{1F3FD}\\x{1F3FF}]))?|\\x{1F3FF}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:[\\x{1F468}\\x{1F469}]|\\x{1F48B}\\u200D[\\x{1F468}\\x{1F469}])[\\x{1F3FB}-\\x{1F3FF}]|\\x{1F91D}\\u200D[\\x{1F468}\\x{1F469}][\\x{1F3FB}-\\x{1F3FE}]))?)?|\\x{1F62E}(?:\\u200D\\x{1F4A8})?|\\x{1F635}(?:\\u200D\\x{1F4AB})?|\\x{1F636}(?:\\u200D\\x{1F32B}\\uFE0F)?|\\x{1F9D1}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F384}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\x{1F91D}\\u200D\\x{1F9D1})|\\x{1F3FB}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F384}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F9D1}[\\x{1F3FC}-\\x{1F3FF}]|\\x{1F91D}\\u200D\\x{1F9D1}[\\x{1F3FB}-\\x{1F3FF}]))?|\\x{1F3FC}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F384}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F9D1}[\\x{1F3FB}\\x{1F3FD}-\\x{1F3FF}]|\\x{1F91D}\\u200D\\x{1F9D1}[\\x{1F3FB}-\\x{1F3FF}]))?|\\x{1F3FD}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F384}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F9D1}[\\x{1F3FB}\\x{1F3FC}\\x{1F3FE}\\x{1F3FF}]|\\x{1F91D}\\u200D\\x{1F9D1}[\\x{1F3FB}-\\x{1F3FF}]))?|\\x{1F3FE}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F384}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F9D1}[\\x{1F3FB}-\\x{1F3FD}\\x{1F3FF}]|\\x{1F91D}\\u200D\\x{1F9D1}[\\x{1F3FB}-\\x{1F3FF}]))?|\\x{1F3FF}(?:\\u200D(?:[\\u2695\\u2696\\u2708]\\uFE0F|[\\x{1F33E}\\x{1F373}\\x{1F37C}\\x{1F384}\\x{1F393}\\x{1F3A4}\\x{1F3A8}\\x{1F3EB}\\x{1F3ED}\\x{1F4BB}\\x{1F4BC}\\x{1F527}\\x{1F52C}\\x{1F680}\\x{1F692}\\x{1F9AF}-\\x{1F9B3}\\x{1F9BC}\\x{1F9BD}]|\\u2764\\uFE0F\\u200D(?:\\x{1F48B}\\u200D)?\\x{1F9D1}[\\x{1F3FB}-\\x{1F3FE}]|\\x{1F91D}\\u200D\\x{1F9D1}[\\x{1F3FB}-\\x{1F3FF}]))?)?|\\x{1FAF1}(?:\\x{1F3FB}(?:\\u200D\\x{1FAF2}[\\x{1F3FC}-\\x{1F3FF}])?|\\x{1F3FC}(?:\\u200D\\x{1FAF2}[\\x{1F3FB}\\x{1F3FD}-\\x{1F3FF}])?|\\x{1F3FD}(?:\\u200D\\x{1FAF2}[\\x{1F3FB}\\x{1F3FC}\\x{1F3FE}\\x{1F3FF}])?|\\x{1F3FE}(?:\\u200D\\x{1FAF2}[\\x{1F3FB}-\\x{1F3FD}\\x{1F3FF}])?|\\x{1F3FF}(?:\\u200D\\x{1FAF2}[\\x{1F3FB}-\\x{1F3FE}])?)?)";

    private String shortnameRegexp = ":([-+ñ\\w]+):";

    private Ruleset ruleset = null;
    private Context context = null;

    public Client(Context context) {
        this.context = context;
        this.ruleset = new Ruleset(context);
    }

    /**
     * This will return the shortname from unicode input.
     */
    public String toShortname(String string) {
        Pattern pattern = Pattern.compile(this.unicodeRegexp);
        Matcher matcher = pattern.matcher(string);
        return replaceUnicodeWithShortname(string, matcher);
    }

    /**
     * This will output unicode from shortname input.
     */
    public String shortnameToUnicode(String string) {
        if(this.ascii) {
            String asciiRegexp = this.ruleset.getAsciiRegexp();
            String asciiRX = (this.riskyMatchAscii) ? "(()" + asciiRegexp + "())" : "((\\s|^)"+asciiRegexp+"(?=\\s|$|[!,.?]))";

            Pattern pattern = Pattern.compile(asciiRX);
            Matcher matches = pattern.matcher(string);
            string = replaceAsciiWithUnicode(string, matches);
        }
        if(this.shortcodes) {
            Pattern pattern = Pattern.compile(this.shortnameRegexp);
            Matcher matches = pattern.matcher(string);
            string = replaceShortnameWithUnicode(string, matches);
        }
        return string;
    }

    /**
     * Changes unicode to a shortname then converts those shortnames
     * to images and places them in a spannablestringbuilder. The size of the images
     * are set using imageSize. A callback is made once the function completes on
     * the main thread so that the spannablestringbuilder may be applied to a ui element.
     */
    public void toImage(String string, int imageSize, com.joypixels.tools.Callback callback) {
        if(this.ascii) {
            String asciiRegexp = this.ruleset.getAsciiRegexp();
            String asciiRX = (this.riskyMatchAscii) ? "(()" + asciiRegexp + "())" : "((\\s|^)"+asciiRegexp+"(?=\\s|$|[!,.?]))";

            Pattern pattern = Pattern.compile(asciiRX);
            Matcher matcher = pattern.matcher(string);
            string = replaceAsciiWithUnicode(string, matcher);
        }
        Pattern pattern = Pattern.compile(this.unicodeRegexp);
        Matcher matcher = pattern.matcher(string);
        string = replaceUnicodeWithShortname(string, matcher);

        SpannableStringBuilder ssb = new SpannableStringBuilder(string);
        pattern = Pattern.compile(this.shortnameRegexp);
        matcher = pattern.matcher(string);
        ArrayList<String> matchList = new ArrayList<>();
        while (matcher.find()) {
            matchList.add(matcher.group(0));
        }
        if(matchList.size()==0) {
            callback.onSuccess(ssb);
        }
        replaceShortnameWithImages(ssb, imageSize, matchList, callback);
    }

    /**
     * Changes shortname to images and places them into a spannablestringbuilder.
     * The size of the images are set using imageSize and callback is made once
     * the function completes.
     */
    public void shortnameToImage(String string, int imageSize, com.joypixels.tools.Callback callback) {
        if (this.ascii) {
            String asciiRegexp = this.ruleset.getAsciiRegexp();
            String asciiRX = (this.riskyMatchAscii) ? "(()" + asciiRegexp + "())" : "((\\s|^)"+asciiRegexp+"(?=\\s|$|[!,.?]))";

            Pattern pattern = Pattern.compile(asciiRX);
            Matcher matcher = pattern.matcher(string);
            string = replaceAsciiWithShortname(string, matcher);
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(string);
        Pattern pattern = Pattern.compile(this.shortnameRegexp);
        Matcher matcher = pattern.matcher(string);
        ArrayList<String> matchList = new ArrayList<>();
        while (matcher.find()) {
            matchList.add(matcher.group(0));
        }
        if(matchList.size()==0) {
            callback.onSuccess(ssb);
        }
        replaceShortnameWithImages(ssb, imageSize, matchList, callback);
    }

    /**
     * Changes everything to a shortname then converts those shortnames
     * to images into a spannablestringbuilder. The size of the images
     * are set using imageSize and callback is made once the function completes.
     */
    public void unicodeToImage(String string, int imageSize, com.joypixels.tools.Callback callback) {
        if(this.ascii) {
            String asciiRegexp = this.ruleset.getAsciiRegexp();
            String asciiRX = (this.riskyMatchAscii) ? "(()" + asciiRegexp + "())" : "((\\s|^)"+asciiRegexp+"(?=\\s|$|[!,.?]))";

            Pattern pattern = Pattern.compile(asciiRX);
            Matcher matcher = pattern.matcher(string);
            string = replaceAsciiWithUnicode(string, matcher);
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(string);
        Pattern pattern = Pattern.compile(this.unicodeRegexp);
        Matcher matcher = pattern.matcher(string);
        ArrayList<String> matchList = new ArrayList<>();
        while (matcher.find()) {
            matchList.add(matcher.group(0));
        }
        if(matchList.size()==0) {
            callback.onSuccess(ssb);
        }
        replaceUnicodeWithImages(ssb, imageSize, matchList, callback);
    }

    /**
     * A recursive function that generates a spannablestringbuilder using the
     * shortnames found in the matchList. Once there are no matches the
     * callback is executed on the main thread so that the spannablestringbuilder
     * may be applied to a ui element.
     */
    private void replaceShortnameWithImages(final SpannableStringBuilder ssb, final int imageSize, final ArrayList<String> matchList, final com.joypixels.tools.Callback callback) {
        if(matchList.size()==0) {
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(ssb);
                }
            });
        } else {
            LinkedHashMap<String, ArrayList<String>> shortcode_replace = this.ruleset.getShortcodeReplace();
            final String shortname = matchList.get(0);
            matchList.remove(0);
            if (shortcode_replace.containsKey(shortname)) {
                String filename = shortcode_replace.get(shortname).get(1);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(this.imagePathPNG+this.emojiVersion+"/png/unicode/"+this.emojiDownloadSize+"/"+filename+".png")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        replaceShortnameWithImages(ssb, imageSize, matchList, callback);
                    }
                    @Override
                    public void onResponse(Call call, final Response response) {
                        if (!response.isSuccessful()) {
                            replaceShortnameWithImages(ssb, imageSize, matchList, callback);
                        } else {
                            int startIndex = ssb.toString().indexOf(shortname);
                            InputStream inputStream = response.body().byteStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            BitmapDrawable bmpDrawable = new BitmapDrawable(context.getResources(), bitmap);
                            bmpDrawable.setBounds(0, 0, imageSize, imageSize);
                            ssb.replace(startIndex,startIndex+shortname.length(), " ");
                            ssb.setSpan(new ImageSpan(bmpDrawable), startIndex, startIndex+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            replaceShortnameWithImages(ssb, imageSize, matchList, callback);
                        }
                    }
                });
            }
        }
    }

    /**
     * A recursive function that generates a spannablestringbuilder using the
     * unicodes found in the matchList. Once there are no matches the
     * callback is executed on the main thread so that the spannablestringbuilder
     * may be applied to a ui element.
     */
    private void replaceUnicodeWithImages(final SpannableStringBuilder ssb, final int imageSize, final ArrayList<String> matchList, final com.joypixels.tools.Callback callback) {
        if(matchList.size()==0) {
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(ssb);
                }
            });
        } else {
            LinkedHashMap<String, String> unicode_replace = this.ruleset.getUnicodeReplace();
            LinkedHashMap<String, String> unicode_replace_greedy = this.ruleset.getUnicodeReplaceGreedy();
            LinkedHashMap<String, ArrayList<String>> shortcode_replace = this.ruleset.getShortcodeReplace();
            final String unicode = matchList.get(0);
            matchList.remove(0);
            String shortname = "";
            StringBuilder hexValue = new StringBuilder();
            String hexString = "";
            try {
                byte[] xxx = unicode.getBytes("UTF-8");
                for (byte x : xxx) {
                    hexValue.append(String.format("%02X", x));
                }
            } catch (Exception e) {
                Log.e("UnicodeWithImages", e.getMessage());
            }
            if(unicode_replace.containsKey(hexValue.toString())) {
                shortname = unicode_replace.get(hexValue.toString());
            } else if(this.greedyMatch && unicode_replace_greedy.containsKey(hexValue.toString())) {
                shortname = unicode_replace_greedy.get(hexValue.toString());
            } else {
                replaceUnicodeWithImages(ssb, imageSize, matchList, callback);
                return;
            }
            String filename = shortcode_replace.get(shortname).get(1);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(this.imagePathPNG + this.emojiVersion + "/png/unicode/" + this.emojiDownloadSize + "/" + filename + ".png")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    replaceUnicodeWithImages(ssb, imageSize, matchList, callback);
                }
                @Override
                public void onResponse(Call call, final Response response) {
                    if (!response.isSuccessful()) {
                        replaceUnicodeWithImages(ssb, imageSize, matchList, callback);
                    } else {
                        int startIndex = ssb.toString().indexOf(unicode);
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        BitmapDrawable bmpDrawable = new BitmapDrawable(context.getResources(), bitmap);
                        bmpDrawable.setBounds(0, 0, imageSize, imageSize);
                        ssb.replace(startIndex, startIndex + unicode.length(), " ");
                        ssb.setSpan(new ImageSpan(bmpDrawable), startIndex, startIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        replaceUnicodeWithImages(ssb, imageSize, matchList, callback);
                    }
                }
            });
        }
    }

    /**
     * Replaces all shortname instances with its unicode equivalent.
     * Matcher contains a list of matching items.
     */
    private String replaceShortnameWithUnicode(String string, Matcher matcher) {
        ArrayList<String> matchList = new ArrayList<>();
        while (matcher.find()) {
            matchList.add(matcher.group(0));
        }
        if(matchList.size()==0) {
            return string;
        } else {
            LinkedHashMap<String, ArrayList<String>> shortcode_replace = this.ruleset.getShortcodeReplace();
            for(String shortname : matchList) {
                try {
                    if (shortcode_replace.containsKey(shortname)) {
                        String emojiHex = shortcode_replace.get(shortname).get(1);
                        String emoji = hexStringToCodePoint(emojiHex);
                        string = string.replace(shortname, emoji);
                    }
                } catch (Exception e) {
                    Log.e("ShortnameWithUnicode",e.getMessage());
                }
            }
            return string;
        }
    }

    /**
     * Replaces all ascii instances with its unicode equivalent.
     */
    private String replaceAsciiWithUnicode(String string, Matcher matcher) {
        ArrayList<String> matchList = new ArrayList<>();
        while (matcher.find()) {
            matchList.add(matcher.group(0));
        }
        if(matchList.size()==0) {
            return string;
        } else {
            LinkedHashMap<String, String> ascii_replace = this.ruleset.getAsciiReplace();
            LinkedHashMap<String, ArrayList<String>> shortcode_replace = this.ruleset.getShortcodeReplace();
            for(String ascii : matchList) {
                try {
                    if (ascii_replace.containsKey(ascii)) {
                        string = string.replace(ascii, ascii_replace.get(ascii));
                        if(shortcode_replace.containsKey(ascii_replace.get(ascii))) {
                            string = string.replace(ascii_replace.get(ascii), shortcode_replace.get(ascii_replace.get(ascii)).get(0));
                        }
                    }
                } catch (Exception e) {
                    Log.e("replaceAsciiWithUnicode",e.getMessage());
                }
            }
            return string;
        }
    }

    /**
     * Replaces all ascii instances with its shortname equivalent.
     */
    private String replaceAsciiWithShortname(String string, Matcher matcher) {
        ArrayList<String> matchList = new ArrayList<>();
        while (matcher.find()) {
            matchList.add(matcher.group(0));
        }
        if(matchList.size()==0) {
            return string;
        } else {
            LinkedHashMap<String, String> ascii_replace = this.ruleset.getAsciiReplace();
            LinkedHashMap<String, ArrayList<String>> shortcode_replace = this.ruleset.getShortcodeReplace();
            for(String ascii : matchList) {
                try {
                    if (ascii_replace.containsKey(ascii)) {
                        string = string.replace(ascii, ascii_replace.get(ascii));
                    }
                } catch (Exception e) {
                    Log.e("replaceAsciiWithUnicode",e.getMessage());
                }
            }
            return string;
        }
    }

    /**
     * Replaces all unicode instances with its shortname equivalent.
     */
    private String replaceUnicodeWithShortname(String string, Matcher matcher) {
        ArrayList<String> matchList = new ArrayList<>();
        while (matcher.find()) {
            matchList.add(matcher.group(0));
        }
        if(matchList.size()==0) {
            return string;
        } else {
            LinkedHashMap<String, String> unicode_replace = this.ruleset.getUnicodeReplace();
            for(String unicode : matchList) {
                try {
                    StringBuilder hexValue = new StringBuilder();
                    byte[] xxx = unicode.getBytes("UTF-8");
                    String hexString = "";
                    for (byte x : xxx) {
                        hexValue.append(String.format("%02X", x));
                    }
                    if (unicode_replace.containsKey(hexValue.toString())) {
                        string = string.replace(unicode, unicode_replace.get(hexValue.toString()));
                    }
                } catch (Exception e) {
                    Log.e("MatchesWithShortname",e.getMessage());
                }
            }
            return string;
        }
    }

    private String hexStringToCodePoint(String hexString) {
        int codePoint;
        int parts[];
        int partsLength = 1;
        if (hexString.contains("-")) {
            String s[] = hexString.split("-");
            partsLength = s.length;
            parts = new int[partsLength];
            for (int i = 0; i < partsLength; i++) {
                parts[i] = Integer.parseInt(s[i], 16);
            }
        } else {
            codePoint = Integer.parseInt(hexString, 16);
            parts = new int[]{codePoint};
        }
        return new String(parts, 0, partsLength);
    }

    public boolean isAscii() {
        return ascii;
    }
    public void setAscii(boolean ascii) {
        this.ascii = ascii;
    }
    public boolean isRiskyMatchAscii() {
        return riskyMatchAscii;
    }
    public void setRiskyMatchAscii(boolean riskyMatchAscii) {
        this.riskyMatchAscii = riskyMatchAscii;
    }
    public boolean isShortcodes() {
        return shortcodes;
    }
    public void setShortcodes(boolean shortcodes) {
        this.shortcodes = shortcodes;
    }
    public String getEmojiVersion() {
        return emojiVersion;
    }
    public void setEmojiVersion(String emojiVersion) {
        this.emojiVersion = emojiVersion;
    }
    public String getEmojiDownloadSize() {
        return emojiDownloadSize;
    }
    public void setEmojiDownloadSize(String emojiDownloadSize) {
        this.emojiDownloadSize = emojiDownloadSize;
    }
    public boolean isGreedyMatch() {
        return greedyMatch;
    }
    public void setGreedyMatch(boolean greedyMatch) {
        this.greedyMatch = greedyMatch;
    }
    public String getImagePathPNG() {
        return imagePathPNG;
    }
    public void setImagePathPNG(String imagePathPNG) {
        this.imagePathPNG = imagePathPNG;
    }
}
