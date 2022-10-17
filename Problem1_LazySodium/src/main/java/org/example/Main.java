package org.example;

import com.goterl.lazysodium.exceptions.SodiumException;

public class Main {
    public static void main(String[] args) throws SodiumException {

        LazySodium lazySodium = new LazySodium();
        lazySodium.sealedBox();
    }
}