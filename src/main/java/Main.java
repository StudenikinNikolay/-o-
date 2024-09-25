import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static AtomicInteger beautifulWordsLength3 = new AtomicInteger(0);
    private static AtomicInteger beautifulWordsLength4 = new AtomicInteger(0);
    private static AtomicInteger beautifulWordsLength5 = new AtomicInteger(0);

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread1 = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text) && isUniqueLetter(text) && isInOrder(text)) {
                    beautifulWordsLength3.incrementAndGet();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 4 && isPalindrome(text) && isUniqueLetter(text) && isInOrder(text)) {
                    beautifulWordsLength4.incrementAndGet();
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 5 && isPalindrome(text) && isUniqueLetter(text) && isInOrder(text)) {
                    beautifulWordsLength5.incrementAndGet();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Красивых слов с длиной 3: " + beautifulWordsLength3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + beautifulWordsLength4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + beautifulWordsLength5.get() + " шт");
    }

    private static boolean isPalindrome(String text) {
        return new StringBuilder(text).reverse().toString().equals(text);
    }

    private static boolean isUniqueLetter(String text) {
        return text.chars().distinct().count() == 1;
    }

    private static boolean isInOrder(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }
}