package com.company;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
       String[] options = args;
       if ((options.length<3) | (options.length % 2 !=1)){
           System.out.println("Wrong number of parameters. The nuber of parameters should be not divisible by two and more or equal 3");
           return;
       }
       System.out.println(options.toString());
       String key= KeyGeneration();

       double cturn = Math.random()* (options.length)+1;;
       int compturn = (int) cturn;
       System.out.println("HMAC: "+hash(options[(int) compturn],key));
       System.out.println("Available moves:");
       for (int i = 0; i <= options.length-1; i++) {
            System.out.print(i + 1);
            System.out.println(" — "+options[i]);
        }
        System.out.println("0 — exit");
        System.out.println("Enter a number of your move:");
        Scanner turnscan = new Scanner((System.in));
        int uturn= turnscan.nextInt();
        if (uturn==0) {
            System.out.println("Game over :(");
            return;
        }
        System.out.println("Your move: "+options[uturn-1]);
        System.out.println("Computer move: "+options[(compturn)-1]);
        if (uturn==compturn) System.out.println("dead heat");
        else {
            if (Math.abs(cturn-uturn)> options.length/2) {
             if (cturn>uturn) uturn= (int) (cturn+(options.length-Math.abs(cturn-uturn)));
             else cturn= (int) (uturn+(options.length-Math.abs(uturn-cturn)));
            }
            if (cturn<uturn) System.out.println("User wins");
            else System.out.println("Computer wins");
        }

       System.out.println(key);
       return;
        }


    public static String KeyGeneration() {
        try {KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        keyGen.init(random);
        SecretKey secretKey = keyGen.generateKey();
        String s = new BigInteger(1, secretKey.getEncoded()).toString(16);
        return s;}
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();}

        return null;
    }
    public static String hash(String cturn, String key) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String passWithSalt = cturn + key;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< passHash.length ;i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedHash = sb.toString();
            return generatedHash;
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }
}
