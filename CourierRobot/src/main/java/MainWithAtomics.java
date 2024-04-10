import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainWithAtomics {
    private static final int TEXTS_SIZE = 100_000;
    private static final String LETTERS = "abc";
    private static final int MIN_WORD_LENGTH = 3;
    private static final int LETTERS_SIZE = 3;
    private static final AtomicInteger minLength = new AtomicInteger();
    private static final AtomicInteger averageLength = new AtomicInteger();
    private static final AtomicInteger maxLength = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException, ConcurrentModificationException {
        Random random = new Random();
        List<Thread> threadList = new ArrayList<>();
        String[] texts = new String[TEXTS_SIZE];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText(LETTERS, MIN_WORD_LENGTH + random.nextInt(LETTERS_SIZE));
        }

        threadList.add(new Thread(() -> {
            for (String word : texts) {
                if (word.length() == 3) {
                    minLength.getAndIncrement();
                }
            }
        }));

        threadList.add(new Thread(() -> {
            for (String word : texts) {
                if (word.length() == 4) {
                    averageLength.getAndIncrement();
                }
            }
        }));

        threadList.add(new Thread(() -> {
            for (String word : texts) {
                if (word.length() == 5) {
                    maxLength.getAndIncrement();
                }
            }
        }));
        for (Thread thread : threadList) {
            thread.start();
            thread.join();
        }
        System.out.println(STR."Красивых слов с длиной 3: \{minLength}\nКрасивых слов с длиной 4: \{averageLength}\nКрасивых слов с длиной 5: \{maxLength}");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        if (!isValidText(text)) {
            return generateText(LETTERS, MIN_WORD_LENGTH + random.nextInt(LETTERS_SIZE));
        }
        return text.toString();
    }

    public static boolean isValidText(StringBuilder text) {
        boolean isValid = false;
        switch (1) {
            case 1:
                StringBuilder reversedText = new StringBuilder(text.toString());
                if (text.compareTo(reversedText.reverse()) == 0) { //проверка на полиндром
                    isValid = true;
                    break;
                }
            case 2:
                for (int i = 0; i < text.length(); i++) { //проверка - слово состоит из одной и той же буквы
                    isValid = (text.charAt(i) == text.charAt(i + 1));
                    if (!isValid) {
                        break;
                    }
                }
                if (isValid) {
                    break;
                }
            case 3:
                for (int i = 0; i < text.length() - 1; i++) { //проверка - буквы идут по возрастанию
                    isValid = ((text.charAt(i + 1) - text.charAt(i)) >= 0);
                    if (!isValid) {
                        break;
                    }
                }
        }

        return isValid;
    }
}
