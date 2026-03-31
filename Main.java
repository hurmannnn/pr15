import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String[] dates = new String[50];
        String[] notes = new String[50];//масив у який буде записано дати та записи до них
        int count = 0;//лічильник записів зараз

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//задавання формату дати

        System.out.println("1 - новий запис у щоденник");
        System.out.println("2 - завантажити");
        String start = sc.nextLine();

        if (start.equals("2")) {
            System.out.print("файл: ");
            String path = sc.nextLine();

            try {
                BufferedReader br = new BufferedReader(new FileReader(path));//відркиття файлу
                String line;
                while ((line = br.readLine()) != null) {//читання файлу порядково
                    if (line.equals(""))
                        continue;//пропуск пустих рядків

                    dates[count] = line;//дата
                    notes[count] = br.readLine();//текст
                    count++;//додавання
                }
                br.close();
                System.out.println("Завантажено");//закриття файлу

            } catch (Exception e) {
                System.out.println("Помилка читання файлу");//якщо помилка
            }
        }

        while (true) {//цикл який працює поки користувач з нього не вийде
            System.out.println("1-Додати дату");
            System.out.println("2-Видалити дату");
            System.out.println("3-Показати дату");
            System.out.println("4-Вихід");
            String choice = sc.nextLine();

            if (choice.equals("1")) {//якщо введене число 1

                if (count >= 50) {
                    System.out.println("Щоденник заповнений");
                    continue;//якщо дат забагато - щоденник заповнений
                }

                System.out.print("Введіть дату (рік-місяць-число): ");
                String input = sc.nextLine();//ввід дати

                LocalDate date;

                try {
                    date = LocalDate.parse(input, formatter);//перетворення тексту у дату
                } catch (Exception e) {
                    System.out.println("Неправильна дата");
                    continue;//якщо виникає помилка - повернення у головне меню
                }

                System.out.println("Введіть текст (щоб завершити запис натисність /):");//вихід з запису

                String text = "";//змінна для збереження записів
                while (true) {//цикл який працює поки користувач не натисне слеш
                    String line = sc.nextLine();
                    if (line.equals("/"))
                        break;
                    text = text + line;//додавання рядка до тексту
                }

                dates[count] = date.format(formatter);
                notes[count] = text;
                count++;//зберігання дати та додавання її
                System.out.println("Додано");
            }

            else if (choice.equals("2")) {//якщо число 2 можна видалити запис
                System.out.print("Введіть дату: ");
                String d = sc.nextLine();

                boolean found = false;//пошук запису чи є він взагалі

                for (int i = 0; i < count; i++) {//пошук запису проходячи усі записи
                    if (dates[i].equals(d)) {//якщо дата співпала
                        for (int j = i; j < count - 1; j++) {
                            dates[j] = dates[j + 1];
                            notes[j] = notes[j + 1];//усі записи зсуваються щоб прибрати обраний запис
                        }

                        count--;
                        found = true;
                        System.out.println("Видалено");
                        break;//зменьшення кількості, дату знайдено і вихід з циклу
                    }
                }

                if (!found) {//якщо дати не знайдено
                    System.out.println("Не знайдено дати");
                }
            }

            else if (choice.equals("3")) {//якщо число 3
                if (count == 0) {//якщо дати не знайдено
                    System.out.println("Немає такої дати");
                } else {//показ усіх записів та обрану дату
                    for (int i = 0; i < count; i++) {
                        System.out.println("Дата: " + dates[i]);
                        System.out.println(notes[i]);
                    }
                }
            }
            else if (choice.equals("4")) {//вихід з головного циклу
                System.out.println("Зберегти запис? (1-так 0-ні)");//запитання про збереження
                String save = sc.nextLine();

                if (save.equals("1")) {//збереження запису
                    System.out.print("Куди: ");
                    String path = sc.nextLine();
                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(path));//відкриття файлу для запису

                        for (int i = 0; i < count; i++) {//обирається запис
                            bw.write(dates[i]);//записується дата
                            bw.newLine();//новий рядок
                            bw.write(notes[i]);//запис тексту запису
                            bw.newLine();//новий рядок
                        }
                        bw.close();
                        System.out.println("Збережено");

                    } catch (Exception e) {
                        System.out.println("Помилка запису");//невірна послідовність дій
                    }
                }
                break;
            }
            else {
                System.out.println("Невірний вибір");
            }
        }
        sc.close();
    }
}