# LLM Kwaliteitsrapport
Vergelijking van antwoorden tussen Spring AI en LangChain4j voor verschillende modellen.

## Model: mistral
- **Prompt:** Schrijf een Java functie die een lijst sorteert.

---
### 🦜 LangChain4j (mistral):
 Hier is een voorbeeld van een Java-functie die een lijst sorteert met behulp van de QuickSort algoritme:

```java
public class Main {
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);

            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 8, 9, 1, 5};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
```

Deze code zal een gegeven array sorteren met behulp van de QuickSort algoritme en drukt de gesorteerde array uit.


### 🌱 Spring AI (mistral):
 Hier is een voorbeeld van een Java-functie die een lijst sorteert met behulp van de QuickSort algoritme:

```java
import java.util.Arrays;

public class Sorting {
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);

            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 8, 9, 1, 5};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
```

Deze functie sorteert de gegevens van een integers-array met behulp van de QuickSort algoritme. De `quickSort()`-functie maakt gebruik van recursieve partities en pivots om de lijst te sorteren.

De `main()`-methode initialiseert een voorbeeld array en roept de `quickSort()`-functie aan met die array als input. De geordende array wordt vervolgens weergegeven op het scherm.

---

## Model: llama3.2
- **Prompt:** Schrijf een Java functie die een lijst sorteert.

---
### 🦜 LangChain4j (llama3.2):
Hier is een voorbeeld van een Java-functie die een lijst sorteert:
```java
import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        String[] lijst = {"appels", "bananen", "ceruse"};
        Arrays.sort(lijst);
        System.out.println(Arrays.toString(lijst));
    }

    /**
     * Sorteert een lijst van strings in alfabetische volgorde.
     *
     * @param lijst de lijst te sorteren
     */
    public static void sorteerLijst(String[] lijst) {
        Arrays.sort(lijst, String.CASE_INSENSITIVE_ORDER);
    }
}
```
In dit voorbeeld wordt de `Arrays.sort()`-methode gebruikt om de lijst te sorteren. De `String.CASE_INSENSITIVE_ORDER` constant is gebruikt om de sortering te doen in alfabetische volgorde, ongeacht het formaat van de strings (bijv. "Appels" en "appels").

Als je wilt sorteren op basis van een andere criteria, zoals lengte of omgekeerde lengte, kun je een `Comparator` gebruiken:
```java
public static void sorteerLijst(String[] lijst) {
    Arrays.sort(lijst, (s1, s2) -> Integer.compare(s1.length(), s2.length()));
}
```
In dit voorbeeld wordt de `Comparator` gebruikt om de lijst te sorteren op basis van de lengte van de strings. De `Integer.compare()`-methode wordt gebruikt om de lengtes te vergelijken.

Je kunt ook een eigen Comparator implementeren:
```java
public static void sorteerLijst(String[] lijst) {
    Arrays.sort(lijst, new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            // Implementeer hier je eigen vergelijking
            return Integer.compare(s1.length(), s2.length());
        }
    });
}
```


### 🌱 Spring AI (llama3.2):
Hier is een Java-functie die een lijst sorteert:
```java
import java.util.Arrays;

public class Main {

    /**
     * Sorteert een lijst van String-waarden in alfabetische volgorde.
     *
     * @param list de lijst die moet worden gesorteerd
     */
    public static void sortStringList(String[] list) {
        Arrays.sort(list);
    }

    /**
     * Sorteert een lijst van Integer-waarden in nummersoortende volgorde.
     *
     * @param list de lijst die moet worden gesorteerd
     */
    public static void sortIntList(Integer[] list) {
        Arrays.sort(list);
    }

    public static void main(String[] args) {
        // Sorteer een lijst van String-waarden
        String[] stringList = {"apple", "banana", "cherry"};
        System.out.println("Voorheen: " + Arrays.toString(stringList));
        sortStringList(stringList);
        System.out.println("Nadat gesorteerd: " + Arrays.toString(stringList));

        // Sorteer een lijst van Integer-waarden
        Integer[] intList = {5, 2, 9};
        System.out.println("\nVoorheen: " + Arrays.toString(intList));
        sortIntList(intList);
        System.out.println("Nadat gesorteerd: " + Arrays.toString(intList));
    }
}
```
In deze code wordt de `Arrays.sort()`-methode gebruikt om de lijst te sortsen. Deze methode sorteert de elementen in de lijst volgens het alfabetische orden voor String-waarden en het nummersoortende orden voor Integer-waarden.

Noteer dat deze code een kopie maakt van de oorspronkelijke lijst. Als je de originele lijst wilt sorteren, moet je de originele lijst gebruiken in plaats van een kopie.

---

## Model: codellama
- **Prompt:** Schrijf een Java functie die een lijst sorteert.

---
### 🦜 LangChain4j (codellama):

```java
import java.util.Collections;

public static List<Integer> sort(List<Integer> list) {
    Collections.sort(list);
    return list;
}
```
In deze functie wordt de `Collections` klasse gebruikt om de lijst te sorteren. De methode `sort()` neemt een lijst als argument en sorteert deze in plaats. Nadat het sorteren is voltooid, wordt de gesorteerde lijst geretourneerd.

Deze functie kan worden gebruikt om een lijst te sorteren, bijvoorbeeld:
```java
List<Integer> myList = Arrays.asList(3, 2, 1);
myList = sort(myList);
System.out.println(myList); // [1, 2, 3]
```
Let op dat de functie de originele lijst veranderd en niet een nieuwe lijst retourneert. Als je een nieuwe lijst wilt maken, kun je de functie gebruiken om een kopie van de originele lijst te maken en deze daarna te sorteren.
```java
List<Integer> myList = Arrays.asList(3, 2, 1);
myList = sort(new ArrayList<>(myList));
System.out.println(myList); // [1, 2, 3]
```
Deze methode is ook effectief om grote lijsten te sorteren, omdat de `Collections` klasse gebruikmaakt van een heap om het sorteren te verrichten, wat zeer efficiënt is.


### 🌱 Spring AI (codellama):
```
import java.util.List;

public static void sort(List<String> list) {
    Collections.sort(list);
}
```
Deze functie neemt een lijst van strings als parameter en gebruikt de `Collections.sort()` methode om de lijst te sorteren. De methode retourneert een nieuwe, gesorteerde lijst.

---

