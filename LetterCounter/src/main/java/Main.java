import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int TEXTS_SIZE = 100_000;
    private static final String LETTERS = "abc";
    private static final int MIN_WORD_LENGTH = 3;
    private static final int LETTERS_SIZE = 3;
    private static final AtomicInteger minLength = new AtomicInteger();
    private static final AtomicInteger averageLength = new AtomicInteger();
    private static final AtomicInteger maxLength = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException, ConcurrentModificationException {
        Random random = new Random();
        String[] texts = new String[TEXTS_SIZE];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText(LETTERS, MIN_WORD_LENGTH + random.nextInt(LETTERS_SIZE));
        }
        Thread threadIsPolindrom = new Thread(() -> {
          for(String text : texts){
              if(isPolindrom(text) && !isSimilarLetters(text)){ // testing
                  increment(text);
              }
          }
        });
        Thread threadIsLettersByIncrease = new Thread(() -> {
            for(String text : texts){
                if(!isSimilarLetters(text) && isLettersByIncrease(text)){ // testing
                    increment(text);
                }
            }
        });
        Thread threadIsSimilarLetters = new Thread(() -> {
            for(String text : texts){
                if(isSimilarLetters(text)){ // testing
                    increment(text);
                }
            }
        });
        threadIsPolindrom.start();
        threadIsSimilarLetters.start();
        threadIsLettersByIncrease.start();



        System.out.println("Красивых слов с длиной 3: " + minLength + "\nКрасивых слов с длиной 4: " +
                averageLength + "\nКрасивых слов с длиной 5: " + maxLength);
    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    public static boolean isPolindrom(String text){
        AtomicBoolean isValid = new AtomicBoolean(false);
            StringBuilder builderText = new StringBuilder(text);
            StringBuilder reversedText = new StringBuilder(text);
            if (builderText.compareTo(reversedText.reverse()) == 0) { //проверка на полиндром
                isValid.set(true);
            }else{
                isValid.set(false);
            }
        return isValid.get();
    }
    public static boolean isSimilarLetters(String text){
        AtomicBoolean isValid = new AtomicBoolean(true);
            for (int i = 0; i < text.length() - 1; i++) { //проверка - слово состоит из одной и той же буквы
                isValid.set((text.charAt(i) == text.charAt(i + 1)));
                if (!isValid.get()) {
                    isValid.set(false);
                    break;
                }
            }
        return isValid.get();
    }
    public static boolean isLettersByIncrease(String text){
        boolean isValid;
        for (int i = 0; i < text.length() - 1; i++) { //проверка - буквы идут по возрастанию
            isValid = ((text.charAt(i + 1) - text.charAt(i)) >= 0);
            if (!isValid) {
                return false;
            }
        }
        return true;
    }
    public static void increment(String text){
        if (text.length() == 3) {
            minLength.getAndIncrement();
        } else if (text.length() == 4) {
            averageLength.getAndIncrement();
        } else if (text.length() == 5) {
            maxLength.getAndIncrement();
        }
    }

}
